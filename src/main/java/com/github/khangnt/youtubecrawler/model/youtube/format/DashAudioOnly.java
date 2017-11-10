package com.github.khangnt.youtubecrawler.model.youtube.format;

import static com.github.khangnt.youtubecrawler.model.youtube.format.AudioEncoding.*;
import static com.github.khangnt.youtubecrawler.model.youtube.format.Container.*;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

/**
 * Reference https://github.com/rg3/youtube-dl/blob/master/youtube_dl/extractor/youtube.py#L367
 */
public enum  DashAudioOnly implements YouTubeFormat {
    /* Dash mp4 audio */
    I139("139", M4A, AAC, 48),
    I140("140", M4A, AAC, 128),
    I141("141", M4A, AAC, 256),
    I256("256", M4A, AAC, 128 /*Guess number*/),
    I258("258", M4A, AAC, 128 /*Guess number*/),

    /* Dash webm audio */
    I171("171", WEBM, VORBIS, 128),
    I172("172", WEBM, VORBIS, 256),

    /* Dash webm audio with opus inside - only compatible with android 21+ */
    I249("249", WEBM, OPUS, 50),
    I250("250", WEBM, OPUS, 70),
    I251("251", WEBM, OPUS, 160),
    ;
    private String itag;
    private Container container;
    private AudioEncoding audioEncoding;
    private int audioBitrate;

    DashAudioOnly(String itag, Container container, AudioEncoding audioEncoding, int audioBitrate) {
        this.itag = itag;
        this.container = container;
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

    public AudioEncoding getAudioEncoding() {
        return audioEncoding;
    }

    public int getAudioBitrate() {
        return audioBitrate;
    }

    @Override
    public String toString() {
        return "DashAudioOnly{" +
                "itag=" + itag +
                ", container=" + container +
                ", audioEncoding=" + audioEncoding +
                ", audioBitrate=" + audioBitrate +
                '}';
    }
}
