package com.eebbk.bfc.http.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.NonNull;

import com.eebbk.bfc.common.app.SharedPreferenceUtils;

/**
 * SharedPreferences operate class
 */
public class UserPreferences {
    private final static String mSharedPreferencesFileName = "httpDns";

    private UserPreferences() {
    }

    public static void saveInt(@NonNull Context context, @NonNull String key, @NonNull Integer value) {
        SharedPreferenceUtils.getInstance(context, mSharedPreferencesFileName).put(key, value);
    }

    public static int loadInt(@NonNull Context context, @NonNull String key, int defValue) {
        return SharedPreferenceUtils.getInstance(context, mSharedPreferencesFileName).get(key, defValue);
    }

    public static void saveLong(@NonNull Context context, @NonNull String key, long value) {
        SharedPreferenceUtils.getInstance(context, mSharedPreferencesFileName).put(key, value);
    }

    public static long loadLong(@NonNull Context context, @NonNull String key, long defValue) {
        return SharedPreferenceUtils.getInstance(context, mSharedPreferencesFileName).get(key, defValue);
    }

    public static void saveString(@NonNull Context context, @NonNull String key, @NonNull String value) {
        SharedPreferenceUtils.getInstance(context, mSharedPreferencesFileName).put(key, value);
    }

    public static String loadString(@NonNull Context context, @NonNull String key, @NonNull String defValue) {
        return SharedPreferenceUtils.getInstance(context, mSharedPreferencesFileName).get(key, defValue);
    }

    public static void saveBoolean(@NonNull Context context, @NonNull String key, boolean value) {
        SharedPreferenceUtils.getInstance(context, mSharedPreferencesFileName).put(key, value);
    }

    public static boolean loadBoolean(@NonNull Context context, @NonNull String key, boolean defValue) {
        return SharedPreferenceUtils.getInstance(context, mSharedPreferencesFileName).get(key, defValue);
    }

    public static void clear(@NonNull Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(mSharedPreferencesFileName, Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.clear();
        edit.apply();
    }
}