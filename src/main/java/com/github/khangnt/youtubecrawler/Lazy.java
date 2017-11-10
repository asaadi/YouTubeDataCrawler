package com.github.khangnt.youtubecrawler;

import com.github.khangnt.youtubecrawler.internal.Preconditions;

import rx.functions.Func0;

/**
 * Created by Khang NT on 11/10/17.
 * Email: khang.neon.1997@gmail.com
 */

public class Lazy<T> {
    private T value;
    private Func0<T> valueGetter;

    public Lazy(Func0<T> valueGetter) {
        this.valueGetter = Preconditions.notNull(valueGetter);
    }

    public T get() {
        synchronized (this) {
            if (value == null) {
                return value = valueGetter.call();
            }
        }
        return value;
    }

    public boolean computed() {
        synchronized (this) {
            return value != null;
        }
    }

}
