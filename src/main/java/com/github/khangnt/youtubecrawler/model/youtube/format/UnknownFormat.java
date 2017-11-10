package com.github.khangnt.youtubecrawler.model.youtube.format;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public class UnknownFormat implements YouTubeFormat {
    private String itag;

    public UnknownFormat(String itag) {
        this.itag = itag;
    }

    @Override
    public String getItag() {
        return itag;
    }

    @Override
    public Container getContainer() {
        return null;
    }

}
