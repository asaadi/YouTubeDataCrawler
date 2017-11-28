package com.github.khangnt.youtubecrawler.model.youtube.stream;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public class YouTubeNonDashStream extends YouTubeStream {

    private int width;
    private int height;
    private int audioBitrate;
    private String audioCodec;
    private String videoCodec;

    public YouTubeNonDashStream(UrlLazy urlLazy, long expireAt, String itag, String container,
                                String mimeType, int width, int height, int audioBitrate,
                                String audioCodec, String videoCodec) {
        super(urlLazy, expireAt, itag, container, mimeType);
        this.width = width;
        this.height = height;
        this.audioBitrate = audioBitrate;
        this.audioCodec = audioCodec;
        this.videoCodec = videoCodec;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getAudioBitrate() {
        return audioBitrate;
    }

    public String getAudioCodec() {
        return audioCodec;
    }

    public String getVideoCodec() {
        return videoCodec;
    }

    @Override
    public String toString() {
        return "YouTubeNonDashStream{" +
                "width=" + width +
                ", height=" + height +
                ", audioBitrate=" + audioBitrate +
                ", audioCodec='" + audioCodec + '\'' +
                ", videoCodec='" + videoCodec + '\'' +
                "} " + super.toString();
    }

}
