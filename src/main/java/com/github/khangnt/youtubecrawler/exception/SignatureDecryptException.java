package com.github.khangnt.youtubecrawler.exception;

/**
 * Created by Khang NT on 11/10/17.
 * Email: khang.neon.1997@gmail.com
 */

public class SignatureDecryptException extends ExtractorException {
    public SignatureDecryptException(String s, String videoId) {
        super(s, videoId);
    }

    public SignatureDecryptException(String s, Throwable throwable, String videoId) {
        super(s, throwable, videoId);
    }
}
