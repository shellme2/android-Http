package com.eebbk.bfc.http.dns;

import android.support.annotation.NonNull;

/**
 * HttpDns request callback interface
 */

public interface HttpDnsRequestCallBack {
    void onSuccess(@NonNull String httpDnsIp);

    void onFail(@NonNull String failMsg);
}
