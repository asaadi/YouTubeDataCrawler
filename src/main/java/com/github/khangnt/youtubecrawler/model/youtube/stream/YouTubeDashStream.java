package com.github.khangnt.youtubecrawler.model.youtube.stream;

import com.github.khangnt.youtubecrawler.internal.Utils;

import java.util.Comparator;

/**
 * Created by Khang NT on 11/24/17.
 * Email: khang.neon.1997@gmail.com
 */

public abstract class YouTubeDashStream extends YouTubeStream {
    private int bandwidth;
    private int contentLength;

    public YouTubeDashStream(UrlLazy urlLazy, long expireAt, String itag, String container,
                             String mimeType, int bandwidth, int contentLength) {
        super(urlLazy, expireAt, itag, container, mimeType);
        this.bandwidth = bandwidth;
        this.contentLength = contentLength;
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public int getContentLength() {
        return contentLength;
    }

    @Override
    public String toString() {
        return "YouTubeDashStream{" +
                "bandwidth=" + bandwidth +
                ", contentLength=" + contentLength +
                "} " + super.toString();
    }

    static Comparator<YouTubeDashStream> comparator() {
        return (s1, s2) -> {
            int res = Utils.compare(s1.bandwidth, s2.bandwidth);
            return res != 0 ? res : Utils.compare(s1.contentLength, s2.contentLength);
        };
    }
}
