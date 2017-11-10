package com.github.khangnt.youtubecrawler.model.youtube.stream;

import com.github.khangnt.youtubecrawler.model.youtube.format.DashAudioOnly;
import com.github.khangnt.youtubecrawler.model.youtube.format.DashVideoOnly;
import com.github.khangnt.youtubecrawler.model.youtube.format.NonDash;
import com.github.khangnt.youtubecrawler.model.youtube.format.YouTubeFormat;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public class YouTubeStream {
    public static final long UNKNOWN_TIME = -1;

    private UrlLazy urlLazy;
    private long expireAt;
    private YouTubeFormat youTubeFormat;
    private boolean live;

    public YouTubeStream(UrlLazy urlLazy, long expireAt, YouTubeFormat format, boolean isLive) {
        this.youTubeFormat = format;
        this.urlLazy = urlLazy;
        this.expireAt = expireAt;
        this.live = isLive;
    }

    public YouTubeFormat getYouTubeFormat() {
        return youTubeFormat;
    }

    public UrlLazy getUrlLazy() {
        return urlLazy;
    }

    public long getExpireAt() {
        return expireAt;
    }

    public boolean isLive() {
        return live;
    }

    public boolean isDashAudio() {
        return youTubeFormat instanceof DashAudioOnly;
    }

    public boolean isDashVideo() {
        return youTubeFormat instanceof DashVideoOnly;
    }

    public boolean isNonDash() {
        return youTubeFormat instanceof NonDash;
    }

    @Override
    public String toString() {
        return "YouTubeStream{" +
                ", expireAt=" + expireAt +
                ", youTubeFormat=" + youTubeFormat +
                '}';
    }
}
