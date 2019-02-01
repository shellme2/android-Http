package com.eebbk.bfc.http.error;

import android.support.annotation.NonNull;

import com.android.volley.VolleyError;
import com.eebbk.bfc.http.error.BfcErrorInfo;

/**
 * ner request Exception
 */
public class BfcHttpError extends Exception {

    /**
     * redirect（3XX） client error（4XX）server error（5XX、6XX）
     */
    private String errorCode;
    private String msg;

    private BfcHttpError() {
    }

    public BfcHttpError(@NonNull String errorCode) {
        super(BfcErrorInfo.getErrorMsg(errorCode));
        this.errorCode = errorCode;
        this.msg = BfcErrorInfo.getErrorMsg(errorCode);
    }

    public BfcHttpError(@NonNull VolleyError volleyError) {
        super(volleyError.toString());
        msg = volleyError.toString();
        if (volleyError.networkResponse != null) {
            int statusCode = volleyError.networkResponse.statusCode;
            errorCode = BfcErrorInfo.MODULE + BfcErrorInfo.SUBJECT_NORMAL + statusCode;
            if (volleyError.networkResponse.data != null) {
                msg = statusCode + " " + new String(volleyError.networkResponse.data);
            }
        }
    }

    public String getMsg() {
        return msg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return "BfcErrorInfo{" +
                "errorCode=" + errorCode +
                ", msg='" + msg + '\'' +
                '}';
    }
}