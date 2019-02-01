package com.eebbk.bfc.http.toolbox;

/**
 * net request response interface
 */
public interface IBfcHttpCallBack<T> {
    /**
     * call back interface for response
     *
     * @param response response data
     */
    void onResponse(T response);
}
