package com.eebbk.bfc.http.tools;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

/**
 * URL tools
 */

public class UrlTools {

    private UrlTools() {
    }

    /**
     * get url's domain
     *
     * @param url net request url（url should start with "http://" or "https://"）
     * @return domain
     */
    public static String getDomain(@NonNull String url) {
        URL tempUrl;
        String host = "";
        try {
            tempUrl = new URL(url);
            host = tempUrl.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return host;
    }

    /**
     * check domain - "\b([a-z0-9]+(-[a-z0-9]+)*\.)+[a-z]{2,}\b"
     */
    public static boolean isDomainValid(@NonNull String domain) {
        return domain != null && domain.length() > 0 && domain.matches("\\b([a-z0-9]+(-[a-z0-9]+)*\\.)+[a-z]{2,}\\b");
    }

    /**
     * check ip - "\b(?:[0-9]{1,3}\.){3}[0-9]{1,3}\b"
     */
    public static boolean isIpValid(@NonNull String ip) {
        return ip != null && ip.length() > 0 && ip.matches("\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b");
    }

    /**
     * replace url's domain
     */
    public static String rebuildUrl(@NonNull String url, @NonNull String replacedDomain) {
        String rebuiltUrl = url;
        try {
            URL originalURL = new URL(url);
            URL newURL = new URL(originalURL.getProtocol(), replacedDomain, originalURL.getFile());
            rebuiltUrl = newURL.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return rebuiltUrl;
    }

    /**
     * add params into url
     */
    public static String appendParams(@NonNull String url, Map<String, String> params) {
        if (url == null || params == null || params.isEmpty()) {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        for (String key : keys) {
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().toString();
    }
}
