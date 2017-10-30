package com.github.khangnt.youtubecrawler.model.youtube;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import static com.github.khangnt.youtubecrawler.model.youtube.TypeAdapterUtils.parseFormattedString;

/**
 * Created by Khang NT on 10/30/17.
 * Email: khang.neon.1997@gmail.com
 */

public class VideoWithContext extends Content {
    static final String ITEM_TYPE = "video_with_context";

    private String title;
    private String bylineText;
    private String viewCountText;
    private String lengthText;
    private String publishedTimeText;
    private String channelThumbnailUrl;
    private String thumbnailUrl;
    private String videoId;
    private String endpoint;
    private String channelEndpoint;

    public VideoWithContext(String title, String bylineText, String viewCountText, String lengthText,
                            String publishedTimeText, String channelThumbnailUrl, String thumbnailUrl,
                            String videoId, String endpoint, String channelEndpoint) {
        super(ITEM_TYPE);
        this.title = title;
        this.bylineText = bylineText;
        this.viewCountText = viewCountText;
        this.lengthText = lengthText;
        this.publishedTimeText = publishedTimeText;
        this.channelThumbnailUrl = channelThumbnailUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.videoId = videoId;
        this.endpoint = endpoint;
        this.channelEndpoint = channelEndpoint;
    }

    public String getTitle() {
        return title;
    }

    public String getBylineText() {
        return bylineText;
    }

    public String getViewCountText() {
        return viewCountText;
    }

    public String getPublishedTimeText() {
        return publishedTimeText;
    }

    public String getChannelThumbnailUrl() {
        return channelThumbnailUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getLengthText() {
        return lengthText;
    }

    public String getChannelEndpoint() {
        return channelEndpoint;
    }

    public static final class TypeAdapter implements JsonDeserializer<VideoWithContext> {

        @Override
        public VideoWithContext deserialize(JsonElement json, Type typeOfT,
                                            JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonObject) {
                JsonObject jsonObj = ((JsonObject) json);
                String title = parseFormattedString(jsonObj.get("headline"));
                String bylineText = parseFormattedString(jsonObj.get("short_byline_text"));
                String viewCount = parseFormattedString(jsonObj.get("short_view_count_text"));
                String length = parseFormattedString(jsonObj.get("length_text"));
                String publishedTime = parseFormattedString(jsonObj.get("published_time_text"));
                String videoId = jsonObj.get("video_id").getAsString();
                String thumbnailUrl = jsonObj.getAsJsonObject("thumbnail_info")
                        .get("url").getAsString();
                String endpoint = jsonObj.getAsJsonObject("navigation_endpoint")
                        .get("url").getAsString();
                JsonObject channel = jsonObj.getAsJsonObject("channel_thumbnail");
                String channelThumbnailUrl = channel.getAsJsonObject("thumbnail_info")
                        .get("url").getAsString();
                String channelEndpoint = channel.getAsJsonObject("navigation_endpoint")
                        .get("url").getAsString();
                return new VideoWithContext(title, bylineText, viewCount, length, publishedTime,
                        channelThumbnailUrl, thumbnailUrl, videoId, endpoint, channelEndpoint);
            } else {
                throw new JsonParseException("Invalid VideoWithContext");
            }
        }
    }

}
