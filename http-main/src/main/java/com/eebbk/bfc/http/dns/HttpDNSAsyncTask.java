package com.eebbk.bfc.http.dns;

import android.os.AsyncTask;

import com.eebbk.bfc.http.tools.L;
import com.eebbk.bfc.http.tools.UrlTools;

/**
 * HttpDNS request async task
 */

public class HttpDNSAsyncTask extends AsyncTask<String, String, String> {

    private HttpDnsRequestCallBack mCallBack;

    public HttpDNSAsyncTask(HttpDnsRequestCallBack callBack) {
        mCallBack = callBack;
    }

    @Override
    protected String doInBackground(String... params) {
        String httpDnsIp = HttpDNS.getInstance().requestHttpDnsManyTimes(params[0]);
        L.d("doInBackground " + params[0] + "  " + httpDnsIp);
        return httpDnsIp;
    }

    @Override
    protected void onPostExecute(String httpDnsIp) {
        L.d("onPostExecute " + httpDnsIp);
        if (mCallBack != null) {
            if (UrlTools.isIpValid(httpDnsIp)) {
                mCallBack.onSuccess(httpDnsIp);
            } else {
                mCallBack.onFail("httpDns response is wrong: " + httpDnsIp);
            }
        }
    }
}
