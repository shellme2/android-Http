package com.example.http.toolbox;

import com.android.volley.toolbox.HurlStack;

import okhttp3.OkHttpClient;

/**
 * Volley OkHttp bridge
 */

public class OkHttp3Stack extends HurlStack {

    private final OkHttpClient okHttpClient;

    /**
     * Create a OkHttpStack with default OkHttpClient.
     */
    public OkHttp3Stack() {
        this(new OkHttpClient());
    }

    /**
     * Create a OkHttpStack with a custom OkHttpClient
     *
     * @param okHttpClient Custom OkHttpClient, NonNull
     */
    private OkHttp3Stack(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }
}
