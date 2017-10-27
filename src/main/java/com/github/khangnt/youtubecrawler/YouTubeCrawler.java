package com.github.khangnt.youtubecrawler;

import com.github.khangnt.youtubecrawler.model.*;
import rx.Observable;

/**
 * Created by khangnt on 10/24/17.
 */
public interface YouTubeCrawler {

    Observable<YouTubeHome> getHomePage();

    Observable<YouTubeTrending> getTrendingPage();

    Observable<YouTubeWatchingVideo> getWatchingVideoPage(String videoId);

    Observable<YouTubeWatchingPlaylist> getWatchingPlaylistPage(String playlistId, String videoId);

    Observable<YouTubePlaylist> getYouTubePlaylistPage(String playlistId, int maxPlaylistItemCount);

    Observable<YouTubeSearch> getYouTubeSearchPage(String query);

}
