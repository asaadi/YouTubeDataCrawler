package com.github.khangnt.youtubecrawler.model.youtube.stream;

import com.github.khangnt.youtubecrawler.Lazy;
import com.github.khangnt.youtubecrawler.exception.SignatureDecryptException;

import rx.functions.Func0;

/**
 * Created by Khang NT on 11/10/17.
 * Email: khang.neon.1997@gmail.com
 *
 * {@link UrlLazy} is using in {@link YouTubeStream}, the reason is signature decrypt task is cost time,
 * but not all url are used. Signature part will be computed when {@link UrlLazy#get()} called,
 * be careful when call this method on main thread.
 */
public class UrlLazy extends Lazy<String> {
    public UrlLazy(Func0<String> valueGetter) {
        super(valueGetter);
    }

    @Override
    public String get() throws SignatureDecryptException {
        return super.get();
    }
}
