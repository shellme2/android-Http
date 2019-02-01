package com.example.http.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eebbk.bfc.http.BfcHttp;
import com.eebbk.bfc.http.config.BfcRequestConfigure;
import com.eebbk.bfc.http.error.BfcHttpError;
import com.eebbk.bfc.http.toolbox.IBfcErrorListener;
import com.eebbk.bfc.http.toolbox.StringCallBack;
import com.example.http.R;
import com.example.http.SSLHttpStack;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class BfcHttpsTestActivity extends Activity {

    private static final String TAG = "BfcHttpsTestActivity";
    private static RequestQueue queue;
    private static String url = "";
    private static TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bfc_https_test);
        queue = Volley.newRequestQueue(this, new SSLHttpStack(this));
        textView = (TextView) findViewById(R.id.requestResult);
        RequestQueue mQueue = Volley.newRequestQueue(BfcHttpsTestActivity.this);
        StringRequest stringRequest = new StringRequest("http://www.baidu.com",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });

        mQueue.add(stringRequest);

    }

    public static void testHttps(View view) {
        textView.setText("...");
        queue.add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response);
                textView.setText(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
                textView.setText(error.toString() +" " + System.currentTimeMillis());
            }
        }));
    }

    public void testHttpsCA(View view) {
        X509Certificate ca = null;
        InputStream caInput = this.getResources().openRawResource(R.raw.bbk_parent);
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            ca = (X509Certificate) cf.generateCertificate(caInput);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                caInput.close();
            } catch (Exception e) {

            }
        }
    }


    public static void setHttpsUrl1(View view) {
        url = "https://kyfw.12306.cn/otn/";
    }

    public static void setHttpsUrl2(View view) {
        url = "https://github.com/";
    }

    public static void setHttpsUrl3(View view) {
        url = "https://maps.googleapis.com";
    }

    public static void setHttpsUrl4(View view) {
        url = "https://www.baidu.com/";
    }

    public static void setHttpsUrl5(View view) {
        url = "http://test.eebbk.net/onlinelessonM/appCoursePackage/getCoursePackageById?coursePackageId=1004";
    }
}
