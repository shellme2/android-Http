package com.eebbk.bfc.http.dns;

import android.content.Context;
import android.support.annotation.NonNull;

import com.eebbk.bfc.http.config.BfcHttpConfigure;
import com.eebbk.bfc.http.tools.L;
import com.eebbk.bfc.http.tools.UrlTools;
import com.eebbk.bfc.http.tools.UserPreferences;

import java.util.concurrent.Executors;

/**
 * DNS antiHijack config class
 */

public class DNSAntiHijackEnv {

    private static final String CACHED_IP = "bfc_cached_httpDns_ip";
    private static final String CACHED_TIMESTAMP = "bfc_cached_httpDns_ip_timestamp";

    static String getCachedIp(@NonNull Context context) {
        return UserPreferences.loadString(context.getApplicationContext(), CACHED_IP, "");
    }

    static boolean isCachedIpValid(@NonNull Context context) {
        String cacheIp = UserPreferences.loadString(context.getApplicationContext(), CACHED_IP, "");
        if (!UrlTools.isIpValid(cacheIp)) {
            L.v("no cache ip or cached ip is not standard ip");
            return false;
        }
        long timestamp = UserPreferences.loadLong(context.getApplicationContext(), CACHED_TIMESTAMP, -1);
        if (timestamp < 0 || Math.abs(System.currentTimeMillis() - timestamp) > 24 * 60 * 60 * 1000) {
            L.v("no cached timestamp or cached timestamp is invalid");
            return false;
        }
        return true;
    }

    public static void cacheIp(@NonNull Context context, @NonNull String ip) {
        L.i("cache ip: " + ip);
        UserPreferences.saveLong(context.getApplicationContext(), CACHED_TIMESTAMP, System.currentTimeMillis());
        UserPreferences.saveString(context.getApplicationContext(), CACHED_IP, ip);
    }

    @Deprecated
    public static void cacheIp(@NonNull Context context, @NonNull String ip, int timestamp) {
        L.i("cache ip: " + ip);
        UserPreferences.saveLong(context.getApplicationContext(), CACHED_TIMESTAMP, System.currentTimeMillis() - timestamp);
        UserPreferences.saveString(context.getApplicationContext(), CACHED_IP, ip);
    }

    /**
     * check does need run DNS antiHijack(if its open DNS antiHijack, if antiHijack domains contain url's domain)
     *
     * @param url request url
     * @return {@code true} need  {@code false}  do not need
     */
    public static boolean isNeedAntiHijack(@NonNull String url) {
        if (!BfcHttpConfigure.isEnableDNSAntiHijack() || BfcHttpConfigure.getDomains() == null || BfcHttpConfigure.getDomains().size() <= 0) {
            return false;
        }
        if (!BfcHttpConfigure.getDomains().contains(UrlTools.getDomain(url))) {
            return false;
        }
        return true;
    }

    static void doHttpDnsRequest(@NonNull final String url, @NonNull final HttpDnsRequestCallBack callBack) {
        new HttpDNSAsyncTask(callBack).executeOnExecutor(Executors.newSingleThreadExecutor(), UrlTools.getDomain(url));
    }
}
