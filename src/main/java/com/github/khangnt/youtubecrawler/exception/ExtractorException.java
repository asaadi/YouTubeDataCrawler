package com.github.khangnt.youtubecrawler.exception;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public class ExtractorException extends RuntimeException {

    public ExtractorException(String s, String videoId) {
        super("[" + videoId + "] " + s);
    }

    public ExtractorException(String s, Throwable throwable, String videoId) {
        super("[" + videoId + "] " + s, throwable);
    }

}
