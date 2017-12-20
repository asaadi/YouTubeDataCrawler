package com.github.khangnt.youtubecrawler.internal;

/**
 * Created by Khang NT on 12/20/17.
 * Email: khang.neon.1997@gmail.com
 */

public interface Consumer<T, R> {
    R call(T t) throws Exception;
}