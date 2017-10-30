package com.github.khangnt.youtubecrawler.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import rx.Observable;

/**
 * Created by Khang NT on 10/30/17.
 * Email: khang.neon.1997@gmail.com
 */

public class ResponseData<T> {
    private @NotNull T data;
    private @Nullable Observable<ResponseData<T>> next;

    public ResponseData(@NotNull T data, @Nullable Observable<ResponseData<T>> next) {
        this.data = data;
        this.next = next;
    }

    @NotNull
    public T getData() {
        return data;
    }

    @Nullable
    public Observable<ResponseData<T>> getNext() {
        return next;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "data=" + data +
                ", next=" + next +
                '}';
    }
}
