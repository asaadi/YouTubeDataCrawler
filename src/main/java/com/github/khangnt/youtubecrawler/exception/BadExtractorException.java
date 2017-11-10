package com.github.khangnt.youtubecrawler.exception;

/**
 * Created by Khang NT on 11/9/17.
 * Email: khang.neon.1997@gmail.com
 */

public class BadExtractorException extends ExtractorException {
    public BadExtractorException(String s, String videoId) {
        super(s, videoId);
    }

    public BadExtractorException(String s, Throwable throwable, String videoId) {
        super(s, throwable, videoId);
    }
}
