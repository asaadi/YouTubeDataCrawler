package com.github.khangnt.youtubecrawler.model.youtube.stream;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public class Subtitle {
    private String lang;
    private String url;
    private boolean autoCaption;

    public Subtitle(String lang, String url, boolean autoCaption) {
        this.lang = lang;
        this.url = url;
        this.autoCaption = autoCaption;
    }

    public String getLang() {
        return lang;
    }

    public String getUrl() {
        return url;
    }

    public boolean isAutoCaption() {
        return autoCaption;
    }

    @Override
    public String toString() {
        return "Subtitle{" +
                "lang='" + lang + '\'' +
                ", url='" + url + '\'' +
                ", autoCaption=" + autoCaption +
                '}';
    }
}
