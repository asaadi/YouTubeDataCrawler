package com.github.khangnt.youtubecrawler;

import com.github.khangnt.youtubecrawler.model.ResponseData;
import com.github.khangnt.youtubecrawler.model.youtube.FeedResponse;
import com.github.khangnt.youtubecrawler.model.youtube.WindowSettings;

import okhttp3.Request;
import rx.Observable;

import static com.github.khangnt.youtubecrawler.Utils.addDefaultWebPageReqHeader;
import static com.github.khangnt.youtubecrawler.Utils.parseWindowSettings;
import static com.github.khangnt.youtubecrawler.Utils.parseXhrResponse;
import static com.github.khangnt.youtubecrawler.Utils.rx;
import static com.github.khangnt.youtubecrawler.Utils.string;

/**
 * Created by Khang NT on 10/30/17.
 * Email: khang.neon.1997@gmail.com
 */

public class YoutubeFeed {
    private static final String HOME_URL = "https://m.youtube.com/";
    private static final String TRENDING_URL = "https://m.youtube.com/feed/trending";
    private static final String HOME_FEED_URL = "https://m.youtube.com/feed?ajax=1&layout=tablet&tsp=1&utcoffset=" + C.UTC_OFFSET;
    private static final String TRENDING_FEED_URL = "https://m.youtube.com/feed/trending?ajax=1&&layout=tablet&tsp=1&utcoffset=" + C.UTC_OFFSET;
    private YouTubeData youTubeData;

    YoutubeFeed(YouTubeData youTubeData) {
        this.youTubeData = youTubeData;
    }

    public Observable<ResponseData<FeedResponse>> home() {
        Request.Builder webPageReqBuilder = new Request.Builder()
                .url(HOME_URL);
        addDefaultWebPageReqHeader(webPageReqBuilder);
        return rx(youTubeData.getOkHttpClient().newCall(webPageReqBuilder.build()))
                .to(string())
                .flatMap(parseWindowSettings(youTubeData.getGson()))
                .flatMap(windowSettings -> fetchFeed(HOME_FEED_URL, HOME_URL, windowSettings));

    }

    public Observable<ResponseData<FeedResponse>> trending() {
        Request.Builder webPageReqBuilder = new Request.Builder()
                .url(TRENDING_URL);
        addDefaultWebPageReqHeader(webPageReqBuilder);
        return rx(youTubeData.getOkHttpClient().newCall(webPageReqBuilder.build()))
                .to(string())
                .flatMap(parseWindowSettings(youTubeData.getGson()))
                .flatMap(windowSettings -> fetchFeed(TRENDING_FEED_URL, TRENDING_URL, windowSettings));

    }

    private Observable<ResponseData<FeedResponse>> fetchFeed(String url, String referer,
                                                             WindowSettings windowSettings) {
        Request request = Utils.createXhrRequest(url, referer, windowSettings);
        return rx(youTubeData.getOkHttpClient().newCall(request))
                .to(string())
                .map(parseXhrResponse(youTubeData.getGson(), FeedResponse.class))
                .map(feedResponse -> createResponseData(feedResponse, windowSettings, referer));
    }

    private ResponseData<FeedResponse> createResponseData(FeedResponse feedResponse,
                                                          WindowSettings windowSettings,
                                                          String referer) {
        if (feedResponse.isSuccess() && feedResponse.getFeed() != null
                && feedResponse.getFeed().getNextUrl() != null) {
            String nextUrl = feedResponse.getFeed().getNextUrl();
            return new ResponseData<>(feedResponse, fetchFeed(nextUrl, referer, windowSettings));
        } else {
            return new ResponseData<>(feedResponse, null);
        }
    }

}
