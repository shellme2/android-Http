package com.eebbk.bfc.http.dns;


import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.eebbk.bfc.http.config.BfcHttpConfigure;
import com.eebbk.bfc.http.tools.L;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HttpDNS {

    private static final String SERVER_IP = BfcHttpConfigure.getHttpDnsServerIp();
    private static final String ACCOUNT_ID = "216";
    private static final int RESOLVE_TIMEOUT_IN_SEC = 10;
    private static final int MAX_HOLD_HOST_NUM = 100;
    private static final int EMPTY_RESULT_HOST_TTL = 30;
    private static final int REQUEST_TIME = 2;

    private static HttpDNS instance = new HttpDNS();

    private HashMap<String, HostObject> hostManager = new HashMap<>();
    private HostObject hostObject = new HostObject();

    private String encryptUrl;

    private boolean isExpiredIpAvailable = false;
    private DegradationFilter degradationFilter = null;

    private HttpDNS() {
    }

    public static HttpDNS getInstance() {
        return instance;
    }

    // 是否允许过期的IP返回
    public void setExpiredIpAvailable(boolean flag) {
        isExpiredIpAvailable = flag;
    }

    public boolean isExpiredIpAvailable() {
        return isExpiredIpAvailable;
    }

    private DnsTask dnsTask;

    protected String getIpByHost(@NonNull String hostName, DnsListener dnsListener) {
        HostObject host = hostManager.get(hostName);
        if(null == host || null  == host.getIp()){
            return null;
        }
        if (null != degradationFilter) {
            if (degradationFilter.shouldDegradeHttpDNS(hostName)) {
                return null;
            }
        }
        if (host != null && (host.isExpired() && !isExpiredIpAvailable())) {
            L.i(">>>>>[getIpByHost] - fetch result from network, host: " + hostName);
            dnsTask = new DnsTask(dnsListener);
            dnsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,hostName);
        } else if (host.isExpired()) {
            // 同步返回，异步更新
            L.i(">>>>>[getIpByHost] - fetch result from cache, host: " + hostName);
            hostManager.remove(hostName);
            return host.getIp();
        }
        L.i(">>>>>[getIpByHost] - fetch result from cache, host: " + hostName);
        return host.getIp();
    }

    public void setDegradationFilter(DegradationFilter filter) {
        degradationFilter = filter;
    }

    public String requestHttpDnsManyTimes(String hostName){
        String result = null;
        int num = REQUEST_TIME;
        while(--num>=0){
            L.v("5.1 httpDns request server IP: " + BfcHttpConfigure.getHttpDnsServerIp() +" " + (REQUEST_TIME - num) + " times");
            result = requestHttpDns(hostName);
            L.v("5.1 httpDns request server IP: " + BfcHttpConfigure.getHttpDnsServerIp() +" result" + result);
            if(null != result){
                break;
            }
        }
        return result;
    }

    public String requestHttpDns(@NonNull String hostName) {
        String encryptHostName = DesEncryptUtil.encrypt(hostName);
        String resolveUrl = "http://" + BfcHttpConfigure.getHttpDnsServerIp() + "/d?dn=" + encryptHostName + "&id=" + ACCOUNT_ID + "&ttl=" + 1;
        L.i("encrypt url is :" + resolveUrl);
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(resolveUrl).openConnection();
            conn.setConnectTimeout(RESOLVE_TIMEOUT_IN_SEC * 1000);
            conn.setReadTimeout(RESOLVE_TIMEOUT_IN_SEC * 1000);
            if (conn.getResponseCode() != 200) {
                L.i("response code : " + conn.getResponseCode());
            } else {
                InputStream in = conn.getInputStream();
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = streamReader.readLine()) != null) {
                    sb.append(line);
                }
                L.i("raw httpDns result :" + sb.toString());
                String decryptStr = DesEncryptUtil.decrypt(sb.toString());
                HttpDNSResult httpDNSResult = convert(decryptStr);
                if (httpDNSResult != null) {
                    L.i("my httpDnsResult obj:" + httpDNSResult.toString());
                    long ttl = httpDNSResult.getTtl();
                    List<String> ips = httpDNSResult.getIps();
                    if (ttl == 0) {
                        // 如果有结果返回，但是ip列表为空，ttl也为空，那默认没有ip就是解析结果，并设置ttl为一个较长的时间
                        // 避免一直请求同一个ip冲击sever
                        ttl = EMPTY_RESULT_HOST_TTL;
                    }
                    HostObject hostObject = new HostObject();
                    String ip = (ips == null || ips.size() == 0) ? null : ips.get(0);
                    L.d("resolve host:" + " ip:" + ip + " ttl:" + ttl);
                    hostObject.setTtl(ttl);
                    hostObject.setIp(ip);
                    hostObject.setQueryTime(System.currentTimeMillis() / 1000);
                    if (hostManager.size() < MAX_HOLD_HOST_NUM) {
                        hostManager.put(hostName, hostObject);
                    }
                    return ip;
                }
            }
        } catch (Exception e) {
            L.e(e);
        }
        return null;
    }


    private HttpDNSResult convert(@NonNull String decryptStr) {
        int lastCommaIndex = decryptStr.lastIndexOf(",");
        String ttlStr = decryptStr.substring(lastCommaIndex + 1, decryptStr.length());
        String[] ipArray = decryptStr.substring(0, lastCommaIndex).split(";");
        HttpDNSResult httpDNSResult = new HttpDNSResult();
        httpDNSResult.setIps(Arrays.asList(ipArray));
        httpDNSResult.setTtl(Long.parseLong(ttlStr));
        return httpDNSResult;
    }


    class DnsTask extends AsyncTask<String,Void,String> {
        private DnsListener dnsListener;

        public DnsTask(DnsListener dnsListener){
            this.dnsListener = dnsListener;
        }

        @Override
        protected String doInBackground(String... params) {
            return requestHttpDns(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(null != dnsListener){
                dnsListener.onGetIp(s);
            }
        }
    }

    public interface DnsListener{
        void onGetIp(String stringIp);
    }

    public HostObject getHostObject() {
        return hostObject;
    }

    public void setHostObject(HostObject hostObject) {
        if(null != hostObject){
            this.hostObject = hostObject;
        }else{
            this.hostObject = new HostObject();
        }
    }

    public String getEncryptUrl() {
        if(null == encryptUrl){
            if(null != hostObject){
                String encryptHostName = DesEncryptUtil.encrypt(hostObject.getHostName());
//                encryptHostName = "ac7875d400dacdf09954edd788887719";
                encryptUrl = "http://" + BfcHttpConfigure.getHttpDnsServerIp() + "/d?dn=" + encryptHostName + "&id=" + 216 + "&ttl=" + 1;
            }
        }
        L.i("encrypt url : " + encryptUrl);
        return encryptUrl;
    }

}
