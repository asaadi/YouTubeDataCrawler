package com.github.khangnt.youtubecrawler.model.youtube.format;

/**
 * Created by Khang NT on 11/11/17.
 * Email: khang.neon.1997@gmail.com
 */

public class HlsManifest implements YouTubeFormat {
    public static final HlsManifest INSTANCE = new HlsManifest();

    private HlsManifest() {
    }

    @Override
    public String getItag() {
        return "hls";
    }

    @Override
    public Container getContainer() {
        return Container.M3U8;
    }
}
