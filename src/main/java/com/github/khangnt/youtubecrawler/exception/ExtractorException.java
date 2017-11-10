package com.github.khangnt.youtubecrawler.exception;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public class ExtractorException extends Exception {
    public ExtractorException() {
    }

    public ExtractorException(String s) {
        super(s);
    }

    public ExtractorException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ExtractorException(Throwable throwable) {
        super(throwable);
    }
}
