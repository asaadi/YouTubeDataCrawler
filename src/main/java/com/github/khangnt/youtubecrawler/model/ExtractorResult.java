package com.github.khangnt.youtubecrawler.model;

import com.github.khangnt.youtubecrawler.model.youtube.stream.Subtitle;
import com.github.khangnt.youtubecrawler.model.youtube.stream.YouTubeStream;

import java.util.List;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public class ExtractorResult {
    private String videoId;
    private List<YouTubeStream> youTubeStreams;
    private List<Subtitle> subtitles;

    public ExtractorResult(String videoId, List<YouTubeStream> youTubeStreams, List<Subtitle> subtitles) {
        this.videoId = videoId;
        this.youTubeStreams = youTubeStreams;
        this.subtitles = subtitles;
    }

    public String getVideoId() {
        return videoId;
    }

    public List<YouTubeStream> getYouTubeStreams() {
        return youTubeStreams;
    }

    public List<Subtitle> getSubtitles() {
        return subtitles;
    }

}
