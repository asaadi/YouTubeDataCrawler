package com.github.khangnt.youtubecrawler;

import com.github.khangnt.youtubecrawler.internal.Preconditions;

import rx.Emitter;
import rx.Observable;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by Khang NT on 11/10/17.
 * Email: khang.neon.1997@gmail.com
 */

public class Lazy<T> {
    private Observable<T> valueObservable;

    public Lazy(Func0<T> valueGetter) {
        Preconditions.notNull(valueGetter);
        valueObservable = Observable.<T>create(emitter -> {
            emitter.onNext(valueGetter.call());
            emitter.onCompleted();
        }, Emitter.BackpressureMode.NONE)
                .subscribeOn(Schedulers.newThread())
                .cache();
    }

    public T get() {
        return valueObservable.toBlocking().first();
    }

    public void getAsync() {
        valueObservable.subscribe(value -> {}, Throwable::printStackTrace);
    }

}
