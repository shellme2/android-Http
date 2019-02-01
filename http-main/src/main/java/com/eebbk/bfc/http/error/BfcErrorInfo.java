package com.eebbk.bfc.http.error;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * net request error info
 */

public class BfcErrorInfo {

    public static final String MODULE = "07";

    public static final String SUBJECT_NORMAL = "0";
    public static final String SUBJECT_DNS = "1";

    //redirect
    private static final String ERROR_REDIRECT_MSG = "3XX net redirected";

    //client error
    private static final String ERROR_CLIENT_MSG = "4XX client error";

    //server error
    private static final String ERROR_SERVER_MSG = "5XX 6XX server error";

    //network unusual
    public static final String ERROR_NETWORK_CODE = MODULE + SUBJECT_NORMAL + "001";
    private static final String ERROR_NETWORK_MSG = "no net connection";

    //httpDNS request error
    public static final String ERROR_HTTPDNS_CODE = MODULE + SUBJECT_DNS + "001";
    private static final String ERROR_HTTPDNS_MSG = "httpDNS request fail";

    private static final Map<String, String> errors = new HashMap<>();

    static {
        errors.put(ERROR_NETWORK_CODE, ERROR_NETWORK_MSG);
        errors.put(ERROR_HTTPDNS_CODE, ERROR_HTTPDNS_MSG);
    }

    private BfcErrorInfo() {
    }

    public static String getErrorMsg(@NonNull String errorCode) {
        if (errors.containsKey(errorCode)) {
            return errors.get(errorCode);
        }
        if (errorCode.startsWith(MODULE + SUBJECT_NORMAL + "5") || errorCode.startsWith(MODULE + SUBJECT_NORMAL + "6")) {
            return ERROR_SERVER_MSG;
        } else if (errorCode.startsWith(MODULE + SUBJECT_NORMAL + "4")) {
            return ERROR_CLIENT_MSG;
        } else if (errorCode.startsWith(MODULE + SUBJECT_NORMAL + "3")) {
            return ERROR_REDIRECT_MSG;
        }
        return errorCode;
    }
}
