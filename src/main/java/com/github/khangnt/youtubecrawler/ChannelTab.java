package com.github.khangnt.youtubecrawler;

/**
 * Created by Khang NT on 10/31/17.
 * Email: khang.neon.1997@gmail.com
 */

public enum ChannelTab {
    HOME("/"),
    VIDEOS("/videos"),
    PLAYLISTS("/playlists"),
    CHANNELS("/channels"),
    ABOUT("/about");

    String lastPathSegment;

    ChannelTab(String lastPathSegment) {
        this.lastPathSegment = lastPathSegment;
    }

    public String getLastPathSegment() {
        return lastPathSegment;
    }
}
