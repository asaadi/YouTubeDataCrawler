package com.github.khangnt.youtubecrawler;

import com.github.khangnt.youtubecrawler.exception.AgeRestrictionException;
import com.github.khangnt.youtubecrawler.exception.BadExtractorException;
import com.github.khangnt.youtubecrawler.exception.HttpClientException;
import com.github.khangnt.youtubecrawler.exception.NotSupportedVideoException;
import com.github.khangnt.youtubecrawler.exception.VideoNotAvailableException;
import com.github.khangnt.youtubecrawler.internal.NaturalDeserializer;
import com.github.khangnt.youtubecrawler.internal.RegexUtils;
import com.github.khangnt.youtubecrawler.internal.Utils;
import com.github.khangnt.youtubecrawler.model.ExtractorResult;
import com.github.khangnt.youtubecrawler.model.youtube.format.FormatUtils;
import com.github.khangnt.youtubecrawler.model.youtube.format.HlsManifest;
import com.github.khangnt.youtubecrawler.model.youtube.format.YouTubeFormat;
import com.github.khangnt.youtubecrawler.model.youtube.stream.Subtitle;
import com.github.khangnt.youtubecrawler.model.youtube.stream.UrlLazy;
import com.github.khangnt.youtubecrawler.model.youtube.stream.YouTubeStream;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import rx.Completable;
import rx.Emitter;
import rx.Observable;
import rx.schedulers.Schedulers;

import static com.github.khangnt.youtubecrawler.internal.Preconditions.notNull;
import static com.github.khangnt.youtubecrawler.internal.RegexUtils.search;
import static com.github.khangnt.youtubecrawler.internal.RegexUtils.sub;
import static com.github.khangnt.youtubecrawler.internal.Utils.closeQuietly;
import static com.github.khangnt.youtubecrawler.internal.Utils.desktopWebPageDownloadRequestBuilder;
import static com.github.khangnt.youtubecrawler.internal.Utils.isEmpty;
import static com.github.khangnt.youtubecrawler.internal.Utils.splitQuery;
import static com.github.khangnt.youtubecrawler.model.youtube.stream.YouTubeStream.UNKNOWN_TIME;

/**
 * Created by Khang NT on 11/8/17.
 * Email: khang.neon.1997@gmail.com
 */

/**
 * Reference: https://github.com/rg3/youtube-dl/blob/master/youtube_dl/extractor/youtube.py
 */
public class DefaultYouTubeStreamExtractor implements YouTubeStreamExtractor {

    private OkHttpClient okHttpClient;
    private Gson gson;
    private SignatureDecipher signatureDecipher;

    public DefaultYouTubeStreamExtractor(OkHttpClient okHttpClient, Gson gson, SignatureDecipher signatureDecipher) {
        this.okHttpClient = okHttpClient;
        this.gson = gson;
        this.signatureDecipher = signatureDecipher;
    }

    private String blockingDownload(String url, boolean fatal, String fatalMessage) {
        Response response = null;
        Exception exception;
        try {
            response = okHttpClient.newCall(desktopWebPageDownloadRequestBuilder(url).build())
                    .execute();
            if (response.code() / 100 == 2) {
                //noinspection ConstantConditions
                return response.body().string();
            } else {
                exception = new HttpClientException(response.code(), response.message());
            }
        } catch (IOException error) {
            exception = error;
        } finally {
            Utils.closeQuietly(response);
        }
        if (fatal) {
            throw new RuntimeException(fatalMessage, exception);
        } else {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public Observable<ExtractorResult> extract(String vid, Options options) {
        return Observable.create(emitter -> {
            Matcher matcher;
            String url;
            String videoWebPage;
            String videoInfoWebPage;
            String playerUrl = null;
            String dashManifestUrl = null;
            boolean isLive = false;
            String embedWebpage = null;
            String sts = null;
            Map ytPlayerConfig;
            Map<String, List<String>> videoInfo = null;
            Set<String> dashMpds = new LinkedHashSet<>();

            Map<YouTubeFormat, YouTubeStream> streams = new HashMap<>();
            try {
                url = "https://www.youtube.com/watch?&gl=US&hl=en&has_verified=1&bpctr=9999999999&v=" + vid;
                videoWebPage = blockingDownload(url, true, "Failed to download video web page");

                // Attempt to extract SWF player URL
                matcher = search("swfConfig.*?\"(https?:\\\\/\\\\/.*?watch.*?-.*?\\.swf)\"", videoWebPage);
                if (matcher != null) {
                    playerUrl = sub("\\\\(.)", "$1", matcher.group(1));
                }
                if (search("player-age-gate-content\">", videoWebPage) != null) {
                    emitter.onError(new AgeRestrictionException("Login to view age gate content", vid));
                    return;
                }
                ytPlayerConfig = getYtPlayerConfig(vid, videoWebPage);
                if (ytPlayerConfig != null) {
                    Map args = ((Map) ytPlayerConfig.get("args"));
                    if (!isEmpty((String)args.get("url_encoded_fmt_stream_map"))) {
                        videoInfo = new HashMap<>();
                        // Convert to the same format returned by Utils.splitQuery()
                        for (Object key : args.keySet()) {
                            List<String> values = new ArrayList<>();
                            values.add(args.get(key).toString());
                            videoInfo.put(key.toString(), values);
                        }
                        List<String> dashMpd = videoInfo.get("dashmpd");
                        if (!isEmpty(dashMpd)) {
                            dashMpds.add(dashMpd.get(0));
                        }
                    }
                    if (videoInfo == null && args.get("ypc_vid") != null) {
                        emitter.onError(new NotSupportedVideoException("'rental' videos not supported.", vid));
                        return;
                    }
                    isLive = "1".equals(String.valueOf(args.get("livestream")))
                            || "1".equals(String.valueOf(args.get("live_playback")));
                    if (ytPlayerConfig.containsKey("sts")) {
                        sts = String.valueOf(ytPlayerConfig.get("sts"));
                    }
                }
                HttpUrl baseUrl = notNull(HttpUrl.parse("https://www.youtube.com/get_video_info" +
                        "?video_id=" + vid +
                        "&ps=default" +
                        "&eurl=" +
                        "&gl=US" +
                        "&hl=en"));
                // looking in get_video_info
                for (String el : Arrays.asList("info", "embedded", "detailpage", "vevo", "")) {
                    HttpUrl.Builder urlBuilder = baseUrl.newBuilder();
                    if (!isEmpty(el)) {
                        urlBuilder.setQueryParameter("el", el);
                    }
                    if (!isEmpty(sts)) {
                        urlBuilder.setQueryParameter("sts", sts);
                    }
                    videoInfoWebPage = blockingDownload(urlBuilder.build().toString(), false, null);
                    if (isEmpty(videoInfoWebPage)) continue;
                    Map<String, List<String>> getVideoInfo = splitQuery(videoInfoWebPage);
                    List<String> dashMpd = getVideoInfo.get("dashmpd");
                    if (dashMpd != null && !dashMpd.isEmpty()) {
                        dashMpds.add(dashMpd.get(0));
                    }
                    if (videoInfo == null) {
                        videoInfo = getVideoInfo;
                    }
                    if (getVideoInfo.containsKey("token")) {
                        if (!videoInfo.containsKey("token")) {
                            videoInfo = getVideoInfo;
                        }
                        break;
                    }
                }
                if (videoInfo == null || !videoInfo.containsKey("token")) {
                    if (videoInfo != null && videoInfo.containsKey("reason")) {
                        List<String> reason = videoInfo.get("reason");
                        if (!reason.isEmpty()) {
                            emitter.onError(new VideoNotAvailableException(reason.get(0), vid));
                            return;
                        }
                    }
                    emitter.onError(new BadExtractorException("'token' parameter not in video info for unknown reason", vid));
                    return;
                }

                // Check for "rental" videos
                if (videoInfo.containsKey("ypc_video_rental_bar_text")
                        && !videoInfo.containsKey("author")) {
                    emitter.onError(new NotSupportedVideoException("'rental' videos not supported.", vid));
                    return;
                }

                Lazy<List<Subtitle>> subtitleListLazy = new Lazy<>(() -> {
                    List<Subtitle> subtitles = new ArrayList<>();
                    subtitles.addAll(getSubtitles(vid, videoWebPage));
                    if (ytPlayerConfig != null) {
                        subtitles.addAll(getAutomaticCaptions(vid, videoWebPage, ytPlayerConfig));
                    }
                    return subtitles;
                });


                if (videoInfo.containsKey("conn") && videoInfo.get("conn").get(0).startsWith("rtmp")) {
                    emitter.onError(new NotSupportedVideoException("RTMP video not supported.", vid));
                    return;
                } else if (!isEmpty(videoInfo.get("url_encoded_fmt_stream_map")) && !isEmpty(videoInfo.get("url_encoded_fmt_stream_map").get(0))
                        || !isEmpty(videoInfo.get("adaptive_fmts")) && !isEmpty(videoInfo.get("adaptive_fmts").get(0))) {
                    String encodedUrlMap = videoInfo.get("url_encoded_fmt_stream_map").get(0)
                            + "," + videoInfo.get("adaptive_fmts").get(0);
                    if (encodedUrlMap.contains("rtmpe%3Dyes")) {
                        emitter.onError(new NotSupportedVideoException("RTMP video not supported.", vid));
                        return;
                    }
                    String[] stringSplit = encodedUrlMap.split(",");
                    for (String urlDataStr : stringSplit) {
                        Map<String, List<String>> urlData = splitQuery(urlDataStr);
                        if (!urlData.containsKey("itag") || !urlData.containsKey("url")) {
                            continue;
                        }
                        String itag = urlData.get("itag").get(0);
                        url = urlData.get("url").get(0);
                        String assetsRegex = "\"assets\":.+?\"js\":\\s*(\"[^\"]+\")";
                        matcher = search(assetsRegex, videoWebPage);
                        if (matcher == null) {
                            if (embedWebpage == null) {
                                embedWebpage = blockingDownload("https://www.youtube.com/embed/" + vid,
                                        true, "Download embed web page failed");
                            }
                            matcher = search(assetsRegex, embedWebpage);
                        }

                        if (matcher != null) {
                            JsonElement playerUrlJson = null;
                            try {
                                playerUrlJson = gson.fromJson(matcher.group(1), JsonElement.class);
                            } catch (Throwable ignore) {
                            }
                            if (playerUrlJson instanceof JsonPrimitive) {
                                playerUrl = playerUrlJson.getAsString();
                            }
                        }

                        if (!url.contains("ratebypass")) {
                            url += "&ratebypass=yes";
                        }

                        UrlLazy urlLazy;
                        final String playerUrlFinal = playerUrl;
                        final String urlFinal = url;
                        if (urlData.containsKey("sig")) {
                            urlLazy = new UrlLazy(() -> urlFinal + "&signature=" + urlData.get("sig").get(0));
                        } else if (urlData.containsKey("s")) {
                            if (playerUrl == null) {
                                emitter.onError(new BadExtractorException("Player url not found", vid));
                                return;
                            }
                            String encryptedSig = urlData.get("s").get(0);
                            urlLazy = new UrlLazy(() -> {
                                String sig = signatureDecipher.decrypt(vid, playerUrlFinal, encryptedSig);
                                return urlFinal + "&signature=" + sig;
                            });
                        } else {
                            urlLazy = new UrlLazy(() -> urlFinal);
                        }

                        long expiresAt = parseExpires(url, UNKNOWN_TIME);
                        YouTubeFormat youTubeFormat = FormatUtils.findByItag(itag);
                        streams.put(youTubeFormat, new YouTubeStream(urlLazy, expiresAt, youTubeFormat));
                    }
                } else if (isLive) {
                    if (isEmpty(videoInfo.get("hlsvp"))) {
                        emitter.onError(new BadExtractorException("Can't extract Live video HLS manifest link", vid));
                        return;
                    }
                    String manifestUrl = videoInfo.get("hlsvp").get(0);
                    UrlLazy urlLazy = new UrlLazy(() -> manifestUrl);
                    streams.put(HlsManifest.INSTANCE,
                            new YouTubeStream(urlLazy, UNKNOWN_TIME, HlsManifest.INSTANCE));
                } else {
                    emitter.onError(new BadExtractorException("no conn, hlsvp or url_encoded_fmt_stream_map information found in video info", vid));
                    return;
                }

                if (!isEmpty(playerUrl)) {
                    final String playerUrlFinal = playerUrl;
                    // Look for the DASH manifest
                    Map<YouTubeFormat, YouTubeStream> formats = new HashMap<>();
                    for (String mpdUrl : dashMpds) {
                        try {
                            mpdUrl = RegexUtils.sub("/s/([a-fA-F0-9\\.]+)", matcher1 -> {
                                String encryptedSig = matcher1.group(1);
                                String sig = signatureDecipher.decrypt(vid, playerUrlFinal, encryptedSig);
                                return "/signature/" + sig;
                            }, mpdUrl);

                            if (dashManifestUrl == null) {
                                dashManifestUrl = mpdUrl;
                            }

                            List<YouTubeStream> dashFormats = extractMpdFormats(mpdUrl, vid, streams.isEmpty());
                            for (YouTubeStream dashFormat : dashFormats) {
                                // Do not overwrite DASH format found in some previous DASH manifest
                                if (!formats.containsKey(dashFormat.getYouTubeFormat())) {
                                    formats.put(dashFormat.getYouTubeFormat(), dashFormat);
                                }
                            }
                        } catch (Exception e) {
                            // skip dash manifest
                            e.printStackTrace();
                        }
                    }
                    // Override the formats we found through non-DASH
                    streams.putAll(formats);
                }

                if (options != null && options.isMarkWatched()) {
                    markWatched(vid, videoInfo);
                }

                // title
                String title = videoInfo.get("title").get(0);

                emitter.onNext(new ExtractorResult(vid, title, dashManifestUrl,
                        new ArrayList<>(streams.values()), subtitleListLazy));
                emitter.onCompleted();
            } catch (Throwable anyError) {
                emitter.onError(anyError);
            }
        }, Emitter.BackpressureMode.NONE);
    }

    @Nullable
    private Map getYtPlayerConfig(String vid, String webPage) {
        Matcher matcher = search(";ytplayer\\.config\\s*=\\s*(\\{.+?\\});ytplayer",
                webPage);
        if (matcher == null) {
            matcher = search(";ytplayer\\.config\\s*=\\s*(\\{.+?\\});", webPage);
        }
        String configJson = matcher != null ? Utils.unescapeUtf32(matcher.group(1)) : null;
        return configJson != null ? parseJson(configJson, false, null) : null;
    }

    private Map parseJson(String json, boolean fatal, String fatalMessage) {
        try {
            //noinspection unchecked
            return (Map) NaturalDeserializer.deserialize(gson.fromJson(json, JsonElement.class));
        } catch (Exception ex) {
            if (fatal) {
                throw new RuntimeException(fatalMessage, ex);
            } else {
                ex.printStackTrace();
                return null;
            }
        }
    }

    @Nullable
    private Document blockingDownloadXml(String url, boolean fatal, String fatalMessage) {
        String xmlString = blockingDownload(url, fatal, fatalMessage);
        if (xmlString == null) return null;
        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return docBuilder.parse(new ByteArrayInputStream(xmlString.getBytes()));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            if (fatal) {
                throw new RuntimeException(fatalMessage, e);
            } else {
                e.printStackTrace();
                return null;
            }
        }
    }

    private List<Subtitle> getSubtitles(String videoId, String webPage) {
        List<Subtitle> subtitles = new ArrayList<>();
        Document document = blockingDownloadXml("https://video.google.com/timedtext?hl=en&type=list&v=" + videoId,
                false, null);
        if (document == null) return Collections.emptyList();
        document.getDocumentElement().normalize();
        NodeList trackList = document.getDocumentElement().getElementsByTagName("track");
        for (int i = 0; i < trackList.getLength(); i++) {
            Node track = trackList.item(i);
            String lang = null;
            String name = "";
            if (track.hasAttributes()) {
                lang = track.getAttributes().getNamedItem("lang_code").getTextContent();
                name = track.getAttributes().getNamedItem("name").getTextContent();
            }
            if (isEmpty(lang)) continue;
            HttpUrl url = notNull(HttpUrl.parse("https://www.youtube.com/api/timedtext"))
                    .newBuilder()
                    .setQueryParameter("lang", lang)
                    .setQueryParameter("v", videoId)
                    .setQueryParameter("fmt", "vtt")
                    .setQueryParameter("name", name)
                    .build();
            subtitles.add(new Subtitle(lang, url.toString(), false));
        }
        return subtitles;
    }

    private List<Subtitle> getAutomaticCaptions(String videoId, String webPage,
                                                @NotNull Map player_config) {
        Map args = (Map) player_config.get("args");
        String captionUrl = args.get("ttsurl") != null ? args.get("ttsurl").toString() : null;
        List<Subtitle> subtitles = new ArrayList<>();
        if (captionUrl != null) {
            String timestamp = String.valueOf(args.get("timestamp"));
            HttpUrl listUrl = notNull(HttpUrl.parse(captionUrl)).newBuilder()
                    .setQueryParameter("type", "list")
                    .setQueryParameter("tlangs", "1")
                    .setQueryParameter("asrs", "1")
                    .build();
            Document captionList = blockingDownloadXml(listUrl.toString(), false, null);
            if (captionList == null) return subtitles;
            NodeList nodeList = captionList.getElementsByTagName("track");
            if (nodeList == null || nodeList.getLength() == 0) {
                return subtitles;
            }
            Node originalLangNode = nodeList.item(0);
            String originalLang = originalLangNode.getAttributes().getNamedItem("lang_code").getTextContent();
            String captionKind = originalLangNode.getAttributes().getNamedItem("kind").getTextContent();
            nodeList = captionList.getElementsByTagName("target");
            for (int i = 0; i < nodeList.getLength(); i++) {
                String subLang = nodeList.item(i).getAttributes()
                        .getNamedItem("lang_code").getTextContent();
                HttpUrl url = notNull(HttpUrl.parse(captionUrl)).newBuilder()
                        .setQueryParameter("lang", originalLang)
                        .setQueryParameter("tlang", subLang)
                        .setQueryParameter("fmt", "vtt")
                        .setQueryParameter("ts", timestamp)
                        .setQueryParameter("kind", captionKind)
                        .build();
                subtitles.add(new Subtitle(subLang, url.toString(), true));
            }
        }
        return subtitles;
    }

    private List<YouTubeStream> extractMpdFormats(String mpdUrl, String videoId, boolean fatal) {
        // download manifest xml
        Response response = null;
        Exception exception;
        try {
            response = okHttpClient.newCall(desktopWebPageDownloadRequestBuilder(mpdUrl).build())
                    .execute();
            String manifestXml;
            if (response.code() / 100 == 2) {
                List<YouTubeStream> result = new ArrayList<>();
                //noinspection ConstantConditions
                manifestXml = response.body().string();
                String baseUrl = "https://" + response.request().url().host();
                Pattern baseUrlPattern = Pattern.compile("<BaseURL[^>]*>([^<]*)<\\/BaseURL>",
                        Pattern.CASE_INSENSITIVE);
                Matcher matcher = baseUrlPattern.matcher(manifestXml);
                while (matcher.find()) {
                    String url = Utils.simpleXmlUnescape(matcher.group(1));
                    if (!url.startsWith("http")) {
                        if (!url.startsWith("/")) {
                            url = "/" + url;
                        }
                        url = baseUrl + url;
                    }
                    HttpUrl httpUrl = notNull(HttpUrl.parse(url));
                    UrlLazy urlLazy = new UrlLazy(httpUrl::toString);
                    String itag = httpUrl.queryParameter("itag");
                    long expires = parseExpires(url, UNKNOWN_TIME);
                    result.add(new YouTubeStream(urlLazy, expires, FormatUtils.findByItag(itag)));
                }
                return result;
            } else {
                exception = new HttpClientException(response.code(), response.message());
            }
        } catch (Exception e) {
            exception = e;
        } finally {
            closeQuietly(response);
        }
        if (fatal) {
            throw new RuntimeException("Parse dash manifest failed", exception);
        } else {
            exception.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Mark video watched async, ignore error.
     */
    private void markWatched(String vid, Map<String, List<String>> videoInfo) {
        if (isEmpty(videoInfo.get("videostats_playback_base_url"))) {
            return;
        }
        Completable.create(emitter -> {
            String playbackUrl = videoInfo.get("videostats_playback_base_url").get(0);
            HttpUrl httpUrl = HttpUrl.parse(playbackUrl);
            if (httpUrl == null) return;
            HttpUrl.Builder newBuilder = httpUrl.newBuilder();
            String cpnAlphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
            StringBuilder cpn = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 16; i++) {
                cpn.append(cpnAlphabet.charAt(random.nextInt(256) & 63));
            }
            newBuilder.setQueryParameter("ver", "2")
                    .setQueryParameter("cpn", cpn.toString());
            // mark watched
            blockingDownload(newBuilder.build().toString(), false, null);
            emitter.onCompleted();
        }).subscribeOn(Schedulers.io()).subscribe(() -> {
            System.out.println("Mark " + vid + " watched");
        }, Throwable::printStackTrace);
    }

    private static long parseExpires(String url, long fallback) {
        HttpUrl httpUrl = HttpUrl.parse(url);
        try {
            String expires = httpUrl != null ? httpUrl.queryParameter("expire") : null;
            return expires != null ? Long.parseLong(expires) : fallback;
        } catch (Throwable ignore) {
        }
        return fallback;
    }

}
