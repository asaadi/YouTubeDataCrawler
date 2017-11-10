package com.github.khangnt.youtubecrawler.model.youtube.format;

import static com.github.khangnt.youtubecrawler.model.youtube.format.AudioEncoding.AAC;
import static com.github.khangnt.youtubecrawler.model.youtube.format.Container.MP4;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public enum  LiveStreaming implements YouTubeFormat {
    I91("91", MP4, 144, AAC, 48),
    I92("92", MP4, 240, AAC, 48),
    I93("93", MP4, 360, AAC, 128),
    I94("94", MP4, 480, AAC, 128),
    I95("95", MP4, 720, AAC, 256),
    I96("96", MP4, 1080, AAC, 256),
    I132("132", MP4, 240, AAC, 48),
    I151("151", MP4, 72, AAC, 24),
    ;

    private String itag;
    private Container container;
    private int videoResolution;
    private AudioEncoding audioEncoding;
    private int audioBitrate;

    LiveStreaming(String itag, Container container, int videoResolution, AudioEncoding audioEncoding, int audioBitrate) {
        this.itag = itag;
        this.container = container;
        this.videoResolution = videoResolution;
        this.audioEncoding = audioEncoding;
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
        return "LiveStreaming{" +
                "itag=" + itag +
                ", container=" + container +
                ", videoResolution=" + videoResolution +
                ", audioEncoding=" + audioEncoding +
                ", audioBitrate=" + audioBitrate +
                '}';
    }
}
