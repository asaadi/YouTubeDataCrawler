package com.github.khangnt.youtubecrawler.internal;

import com.github.khangnt.youtubecrawler.exception.HttpClientException;
import com.github.khangnt.youtubecrawler.exception.RegexMismatchException;
import com.github.khangnt.youtubecrawler.model.youtube.WindowSettings;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import rx.Emitter;
import rx.Observable;
import rx.functions.Func1;

import static com.github.khangnt.youtubecrawler.internal.Headers.HTTP_ACCEPT;
import static com.github.khangnt.youtubecrawler.internal.Headers.HTTP_ACCEPT_LANGUAGE;
import static com.github.khangnt.youtubecrawler.internal.Headers.HTTP_REFERER;
import static com.github.khangnt.youtubecrawler.internal.Headers.HTTP_USER_AGENT;
import static com.github.khangnt.youtubecrawler.internal.Headers.X_YOUTUBE_CLIENT_NAME;
import static com.github.khangnt.youtubecrawler.internal.Headers.X_YOUTUBE_CLIENT_VERSION;
import static com.github.khangnt.youtubecrawler.internal.Headers.X_YOUTUBE_PAGE_CL;
import static com.github.khangnt.youtubecrawler.internal.Headers.X_YOUTUBE_PAGE_LABEL;
import static com.github.khangnt.youtubecrawler.internal.Headers.X_YOUTUBE_VARIANTS_CHECKSUM;


/**
 * Created by Khang NT on 10/30/17.
 * Email: khang.neon.1997@gmail.com
 */

public class Utils {

    static void addXYouTubeHeader(Request.Builder builder, WindowSettings windowSettings) {
        builder.header(X_YOUTUBE_VARIANTS_CHECKSUM, windowSettings.getVariantChecksum())
                .header(X_YOUTUBE_PAGE_LABEL, windowSettings.getBuildLabel())
                .header(X_YOUTUBE_PAGE_CL, windowSettings.getBuildId())
                .header(X_YOUTUBE_CLIENT_VERSION, windowSettings.getClientVersion())
                .header(X_YOUTUBE_CLIENT_NAME, windowSettings.getClientName());
    }

    public static Request.Builder mobileWebPageDownloadRequestBuilder(String url) {
        return new Request.Builder().url(url)
                .addHeader(HTTP_ACCEPT, C.BROWSER_ACCEPT)
                .addHeader(HTTP_ACCEPT_LANGUAGE, C.BROWSER_ACCEPT_LANGUAGE)
                .addHeader(HTTP_USER_AGENT, C.MOBILE_BROWSER_USER_AGENT);
    }

    public static Observable<Response> rx(Call call) {
        return Observable.create(emitter -> {
            try {
                Response response = call.execute();
                emitter.onNext(response);
                emitter.onCompleted();
            } catch (IOException ex) {
                emitter.onError(ex);
            }
        }, Emitter.BackpressureMode.NONE);
    }

    public static Func1<Observable<Response>, Observable<String>> string() {
        return responseObservable -> responseObservable.flatMap(response -> {
            try {
                if (response.code() / 100 == 2) {
                    //noinspection ConstantConditions
                    return Observable.just(response.body().string());
                } else {
                    return Observable.error(
                            new HttpClientException(response.code(), response.message()));
                }
            } catch (IOException e) {
                return Observable.error(e);
            } finally {
                response.close();
            }
        });
    }

    public static Func1<String, Observable<WindowSettings>> parseWindowSettings(Gson gson) {
        return webPage -> {
            Matcher matcher = RegexUtils.search("window\\.settings\\s*=\\s*(\\{.+?\\})\\s*;", webPage);
            if (matcher != null) {
                String windowSettingsJson = matcher.group(1);
                WindowSettings windowSettings = gson.fromJson(windowSettingsJson, WindowSettings.class);
                return Observable.just(windowSettings);
            } else {
                return Observable.error(new RegexMismatchException("Couldn't parse windows settings"));
            }
        };
    }

    public static <T> Func1<String, T> parseAjaxResponse(Gson gson, Class<T> tClass) {
        return ajaxRes -> {
            int offset = ajaxRes.indexOf("{");
            // todo: handle utf-32 unescape
            ajaxRes = ajaxRes.substring(offset)
                    .replaceAll("(\\\\U[a-f0-9A-F]{8})", "");
            return gson.fromJson(ajaxRes, tClass);
        };
    }

    public static Request createAjaxRequest(String url, String referer, WindowSettings windowSettings) {
        url = getYouTubeFullUrl(url);
        Request.Builder requestBuilder = new Request.Builder().url(url)
                .addHeader(HTTP_ACCEPT, "*/*")
                .addHeader(HTTP_REFERER, referer)
                .addHeader(HTTP_ACCEPT_LANGUAGE, C.BROWSER_ACCEPT_LANGUAGE)
                .addHeader(HTTP_USER_AGENT, C.MOBILE_BROWSER_USER_AGENT);
        Utils.addXYouTubeHeader(requestBuilder, windowSettings);
        return requestBuilder.build();
    }

    public static String getYouTubeFullUrl(String endpoint) {
        if (endpoint.startsWith("//")) {
            return "https:" + endpoint;
        } else if (endpoint.startsWith("/")) {
            return "https://m.youtube.com" + endpoint;
        }
        return endpoint;
    }

    static Map<String, List<String>> splitQuery(String url) throws UnsupportedEncodingException {
        final Map<String, List<String>> queryPairs = new LinkedHashMap<>();
        final String[] pairs = url.split("&");
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (!queryPairs.containsKey(key)) {
                queryPairs.put(key, new LinkedList<>());
            }
            final String value = idx > 0 && pair.length() > idx + 1 ?
                    URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            queryPairs.get(key).add(value);
        }
        return queryPairs;
    }


}
