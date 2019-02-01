package com.eebbk.bfc.http.toolbox;

import com.eebbk.bfc.http.tools.L;

/**
 * default http status watch
 */

public class DefaultHttpStatusWatch implements IBfcHttpStatusWatch {
    @Override
    public void onPreExecute() {
        L.d("onPreExecute");
    }

    @Override
    public void onPostExecute() {
        L.d("onPostExecute");
    }
}
