package com.github.khangnt.youtubecrawler.exception;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public class AgeRestrictionException extends ExtractorException {
    public AgeRestrictionException() {
    }

    public AgeRestrictionException(String s) {
        super(s);
    }

    public AgeRestrictionException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AgeRestrictionException(Throwable throwable) {
        super(throwable);
    }
}
