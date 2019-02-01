package com.example.http.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.eebbk.bfc.http.BfcHttp;
import com.eebbk.bfc.http.config.BfcHttpConfigure;
import com.eebbk.bfc.http.dns.DNSAntiHijackEnv;
import com.eebbk.bfc.http.error.BfcHttpError;
import com.eebbk.bfc.http.toolbox.IBfcErrorListener;
import com.eebbk.bfc.http.toolbox.StringCallBack;
import com.eebbk.bfc.http.tools.UserPreferences;
import com.example.http.R;
import com.example.http.bean.TerminalMsg;
import com.example.http.databinding.ActivityHttpDnsTestBinding;

import java.util.Map;

public class BfcHttpDnsTestActivity extends Activity {

    private static final String TAG = "BFC_HTTP";

    private static final String normalRequest = "http://test.eebbk.net/social-scan/testApp/test200";
    private static final String timeOutRequest = "http://test.eebbk.net/social-scan/testApp/testTimeout";
    private static final String badRequest = "http://test.eebbk.net/social-scan/testApp/test500";

    private ToggleButton httpToggle;

    private long funStartTime;

    private EditText etTimeOut;

    private ActivityHttpDnsTestBinding binding;

    private TerminalMsg terminalMsg = new TerminalMsg();

    private int time = 0;
    private ToggleButton httpDnsUrlToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_http_dns_test);
        binding.setTerminalMsg(terminalMsg);

        httpToggle = (ToggleButton) findViewById(R.id.http_toggle);
        httpDnsUrlToggle = (ToggleButton) findViewById(R.id.http_dns_url_toggle);

        etTimeOut = (EditText) findViewById(R.id.timeOut);

        httpToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                httpToggle.setChecked(isChecked);
                BfcHttpConfigure.setEnableDNSAntiHijack(isChecked);
                updateMessage(isChecked ? "设置为反劫持网络请求" : "设置为普通网络请求", "", null);
            }
        });

        httpDnsUrlToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                httpDnsUrlToggle.setChecked(isChecked);
                BfcHttpConfigure.setHttpsDnsServerIp(isChecked ? "111.111.111.111" : "");
                updateMessage(isChecked ? "设置模拟httpDnsServerUrl" : "设置真实httpDnsServerUrl", "", null);
            }
        });

        BfcHttpConfigure.setEnableDNSAntiHijack(httpToggle.isChecked());
    }

    private void doOnResponse(String response, String url, Map<String, String> params) {
        Log.d(TAG, "doOnResponse: " + response);
        updateMessage(response, url, params);
    }

    private void doOnError(BfcHttpError error, String url, Map<String, String> params) {
        Log.d(TAG, "onError: " + error.getMessage());
        updateMessage(error.getMessage() + "\nstatus:" + error.getErrorCode(), url, params);
    }

    public void processNormalRequest(View view) {
        funStartTime = System.currentTimeMillis();
        terminalMsg.setMsg("网络请求运行中...");
        binding.setTerminalMsg(terminalMsg);

        BfcHttp.get(getApplicationContext(), normalRequest, null, new StringCallBack() {
            @Override
            public void onResponse(String response) {
                doOnResponse(response + "\n\n 缓存有效时长" + time + "秒", normalRequest, null);
            }
        }, new IBfcErrorListener() {
            @Override
            public void onError(BfcHttpError error) {
                doOnError(error, normalRequest, null);
            }
        });
    }

    public void processTimeOutRequest(View view) {
        funStartTime = System.currentTimeMillis();
        terminalMsg.setMsg("网络请求运行中...");
        binding.setTerminalMsg(terminalMsg);

        BfcHttp.get(getApplicationContext(), timeOutRequest, null, new StringCallBack() {
            @Override
            public void onResponse(String response) {
                doOnResponse(response + "\n\n 缓存有效时长" + time + "秒", timeOutRequest, null);
            }
        }, new IBfcErrorListener() {
            @Override
            public void onError(BfcHttpError error) {
                doOnError(error, timeOutRequest, null);
            }
        });
    }

    public void processBadRequest(View view) {
        funStartTime = System.currentTimeMillis();
        terminalMsg.setMsg("网络请求运行中...");
        binding.setTerminalMsg(terminalMsg);

        BfcHttp.get(getApplicationContext(), badRequest, null, new StringCallBack() {
            @Override
            public void onResponse(String response) {
                doOnResponse(response + "\n\n 缓存有效时长" + time + "秒", badRequest, null);
            }
        }, new IBfcErrorListener() {
            @Override
            public void onError(BfcHttpError error) {
                doOnError(error, badRequest, null);
            }
        });
    }

    public void clearPreferences(View view) {
        funStartTime = System.currentTimeMillis();
        UserPreferences.clear(getApplicationContext());
        updateMessage("清除缓存IP", "", null);
    }

    public void setHttpDnsCacheIp(View view) {
        String httpDnsIp = "106.75.148.176";
        funStartTime = System.currentTimeMillis();
        updateMessage("设置httpDns缓存IP", "httpDnsIp : " + httpDnsIp, null);
        DNSAntiHijackEnv.cacheIp(this, httpDnsIp);
    }

    public void setBadHttpDnsCacheIp(View view) {
        String httpDnsIp = "106.75.148.176";
        funStartTime = System.currentTimeMillis();
        updateMessage("设置过期httpDns缓存IP", "httpDnsIp : " + httpDnsIp + " timestamp 2 days ago", null);
        DNSAntiHijackEnv.cacheIp(this, httpDnsIp, 2 * 24 * 60 * 60 * 1000);
    }

    public void setWrongHttpDnsCacheIp(View view) {
        String httpDnsIp = "111.111.111.111";
        funStartTime = System.currentTimeMillis();
        updateMessage("设置异常httpDns缓存IP", "httpDnsIp : " + httpDnsIp, null);
        DNSAntiHijackEnv.cacheIp(this, httpDnsIp, 60 * 60 * 1000);
    }

    public void setTimeOut(View view) {
        funStartTime = System.currentTimeMillis();
        int timeOut = 1000;
        try {
            if (TextUtils.isEmpty(etTimeOut.getText())) {
                return;
            }
            timeOut = Integer.parseInt(etTimeOut.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }
        BfcHttpConfigure.setTimeout(timeOut);
        updateMessage("设置超时时长", "", null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        BfcHttp.cancelAll();
    }

    private void updateMessage(String msg, String url, Map<String, String> params) {
        long funTime = System.currentTimeMillis() - funStartTime;
        StringBuilder builder = new StringBuilder();
        builder.append("测试信息:  ");
        builder.append((BfcHttpConfigure.isEnableDNSAntiHijack() ? "反劫持网络请求" : "普通网络请求"));
        builder.append(funStartTime == 0 ? "\n当前请求响应时间：" : "\n请求耗时：");
        builder.append(funTime).append(funStartTime == 0 ? "\n" : "ms\n");
        builder.append("\n请求URL：").append(url).append("\n");
        builder.append("\n请求参数：").append(params).append("\n\n");
        builder.append(msg);

        terminalMsg.setMsg(builder.toString());
        binding.setTerminalMsg(terminalMsg);
        funStartTime = 0;
    }
}
