package com.github.khangnt.youtubecrawler.model.youtube.stream;

import com.github.khangnt.youtubecrawler.Const;

/**
 * Created by Khang NT on 11/24/17.
 * Email: khang.neon.1997@gmail.com
 */

public class YouTubeLiveStream extends YouTubeStream {
    public enum Type {
        HLS {
            @Override
            public String toString() {
                return "m3u8";
            }
        }, DASH {
            @Override
            public String toString() {
                return "mpd";
            }
        }
    }

    private Type type;

    public YouTubeLiveStream(String manifestUrl, Type type) {
        super(new UrlLazy(() -> manifestUrl), Const.UNKNOWN_VALUE, "-1", type.toString(),
                "application/vnd.apple.mpegurl");
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "YouTubeLiveStream{" +
                "type=" + type +
                "} " + super.toString();
    }
}
