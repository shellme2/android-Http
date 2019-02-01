package com.eebbk.bfc.http.tools;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * UrlTools unit test.
 */
public class UrlToolsTest {
    @Test
    public void getDomain() throws Exception {
        assertEquals("", UrlTools.getDomain("www.eebbk.com"));
        assertEquals("www.eebbk.com", UrlTools.getDomain("http://www.eebbk.com"));
        assertEquals("www.eebbk.com", UrlTools.getDomain("https://www.eebbk.com"));
        assertEquals("www.eebbk.com", UrlTools.getDomain("http://www.eebbk.com/"));
        assertEquals("www.eebbk.com", UrlTools.getDomain("http://www.eebbk.com/abc"));
        assertEquals("eebbk.com", UrlTools.getDomain("http://eebbk.com/"));
        assertEquals("eebbk.com", UrlTools.getDomain("http://eebbk.com/abc"));
        assertEquals("cn.bing.com", UrlTools.getDomain("http://cn.bing.com/"));
        assertEquals("cn.bing.com", UrlTools.getDomain("http://cn.bing.com/test"));
        assertEquals("cn.bing.com", UrlTools.getDomain("https://cn.bing.com"));
        assertEquals("cn.bing.com", UrlTools.getDomain("https://cn.bing.com/test"));
    }

    @Test
    public void isDomainValid() throws Exception {
        assertTrue(UrlTools.isDomainValid("www.eebbk.com"));
        assertTrue(UrlTools.isDomainValid("eebbk.com"));
        assertTrue(UrlTools.isDomainValid("www.eebbk.com.cn"));
        assertTrue(UrlTools.isDomainValid("163.com"));
        assertTrue(UrlTools.isDomainValid("www.eebbk"));
        assertTrue(UrlTools.isDomainValid("www.eebbkcom"));

        assertTrue(!UrlTools.isDomainValid("www.eebbk/com"));
        assertTrue(!UrlTools.isDomainValid("www.eebbk."));
        assertTrue(!UrlTools.isDomainValid("eebbk"));
        assertTrue(!UrlTools.isDomainValid(".eebbk.com"));
        assertTrue(!UrlTools.isDomainValid("http://www.eebbk.com"));
    }

    @Test
    public void rebuildUrl() throws Exception {
        assertEquals(UrlTools.rebuildUrl("http://www.eebbk.com", "1.1.1.1"), "http://1.1.1.1");
        assertEquals(UrlTools.rebuildUrl("http://www.eebbk.com", "127.0.0.1"), "http://127.0.0.1");
        assertEquals(UrlTools.rebuildUrl("http://www.eebbk.com/", "1.1.1.1"), "http://1.1.1.1/");
        assertEquals(UrlTools.rebuildUrl("http://www.eebbk.com/abc", "1.1.1.1"), "http://1.1.1.1/abc");
        assertEquals(UrlTools.rebuildUrl("http://eebbk.com", "1.1.1.1"), "http://1.1.1.1");
        assertEquals(UrlTools.rebuildUrl("http://www.eebbk.com", "1.1.1.1"), "http://1.1.1.1");
    }

    @Test
    public void appendParams() throws Exception {

    }

}