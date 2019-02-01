package com.eebbk.bfc.http.request;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.eebbk.bfc.http.config.BfcHttpConfigure;
import com.eebbk.bfc.http.config.BfcRequestConfigure;
import com.eebbk.bfc.http.tools.L;

import java.util.Map;

/**
 * request generate factory, now support generate request as
 * <p>
 * {@link DefaultRequest} link to {@link com.eebbk.bfc.http.config.BfcRequestConfigure.Type#STRING},<br/>
 * {@link GZipRequest} link to {@link com.eebbk.bfc.http.config.BfcRequestConfigure.Type#GZIP},<br/>
 * <p>
 * Simple Factory Pattern (Static FactoryMethod Pattern)
 */

public class BfcRequestFactory {

    private BfcRequestFactory() {
    }

    public static Request generate(String url, int method, final Map<String, String> params, final Map<String, String> header, Response.Listener listener, Response.ErrorListener errorListener) {
        DefaultRequest defaultRequest = new DefaultRequest(method, url, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (header == null) {
                    return super.getHeaders();
                }
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (params == null) {
                    return super.getParams();
                }
                return params;
            }
        };
        if (BfcHttpConfigure.debugMode()) {
            printRequestInfo(defaultRequest);
        }
        return defaultRequest;
    }

    public static Request generate(String url, int method, final Map<String, String> params, final BfcRequestConfigure conf, Response.Listener listener, Response.ErrorListener errorListener) {
        switch (conf.getType()) {
            case CACHE_ABLE:
            case STRING:
                DefaultRequest defaultRequest = new DefaultRequest(method, url, listener, errorListener) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        if (params == null) {
                            return super.getParams();
                        }
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        if (conf.getHeader() == null) {
                            return super.getHeaders();
                        }
                        return conf.getHeader();
                    }

                    @Override
                    public String getCacheKey() {
                        if (TextUtils.isEmpty(conf.getCacheKey())) {
                            return super.getCacheKey();
                        }
                        return conf.getCacheKey();
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        if (conf.getBody() != null && conf.getBody().length > 0) {
                            return conf.getBody();
                        }
                        return super.getBody();
                    }
                };
                defaultRequest.setTag(conf.getTag());
                defaultRequest.setShouldCache(conf.isCacheAble());
                defaultRequest.setExpiredTime(conf.getExpiredTime());
                defaultRequest.setRetryPolicy(new DefaultRetryPolicy(conf.getTimeout(), conf.getRetryTimes(), 1f));
                if (BfcHttpConfigure.debugMode()) {
                    printRequestInfo(defaultRequest);
                }
                return defaultRequest;
            case GZIP:
                GZipRequest gzipRequest = new GZipRequest(method, url, listener, errorListener) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        if (conf.getHeader() == null) {
                            return super.getHeaders();
                        }
                        return conf.getHeader();
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        if (params == null) {
                            return super.getParams();
                        }
                        return params;
                    }

                    @Override
                    public String getCacheKey() {
                        if (TextUtils.isEmpty(conf.getCacheKey())) {
                            return super.getCacheKey();
                        }
                        return conf.getCacheKey();
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        if (conf.getBody() != null && conf.getBody().length > 0) {
                            return conf.getBody();
                        }
                        return super.getBody();
                    }
                };
                gzipRequest.enableGZip();
                gzipRequest.setTag(conf.getTag());
                gzipRequest.setShouldCache(conf.isCacheAble());
                gzipRequest.setExpiredTime(conf.getExpiredTime());
                gzipRequest.setRetryPolicy(new DefaultRetryPolicy(conf.getTimeout(), conf.getRetryTimes(), 1f));
                if (BfcHttpConfigure.debugMode()) {
                    printRequestInfo(gzipRequest);
                }
                return gzipRequest;
        }
        throw new IllegalArgumentException();
    }

    private static void printRequestInfo(Request request) {
        L.d("Client: url > " + request.getUrl());
        switch (request.getMethod()) {
            case Request.Method.GET:
                L.d("Client: method > GET");
                break;
            case Request.Method.POST:
                L.d("Client: method > POST");
                break;
            default:
                L.d("Client: method > OTHER");
                break;
        }
        try {
            L.d("Client: header > " + request.getHeaders().toString());
        } catch (AuthFailureError authFailureError) {
            L.d("Client: header is empty");
        }
        try {
            L.d("Client: body > " + new String(request.getBody(), "UTF-8"));
        } catch (Exception e) {
            L.d("Client: body is empty");
        }
        L.d("Client: bodyContentType > " + request.getBodyContentType());
        L.d("Client: tag > " + request.getTag());
        L.d("Client: timeout > " + request.getTimeoutMs());
        L.d("Client: cacheKey > " + request.getCacheKey());
    }
}
