package com.github.khangnt.youtubecrawler.model;

import com.github.khangnt.youtubecrawler.Lazy;
import com.github.khangnt.youtubecrawler.model.youtube.stream.DashManifestInfo;
import com.github.khangnt.youtubecrawler.model.youtube.stream.Subtitle;
import com.github.khangnt.youtubecrawler.model.youtube.stream.YouTubeLiveStream;
import com.github.khangnt.youtubecrawler.model.youtube.stream.YouTubeStream;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

public class ExtractorResult {
    private String videoId;
    private String title;
    private DashManifestInfo dashManifestInfo;
    private List<YouTubeStream> youTubeStreams;
    private Lazy<List<Subtitle>> subtitleListLazy;

    public ExtractorResult(String videoId, String title, DashManifestInfo dashManifestInfo,
                           List<YouTubeStream> youTubeStreams, Lazy<List<Subtitle>> subtitleListLazy) {
        this.videoId = videoId;
        this.title = title;
        this.dashManifestInfo = dashManifestInfo;
        this.youTubeStreams = youTubeStreams;
        this.subtitleListLazy = subtitleListLazy;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getTitle() {
        return title;
    }

    public boolean hasLiveStream() {
        for (YouTubeStream youTubeStream : youTubeStreams) {
            if (youTubeStream instanceof YouTubeLiveStream) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public DashManifestInfo getDashManifestInfo() {
        return dashManifestInfo;
    }

    public List<YouTubeStream> getYouTubeStreams() {
        return youTubeStreams;
    }

    public Lazy<List<Subtitle>> getSubtitleListLazy() {
        return subtitleListLazy;
    }

}
