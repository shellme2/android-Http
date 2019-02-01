package com.example.http.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Cache;
import com.eebbk.bfc.http.BfcHttp;
import com.eebbk.bfc.http.config.BfcHttpConfigure;
import com.eebbk.bfc.http.config.BfcRequestConfigure;
import com.eebbk.bfc.http.error.BfcHttpError;
import com.eebbk.bfc.http.toolbox.IBfcErrorListener;
import com.eebbk.bfc.http.toolbox.StringCallBack;
import com.example.http.R;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public class RequestConfTestActivity extends Activity {

    private static final String TAG = "RequestConfTestActivity";

    private String getUrl = "http://test.eebbk.net/social-scan/searcher/Switch/test";

    private String getUrlWithParams = "http://test.eebbk.net/social-scan/searcher/Switch/test?testContent=this%20is%20RequestConf%20with%20params";
    private static Map<String, String> params = new HashMap<>();
    private static Map<String, String> header = new HashMap<>();
    private TextView testTerminal;
    private long funStartTime;

    static {
        params.put("testContent", "this is RequestConf");
        header.put("Accept-Encoding", "gzip");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_conf_test);
        testTerminal = (TextView) findViewById(R.id.testTerminal);
        BfcHttpConfigure.setEnableDNSAntiHijack(false);
    }

    public void testRequestConf1(View view) {
        funStartTime = System.currentTimeMillis();
        BfcHttp.get(RequestConfTestActivity.this, getUrl, params, header, new StringCallBack() {
            @Override
            public void onResponse(String response) {
                updateMessage(response);
            }
        }, new IBfcErrorListener() {
            @Override
            public void onError(BfcHttpError error) {
                updateMessage(error.getMsg() + "   " + error.getErrorCode());
            }
        }, null);
    }

    public void testRequestConf2(View view) {
        funStartTime = System.currentTimeMillis();
        BfcHttp.get(RequestConfTestActivity.this, getUrl, params, null, new StringCallBack() {
            @Override
            public void onResponse(String response) {
                updateMessage(response);
            }
        }, new IBfcErrorListener() {
            @Override
            public void onError(BfcHttpError error) {
                updateMessage(error.getMsg() + "   " + error.getErrorCode());
            }
        }, null);
    }

    public void testRequestConf3(View view) {
        funStartTime = System.currentTimeMillis();
        BfcHttp.get(RequestConfTestActivity.this, getUrl, null, header, new StringCallBack() {
            @Override
            public void onResponse(String response) {
                updateMessage(response);
            }
        }, new IBfcErrorListener() {
            @Override
            public void onError(BfcHttpError error) {
                updateMessage(error.getMsg() + "   " + error.getErrorCode());
            }
        }, null);
    }

    public void testRequestConf4(View view) {
        funStartTime = System.currentTimeMillis();
        BfcHttp.get(RequestConfTestActivity.this, getUrl, null, null, new StringCallBack() {
            @Override
            public void onResponse(String response) {
                updateMessage(response);
            }
        }, new IBfcErrorListener() {
            @Override
            public void onError(BfcHttpError error) {
                updateMessage(error.getMsg() + "   " + error.getErrorCode());
            }
        }, null);
    }

    public void testRequestConf5(View view) {
        funStartTime = System.currentTimeMillis();
        BfcHttp.get(RequestConfTestActivity.this, null, params, null, new StringCallBack() {
            @Override
            public void onResponse(String response) {
                updateMessage(response);
            }
        }, new IBfcErrorListener() {
            @Override
            public void onError(BfcHttpError error) {
                updateMessage(error.getMsg());
            }
        }, null);
    }

    public void testRequestConf6(View view) {
        funStartTime = System.currentTimeMillis();
        BfcHttp.get(RequestConfTestActivity.this, getUrlWithParams, params, header, new StringCallBack() {
            @Override
            public void onResponse(String response) {
                updateMessage(response);
            }
        }, new IBfcErrorListener() {
            @Override
            public void onError(BfcHttpError error) {
                updateMessage(error.getMsg() + "   " + error.getErrorCode());
            }
        }, null);
    }

    public void testRequestConf7(View view) {
        funStartTime = System.currentTimeMillis();
        BfcHttp.get(RequestConfTestActivity.this, getUrlWithParams, params, null, new StringCallBack() {
            @Override
            public void onResponse(String response) {
                updateMessage(response);
            }
        }, new IBfcErrorListener() {
            @Override
            public void onError(BfcHttpError error) {
                updateMessage(error.getMsg() + "   " + error.getErrorCode());
            }
        }, null);
    }

    public void testRequestCustomConf(View view) {
        funStartTime = System.currentTimeMillis();

        BfcRequestConfigure conf = new BfcRequestConfigure.Builder()
                .setHeader(header)
                .setType(BfcRequestConfigure.Type.GZIP)
                .setTag("BfcRequestConfigure")
                .setCacheAble(true)
                .setCacheKey("...")
                .setExpiredTime(5 * 60 * 60 * 1000)
                .setRetryTimes(1)
                .setTimeout(10 * 1000).build();
        BfcHttp.get(this, getUrlWithParams, null, conf, new StringCallBack() {
            @Override
            public void onResponse(String response) {
                updateMessage(response);
            }
        }, new IBfcErrorListener() {
            @Override
            public void onError(BfcHttpError error) {
                updateMessage(error.getMsg() + "   " + error.getErrorCode());
            }
        });

        Cache.Entry cache = BfcHttpConfigure.getHttpRequestQueue().getCache().get("...");
        if (cache != null) {
            Log.d(TAG, "testRequestCustomConf: " + new String(cache.data));
        }
    }

    private void updateMessage(String msg) {
        Log.d(TAG, "updateMessage: " + msg);
        long funTime = System.currentTimeMillis() - funStartTime;
        StringBuilder builder = new StringBuilder();
        builder.append("测试信息:  ").append("  ");
        builder.append((BfcHttpConfigure.isEnableDNSAntiHijack() ? "反劫持网络请求" : "普通网络请求"));
        builder.append("\n请求耗时：").append(funTime).append("ms\n\n");
        builder.append(msg);

        testTerminal.setText(builder);
        funStartTime = 0;
    }
}
