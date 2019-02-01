package com.eebbk.bfc.http;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.eebbk.bfc.http.config.BfcHttpConfigure;
import com.eebbk.bfc.http.config.BfcRequestConfigure;
import com.eebbk.bfc.http.dns.BfcHttpDnsRequest;
import com.eebbk.bfc.http.dns.DNSAntiHijackEnv;
import com.eebbk.bfc.http.toolbox.IBfcErrorListener;
import com.eebbk.bfc.http.toolbox.IBfcHttpCallBack;
import com.eebbk.bfc.http.tools.L;

import java.io.File;
import java.util.Map;

/**
 * net request operation class, contain DNS antiHijack caller
 */

public class BfcHttp {

    private static final String BFC_HTTP_TAG = "bfc-http";

    private BfcHttp() {
    }

    /**
     * {@link #get(Context, String, Map, Map, IBfcHttpCallBack, IBfcErrorListener, Object)}
     */
    public static void get(@NonNull Context context, @NonNull String url, Map<String, String> params, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener) {
        get(context, url, params, null, callBack, errorListener, BFC_HTTP_TAG);
    }

    /**
     * {@link #get(Context, String, Map, Map, IBfcHttpCallBack, IBfcErrorListener, Object)}
     */
    public static void get(@NonNull Context context, final @NonNull String url, Map<String, String> params, Map<String, String> header, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener) {
        get(context, url, params, header, callBack, errorListener, BFC_HTTP_TAG);
    }

    /**
     * {@link #get(Context, String, Map, Map, IBfcHttpCallBack, IBfcErrorListener, Object)}
     */
    public static void get(@NonNull Context context, final @NonNull String url, Map<String, String> params, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener, Object tag) {
        get(context, url, params, null, callBack, errorListener, tag);
    }

    /**
     * get
     *
     * @param context       context
     * @param url           request url
     * @param params        request params
     * @param header        current request head
     * @param callBack      request success callback
     * @param errorListener request fail callback
     * @param tag           set tag to current request, use to cancel this request
     */
    public static void get(@NonNull Context context, final @NonNull String url, Map<String, String> params, Map<String, String> header, @NonNull final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener, Object tag) {
        if (callBack == null) {
            throw new IllegalArgumentException("request callback can not be null");
        }

        if (tag == null) {
            tag = BFC_HTTP_TAG;
        }
        BfcHttpConfigure.getBfcHttpStatusWatch().onPreExecute();

        L.v("0. check does need run DNS antiHijack");
        if (DNSAntiHijackEnv.isNeedAntiHijack(url)) {
            L.v("antiHijack get request");
            BfcHttpDnsRequest.getInstance().get(context, url, params, header, callBack, errorListener, tag);
        } else {
            if (BfcHttpConfigure.getCustomHttpRequest() != null) {
                L.v("user custom get request");
                BfcHttpConfigure.getCustomHttpRequest().get(context, url, params, header, callBack, errorListener, tag);
            } else {
                L.v("inner get request");
                BfcHttpRequest.getInstance().get(context, url, params, header, callBack, errorListener, tag);
            }
        }
    }

    /**
     * get
     *
     * @param context       context
     * @param url           request url
     * @param params        request params
     * @param conf          request configure {@link BfcRequestConfigure}
     * @param callBack      request success callback
     * @param errorListener request fail callback
     */
    public static void get(@NonNull Context context, @NonNull String url, Map<String, String> params, @NonNull BfcRequestConfigure conf, @NonNull IBfcHttpCallBack callBack, IBfcErrorListener errorListener) {
        if (callBack == null) {
            throw new IllegalArgumentException("request callback can not be null");
        }

        if (conf == null) {
            throw new IllegalArgumentException("request configure can not be null");
        }
        BfcHttpConfigure.getBfcHttpStatusWatch().onPreExecute();

        L.v("0. check does need run DNS antiHijack");
        if (DNSAntiHijackEnv.isNeedAntiHijack(url)) {
            L.v("antiHijack get request");
            BfcHttpDnsRequest.getInstance().get(context, url, params, conf, callBack, errorListener);
        } else {
            if (BfcHttpConfigure.getCustomHttpRequest() != null) {
                L.v("user custom get request");
                BfcHttpConfigure.getCustomHttpRequest().get(context, url, params, conf, callBack, errorListener);
            } else {
                L.v("inner get request");
                BfcHttpRequest.getInstance().get(context, url, params, conf, callBack, errorListener);
            }
        }
    }

    /**
     * {@link #post(Context, String, Map, Map, IBfcHttpCallBack, IBfcErrorListener, Object)}
     */
    public static void post(@NonNull Context context, @NonNull String url, final Map<String, String> params, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener) {
        post(context, url, params, null, callBack, errorListener, BFC_HTTP_TAG);
    }

    /**
     * {@link #post(Context, String, Map, Map, IBfcHttpCallBack, IBfcErrorListener, Object)}
     */
    public static void post(@NonNull Context context, @NonNull String url, final Map<String, String> params, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener, Object tag) {
        post(context, url, params, null, callBack, errorListener, tag);
    }

    /**
     * {@link #post(Context, String, Map, Map, IBfcHttpCallBack, IBfcErrorListener, Object)}
     */
    public static void post(@NonNull Context context, @NonNull String url, final Map<String, String> params, Map<String, String> header, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener) {
        post(context, url, params, header, callBack, errorListener, BFC_HTTP_TAG);
    }

    /**
     * post
     *
     * @param context       context
     * @param url           request url
     * @param params        request params
     * @param header        current request header
     * @param callBack      request success callback
     * @param errorListener request fail callback
     * @param tag           set tag to current request, use to cancel this request
     */
    public static void post(@NonNull Context context, @NonNull String url, final Map<String, String> params, final Map<String, String> header, @NonNull final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener, Object tag) {
        if (callBack == null) {
            throw new IllegalArgumentException("request callback can not be null");
        }

        if (tag == null) {
            tag = BFC_HTTP_TAG;
        }
        L.v("0. check does need run DNS antiHijack");
        if (DNSAntiHijackEnv.isNeedAntiHijack(url)) {
            L.v("antiHijack post request");
            BfcHttpDnsRequest.getInstance().post(context, url, params, header, callBack, errorListener, tag);
        } else {
            if (BfcHttpConfigure.getCustomHttpRequest() != null) {
                L.v("user custom post request");
                BfcHttpConfigure.getCustomHttpRequest().post(context, url, params, header, callBack, errorListener, tag);
            } else {
                L.v("inner post request");
                BfcHttpRequest.getInstance().post(context, url, params, header, callBack, errorListener, tag);
            }
        }
    }

    /**
     * post
     *
     * @param context       context
     * @param url           request url
     * @param params        request params
     * @param conf          request configure {@link BfcRequestConfigure}
     * @param callBack      request success callback
     * @param errorListener request fail callback
     */
    public static void post(@NonNull Context context, @NonNull String url, Map<String, String> params, @NonNull BfcRequestConfigure conf, @NonNull IBfcHttpCallBack callBack, IBfcErrorListener errorListener) {
        if (callBack == null) {
            throw new IllegalArgumentException("request callback can not be null");
        }

        if (conf == null) {
            throw new IllegalArgumentException("request configure can not be null");
        }
        BfcHttpConfigure.getBfcHttpStatusWatch().onPreExecute();

        L.v("0. check does need run DNS antiHijack");
        if (DNSAntiHijackEnv.isNeedAntiHijack(url)) {
            L.v("antiHijack get request");
            BfcHttpDnsRequest.getInstance().post(context, url, params, conf, callBack, errorListener);
        } else {
            if (BfcHttpConfigure.getCustomHttpRequest() != null) {
                L.v("user custom get request");
                BfcHttpConfigure.getCustomHttpRequest().post(context, url, params, conf, callBack, errorListener);
            } else {
                L.v("inner get request");
                BfcHttpRequest.getInstance().post(context, url, params, conf, callBack, errorListener);
            }
        }
    }

//    /**
//     * {@link #put(Context, String, Map, StringCallBack, IBfcErrorListener, Object)}
//     */
//    public static void put(@NonNull Context context, @NonNull String url, Map<String, String> params, final StringCallBack callBack, final IBfcErrorListener errorListener) {
//        put(context, url, params, callBack, errorListener, BFC_HTTP_TAG);
//    }
//
//    /**
//     * put
//     *
//     * @param context       context
//     * @param url           request url
//     * @param params        request params
//     * @param callBack      request success callback
//     * @param errorListener request fail callback
//     * @param tag           set tag to current request, use to cancel this request
//     */
//    public static void put(@NonNull Context context, final @NonNull String url, Map<String, String> params, final StringCallBack callBack, final IBfcErrorListener errorListener, Object tag) {
//        if (BfcHttpConfigure.getCustomHttpRequest() != null) {
//            L.v("user custom put request");
//            BfcHttpConfigure.getCustomHttpRequest().put(context, url, params, callBack, errorListener, tag);
//        } else {
//            L.v("inner put request");
//            BfcHttpRequest.getInstance().put(context, url, params, callBack, errorListener, tag);
//        }
//    }

//    /**
//     * {@link #delete(Context, String, Map, StringCallBack, IBfcErrorListener, Object)}
//     */
//    public static void delete(@NonNull Context context, @NonNull String url, Map<String, String> params, final StringCallBack callBack, final IBfcErrorListener errorListener) {
//        delete(context, url, params, callBack, errorListener, BFC_HTTP_TAG);
//    }
//
//    /**
//     * delete
//     *
//     * @param context       context
//     * @param url           request url
//     * @param params        request params
//     * @param callBack      request success callback
//     * @param errorListener request fail callback
//     * @param tag           set tag to current request, use to cancel this request
//     */
//    public static void delete(@NonNull Context context, final @NonNull String url, Map<String, String> params, final StringCallBack callBack, final IBfcErrorListener errorListener, Object tag) {
//        if (BfcHttpConfigure.getCustomHttpRequest() != null) {
//            L.v("user custom delete request");
//            BfcHttpConfigure.getCustomHttpRequest().delete(context, url, params, callBack, errorListener, tag);
//        } else {
//            L.v("inner delete request");
//            BfcHttpRequest.getInstance().delete(context, url, params, callBack, errorListener, tag);
//        }
//    }

    /**
     * cancel all request in request queue
     */
    public static void cancelAll() {
        L.v("cancel all request");
        if (BfcHttpConfigure.getHttpRequestQueue() == null) {
            L.e("bfc http request queue is null, call BfcHttpConfigure.init() before cancel request");
            return;
        }
        BfcHttpConfigure.getHttpRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                L.v("cancel all request " + request.getUrl());
                return true;
            }
        });
        //FIXME cannot cancel user custom request
    }

    /**
     * cancel request in request queue by tag
     *
     * @param tag net request tag
     */
    public static void cancelAll(@NonNull Object tag) {
        L.v("cancel all request by tag " + tag);
        if (BfcHttpConfigure.getHttpRequestQueue() == null) {
            L.e("bfc http request queue is null, call BfcHttpConfigure.init() before cancel request");
            return;
        }
        BfcHttpConfigure.getHttpRequestQueue().cancelAll(tag);
    }

    /**
     * clear all cache
     */
    public static void clearCache() {
        L.v("clear all cache ");
        if (BfcHttpConfigure.getHttpRequestQueue() == null) {
            L.e("bfc http request queue is null, call BfcHttpConfigure.init() before clear cache");
            return;
        }
        BfcHttpConfigure.getHttpRequestQueue().getCache().clear();
    }

    /**
     * clear cache by key
     *
     * @param key cache key
     */
    public static void clearCache(@NonNull String key) {
        L.v("clear cache by url: " + key);
        if (BfcHttpConfigure.getHttpRequestQueue() == null) {
            L.e("bfc http request queue is null, call BfcHttpConfigure.init() before clear cache");
            return;
        }
        BfcHttpConfigure.getHttpRequestQueue().getCache().remove(key);
    }

    /**
     * get cache dir
     */
    public static File getCacheDir(@NonNull Context context) {
        return context.getApplicationContext().getCacheDir();
    }
}
