package com.example.http.toolbox;

import android.util.Log;

import com.eebbk.bfc.http.toolbox.IBfcHttpStatusWatch;

/**
 * Created by lzy on 2016/10/14.
 */

public class CustomHttpStatusWatch implements IBfcHttpStatusWatch {
    private static final String TAG = "CustomHttpStatusWatch";

    @Override
    public void onPreExecute() {
        Log.d(TAG, "onPreExecute: ");
    }

    @Override
    public void onPostExecute() {
        Log.d(TAG, "onPostExecute: ");
    }
}
