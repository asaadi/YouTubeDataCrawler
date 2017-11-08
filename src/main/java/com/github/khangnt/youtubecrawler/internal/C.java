package com.github.khangnt.youtubecrawler.internal;

import java.util.TimeZone;

/**
 * Created by Khang NT on 10/30/17.
 * Email: khang.neon.1997@gmail.com
 */


public class C {
    // user agent of tablet
    public static final int UTC_OFFSET = TimeZone.getDefault().getRawOffset() / (60 * 1000);

    static final String BROWSER_ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
    static final String BROWSER_ACCEPT_LANGUAGE = "en-us,en;q=0.5";
    static final String BROWSER_ACCEPT_CHARSET = "ISO-8859-1,utf-8;q=0.7,*;q=0.7";
    static final String MOBILE_BROWSER_USER_AGENT = "Mozilla/5.0 (Linux; Android 4.3; Nexus 10 Build/JSS15Q) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";
    static final String DESKTOP_BROWSER_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64; rv:10.0) Gecko/20150101 Firefox/47.0 (Chrome)";

}
