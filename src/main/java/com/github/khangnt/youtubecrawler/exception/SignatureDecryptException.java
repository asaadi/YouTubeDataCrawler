package com.github.khangnt.youtubecrawler.exception;

/**
 * Created by Khang NT on 11/10/17.
 * Email: khang.neon.1997@gmail.com
 */

public class SignatureDecryptException extends ExtractorException {
    public SignatureDecryptException() {
    }

    public SignatureDecryptException(String s) {
        super(s);
    }

    public SignatureDecryptException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SignatureDecryptException(Throwable throwable) {
        super(throwable);
    }
}
