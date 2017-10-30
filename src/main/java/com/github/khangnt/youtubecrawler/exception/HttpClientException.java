package com.github.khangnt.youtubecrawler.exception;

/**
 * Created by Khang NT on 10/30/17.
 * Email: khang.neon.1997@gmail.com
 */

public class HttpClientException extends Exception {
    private int code;
    private String message;

    public HttpClientException(int code, String message) {
        super(code + " - " + message);
        this.code = code;
        this.message = message;
    }

}
