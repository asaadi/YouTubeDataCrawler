package com.github.khangnt.youtubecrawler.exception;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public class AgeRestrictionException extends ExtractorException {

    public AgeRestrictionException(String s, String videoId) {
        super(s, videoId);
    }

    public AgeRestrictionException(String s, Throwable throwable, String videoId) {
        super(s, throwable, videoId);
    }

}
