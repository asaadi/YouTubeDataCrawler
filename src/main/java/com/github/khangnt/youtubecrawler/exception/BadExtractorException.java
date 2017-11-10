package com.github.khangnt.youtubecrawler.exception;

/**
 * Created by Khang NT on 11/9/17.
 * Email: khang.neon.1997@gmail.com
 */

public class BadExtractorException extends ExtractorException {
    public BadExtractorException() {
    }

    public BadExtractorException(String s) {
        super(s);
    }

    public BadExtractorException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BadExtractorException(Throwable throwable) {
        super(throwable);
    }
}
