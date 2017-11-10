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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnknownFormat that = (UnknownFormat) o;

        return itag != null ? itag.equals(that.itag) : that.itag == null;
    }

    @Override
    public int hashCode() {
        return itag != null ? itag.hashCode() : 0;
    }
}
