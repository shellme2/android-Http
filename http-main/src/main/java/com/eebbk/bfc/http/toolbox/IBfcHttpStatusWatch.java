package com.eebbk.bfc.http.toolbox;

/**
 * http status watch interface
 */

public interface IBfcHttpStatusWatch {

    /**
     * before running net request
     */
    void onPreExecute();

    /**
     * after running net request
     */
    void onPostExecute();
}
