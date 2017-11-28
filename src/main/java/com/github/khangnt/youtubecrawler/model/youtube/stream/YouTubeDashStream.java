package com.github.khangnt.youtubecrawler.model.youtube.stream;

import com.github.khangnt.youtubecrawler.internal.Utils;

import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

/**
 * Created by Khang NT on 11/24/17.
 * Email: khang.neon.1997@gmail.com
 */

public abstract class YouTubeDashStream extends YouTubeStream {
    private int bandwidth;
    private int contentLength;
    private SegmentBaseData segmentBase;

    public YouTubeDashStream(UrlLazy urlLazy, long expireAt, String itag, String container,
                             String mimeType, int bandwidth, int contentLength, SegmentBaseData segmentBase) {
        super(urlLazy, expireAt, itag, container, mimeType);
        this.bandwidth = bandwidth;
        this.contentLength = contentLength;
        this.segmentBase = segmentBase;
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public int getContentLength() {
        return contentLength;
    }

    @Nullable
    public SegmentBaseData getSegmentBase() {
        return segmentBase;
    }

    @Override
    public String toString() {
        return "YouTubeDashStream{" +
                "bandwidth=" + bandwidth +
                ", contentLength=" + contentLength +
                ", segmentBase=" + segmentBase +
                "} " + super.toString();
    }

    static Comparator<YouTubeDashStream> comparator() {
        return (s1, s2) -> {
            int res = Utils.compare(s1.bandwidth, s2.bandwidth);
            return res != 0 ? res : Utils.compare(s1.contentLength, s2.contentLength);
        };
    }
}
