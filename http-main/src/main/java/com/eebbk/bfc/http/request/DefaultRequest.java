package com.eebbk.bfc.http.request;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.eebbk.bfc.http.config.BfcHttpConfigure;
import com.eebbk.bfc.http.tools.L;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Default Request extends from {@link StringRequest}
 */

public class DefaultRequest extends StringRequest {

    private long expiredTime;

    public DefaultRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public DefaultRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            if (BfcHttpConfigure.debugMode()) {
                printResponseInfo(response);
            }
            parsed = new String(response.data, CacheHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, CacheHeaderParser.parseCacheHeaders(response, expiredTime));
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    private void printResponseInfo(NetworkResponse response) {
        if (response == null) {
            L.d("Server: response null");
            return;
        }
        L.d("Server: status > " + response.statusCode);
        L.d("Server: header > " + response.headers);
        L.d("Server: data > " + response.data.length + "    " + new String(response.data));
        L.d("Server: networkTimeMs > " + response.networkTimeMs);
        Cache.Entry entry = CacheHeaderParser.parseCacheHeaders(response, expiredTime);
        if (entry != null) {
            L.d("Server: cache need refresh time > " + new Date(entry.softTtl));
            L.d("Server: cache expires time > " + new Date(entry.ttl));
            L.d("Server: cache isExpired > " + entry.isExpired());
            L.d("Server: cache refreshNeeded > " + entry.refreshNeeded());
            L.d("Server: cache last modified > " + new Date(entry.lastModified));
            L.d("Server: cache server data > " + new Date(entry.serverDate));
        } else {
            L.d("Server: server response header contain \"no-cache\" or \"on store\" disable cache.");
        }
    }
}
