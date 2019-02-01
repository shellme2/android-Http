package com.eebbk.bfc.http.request;

import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.eebbk.bfc.http.config.BfcHttpConfigure;
import com.eebbk.bfc.http.tools.L;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.zip.GZIPInputStream;

/**
 * Gzip 请求.
 */

public class GZipRequest extends StringRequest {

    private boolean isEnableGZip = false;
    private long expiredTime;

    public GZipRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public GZipRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String output = "";
        try {
            if (BfcHttpConfigure.debugMode()) {
                printResponseInfo(response);
            }
            String contentType = response.headers.get("Content-Encoding");
            if (!TextUtils.isEmpty(contentType) && contentType.equalsIgnoreCase("gzip") && isEnableGZip) {
                GZIPInputStream gStream = new GZIPInputStream(new ByteArrayInputStream(response.data));
                InputStreamReader reader = new InputStreamReader(gStream);
                BufferedReader in = new BufferedReader(reader);
                String read;
                while ((read = in.readLine()) != null) {
                    output += read;
                }
                reader.close();
                in.close();
                gStream.close();
            } else {
                output = new String(response.data, CacheHeaderParser.parseCharset(response.headers));
            }
        } catch (IOException e) {
            return Response.error(new ParseError());
        }
        return Response.success(output, CacheHeaderParser.parseCacheHeaders(response, expiredTime));
    }

    public void enableGZip() {
        isEnableGZip = true;
    }

    public void disableGZip() {
        isEnableGZip = false;
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
