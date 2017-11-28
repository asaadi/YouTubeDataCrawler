package com.github.khangnt.youtubecrawler.model.youtube.stream;

import com.github.khangnt.youtubecrawler.Const;

/**
 * Created by Khang NT on 11/24/17.
 * Email: khang.neon.1997@gmail.com
 */

public class YouTubeLiveStream extends YouTubeStream {
    public YouTubeLiveStream(String manifestUrl) {
        super(new UrlLazy(() -> manifestUrl), Const.UNKNOWN_VALUE, "-1", "m3u8",
                "application/vnd.apple.mpegurl");
    }
}
