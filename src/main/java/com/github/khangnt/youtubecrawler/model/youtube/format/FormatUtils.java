package com.github.khangnt.youtubecrawler.model.youtube.format;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public class FormatUtils {
    private static final Map<Integer, YouTubeFormat> sYoutubeFormatMap;
    static {
        sYoutubeFormatMap = new HashMap<>();
        NonDash[] nonDashes = NonDash.values();
        for (NonDash nonDash : nonDashes) {
            sYoutubeFormatMap.put(nonDash.getItag(), nonDash);
        }
        DashAudioOnly[] dashAudios = DashAudioOnly.values();
        for (DashAudioOnly dashAudio : dashAudios) {
            sYoutubeFormatMap.put(dashAudio.getItag(), dashAudio);
        }
        DashVideoOnly[] dashVideos = DashVideoOnly.values();
        for (DashVideoOnly dashVideo : dashVideos) {
            sYoutubeFormatMap.put(dashVideo.getItag(), dashVideo);
        }
        LiveStreaming[] liveStreamings = LiveStreaming.values();
        for (LiveStreaming liveStreaming : liveStreamings) {
            sYoutubeFormatMap.put(liveStreaming.getItag(), liveStreaming);
        }
    }

    public static YouTubeFormat findByItag(int itag) {
        YouTubeFormat format = sYoutubeFormatMap.get(itag);
        return format != null ? format : new UnknownFormat(itag);
    }
}
