package com.github.khangnt.youtubecrawler.exception;

import java.io.IOException;

/**
 * Created by Khang NT on 10/30/17.
 * Email: khang.neon.1997@gmail.com
 */

public class HttpClientException extends IOException {
    private int code;
    private String message;

    public HttpClientException(int code, String message) {
        super(code + " - " + message);
        this.code = code;
        this.message = message;
    }

}
