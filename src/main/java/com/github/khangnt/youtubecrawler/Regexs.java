package com.github.khangnt.youtubecrawler;

import java.util.regex.Pattern;

/**
 * Created by Khang NT on 10/30/17.
 * Email: khang.neon.1997@gmail.com
 */

class Regexs {
    static final Pattern WINDOW_SETTINGS = Pattern.compile("window\\.settings\\s*=\\s*(\\{.+?\\})\\s*;");
}
