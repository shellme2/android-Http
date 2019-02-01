package com.example.http.toolbox;

import android.content.Context;
import android.util.Log;

import com.eebbk.bfc.http.config.BfcRequestConfigure;
import com.eebbk.bfc.http.toolbox.IBfcErrorListener;
import com.eebbk.bfc.http.toolbox.IBfcHttpCallBack;
import com.eebbk.bfc.http.toolbox.IBfcHttpRequest;

import java.util.Map;

/**
 * 自定义网络请求实现类
 */

public class CustomHttpRequest implements IBfcHttpRequest {

    private static final String TAG = "CustomHttpRequest";

    @Override
    public void get(Context context, String url, Map<String, String> params, Map<String, String> header, IBfcHttpCallBack callBack, IBfcErrorListener errorListener, Object tag) {
        Log.d(TAG, "get: ");
    }

    @Override
    public void get(Context context, String url, Map<String, String> params, BfcRequestConfigure conf, IBfcHttpCallBack callBack, IBfcErrorListener errorListener) {
        Log.d(TAG, "get: ");
    }

    @Override
    public void post(Context context, String url, Map<String, String> params, Map<String, String> header, IBfcHttpCallBack callBack, IBfcErrorListener errorListener, Object tag) {
        Log.d(TAG, "post: ");
    }

    @Override
    public void post(Context context, String url, Map<String, String> params, BfcRequestConfigure conf, IBfcHttpCallBack callBack, IBfcErrorListener errorListener) {
        Log.d(TAG, "post: ");
    }
}
