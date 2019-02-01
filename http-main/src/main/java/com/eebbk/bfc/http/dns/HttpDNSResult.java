package com.eebbk.bfc.http.dns;

import java.util.List;

/**
 * HttpDNS response entity
 */
public class HttpDNSResult {

    private boolean fromHttpDns;

    private String host;

    private List<String> ips;

    private long ttl;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public List<String> getIps() {
        return ips;
    }

    public void setIps(List<String> ips) {
        this.ips = ips;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public boolean isFromHttpDns() {
        return fromHttpDns;
    }

    public void setFromHttpDns(boolean fromHttpDns) {
        this.fromHttpDns = fromHttpDns;
    }

    @Override
    public String toString() {
        return "HttpDNSResult{" +
                "fromHttpDns=" + fromHttpDns +
                ", host='" + host + '\'' +
                ", ips=" + ips +
                ", ttl=" + ttl +
                '}';
    }
}
