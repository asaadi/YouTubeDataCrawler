package com.github.khangnt.youtubecrawler;

import com.github.khangnt.youtubecrawler.model.ExtractorResult;

import rx.Observable;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public interface YouTubeStreamExtractor {
    class Options {
        // todo: add builder
        private boolean markWatched;

        public Options(boolean markWatched) {
            this.markWatched = markWatched;
        }

        public boolean isMarkWatched() {
            return markWatched;
        }
    }

    Observable<ExtractorResult> extract(String vid, Options options);
}
