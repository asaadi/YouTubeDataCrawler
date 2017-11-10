package com.github.khangnt.youtubecrawler.exception;

/**
 * Created by Khang NT on 11/9/17.
 * Email: khang.neon.1997@gmail.com
 */

public class NotSupportedVideoException extends ExtractorException {
    public NotSupportedVideoException(String s, String videoId) {
        super(s, videoId);
    }

    public NotSupportedVideoException(String s, Throwable throwable, String videoId) {
        super(s, throwable, videoId);
    }
}
