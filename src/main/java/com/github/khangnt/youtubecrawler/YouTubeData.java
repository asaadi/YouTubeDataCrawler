package com.github.khangnt.youtubecrawler;

import com.github.khangnt.youtubecrawler.internal.C;
import com.github.khangnt.youtubecrawler.internal.Utils;
import com.github.khangnt.youtubecrawler.model.ResponseData;
import com.github.khangnt.youtubecrawler.model.youtube.AbstractResponse;
import com.github.khangnt.youtubecrawler.model.youtube.ArtistWatchCard;
import com.github.khangnt.youtubecrawler.model.youtube.Channel;
import com.github.khangnt.youtubecrawler.model.youtube.ChannelResponse;
import com.github.khangnt.youtubecrawler.model.youtube.CompactChannel;
import com.github.khangnt.youtubecrawler.model.youtube.CompactPlaylist;
import com.github.khangnt.youtubecrawler.model.youtube.CompactRadio;
import com.github.khangnt.youtubecrawler.model.youtube.CompactVideo;
import com.github.khangnt.youtubecrawler.model.youtube.Continuation;
import com.github.khangnt.youtubecrawler.model.youtube.Feed;
import com.github.khangnt.youtubecrawler.model.youtube.FeedResponse;
import com.github.khangnt.youtubecrawler.model.youtube.ItemSection;
import com.github.khangnt.youtubecrawler.model.youtube.SearchResponse;
import com.github.khangnt.youtubecrawler.model.youtube.SearchResult;
import com.github.khangnt.youtubecrawler.model.youtube.SectionList;
import com.github.khangnt.youtubecrawler.model.youtube.Shelf;
import com.github.khangnt.youtubecrawler.model.youtube.VerticalList;
import com.github.khangnt.youtubecrawler.model.youtube.VideoWithContext;
import com.github.khangnt.youtubecrawler.model.youtube.WatchCardAlbumList;
import com.github.khangnt.youtubecrawler.model.youtube.WatchCardVideoList;
import com.github.khangnt.youtubecrawler.model.youtube.WindowSettings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import rx.Observable;

import static com.github.khangnt.youtubecrawler.internal.Preconditions.notNull;
import static com.github.khangnt.youtubecrawler.internal.Utils.mobileWebPageDownloadRequestBuilder;
import static com.github.khangnt.youtubecrawler.internal.Utils.parseAjaxResponse;
import static com.github.khangnt.youtubecrawler.internal.Utils.parseWindowSettings;
import static com.github.khangnt.youtubecrawler.internal.Utils.rx;
import static com.github.khangnt.youtubecrawler.internal.Utils.string;

/**
 * Created by Khang NT on 10/24/17.
 * Email: khang.neon.1997@gmail.com
 */
public class YouTubeData {
    private static final String HOME_PAGE_URL = "https://m.youtube.com/";
    private static final String HOME_FEED_ENTRY_URL = "https://m.youtube.com/feed?ajax=1&layout=tablet&tsp=1&utcoffset=" + C.UTC_OFFSET;

    private static final String TRENDING_PAGE_URL = "https://m.youtube.com/feed/trending";
    private static final String TRENDING_FEED_ENTRY_URL = "https://m.youtube.com/feed/trending?ajax=1&&layout=tablet&tsp=1&utcoffset=" + C.UTC_OFFSET;

    private static final String RECOMMENDED_PAGE_URL = "https://m.youtube.com/feed/recommended";
    private static final String RECOMMENDED_FEED_ENTRY_URL = "https://m.youtube.com/feed/recommended?ajax=1&&layout=tablet&tsp=1&utcoffset=" + C.UTC_OFFSET;

    private static final String SEARCH_RESULT_PAGE_URL = "https://m.youtube.com/results"; // ?search_query=...
    private static final String SEARCH_RESULT_AJAX_URL = "https://m.youtube.com/results?ajax=1&layout=tablet&utcoffset=" + C.UTC_OFFSET;

    private static final String SEARCH_QUERY = "search_query";

    private static final String CHANNEL_HOME_PAGE_URL = "https://m.youtube.com/channel"; // /channel_id

    private static final String ACTION_CONTINUATION_PARAM = "action_continuation";
    private static final String CLICK_TRACKING_PARAM = "itct";
    private static final String CONTINUATION_TOKEN_PARAM = "ctoken";
    private static final String AJAX_PARAM = "ajax";
    private static final String LAYOUT_PARAM = "layout";
    private static final String UTC_OFFSET_PARAM = "utcoffset";

    private OkHttpClient okHttpClient;
    private Gson gson;

    private YouTubeData(OkHttpClient okHttpClient, Gson gson) {
        this.okHttpClient = okHttpClient;
        this.gson = gson;
    }


    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public Gson getGson() {
        return gson;
    }

    public Observable<ResponseData<FeedResponse>> homeFeed() {
        return getWindowSettings(HOME_PAGE_URL)
                .flatMap(windowSettings -> handleAjaxRequest(HOME_FEED_ENTRY_URL,
                        HOME_PAGE_URL, windowSettings, FeedResponse.class));

    }

    public Observable<ResponseData<FeedResponse>> trendingFeed() {
        return getWindowSettings(TRENDING_PAGE_URL)
                .flatMap(windowSettings -> handleAjaxRequest(TRENDING_FEED_ENTRY_URL,
                        TRENDING_PAGE_URL, windowSettings, FeedResponse.class));

    }

    public Observable<ResponseData<FeedResponse>> recommendedFeed() {
        return getWindowSettings(RECOMMENDED_PAGE_URL)
                .flatMap(windowSettings -> handleAjaxRequest(RECOMMENDED_FEED_ENTRY_URL,
                        RECOMMENDED_PAGE_URL, windowSettings, FeedResponse.class));

    }

    public Observable<ResponseData<SearchResponse>> search(String query) {
        String searchResultPageUrl = notNull(HttpUrl.parse(SEARCH_RESULT_PAGE_URL))
                .newBuilder().setQueryParameter(SEARCH_QUERY, query)
                .build().toString();
        String searchResultAjaxUrl = notNull(HttpUrl.parse(SEARCH_RESULT_AJAX_URL))
                .newBuilder().setQueryParameter(SEARCH_QUERY, query)
                .build().toString();
        return getWindowSettings(searchResultPageUrl)
                .flatMap(windowSettings -> handleAjaxRequest(searchResultAjaxUrl, searchResultPageUrl,
                        windowSettings, SearchResponse.class));
    }

    public Observable<ResponseData<ChannelResponse>> channel(String channelId, ChannelTab channelTab) {
        String channelHomePageUrl = CHANNEL_HOME_PAGE_URL + "/" + channelId
                + channelTab.getLastPathSegment();
        String ajaxUrl = notNull(HttpUrl.parse(channelHomePageUrl)).newBuilder()
                .setQueryParameter(AJAX_PARAM, "1")
                .setQueryParameter(LAYOUT_PARAM, "tablet")
                .setQueryParameter(UTC_OFFSET_PARAM, String.valueOf(C.UTC_OFFSET))
                .build().toString();
        return getWindowSettings(channelHomePageUrl)
                .flatMap(windowSettings -> handleAjaxRequest(ajaxUrl, channelHomePageUrl,
                        windowSettings, ChannelResponse.class));
    }

    public YouTubeStreamExtractor getStreamExtractor(SignatureDecipher signatureDecipher) {
        return new DefaultYouTubeStreamExtractor(okHttpClient, gson, signatureDecipher);
    }

    public YouTubeStreamExtractor getStreamExtractor() {
        return getStreamExtractor(new MusicAppSignatureDecipher(okHttpClient, gson));
    }

    private Observable<WindowSettings> getWindowSettings(String webPageUrl) {
        Request.Builder webPageReqBuilder = mobileWebPageDownloadRequestBuilder(webPageUrl);
        return rx(getOkHttpClient().newCall(webPageReqBuilder.build()))
                .to(string())
                .flatMap(parseWindowSettings(getGson()));
    }

    private <T extends AbstractResponse> Observable<ResponseData<T>> handleAjaxRequest(
            String ajaxUrl, String referer, WindowSettings windowSettings, Class<T> tClass) {
        Request request = Utils.createAjaxRequest(ajaxUrl, referer, windowSettings);
        return rx(getOkHttpClient().newCall(request))
                .to(string())
                .map(parseAjaxResponse(getGson(), tClass))
                .map(response -> createResponseData(response, ajaxUrl, referer, windowSettings, tClass));
    }

    private <T extends AbstractResponse> ResponseData<T> createResponseData(
            T response, String ajaxUrl, String referer, WindowSettings windowSettings, Class<T> tClass) {
        if (response.isSuccess() && response.hasContinuation()) {
            Continuation continuation = response.getContinuation();
            HttpUrl nextUrl = notNull(HttpUrl.parse(ajaxUrl)).newBuilder()
                    .setQueryParameter(ACTION_CONTINUATION_PARAM, "1")
                    .setQueryParameter(CONTINUATION_TOKEN_PARAM, continuation.getContinuationToken())
                    .setQueryParameter(CLICK_TRACKING_PARAM, continuation.getClickTrackingParams())
                    .build();
            return new ResponseData<>(response, handleAjaxRequest(nextUrl.toString(), referer, windowSettings, tClass));
        } else {
            return new ResponseData<>(response, null);
        }
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
                    .registerTypeAdapter(Channel.class, new Channel.TypeAdapter())
                    .registerTypeAdapter(ChannelResponse.class, new ChannelResponse.TypeAdapter())
                    .registerTypeAdapter(CompactChannel.class, new CompactChannel.TypeAdapter())
                    .registerTypeAdapter(CompactPlaylist.class, new CompactPlaylist.TypeAdapter())
                    .registerTypeAdapter(CompactRadio.class, new CompactRadio.TypeAdapter())
                    .registerTypeAdapter(CompactVideo.class, new CompactVideo.TypeAdapter())
                    .registerTypeAdapter(Feed.class, new Feed.TypeAdapter())
                    .registerTypeAdapter(FeedResponse.class, new FeedResponse.TypeAdapter())
                    .registerTypeAdapter(ItemSection.class, new ItemSection.TypeAdapter())
                    .registerTypeAdapter(SearchResponse.class, new SearchResponse.TypeAdapter())
                    .registerTypeAdapter(SearchResult.class, new SearchResult.TypeAdapter())
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
