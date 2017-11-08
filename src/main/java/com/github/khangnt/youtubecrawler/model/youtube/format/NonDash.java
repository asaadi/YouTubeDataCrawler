package com.github.khangnt.youtubecrawler.model.youtube.format;

import static com.github.khangnt.youtubecrawler.model.youtube.format.AudioEncoding.*;
import static com.github.khangnt.youtubecrawler.model.youtube.format.Container.*;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public enum  NonDash implements YouTubeFormat {
    I5(5, FLV, 240, MP3, 64),
    I6(6, FLV, 270, MP3, 64),

    I13(13, _3GP, 270 /*Guess*/, AAC, 64 /*Guess*/),

    I17(17, _3GP, 144, AAC, 24),
    I18(18, MP4, 360, AAC, 96),

    I22(22, MP4, 720, AAC, 192),

    I34(34, FLV, 360, AAC, 128),
    I35(35, FLV, 480, AAC, 128),
    I36(36, _3GP, 240, AAC, 32),
    I37(37, MP4, 1080, AAC, 192),
    I38(38, MP4, 3702, AAC, 192),
    I43(43, WEBM, 360, VORBIS, 128),
    I44(44, WEBM, 480, VORBIS, 128),
    I45(45, WEBM, 720, VORBIS, 192),
    I46(46, WEBM, 1080, VORBIS, 192),
    I59(59, MP4, 480, AAC, 128),
    I78(78, MP4, 480, AAC, 128),
    ;
    private int itag;
    private Container container;
    private int videoResolution;
    private AudioEncoding audioEncoding;
    private int audioBitrate;

    NonDash(int itag, Container container, int videoResolution, AudioEncoding audioEncoding, int audioBitrate) {
        this.itag = itag;
        this.container = container;
        this.videoResolution = videoResolution;
        this.audioEncoding = audioEncoding;
        this.audioBitrate = audioBitrate;
    }

    @Override
    public int getItag() {
        return itag;
    }

    @Override
    public Container getContainer() {
        return container;
    }

    public int getVideoResolution() {
        return videoResolution;
    }

    public AudioEncoding getAudioEncoding() {
        return audioEncoding;
    }

    public int getAudioBitrate() {
        return audioBitrate;
    }

    @Override
    public String toString() {
        return "NonDash{" +
                "itag=" + itag +
                ", container=" + container +
                ", videoResolution=" + videoResolution +
                ", audioEncoding=" + audioEncoding +
                ", audioBitrate=" + audioBitrate +
                '}';
    }
}
