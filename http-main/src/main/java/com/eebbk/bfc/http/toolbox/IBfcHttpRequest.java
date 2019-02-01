package com.eebbk.bfc.http.toolbox;

import android.content.Context;
import android.support.annotation.NonNull;

import com.eebbk.bfc.http.config.BfcRequestConfigure;

import java.util.Map;

/**
 * net request interface
 */

public interface IBfcHttpRequest {
    /**
     * get
     *
     * @param context       context
     * @param url           request url
     * @param params        request params
     * @param callBack      request success callback
     * @param errorListener request fail callback
     * @param tag           set tag to current request, use to cancel this request
     */
    void get(@NonNull Context context, @NonNull String url, Map<String, String> params, Map<String, String> header, IBfcHttpCallBack callBack, IBfcErrorListener errorListener, Object tag);

    void get(@NonNull Context context, @NonNull String url, Map<String, String> params, BfcRequestConfigure conf, IBfcHttpCallBack callBack, IBfcErrorListener errorListener);

    /**
     * post
     *
     * @param context       context
     * @param url           request url
     * @param params        request params
     * @param callBack      request success callback
     * @param errorListener request fail callback
     * @param tag           set tag to current request, use to cancel this request
     */
    void post(@NonNull Context context, @NonNull String url, Map<String, String> params, Map<String, String> header, IBfcHttpCallBack callBack, IBfcErrorListener errorListener, Object tag);

    void post(@NonNull Context context, @NonNull String url, Map<String, String> params, BfcRequestConfigure conf, IBfcHttpCallBack callBack, IBfcErrorListener errorListener);
}
