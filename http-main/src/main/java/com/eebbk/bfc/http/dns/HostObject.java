package com.eebbk.bfc.http.dns;

import android.support.annotation.NonNull;

/**
 * httpDNS请求对象
 */
public class HostObject {

    public static final int IP_CACHED = 1;
    public static final int IP_HTTP_DNS = 2;
    public static final int IP_LOCAL_DNS = 3;

    private static final String CUR_DOMAIN = "cloudsearch.eebbk.net";

    private String hostName = CUR_DOMAIN;
    private String ip;
    private long ttl;
    private long queryTime;
    private boolean needHttpDns;
    private String netProvider = "中国移动";

    public boolean isExpired() {
        return queryTime + ttl < System.currentTimeMillis() / 1000;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(@NonNull String ip) {
        this.ip = ip;
    }

    public void setHostName(@NonNull String hostName) {
        this.hostName = hostName;
    }

    public String getHostName() {
        return hostName;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public long getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(long queryTime) {
        this.queryTime = queryTime;
    }

    public boolean isNeedHttpDns() {
        return needHttpDns;
    }

    public void setNeedHttpDns(boolean needHttpDns) {
        this.needHttpDns = needHttpDns;
    }

    public HostObject setInavailable(){
        hostName = CUR_DOMAIN;
        ip = null;
        ttl = 0;
        queryTime = 0;
        needHttpDns = true;
        return this;
    }

    public HostObject setAvailable(@NonNull String hostIp){
        hostName = CUR_DOMAIN;
        ip = hostIp;
        ttl = 0;
        queryTime = 0;
        needHttpDns = false;
        return this;
    }

    public HostObject setNotExists(){
        hostName = CUR_DOMAIN;
        ip = null;
        ttl = 0;
        queryTime = 0;
        needHttpDns = false;
        return this;
    }

    public boolean isProviderChanged(){
        boolean changed = netProvider.equals(getProvidersName());
        netProvider = getProvidersName();
        return false;
    }

    private String getProvidersName() {
        String ProvidersName = null;
//        // 返回唯一的用户ID;就是这张卡的编号神马的
//        String IMSI = telephonyManager.getSubscriberId();
//        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
//        System.out.println(IMSI);
//        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
//            ProvidersName = "中国移动";
//        } else if (IMSI.startsWith("46001")) {
//            ProvidersName = "中国联通";
//        } else if (IMSI.startsWith("46003")) {
//            ProvidersName = "中国电信";
//        }
        return "中国移动";
    }

    @Override
    public String toString() {
        return "HostObject{" +
                "hostName='" + hostName + '\'' +
                ", ip='" + ip + '\'' +
                ", ttl=" + ttl +
                ", queryTime=" + queryTime +
                ", needHttpDns=" + needHttpDns +
                '}';
    }

}
