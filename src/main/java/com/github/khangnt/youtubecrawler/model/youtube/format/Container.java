package com.github.khangnt.youtubecrawler.model.youtube.format;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public enum Container {
    _3GP("3gp"), MP4("mp4"), WEBM("webm"), M4A("m4a"), FLV("flv");

    private String name;

    Container(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
