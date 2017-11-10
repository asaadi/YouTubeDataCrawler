package com.github.khangnt.youtubecrawler.exception;

/**
 * Created by Khang NT on 11/9/17.
 * Email: khang.neon.1997@gmail.com
 */

public class NotSupportedVideoException extends ExtractorException {
    public NotSupportedVideoException() {
    }

    public NotSupportedVideoException(String s) {
        super(s);
    }

    public NotSupportedVideoException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public NotSupportedVideoException(Throwable throwable) {
        super(throwable);
    }
}
