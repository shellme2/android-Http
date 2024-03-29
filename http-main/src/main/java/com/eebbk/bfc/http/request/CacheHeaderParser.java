package com.eebbk.bfc.http.request;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;
import com.eebbk.bfc.http.config.BfcHttpConfigure;

import java.util.Map;

/**
 * 自定义的HeaderParser,跟默认的比，可以强制缓存，忽略服务器的设置
 */

public class CacheHeaderParser extends HttpHeaderParser {
    /**
     * Extracts a {@link com.android.volley.Cache.Entry} from a {@link com.android.volley.NetworkResponse}.
     *
     * @param response  The network response to parse headers from
     * @param cacheTime 缓存时间，如果设置了这个值，不管服务器返回是否可以缓存，都会缓存,一天为1000*60*60*24
     * @return a cache entry for the given response, or null if the response is not cacheable.
     */
    public static Cache.Entry parseCacheHeaders(NetworkResponse response, long cacheTime) {
        Cache.Entry entry = parseCacheHeaders(response);
        if (cacheTime <= 0) {
            return entry;
        }

        if (entry == null) {
            long serverDate = 0;
            long lastModified = 0;
            String headerValue;

            headerValue = response.headers.get("Last-Modified");
            if (headerValue != null) {
                lastModified = parseDateAsEpoch(headerValue);
            }
            headerValue = response.headers.get("Date");
            if (headerValue != null) {
                serverDate = parseDateAsEpoch(headerValue);
            }

            entry = new Cache.Entry();
            entry.data = response.data;
            entry.etag = response.headers.get("ETag");
            entry.serverDate = serverDate;
            entry.lastModified = lastModified;
            entry.responseHeaders = response.headers;
        }
        long now = System.currentTimeMillis();
        entry.softTtl = now + cacheTime;
        entry.ttl = entry.softTtl;
        return entry;
    }


    public static String parseCharset(Map<String, String> headers) {
        return parseCharset(headers, BfcHttpConfigure.getDefaultCharset());
    }
}
