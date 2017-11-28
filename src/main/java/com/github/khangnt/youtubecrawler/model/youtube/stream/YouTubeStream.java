package com.github.khangnt.youtubecrawler.model.youtube.stream;

import com.github.khangnt.youtubecrawler.internal.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public class YouTubeStream {

    private UrlLazy urlLazy;
    private long expireAt;
    private String itag;
    private String container;
    private String mimeType;

    public YouTubeStream(UrlLazy urlLazy, long expireAt, String itag, String container, String mimeType) {
        this.urlLazy = urlLazy;
        this.expireAt = expireAt;
        this.itag = itag;
        this.container = container;
        this.mimeType = mimeType;
    }

    public UrlLazy getUrlLazy() {
        return urlLazy;
    }

    public long getExpireAt() {
        return expireAt;
    }

    @NotNull
    public String getItag() {
        return itag;
    }

    public String getContainer() {
        return container;
    }

    public String getMimeType() {
        return mimeType;
    }

    @Override
    public String toString() {
        return "YouTubeStream{" +
                ", expireAt=" + expireAt +
                ", itag='" + itag + '\'' +
                ", container='" + container + '\'' +
                ", mimeType='" + mimeType + '\'' +
                '}';
    }

    public static Comparator<YouTubeStream> compareStream() {
        return (s1, s2) -> {
            int compare = Utils.compare(getType(s1), getType(s2));
            if (compare == 0) {
                if (s1 instanceof YouTubeDashStream
                        && s2 instanceof YouTubeDashStream) {
                    return YouTubeDashStream.comparator()
                            .compare(((YouTubeDashStream) s1), ((YouTubeDashStream) s2));
                }
            }
            return compare;
        };
    }

    private static int getType(YouTubeStream stream) {
        if (stream instanceof YouTubeDashAudioStream) {
            return 0;
        } else if (stream instanceof YouTubeDashVideoStream) {
            return 1;
        } else if (stream instanceof YouTubeNonDashStream) {
            return 2;
        }
        return 3;
    }

}
