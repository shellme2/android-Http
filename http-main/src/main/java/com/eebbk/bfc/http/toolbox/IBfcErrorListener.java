package com.eebbk.bfc.http.toolbox;

import com.eebbk.bfc.http.error.BfcHttpError;

/**
 * net request error interface
 */
public interface IBfcErrorListener {
    /**
     * call back interface for error response
     *
     * @param error {@link BfcHttpError}
     */
    void onError(BfcHttpError error);
}
