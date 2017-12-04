package com.github.khangnt.youtubecrawler;

import com.github.khangnt.youtubecrawler.internal.Preconditions;

import rx.Emitter;
import rx.Observable;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by Khang NT on 11/10/17.
 * Email: khang.neon.1997@gmail.com
 */

public class Lazy<T> {
    private final Object lock = new Object();
    private Observable<T> sourceObservable;
    private BehaviorSubject<T> mSubject;

    public Lazy(Func0<T> valueGetter) {
        Preconditions.notNull(valueGetter);
        sourceObservable = Observable.<T>create(emitter -> {
            emitter.onNext(valueGetter.call());
            emitter.onCompleted();
        }, Emitter.BackpressureMode.NONE)
                .subscribeOn(Schedulers.newThread())
                .cache();
    }

    public T get() {
        prepareBehaviorSubject();
        return mSubject.toBlocking().first();
    }

    public Observable<T> getAsync() {
        prepareBehaviorSubject();
        return mSubject.take(1);
    }

    private void prepareBehaviorSubject() {
        synchronized (lock) {
            if (mSubject == null) {
                mSubject = BehaviorSubject.create();
                sourceObservable.subscribe(mSubject::onNext, mSubject::onError);
                sourceObservable = null; // release source observable
            }
        }
    }

}
