package com.github.khangnt.youtubecrawler;

import com.github.khangnt.youtubecrawler.model.youtube.ArtistWatchCard;
import com.github.khangnt.youtubecrawler.model.youtube.CompactChannel;
import com.github.khangnt.youtubecrawler.model.youtube.CompactPlaylist;
import com.github.khangnt.youtubecrawler.model.youtube.CompactRadio;
import com.github.khangnt.youtubecrawler.model.youtube.CompactVideo;
import com.github.khangnt.youtubecrawler.model.youtube.Feed;
import com.github.khangnt.youtubecrawler.model.youtube.FeedResponse;
import com.github.khangnt.youtubecrawler.model.youtube.ItemSection;
import com.github.khangnt.youtubecrawler.model.youtube.SectionList;
import com.github.khangnt.youtubecrawler.model.youtube.Shelf;
import com.github.khangnt.youtubecrawler.model.youtube.VerticalList;
import com.github.khangnt.youtubecrawler.model.youtube.VideoWithContext;
import com.github.khangnt.youtubecrawler.model.youtube.WatchCardAlbumList;
import com.github.khangnt.youtubecrawler.model.youtube.WatchCardVideoList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Created by khangnt on 10/24/17.
 */
public class YouTubeData {

    private OkHttpClient okHttpClient;
    private Gson gson;

    private YouTubeData(OkHttpClient okHttpClient, Gson gson) {
        this.okHttpClient = okHttpClient;
        this.gson = gson;
    }


    OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    Gson getGson() {
        return gson;
    }

    public YoutubeFeed feed() {
        return new YoutubeFeed(this);
    }

    public static final class Builder {
        private OkHttpClient.Builder okHttpClientBuilder;
        private GsonBuilder gsonBuilder;

        public Builder() {
            this(new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS));
        }

        public Builder(OkHttpClient.Builder okHttpClientBuilder) {
            this.okHttpClientBuilder = okHttpClientBuilder.followRedirects(true);
            this.gsonBuilder = new GsonBuilder()
                    .registerTypeAdapter(ArtistWatchCard.class, new ArtistWatchCard.TypeAdapter())
                    .registerTypeAdapter(CompactChannel.class, new CompactChannel.TypeAdapter())
                    .registerTypeAdapter(CompactPlaylist.class, new CompactPlaylist.TypeAdapter())
                    .registerTypeAdapter(CompactRadio.class, new CompactRadio.TypeAdapter())
                    .registerTypeAdapter(CompactVideo.class, new CompactVideo.TypeAdapter())
                    .registerTypeAdapter(Feed.class, new Feed.TypeAdapter())
                    .registerTypeAdapter(FeedResponse.class, new FeedResponse.TypeAdapter())
                    .registerTypeAdapter(ItemSection.class, new ItemSection.TypeAdapter())
                    .registerTypeAdapter(SectionList.class, new SectionList.TypeAdapter())
                    .registerTypeAdapter(Shelf.class, new Shelf.TypeAdapter())
                    .registerTypeAdapter(VerticalList.class, new VerticalList.TypeAdapter())
                    .registerTypeAdapter(VideoWithContext.class, new VideoWithContext.TypeAdapter())
                    .registerTypeAdapter(WatchCardAlbumList.class, new WatchCardAlbumList.TypeAdapter())
                    .registerTypeAdapter(WatchCardVideoList.class, new WatchCardVideoList.TypeAdapter())
                    .setLenient();
        }

        public Builder setCache(File cacheDir, long cacheSize) {
            okHttpClientBuilder.cache(new Cache(cacheDir, cacheSize));
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            okHttpClientBuilder.addInterceptor(interceptor);
            return this;
        }

        public Builder addNetworkInterceptor(Interceptor interceptor) {
            okHttpClientBuilder.addNetworkInterceptor(interceptor);
            return this;
        }

        public Builder setCookieJar(CookieJar cookieJar) {
            okHttpClientBuilder.cookieJar(cookieJar);
            return this;
        }

        public YouTubeData build() {
            Gson gson = gsonBuilder.create();
            OkHttpClient okHttpClient = okHttpClientBuilder.build();
            if (okHttpClient.cookieJar() == null) {
                throw new IllegalStateException("CookieJar is required");
            }
            return new YouTubeData(okHttpClient, gson);
        }
    }

}
