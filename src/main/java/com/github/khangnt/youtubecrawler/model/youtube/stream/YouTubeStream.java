package com.github.khangnt.youtubecrawler.model.youtube.stream;

import com.github.khangnt.youtubecrawler.model.youtube.format.DashAudioOnly;
import com.github.khangnt.youtubecrawler.model.youtube.format.DashVideoOnly;
import com.github.khangnt.youtubecrawler.model.youtube.format.LiveStreaming;
import com.github.khangnt.youtubecrawler.model.youtube.format.NonDash;
import com.github.khangnt.youtubecrawler.model.youtube.format.YouTubeFormat;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public class YouTubeStream {
    public static final long UNKOWN_TIME = -1;

    private String videoId; // reference to video contains this stream
    private String url;
    private long expireAt;
    private YouTubeFormat youTubeFormat;

    protected YouTubeStream(String videoId, String url, long expireAt, YouTubeFormat format) {
        this.videoId = videoId;
        this.youTubeFormat = format;
        this.url = url;
        this.expireAt = expireAt;
    }

    public String getVideoId() {
        return videoId;
    }

    public YouTubeFormat getYouTubeFormat() {
        return youTubeFormat;
    }

    public String getUrl() {
        return url;
    }

    public long getExpireAt() {
        return expireAt;
    }

    public boolean isLive() {
        return youTubeFormat instanceof LiveStreaming;
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
                "videoId='" + videoId + '\'' +
                ", url='" + url + '\'' +
                ", expireAt=" + expireAt +
                ", youTubeFormat=" + youTubeFormat +
                '}';
    }
}
