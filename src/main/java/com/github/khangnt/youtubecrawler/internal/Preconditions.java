package com.github.khangnt.youtubecrawler.internal;

/**
 * Created by Khang NT on 10/30/17.
 * Email: khang.neon.1997@gmail.com
 */

public class Preconditions {
    public static <T> T notNull(T value) {
        if (value == null) {
            throw new NullPointerException();
        }
        return value;
    }
}
