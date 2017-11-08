package com.github.khangnt.youtubecrawler.exception;

/**
 * Created by Khang NT on 10/30/17.
 * Email: khang.neon.1997@gmail.com
 */

public class RegexMismatchException extends RuntimeException {
    public RegexMismatchException() {
    }

    public RegexMismatchException(String s) {
        super(s);
    }

    public RegexMismatchException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RegexMismatchException(Throwable throwable) {
        super(throwable);
    }
}
