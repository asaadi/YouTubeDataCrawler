package com.github.khangnt.youtubecrawler.exception;

/**
 * Created by Khang NT on 11/9/17.
 * Email: khang.neon.1997@gmail.com
 */

public class VideoNotAvailableException extends ExtractorException {
    public VideoNotAvailableException() {
    }

    public VideoNotAvailableException(String s) {
        super(s);
    }

    public VideoNotAvailableException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public VideoNotAvailableException(Throwable throwable) {
        super(throwable);
    }
}
