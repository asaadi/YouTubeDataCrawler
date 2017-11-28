package com.github.khangnt.youtubecrawler.model.youtube.format;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public enum  AudioEncoding {
    AAC("mp4a"), VORBIS("vorbis"), OPUS("opus"), MP3("mpeg");
    private String codec;

    AudioEncoding(String codec) {
        this.codec = codec;
    }

    public String getCodec() {
        return codec;
    }

    @Override
    public String toString() {
        return codec;
    }
}
