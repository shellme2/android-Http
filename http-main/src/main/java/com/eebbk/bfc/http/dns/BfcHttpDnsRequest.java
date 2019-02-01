package com.eebbk.bfc.http.dns;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.eebbk.bfc.http.config.BfcHttpConfigure;
import com.eebbk.bfc.http.config.BfcRequestConfigure;
import com.eebbk.bfc.http.error.BfcErrorInfo;
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
 * net request with ip cache and httpDNS
 */
public class BfcHttpDnsRequest implements IBfcHttpRequest {

    private static BfcHttpDnsRequest instance = null;

    private BfcHttpDnsRequest() {
    }

    public static BfcHttpDnsRequest getInstance() {
        if (instance == null) {
            instance = new BfcHttpDnsRequest();
        }
        return instance;
    }

    @Override
    public void get(Context context, String url, Map<String, String> params, Map<String, String> header, IBfcHttpCallBack callBack, IBfcErrorListener errorListener, Object tag) {
        if (params != null) {
            url = UrlTools.appendParams(url, params);
        }

        L.v("1. check if ip is valid ");
        if (DNSAntiHijackEnv.isCachedIpValid(context)) {
            L.v("1.1 valid -> 2. replace domain with ip to run net request");
            requestDataWithCachedIpByGet(context, url, header, callBack, errorListener, tag);
        } else {
            L.v("1.2 invalid -> 3. use original url to run net request");
            requestDataWithOriginalDomainByGet(context, url, header, callBack, errorListener, tag);
        }
    }

    @Override
    public void get(Context context, String url, Map<String, String> params, BfcRequestConfigure conf, IBfcHttpCallBack callBack, IBfcErrorListener errorListener) {
        if (params != null) {
            url = UrlTools.appendParams(url, params);
        }

        L.v("1. check if ip is valid ");
        if (DNSAntiHijackEnv.isCachedIpValid(context)) {
            L.v("1.1 valid -> 2. replace domain with ip to run net request");
            requestDataWithCachedIpByGet(context, url, conf, callBack, errorListener);
        } else {
            L.v("1.2 invalid -> 3. use original url to run net request");
            requestDataWithOriginalDomainByGet(context, url, conf, callBack, errorListener);
        }
    }

    private void requestDataWithCachedIpByGet(final Context context, final String url, final Map<String, String> header, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener, final Object tag) {
        String rebuiltUrl = UrlTools.rebuildUrl(url, DNSAntiHijackEnv.getCachedIp(context));

        Request requestWithCachedIp = BfcRequestFactory.generate(rebuiltUrl, Request.Method.GET, null, header, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.v("2.1 request success");
                if (callBack != null) callBack.onResponse(response);
                L.v("END SUCCESS");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.v("2.2 request fail" + error + "-> 3. use original url to run net request");
                requestDataWithOriginalDomainByGet(context, url, header, callBack, errorListener, tag);
            }
        });
        requestWithCachedIp.setTag(tag);
        requestWithCachedIp.setShouldCache(BfcHttpConfigure.shouldCache());
        requestWithCachedIp.setRetryPolicy(new DefaultRetryPolicy(NetStateTools.getSuitableTimeout(context), 1, 1f));

        addQueue(context, requestWithCachedIp, null);
    }

    private void requestDataWithCachedIpByGet(final Context context, final String url, final BfcRequestConfigure conf, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener) {
        String rebuiltUrl = UrlTools.rebuildUrl(url, DNSAntiHijackEnv.getCachedIp(context));

        Request requestWithCachedIp = BfcRequestFactory.generate(rebuiltUrl, Request.Method.GET, null, conf, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.v("2.1 request success");
                if (callBack != null) callBack.onResponse(response);
                L.v("END SUCCESS");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.v("2.2 request fail" + error + "-> 3. use original url to run net request");
                requestDataWithOriginalDomainByGet(context, url, conf, callBack, errorListener);
            }
        });

        addQueue(context, requestWithCachedIp, conf);
    }

    private void requestDataWithOriginalDomainByGet(final Context context, final String url, final Map<String, String> header, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener, final Object tag) {
        Request requestWithOriginalDomain = BfcRequestFactory.generate(url, Request.Method.GET, null, header, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.v("3.1 request success");
                if (callBack != null) callBack.onResponse(response);
                L.v("END SUCCESS");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.v("3.2 request fail " + error + " -> 4. check network");
                if (!NetStateTools.checkNetOpen(context)) {
                    L.v("4.1 network unusual");
                    if (errorListener != null) {
                        errorListener.onError(new BfcHttpError(BfcErrorInfo.ERROR_NETWORK_CODE));
                    }
                    L.v("END ERROR");
                } else {
                    L.v("4.2 network ok -> 5. httpDNS request");
                    DNSAntiHijackEnv.doHttpDnsRequest(url, new HttpDnsRequestCallBack() {
                        @Override
                        public void onSuccess(String httpDnsIp) {
                            L.v("5.1 HttpDNS request success -> 6. replace domain with httpDNS ip to run net request");
                            requestDataWithHttpDnsIpByGet(context, url, httpDnsIp, header, callBack, errorListener, tag);
                        }

                        @Override
                        public void onFail(String failMsg) {
                            L.v("5.2 httpDNS request fail");
                            if (errorListener != null) {
                                errorListener.onError(new BfcHttpError(BfcErrorInfo.ERROR_HTTPDNS_CODE));
                            }
                            L.v("END ERROR");
                        }
                    });
                }
            }
        });
        requestWithOriginalDomain.setTag(tag);
        requestWithOriginalDomain.setShouldCache(BfcHttpConfigure.shouldCache());
        requestWithOriginalDomain.setRetryPolicy(new DefaultRetryPolicy(NetStateTools.getSuitableTimeout(context), 1, 1f));

        addQueue(context, requestWithOriginalDomain, null);
    }

    private void requestDataWithOriginalDomainByGet(final Context context, final String url, final BfcRequestConfigure conf, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener) {
        Request requestWithOriginalDomain = BfcRequestFactory.generate(url, Request.Method.GET, null, conf, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.v("3.1 request success");
                if (callBack != null) callBack.onResponse(response);
                L.v("END SUCCESS");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.v("3.2 request fail " + error + " -> 4. check network");
                if (!NetStateTools.checkNetOpen(context)) {
                    L.v("4.1 network unusual");
                    if (errorListener != null) {
                        errorListener.onError(new BfcHttpError(BfcErrorInfo.ERROR_NETWORK_CODE));
                    }
                    L.v("END ERROR");
                } else {
                    L.v("4.2 network ok -> 5. httpDNS request");
                    DNSAntiHijackEnv.doHttpDnsRequest(url, new HttpDnsRequestCallBack() {
                        @Override
                        public void onSuccess(String httpDnsIp) {
                            L.v("5.1 HttpDNS request success -> 6. replace domain with httpDNS ip to run net request");
                            requestDataWithHttpDnsIpByGet(context, url, httpDnsIp, conf, callBack, errorListener);
                        }

                        @Override
                        public void onFail(String failMsg) {
                            L.v("5.2 httpDNS request fail");
                            if (errorListener != null) {
                                errorListener.onError(new BfcHttpError(BfcErrorInfo.ERROR_HTTPDNS_CODE));
                            }
                            L.v("END ERROR");
                        }
                    });
                }
            }
        });

        addQueue(context, requestWithOriginalDomain, conf);
    }

    private void requestDataWithHttpDnsIpByGet(final Context context, String url, final String httpDnsIp, final Map<String, String> header, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener, Object tag) {
        url = UrlTools.rebuildUrl(url, httpDnsIp);
        Request requestWithHttpDnsIp = BfcRequestFactory.generate(url, Request.Method.GET, null, header, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.v("6.1 request success");
                if (callBack != null) {
                    callBack.onResponse(response);
                }
                L.v("cache ip");
                DNSAntiHijackEnv.cacheIp(context, httpDnsIp);
                L.v("END SUCCESS");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.v("6.2 request fail");
                if (errorListener != null) {
                    errorListener.onError(new BfcHttpError(error));
                }
                L.v("END ERROR" + error);
            }
        });
        requestWithHttpDnsIp.setTag(tag);
        requestWithHttpDnsIp.setShouldCache(BfcHttpConfigure.shouldCache());
        requestWithHttpDnsIp.setRetryPolicy(new DefaultRetryPolicy(NetStateTools.getSuitableTimeout(context), 1, 1f));

        addQueue(context, requestWithHttpDnsIp, null);
    }

    private void requestDataWithHttpDnsIpByGet(final Context context, String url, final String httpDnsIp, BfcRequestConfigure conf, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener) {
        url = UrlTools.rebuildUrl(url, httpDnsIp);
        Request requestWithHttpDnsIp = BfcRequestFactory.generate(url, Request.Method.GET, null, conf, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.v("6.1 request success");
                if (callBack != null) {
                    callBack.onResponse(response);
                }
                L.v("cache ip");
                DNSAntiHijackEnv.cacheIp(context, httpDnsIp);
                L.v("END SUCCESS");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.v("6.2 request fail");
                if (errorListener != null) {
                    errorListener.onError(new BfcHttpError(error));
                }
                L.v("END ERROR" + error);
            }
        });

        addQueue(context, requestWithHttpDnsIp, conf);
    }

    @Override
    public void post(Context context, String url, Map<String, String> params, Map<String, String> header, IBfcHttpCallBack callBack, IBfcErrorListener errorListener, Object tag) {
        if (params == null || params.isEmpty()) {
            throw new RuntimeException("params is null");
        }
        L.v("1. check if ip is valid ");
        if (DNSAntiHijackEnv.isCachedIpValid(context)) {
            L.v("1.1 valid -> 2. replace domain with ip to run net request");
            requestDataWithCachedIpByPost(context, url, params, header, callBack, errorListener, tag);
        } else {
            L.v("1.2 invalid -> 3. use original url to run net request");
            requestDataWithOriginalDomainByPost(context, url, params, header, callBack, errorListener, tag);
        }
    }

    @Override
    public void post(Context context, String url, Map<String, String> params, BfcRequestConfigure conf, IBfcHttpCallBack callBack, IBfcErrorListener errorListener) {
        if (params == null || params.isEmpty()) {
            throw new RuntimeException("params is null");
        }
        L.v("1. check if ip is valid ");
        if (DNSAntiHijackEnv.isCachedIpValid(context)) {
            L.v("1.1 valid -> 2. replace domain with ip to run net request");
            requestDataWithCachedIpByPost(context, url, params, conf, callBack, errorListener);
        } else {
            L.v("1.2 invalid -> 3. use original url to run net request");
            requestDataWithOriginalDomainByPost(context, url, params, conf, callBack, errorListener);
        }
    }

    private void requestDataWithCachedIpByPost(final Context context, final String url, final Map<String, String> params, final Map<String, String> header, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener, final Object tag) {
        String rebuiltUrl = UrlTools.rebuildUrl(url, DNSAntiHijackEnv.getCachedIp(context));
        Request requestWithCachedIp = BfcRequestFactory.generate(rebuiltUrl, Request.Method.POST, params, header, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.v("2.1 request success");
                if (callBack != null) {
                    callBack.onResponse(response);
                }
                L.v("END SUCCESS");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.v("2.2 request fail" + error + " -> 3. use original url to run net request");
                requestDataWithOriginalDomainByPost(context, url, params, header, callBack, errorListener, tag);
            }
        });
        requestWithCachedIp.setTag(tag);
        requestWithCachedIp.setShouldCache(BfcHttpConfigure.shouldCache());
        requestWithCachedIp.setRetryPolicy(new DefaultRetryPolicy(NetStateTools.getSuitableTimeout(context), 1, 1f));

        addQueue(context, requestWithCachedIp, null);
    }

    private void requestDataWithCachedIpByPost(final Context context, final String url, final Map<String, String> params, final BfcRequestConfigure conf, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener) {
        String rebuiltUrl = UrlTools.rebuildUrl(url, DNSAntiHijackEnv.getCachedIp(context));
        Request requestWithCachedIp = BfcRequestFactory.generate(rebuiltUrl, Request.Method.POST, params, conf, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.v("2.1 request success");
                if (callBack != null) {
                    callBack.onResponse(response);
                }
                L.v("END SUCCESS");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.v("2.2 request fail" + error + " -> 3. use original url to run net request");
                requestDataWithOriginalDomainByPost(context, url, params, conf, callBack, errorListener);
            }
        });

        addQueue(context, requestWithCachedIp, conf);
    }

    private void requestDataWithOriginalDomainByPost(final Context context, final String url, final Map<String, String> params, final Map<String, String> header, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener, final Object tag) {
        Request requestWithOriginalDomain = BfcRequestFactory.generate(url, Request.Method.POST, params, header, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.v("3.1 request success");
                if (callBack != null) {
                    callBack.onResponse(response);
                }
                L.v("END SUCCESS");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.v("3.2 request fail " + error + " -> 4. check network");
                if (!NetStateTools.checkNetOpen(context)) {
                    L.v("4.1 network unusual");
                    if (errorListener != null) {
                        errorListener.onError(new BfcHttpError(BfcErrorInfo.ERROR_NETWORK_CODE));
                    }
                    L.v("END ERROR");
                } else {
                    L.v("4.2 network ok -> 5. httpDNS request");
                    DNSAntiHijackEnv.doHttpDnsRequest(url, new HttpDnsRequestCallBack() {
                        @Override
                        public void onSuccess(String httpDnsIp) {
                            L.v("5.1 HttpDNS request success -> 6. replace domain with httpDNS ip to run net request");
                            requestDataWithHttpDnsIpByPost(context, url, params, header, httpDnsIp, callBack, errorListener, tag);
                        }

                        @Override
                        public void onFail(String failMsg) {
                            L.v("5.2 httpDNS request fail");
                            if (errorListener != null) {
                                errorListener.onError(new BfcHttpError(BfcErrorInfo.ERROR_HTTPDNS_CODE));
                            }
                            L.v("END ERROR");
                        }
                    });
                }
            }
        });
        requestWithOriginalDomain.setTag(tag);
        requestWithOriginalDomain.setShouldCache(BfcHttpConfigure.shouldCache());
        requestWithOriginalDomain.setRetryPolicy(new DefaultRetryPolicy(NetStateTools.getSuitableTimeout(context), 1, 1f));

        addQueue(context, requestWithOriginalDomain, null);
    }

    private void requestDataWithOriginalDomainByPost(final Context context, final String url, final Map<String, String> params, final BfcRequestConfigure conf, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener) {
        Request requestWithOriginalDomain = BfcRequestFactory.generate(url, Request.Method.POST, params, conf, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.v("3.1 request success");
                if (callBack != null) {
                    callBack.onResponse(response);
                }
                L.v("END SUCCESS");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.v("3.2 request fail " + error + " -> 4. check network");
                if (!NetStateTools.checkNetOpen(context)) {
                    L.v("4.1 network unusual");
                    if (errorListener != null) {
                        errorListener.onError(new BfcHttpError(BfcErrorInfo.ERROR_NETWORK_CODE));
                    }
                    L.v("END ERROR");
                } else {
                    L.v("4.2 network ok -> 5. httpDNS request");
                    DNSAntiHijackEnv.doHttpDnsRequest(url, new HttpDnsRequestCallBack() {
                        @Override
                        public void onSuccess(String httpDnsIp) {
                            L.v("5.1 HttpDNS request success -> 6. replace domain with httpDNS ip to run net request");
                            requestDataWithHttpDnsIpByPost(context, url, params, conf, httpDnsIp, callBack, errorListener);
                        }

                        @Override
                        public void onFail(String failMsg) {
                            L.v("5.2 httpDNS request fail");
                            if (errorListener != null) {
                                errorListener.onError(new BfcHttpError(BfcErrorInfo.ERROR_HTTPDNS_CODE));
                            }
                            L.v("END ERROR");
                        }
                    });
                }
            }
        });

        addQueue(context, requestWithOriginalDomain, conf);
    }


    private void requestDataWithHttpDnsIpByPost(final Context context, String url, final Map<String, String> params, final Map<String, String> header, final String httpDnsIp, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener, Object tag) {
        url = UrlTools.rebuildUrl(url, httpDnsIp);
        Request requestWithHttpDnsIp = BfcRequestFactory.generate(url, Request.Method.POST, params, header, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.v("6.1 request success");
                if (callBack != null) {
                    callBack.onResponse(response);
                }
                L.v("cache ip");
                DNSAntiHijackEnv.cacheIp(context, httpDnsIp);
                L.v("END SUCCESS");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.v("6.2 request fail");
                if (errorListener != null) {
                    errorListener.onError(new BfcHttpError(error));
                }
                L.v("END ERROR" + error);
            }
        });
        requestWithHttpDnsIp.setTag(tag);
        requestWithHttpDnsIp.setShouldCache(BfcHttpConfigure.shouldCache());
        requestWithHttpDnsIp.setRetryPolicy(new DefaultRetryPolicy(NetStateTools.getSuitableTimeout(context), 1, 1f));

        addQueue(context, requestWithHttpDnsIp, null);
    }

    private void requestDataWithHttpDnsIpByPost(final Context context, String url, final Map<String, String> params, BfcRequestConfigure conf, final String httpDnsIp, final IBfcHttpCallBack callBack, final IBfcErrorListener errorListener) {
        url = UrlTools.rebuildUrl(url, httpDnsIp);
        Request requestWithHttpDnsIp = BfcRequestFactory.generate(url, Request.Method.POST, params, conf, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.v("6.1 request success");
                if (callBack != null) {
                    callBack.onResponse(response);
                }
                L.v("cache ip");
                DNSAntiHijackEnv.cacheIp(context, httpDnsIp);
                L.v("END SUCCESS");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.v("6.2 request fail");
                if (errorListener != null) {
                    errorListener.onError(new BfcHttpError(error));
                }
                L.v("END ERROR" + error);
            }
        });

        addQueue(context, requestWithHttpDnsIp, conf);
    }

    private void addQueue(Context context, Request request, BfcRequestConfigure conf) {
        if (BfcHttpConfigure.getHttpRequestQueue() == null) {
            BfcHttpConfigure.initRequestQueue(context.getApplicationContext());
        }
        BfcHttpConfigure.getHttpRequestQueue().add(request);
    }
}
