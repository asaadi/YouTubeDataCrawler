package com.github.khangnt.youtubecrawler.model.youtube.stream;

/**
 * Created by Khang NT on 11/24/17.
 * Email: khang.neon.1997@gmail.com
 */

public class YouTubeDashVideoStream extends YouTubeDashStream {

    private int width;
    private int height;
    private String videoCodec;
    private int fps;

    public YouTubeDashVideoStream(UrlLazy urlLazy, long expireAt, String itag, String container,
                                  String mimeType, int bandwidth, int contentLength, SegmentBaseData segmentBase,
                                  int width, int height, String videoCodec, int fps) {
        super(urlLazy, expireAt, itag, container, mimeType, bandwidth, contentLength, segmentBase);
        this.width = width;
        this.height = height;
        this.videoCodec = videoCodec;
        this.fps = fps;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getVideoCodec() {
        return videoCodec;
    }

    public int getFps() {
        return fps;
    }

    @Override
    public String toString() {
        return "YouTubeDashVideoStream{" +
                "width=" + width +
                ", height=" + height +
                ", videoCodec='" + videoCodec + '\'' +
                ", fps=" + fps +
                "} " + super.toString();
    }
}
