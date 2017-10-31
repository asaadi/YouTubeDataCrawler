package com.github.khangnt.youtubecrawler;

import java.util.TimeZone;

/**
 * Created by Khang NT on 10/30/17.
 * Email: khang.neon.1997@gmail.com
 */


public class C {
    // user agent of tablet
    public static final int UTC_OFFSET = TimeZone.getDefault().getRawOffset() / (60 * 1000);

    static final String BROWSER_ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";
    static final String BROWSER_ACCEPT_LANGUAGE = "en-US,en;q=0.8,vi;q=0.6";
    static final String BROWSER_USER_AGENT = "Mozilla/5.0 (Linux; Android 4.3; Nexus 10 Build/JSS15Q) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";
}
