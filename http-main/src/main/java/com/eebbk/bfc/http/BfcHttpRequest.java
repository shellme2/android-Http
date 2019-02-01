package com.eebbk.bfc.http;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.eebbk.bfc.http.config.BfcHttpConfigure;
import com.eebbk.bfc.http.config.BfcRequestConfigure;
import com.eebbk.bfc.http.error.BfcHttpError;
import com.eebbk.bfc.http.request.BfcRequestFactory;
import com.eebbk.bfc.http.toolbox.IBfcErrorListener;
import com.eebbk.bfc.http.toolbox.IBfcHttpCallBack;
import com.eebbk.bfc.http.toolbox.IBfcHttpRequest;
import com.eebbk.bfc.http.tools.L;
import com.eebbk.bfc.http.tools.NetStateTools;
import com.eebbk.bfc.http.tools.UrlTools;

import java.util.Map;

/**
 * http request realize class
 */

public class BfcHttpRequest implements IBfcHttpRequest {

    private static final String TAG = "BFC_HTTP";

    private static BfcHttpRequest instance = null;

    private BfcHttpRequest() {
    }

    public static BfcHttpRequest getInstance() {
        if (instance == null) {
            instance = new BfcHttpRequest();
        }
        return instance;
    }

    @Override
    public void get(Context context, String url, Map<String, String> params, final Map<String, String> header, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener, Object tag) {
        if (params != null) {
            url = UrlTools.appendParams(url, params);
        }

        Request request = BfcRequestFactory.generate(url, Request.Method.GET, null, header, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.v("onResponse END");
                if (callBack != null) {
                    callBack.onResponse(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorListener != null) {
                    L.v("onErrorResponse END" + error);
                    errorListener.onError(new BfcHttpError(error));
                }
            }
        });

        request.setTag(tag);
        request.setShouldCache(BfcHttpConfigure.shouldCache());
        request.setRetryPolicy(new DefaultRetryPolicy(NetStateTools.getSuitableTimeout(context), 1, 1f));

        // FIXME redundant
        if (BfcHttpConfigure.getHttpRequestQueue() == null) {
            BfcHttpConfigure.initRequestQueue(context.getApplicationContext());
        }
        BfcHttpConfigure.getHttpRequestQueue().add(request);
    }

    @Override
    public void get(Context context, String url, Map<String, String> params, final BfcRequestConfigure conf, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener) {
        if (params != null) {
            url = UrlTools.appendParams(url, params);
        }

        Request request = BfcRequestFactory.generate(url, Request.Method.GET, null, conf, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.v("onResponse END");
                if (callBack != null) {
                    callBack.onResponse(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorListener != null) {
                    L.v("onErrorResponse END" + error);
                    errorListener.onError(new BfcHttpError(error));
                }
            }
        });

        // FIXME redundant
        if (BfcHttpConfigure.getHttpRequestQueue() == null) {
            BfcHttpConfigure.initRequestQueue(context.getApplicationContext());
        }
        BfcHttpConfigure.getHttpRequestQueue().add(request);
    }

    @Override
    public void post(Context context, String url, final Map<String, String> params, final Map<String, String> header, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener, Object tag) {
        Request request = BfcRequestFactory.generate(url, Request.Method.POST, params, header, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.v("onResponse END");
                if (callBack != null) {
                    callBack.onResponse(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorListener != null) {
                    L.v("onErrorResponse END" + error);
                    errorListener.onError(new BfcHttpError(error));
                }
            }
        });

        request.setTag(tag);
        request.setShouldCache(BfcHttpConfigure.shouldCache());
        request.setRetryPolicy(new DefaultRetryPolicy(NetStateTools.getSuitableTimeout(context), 1, 1f));

        // FIXME redundant
        if (BfcHttpConfigure.getHttpRequestQueue() == null) {
            BfcHttpConfigure.initRequestQueue(context.getApplicationContext());
        }
        BfcHttpConfigure.getHttpRequestQueue().add(request);
    }

    @Override
    public void post(Context context, String url, Map<String, String> params, final BfcRequestConfigure conf, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener) {
        if (params == null || params.isEmpty()) {
            throw new RuntimeException("params is null");
        }
        Request request = BfcRequestFactory.generate(url, Request.Method.POST, params, conf, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.v("onResponse END");
                if (callBack != null) {
                    callBack.onResponse(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorListener != null) {
                    L.v("onErrorResponse END" + error);
                    errorListener.onError(new BfcHttpError(error));
                }
            }
        });

        request.setTag(conf.getTag());
        request.setShouldCache(conf.isCacheAble());
        request.setRetryPolicy(new DefaultRetryPolicy(conf.getTimeout(), conf.getRetryTimes(), 1f));

        // FIXME redundant
        if (BfcHttpConfigure.getHttpRequestQueue() == null) {
            BfcHttpConfigure.initRequestQueue(context.getApplicationContext());
        }
        BfcHttpConfigure.getHttpRequestQueue().add(request);
    }
}
