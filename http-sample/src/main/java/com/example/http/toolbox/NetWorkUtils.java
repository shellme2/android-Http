package com.example.http.toolbox;

import android.content.Context;

import com.eebbk.bfc.http.BfcHttp;
import com.eebbk.bfc.http.config.BfcRequestConfigure;
import com.eebbk.bfc.http.toolbox.IBfcErrorListener;
import com.eebbk.bfc.http.toolbox.StringCallBack;

import java.util.HashMap;
import java.util.Map;

/**
 * 网络请求工具类
 */

public class NetWorkUtils {

    private static Map<String, String> globalHeader = new HashMap<>();

    static {
        globalHeader.put("Accept-Encoding", "gzip");
    }

    /**
     * 全部参数列表
     */
    public static void get(Context context, String url, Map<String, String> params, Map<String, String> header, StringCallBack callBack, IBfcErrorListener errorListener, Object tag) {
        BfcHttp.get(context, url, params, header, callBack, errorListener, tag);
    }

    /**
     * 最简参数列表
     */
    public static void get(Context context, String url, Map<String, String> params, StringCallBack callBack, IBfcErrorListener errorListener) {
        BfcHttp.get(context, url, params, callBack, errorListener);
    }

    /**
     * 使用BfcRequestConfigure配置
     */
    public static void get(Context context, String url, Map<String, String> params, BfcRequestConfigure conf, StringCallBack callBack, IBfcErrorListener errorListener) {
        BfcHttp.get(context, url, params, conf, callBack, errorListener);
    }

    /**
     * 全部参数列表
     */
    public static void post(Context context, String url, Map<String, String> params, Map<String, String> header, StringCallBack callBack, IBfcErrorListener errorListener, Object tag) {
        BfcHttp.post(context, url, params, header, callBack, errorListener, tag);
    }

    /**
     * 最简参数列表
     */
    public static void post(Context context, String url, Map<String, String> params, StringCallBack callBack, IBfcErrorListener errorListener) {
        BfcHttp.post(context, url, params, callBack, errorListener);
    }

    /**
     * 使用BfcRequestConfigure配置
     */
    public static void post(Context context, String url, Map<String, String> params, BfcRequestConfigure conf, StringCallBack callBack, IBfcErrorListener errorListener) {
        BfcHttp.post(context, url, params, conf, callBack, errorListener);
    }

    public static void cancelAll() {
        BfcHttp.cancelAll();
    }

    public static void cancelAll(Object obj) {
        BfcHttp.cancelAll(obj);
    }
}
