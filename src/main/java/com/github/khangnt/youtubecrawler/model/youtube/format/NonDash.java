package com.github.khangnt.youtubecrawler.model.youtube.format;

import com.github.khangnt.youtubecrawler.Const;

import static com.github.khangnt.youtubecrawler.model.youtube.format.AudioEncoding.AAC;
import static com.github.khangnt.youtubecrawler.model.youtube.format.AudioEncoding.MP3;
import static com.github.khangnt.youtubecrawler.model.youtube.format.AudioEncoding.VORBIS;
import static com.github.khangnt.youtubecrawler.model.youtube.format.Container.FLV;
import static com.github.khangnt.youtubecrawler.model.youtube.format.Container.MP4;
import static com.github.khangnt.youtubecrawler.model.youtube.format.Container.WEBM;
import static com.github.khangnt.youtubecrawler.model.youtube.format.Container._3GP;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public enum NonDash implements YouTubeFormat {
    I5("5", FLV, 240, 400, MP3, "h263", 64),
    I6("6", FLV, 270, 450, MP3, "h263", 64),

    I13("13", _3GP, Const.UNKNOWN_VALUE, Const.UNKNOWN_VALUE, AAC, "mp4v", Const.UNKNOWN_VALUE),

    I17("17", _3GP, 144, 176, AAC, "mp4v", 24),
    I18("18", MP4, 360, 640, AAC, "h264", 96),

    I22("22", MP4, 720, 1280, AAC, "h264", 192),

    I34("34", FLV, 360, 640, AAC, "h264", 128),
    I35("35", FLV, 480, 854, AAC, "h264", 128),
    I36("36", _3GP, 240, 320, AAC, "mp4v", 32),
    I37("37", MP4, 1080, 1920, AAC, "h264", 192),
    I38("38", MP4, 3702, 4096, AAC, "h264", 192),
    I43("43", WEBM, 360, 640, VORBIS, "vp8", 128),
    I44("44", WEBM, 480, 854, VORBIS, "vp8", 128),
    I45("45", WEBM, 720, 1280, VORBIS, "vp8", 192),
    I46("46", WEBM, 1080, 1920, VORBIS, "vp8", 192),
    I59("59", MP4, 480, 854, AAC, "h264", 128),
    I78("78", MP4, 480, 854, AAC, "h264", 128);
    private String itag;
    private Container container;
    private int height;
    private int width;
    private AudioEncoding audioEncoding;
    private String videoCodec;
    private int audioBitrate;

    NonDash(String itag, Container container, int height, int width, AudioEncoding audioEncoding,
            String videoCodec, int audioBitrate) {
        this.itag = itag;
        this.container = container;
        this.height = height;
        this.width = width;
        this.audioEncoding = audioEncoding;
        this.videoCodec = videoCodec;
        this.audioBitrate = audioBitrate;
    }

    @Override
    public String getItag() {
        return itag;
    }

    @Override
    public Container getContainer() {
        return container;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public AudioEncoding getAudioEncoding() {
        return audioEncoding;
    }

    public String getVideoCodec() {
        return videoCodec;
    }

    public int getAudioBitrate() {
        return audioBitrate;
    }

    @Override
    public String toString() {
        return "NonDash{" +
                "itag='" + itag + '\'' +
                ", container=" + container +
                ", height=" + height +
                ", width=" + width +
                ", audioEncoding=" + audioEncoding +
                ", videoCodec='" + videoCodec + '\'' +
                ", audioBitrate=" + audioBitrate +
                "} " + super.toString();
    }
}
