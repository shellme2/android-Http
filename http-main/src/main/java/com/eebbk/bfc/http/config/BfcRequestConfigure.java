package com.eebbk.bfc.http.config;

import java.util.HashMap;
import java.util.Map;

/**
 * single request configuration, using guide:
 * <pre class="prettyprint">
 * BfcRequestConfigure conf = new BfcRequestConfigure.Builder()
 * .setHeader(header)
 * .setType(BfcRequestConfigure.Type.GZIP)
 * .setTag("BfcRequestConfigure")
 * .setCacheAble(true)
 * .setExpiredTime(5 * 60 * 60 * 1000)
 * .setRetryTimes(1)
 * .setTimeout(10 * 1000).build();
 * </pre>
 */

public class BfcRequestConfigure {

    private int expiredTime = 0;
    private Object tag = "bfc-http";
    private int timeout = BfcHttpConfigure.getTimeoutFast();
    private int retryTimes = 1;
    private boolean cacheAble = BfcHttpConfigure.shouldCache();
    private int diskCacheSize = 5 * 1024 * 1024;
    private Map<String, String> header = new HashMap<>();
    private Type type = Type.STRING;
    private String cacheKey;
    private byte[] body;

    public enum Type {
        STRING,
        GZIP,
        /**
         * <b>notice: this type will remove before V1.0.0, please check.<b/>
         *
         * @deprecated this type is deprecated, now every type request will enable cache through {@link Builder#setCacheAble(boolean)}.
         */
        @Deprecated
        CACHE_ABLE
    }

    private BfcRequestConfigure() {
    }

    public BfcRequestConfigure(BfcRequestConfigure.Builder builder) {
        this.cacheAble = builder.cacheAble;
        this.diskCacheSize = builder.diskCacheSize;
        this.expiredTime = builder.expiredTime;
        this.header = builder.header;
        this.retryTimes = builder.retryTimes;
        this.tag = builder.tag;
        this.timeout = builder.timeout;
        this.type = builder.type;
        this.cacheKey = builder.cacheKey;
        this.body = builder.body;
    }

    public boolean isCacheAble() {
        return cacheAble;
    }

    public int getDiskCacheSize() {
        return diskCacheSize;
    }

    public int getExpiredTime() {
        return expiredTime;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public Object getTag() {
        return tag;
    }

    public int getTimeout() {
        return timeout;
    }

    public Type getType() {
        return type;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public byte[] getBody() {
        return body;
    }

    public static class Builder {
        private int expiredTime = 0;
        private Object tag = "bfc-http";
        private int timeout = BfcHttpConfigure.getTimeoutFast();
        private int retryTimes = 1;
        private boolean cacheAble = true;
        private int diskCacheSize = 5 * 1024 * 1024;
        private Map<String, String> header = new HashMap<>();
        private Type type = Type.STRING;
        private String cacheKey;
        private byte[] body;

        /**
         * set weather current request is enable cache.
         * <p>
         * {@code true} for enable cache.<br/>
         * {@code false} for skip the cache queue and go straight to the network.
         * <p>
         * when enable cache, you can set {@link Builder#setExpiredTime(int)} to custom define cache expired time.
         */
        public Builder setCacheAble(boolean cacheAble) {
            this.cacheAble = cacheAble;
            return this;
        }

        /**
         * custom define disk cache size.
         *
         * @deprecated not realize
         */
        @Deprecated
        public Builder setDiskCacheSize(int diskCacheSize) {
            this.diskCacheSize = diskCacheSize;
            return this;
        }

        /**
         * set cache enable time (ms), default value is 0ms, if expired time <= 0 and enable cache, request cache configure will dependence on server response header's "Cache-Control" key.
         */
        public Builder setExpiredTime(int expiredTime) {
            this.expiredTime = expiredTime;
            return this;
        }

        /**
         * set request header ( enable gzip model should add {"Accept-Encoding":"gzip"} into header)
         */
        public Builder setHeader(Map<String, String> header) {
            this.header = header;
            return this;
        }

        /**
         * set retry times with request, if retry times > 1, request will recall when first request is fail. default value is 1.
         */
        public Builder setRetryTimes(int retryTimes) {
            this.retryTimes = retryTimes;
            return this;
        }

        /**
         * set request tag for every request, which can use in {@link com.eebbk.bfc.http.BfcHttp#cancelAll(Object)} to cancel request.
         */
        public Builder setTag(Object tag) {
            this.tag = tag;
            return this;
        }

        /**
         * set time out value, default value is 5000ms.
         */
        public Builder setTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * set request type, now support three different type:
         * <p>
         * {@link Type#STRING} default request type, process normal network request.<br/>
         * {@link Type#GZIP} gzip enable request, auto unzip gzip response data. Also support normal response, if response header's "Content-Encoding" not include "gzip", this will direct return data as normal request.<br/>
         * <p>
         * default type is {@link Type#STRING}.
         */
        public Builder setType(Type type) {
            this.type = type;
            return this;
        }

        /**
         * set cache key, default value is request url
         */
        public Builder setCacheKey(String cacheKey) {
            this.cacheKey = cacheKey;
            return this;
        }

        /**
         * set cache key, default value is request url
         */
        public Builder setBody(byte[] body) {
            this.body = body;
            return this;
        }

        public BfcRequestConfigure build() {
            return new BfcRequestConfigure(this);
        }
    }
}
