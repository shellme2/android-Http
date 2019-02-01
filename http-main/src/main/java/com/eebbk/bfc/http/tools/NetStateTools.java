package com.eebbk.bfc.http.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.eebbk.bfc.common.devices.NetUtils;
import com.eebbk.bfc.http.config.BfcHttpConfigure;

/**
 * net state tools
 */

public class NetStateTools {

    private static final String TAG = "NetStateTools";

    private static final int SPEED_SLOW = 0x01;
    private static final int SPEED_MIDDLE = 0x02;
    private static final int SPEED_FAST = 0x03;

    private NetStateTools() {
    }

    private static int getNetSpeedType(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            // no net connection
            return SPEED_FAST;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return SPEED_FAST;
        } else {
            String typeName = networkInfo.getSubtypeName();
            Log.d(TAG, "getNetSpeedType: " + typeName);
            int type = networkInfo.getSubtype();
            switch (type) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                    return SPEED_SLOW;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return SPEED_MIDDLE;
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return SPEED_FAST;
                default:
                    return SPEED_FAST;
            }
        }
    }

    public static int getSuitableTimeout(@NonNull Context context) {
        if (!BfcHttpConfigure.isEnableMultiTimeout()) {
            return BfcHttpConfigure.getTimeoutFast();
        }
        switch (getNetSpeedType(context)) {
            case SPEED_FAST:
                return BfcHttpConfigure.getTimeoutFast();
            case SPEED_MIDDLE:
                return BfcHttpConfigure.getTimeoutMiddle();
            case SPEED_SLOW:
                return BfcHttpConfigure.getTimeoutSlow();
            default:
                return BfcHttpConfigure.getTimeoutFast();
        }
    }

    public static boolean checkNetOpen(@NonNull Context context) {
        return NetUtils.isConnected(context);
    }
}
