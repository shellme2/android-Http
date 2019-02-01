package com.eebbk.bfc.http.config;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;
import com.eebbk.bfc.http.SDKVersion;
import com.eebbk.bfc.http.toolbox.DefaultHttpStatusWatch;
import com.eebbk.bfc.http.toolbox.IBfcHttpRequest;
import com.eebbk.bfc.http.toolbox.IBfcHttpStatusWatch;
import com.eebbk.bfc.http.tools.L;
import com.eebbk.bfc.http.tools.UrlTools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * NetRequest global config.
 */

public class BfcHttpConfigure {

    private static final String TAG = "BfcHttpConfigure";

    /**
     * Wifi、HSUPA、HSPA、LTE high network timeout
     */
    private static int TIMEOUT_FAST = 5000;
    /**
     * UMTS、EVDO revison A、EVDO revison B、HSDPA middle network timeout
     */
    private static int TIMEOUT_MIDDLE = 7500;
    /**
     * GPRS、EDGE、CDMA、EVDO revison 0、1xRTT、iDen、eHRPO low network timeout
     */
    private static int TIMEOUT_SLOW = 10000;
    /**
     * is open DNS antiHijack
     */
    private static boolean isEnableDNSAntiHijack = false;
    /**
     * net request queue
     */
    private static RequestQueue mVolleyRequestQueue;
    /**
     * domains which need to run DNS antiHijack
     */
    private static final List<String> DNSAntiHijackDomains = new ArrayList<>();
    /**
     * is enable to set different timeout value with different network type
     */
    private static boolean enableMultiTimeout = false;
    /**
     * user custom net request interface
     */
    private static IBfcHttpRequest mCustomHttpRequest;
    /**
     * debug mode
     */
    private static boolean DEBUG = false;
    /**
     * http request cache
     */
    private static boolean SHOULD_CACHE = true;
    /**
     * default response charset, default is utf-8
     */
    private static String defaultCharset = "utf-8";
    /**
     * net request status watcher
     */
    private static IBfcHttpStatusWatch mBfcHttpStatusWatch = new DefaultHttpStatusWatch();

    private static String mHttpsDnsServerIp = "119.29.29.29";

    private BfcHttpConfigure() {
    }

    public static IBfcHttpStatusWatch getBfcHttpStatusWatch() {
        return mBfcHttpStatusWatch;
    }

    /**
     * set http request status watch
     *
     * @deprecated this function now is not finish, using carefully
     */
    public static void setBfcHttpStatusWatch(IBfcHttpStatusWatch bfcHttpStatusWatch) {
        mBfcHttpStatusWatch = bfcHttpStatusWatch;
    }

    public static void openDebug() {
        DEBUG = true;
        VolleyLog.DEBUG = true;
        VolleyLog.TAG = L.DEFAULT_TAG;
    }

    /**
     * @param TAG tag for bfc http log print
     */
    public static void openDebug(String TAG) {
        DEBUG = true;
        VolleyLog.DEBUG = true;
        VolleyLog.TAG = TAG;
        L.DEFAULT_TAG = TAG;
    }

    /**
     * @deprecated this method only used for test
     */
    @Deprecated
    public static void openDebug(boolean enable) {
        DEBUG = enable;
        VolleyLog.DEBUG = enable;
        VolleyLog.TAG = L.DEFAULT_TAG;
    }


    public static boolean debugMode() {
        return DEBUG;
    }

    /**
     * global set whether enable cache
     */
    public static void setShouldCache(boolean shouldCache) {
        SHOULD_CACHE = shouldCache;
    }

    /**
     * get config of whether enable cache
     */
    public static boolean shouldCache() {
        return SHOULD_CACHE;
    }

    /**
     * get domains which need to run DNS antiHijack
     */
    public static List<String> getDomains() {
        return DNSAntiHijackDomains;
    }

    /**
     * init config
     */
    public static void init(@NonNull Context context) {
        initRequestQueue(context.getApplicationContext(), null);
    }

    /**
     * init config
     */
    public static void init(@NonNull Context context, HttpStack stack) {
        initRequestQueue(context.getApplicationContext(), stack);
    }


    /**
     * get if its enable to set different timeout value with different network type
     */
    public static boolean isEnableMultiTimeout() {
        return enableMultiTimeout;
    }

    /**
     * set enable to set different timeout value with different network type
     */
    private static void enableMultiTimeout() {
        enableMultiTimeout = true;
    }

    /**
     * set disable to set different timeout value with different network type
     */
    private static void disableMultiTimeout() {
        enableMultiTimeout = false;
    }

    /**
     * get if its enable DNS antiHijack
     */
    public static boolean isEnableDNSAntiHijack() {
        return isEnableDNSAntiHijack && DNSAntiHijackDomains != null && DNSAntiHijackDomains.size() > 0;
    }

    /**
     * set if its enable DNS antiHijack
     */
    public static void setEnableDNSAntiHijack(boolean enableDNSAntiHijack) {
        isEnableDNSAntiHijack = enableDNSAntiHijack;
    }

    /**
     * set domains which need to tun DNS antiHijack
     */
    private static void setDomains(String... domains) {
        for (String domain : domains) {
            if (!UrlTools.isDomainValid(domain)) {
                throw new IllegalArgumentException("域名格式不正确");
            }
        }
        initDNSAntiHijackEnv();
        Collections.addAll(DNSAntiHijackDomains, domains);
        isEnableDNSAntiHijack = DNSAntiHijackDomains != null && DNSAntiHijackDomains.size() > 0;
    }

    /**
     * set domain which need to tun DNS antiHijack
     */
    public static void setDomain(String domain) {
        if (!UrlTools.isDomainValid(domain)) {
            throw new IllegalArgumentException("域名格式不正确");
        }
        initDNSAntiHijackEnv();
        Collections.addAll(DNSAntiHijackDomains, domain);
        isEnableDNSAntiHijack = DNSAntiHijackDomains != null && DNSAntiHijackDomains.size() > 0;
    }

    @Deprecated
    private static void initDNSAntiHijackEnv() {
//        DNSAntiHijackEnv.init(mAppContext);
    }

    /**
     * set timeout
     */
    public static void setTimeout(int timeoutMs) {
        disableMultiTimeout();
        TIMEOUT_SLOW = timeoutMs;
        TIMEOUT_MIDDLE = timeoutMs;
        TIMEOUT_FAST = timeoutMs;
    }

    public static int getTimeoutFast() {
        return TIMEOUT_FAST;
    }

    public static int getTimeoutMiddle() {
        return TIMEOUT_MIDDLE;
    }

    public static int getTimeoutSlow() {
        return TIMEOUT_SLOW;
    }

    /**
     * set timeout, enable set different timeout with different network type.<br/>
     * when set different timeout with single timeout meanwhile, the latest config will function.<br/>
     * every net request will check current network type to set suitable timeout value.
     *
     * @param timeoutFast   Wifi、HSUPA、HSPA、LTE high network timeout
     * @param timeoutMiddle UMTS、EVDO revison A、EVDO revison B、HSDPA middle network timeout
     * @param timeoutSlow   GPRS、EDGE、CDMA、EVDO revison 0、1xRTT、iDen、eHRPO low network timeout
     */
    public static void setTimeout(int timeoutFast, int timeoutMiddle, int timeoutSlow) {
        enableMultiTimeout();
        TIMEOUT_FAST = timeoutFast;
        TIMEOUT_MIDDLE = timeoutMiddle;
        TIMEOUT_SLOW = timeoutSlow;
    }

    /**
     * init net request queue
     */
    public static void initRequestQueue(@NonNull Context context) {
        initRequestQueue(context, null);
    }

    /**
     * init net request queue
     */
    public synchronized static void initRequestQueue(@NonNull Context context, HttpStack stack) {
        if (mVolleyRequestQueue == null) {
            mVolleyRequestQueue = Volley.newRequestQueue(context.getApplicationContext(), stack);
            mVolleyRequestQueue.start();
        } else {
            Log.e(TAG, "initRequestQueue: RequestQueue has been initialized");
        }
    }

    /**
     * get global net request queue
     */
    public static RequestQueue getHttpRequestQueue() {
//        if(mVolleyRequestQueue == null){
//            throw new IllegalStateException("net request queue is null, please config init() in application.");
//        }
        return mVolleyRequestQueue;
    }

    /**
     * get user custom net request realize class
     */
    public static IBfcHttpRequest getCustomHttpRequest() {
        return mCustomHttpRequest;
    }

    /**
     * set user custom net request realize class
     */
    public static void setCustomHttpRequest(IBfcHttpRequest customHttpRequest) {
        mCustomHttpRequest = customHttpRequest;
    }

    public static String getDefaultCharset() {
        return defaultCharset;
    }

    /**
     * custom default charset, it is only effective when server response header not contains Content-Type
     */
    public static void setDefaultCharset(String defaultCharset) {
        BfcHttpConfigure.defaultCharset = defaultCharset;
    }

    public static void logSdkVersion() {
        Log.i(TAG, "sdk versionName : " + SDKVersion.getVersionName());
        Log.i(TAG, "sdk libraryName : " + SDKVersion.getLibraryName());
    }

    public static String getHttpDnsServerIp() {
        if(TextUtils.isEmpty(mHttpsDnsServerIp)) {
            mHttpsDnsServerIp = "119.29.29.29";
        }
        return mHttpsDnsServerIp;
    }


    /**
     * do not use this method
     */
    @Deprecated
    public static void setHttpsDnsServerIp(String httpsDnsServerIp) {
        mHttpsDnsServerIp = httpsDnsServerIp;
    }
}
