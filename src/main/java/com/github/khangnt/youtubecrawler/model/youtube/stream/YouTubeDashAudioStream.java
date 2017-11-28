package com.github.khangnt.youtubecrawler.model.youtube.stream;

/**
 * Created by Khang NT on 11/24/17.
 * Email: khang.neon.1997@gmail.com
 */

public class YouTubeDashAudioStream extends YouTubeDashStream {

    private String audioCodec;
    private int channelCount;

    public YouTubeDashAudioStream(UrlLazy urlLazy, long expireAt, String itag, String container,
                                  String mimeType, int bandwidth, int contentLength, String audioCodec,
                                  int channelCount) {
        super(urlLazy, expireAt, itag, container, mimeType, bandwidth, contentLength);
        this.audioCodec = audioCodec;
        this.channelCount = channelCount;
    }

    public String getAudioCodec() {
        return audioCodec;
    }

    public int getChannelCount() {
        return channelCount;
    }

    @Override
    public String toString() {
        return "YouTubeDashAudioStream{" +
                "audioCodec='" + audioCodec + '\'' +
                ", channelCount=" + channelCount +
                "} " + super.toString();
    }

}
