package com.example.http.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.eebbk.bfc.http.config.BfcHttpConfigure;
import com.eebbk.bfc.http.config.BfcRequestConfigure;
import com.eebbk.bfc.http.dns.DNSAntiHijackEnv;
import com.eebbk.bfc.http.error.BfcHttpError;
import com.eebbk.bfc.http.toolbox.IBfcErrorListener;
import com.eebbk.bfc.http.toolbox.StringCallBack;
import com.eebbk.bfc.http.tools.NetStateTools;
import com.example.http.R;
import com.example.http.toolbox.NetWorkUtils;

import java.util.HashMap;
import java.util.Map;

public class BfcHttpCustomRequestTestActivity extends Activity {

    private EditText requestUrl;
    private EditText requestParams;

    private RadioButton btnGet;
    private RadioButton btnPost;

    private TextView testTerminal;

    private long funStartTime;

    private static final String url = "http://test.eebbk.net/social-scan/searcher/Switch/test";

    private String requestType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bfc_http_custom_request_test);

        btnGet = (RadioButton) findViewById(R.id.requestTypeGet);
        btnPost = (RadioButton) findViewById(R.id.requestTypePost);

        requestUrl = (EditText) findViewById(R.id.requestUrl);
        requestParams = (EditText) findViewById(R.id.requestParams);

        testTerminal = (TextView) findViewById(R.id.testTerminal);

//        requestParams.setText("`1234567890-=qwertyuiop[]\\asdfghjkl;'zxcvbnm,./~!@#$%^&*()_+QWERTYUIOP{}|ASDFGHJKL:ZXCVBNM<>?");
        requestParams.setText("１～！＠＃￥％％……＆×（）——ｙｕＦＦ＝－０、。，？》《｜＼》＋【】｛＊－/１４｝】");
        requestUrl.setText(url);

        requestType = btnGet.isChecked() ? "GET" : "POST";
        BfcHttpConfigure.setEnableDNSAntiHijack(true);
    }

    public void runCustomRequest(View view) {
        testTerminal.setText("网络请求运行中...");
        Map<String, String> params = new HashMap<>();
        params.put("testContent", requestParams.getText().toString());
        Map<String, String> header = new HashMap<>();
        header.put("Accept-Encoding", "gzip");
        requestType = btnGet.isChecked() ? "GET" : "POST";
        try {
            funStartTime = System.currentTimeMillis();
            if (btnGet.isChecked()) {
                NetWorkUtils.get(this.getApplicationContext(), requestUrl.getText().toString(), params, header, new StringCallBack() {
                    @Override
                    public void onResponse(String response) {
                        updateMessage(response);
                    }
                }, new IBfcErrorListener() {
                    @Override
                    public void onError(BfcHttpError error) {
                        if (error.getMsg() == null) {
                            updateMessage(error.toString());
                        } else {
                            updateMessage(error.getMsg() + "\nstatus:" + error.getErrorCode());
                        }
                    }
                }, null);
            } else if (btnPost.isChecked()) {
                NetWorkUtils.post(this.getApplicationContext(), requestUrl.getText().toString(), params, header, new StringCallBack() {
                    @Override
                    public void onResponse(String response) {
                        updateMessage(response);
                    }
                }, new IBfcErrorListener() {
                    @Override
                    public void onError(BfcHttpError error) {
                        if (error.getMsg() == null) {
                            updateMessage(error.toString());
                        } else {
                            updateMessage(error.getMsg() + "\nstatus:" + error.getErrorCode());
                        }
                    }
                }, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            updateMessage(e.getMessage());
        }
    }


    public void runCustomConfRequest(View view) {
        testTerminal.setText("网络请求运行中...");
        Map<String, String> params = new HashMap<>();
        params.put("testContent", requestParams.getText().toString());
        Map<String, String> header = new HashMap<>();
        header.put("Accept-Encoding", "gzip");
        requestType = btnGet.isChecked() ? "GET" : "POST";
        BfcRequestConfigure conf = new BfcRequestConfigure.Builder().setHeader(header).setType(BfcRequestConfigure.Type.GZIP).setCacheAble(false).build();

        try {
            funStartTime = System.currentTimeMillis();
            if (btnGet.isChecked()) {
                NetWorkUtils.get(this.getApplicationContext(), requestUrl.getText().toString(), params, conf, new StringCallBack() {
                    @Override
                    public void onResponse(String response) {
                        updateMessage(response);
                    }
                }, new IBfcErrorListener() {
                    @Override
                    public void onError(BfcHttpError error) {
                        if (error.getMsg() == null) {
                            updateMessage(error.toString());
                        } else {
                            updateMessage(error.getMsg() + "\nstatus:" + error.getErrorCode());
                        }
                    }
                });
            } else if (btnPost.isChecked()) {
                NetWorkUtils.post(this.getApplicationContext(), requestUrl.getText().toString(), params, conf, new StringCallBack() {
                    @Override
                    public void onResponse(String response) {
                        updateMessage(response);
                    }
                }, new IBfcErrorListener() {
                    @Override
                    public void onError(BfcHttpError error) {
                        if (error.getMsg() == null) {
                            updateMessage(error.toString());
                        } else {
                            updateMessage(error.getMsg() + "\nstatus:" + error.getErrorCode());
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            updateMessage(e.getMessage());
        }
    }

    public void cancel(View view) {
        NetWorkUtils.cancelAll();
        updateMessage("取消请求");
    }

    private void updateMessage(String msg) {
        long funTime = System.currentTimeMillis() - funStartTime;
        StringBuilder builder = new StringBuilder();
        builder.append("测试信息:  ").append(requestType).append("  ");
        builder.append((BfcHttpConfigure.isEnableDNSAntiHijack() && DNSAntiHijackEnv.isNeedAntiHijack(requestUrl.getText().toString()) ? "反劫持网络请求" : "普通网络请求"));
        builder.append("\n请求耗时：").append(funTime).append("ms\n\n");
        builder.append(msg);

        testTerminal.setText(builder);
        funStartTime = 0;
    }
}
