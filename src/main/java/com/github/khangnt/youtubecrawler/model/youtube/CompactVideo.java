package com.github.khangnt.youtubecrawler.model.youtube;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

import static com.github.khangnt.youtubecrawler.model.youtube.TypeAdapterUtils.KEY_ITEM_TYPE;
import static com.github.khangnt.youtubecrawler.model.youtube.TypeAdapterUtils.parseFormattedString;
import static com.github.khangnt.youtubecrawler.model.youtube.TypeAdapterUtils.safeGet;

/**
 * Created by Khang NT on 10/27/17.
 * Email: khang.neon.1997@gmail.com
 */

public class CompactVideo extends Content {
    public static final String ITEM_TYPE = "compact_video";

    private String title;
    private String channelTitle;
    private String lengthText;
    private String viewCountText;
    private String thumbnailUrl;
    private String liveBadge;
    private String videoId;
    private String endpoint;

    public CompactVideo(String title, String channelTitle, String lengthText, String viewCountText,
                        String thumbnailUrl, String liveBadge, String videoId, String endpoint) {
        super(ITEM_TYPE);
        this.title = title;
        this.channelTitle = channelTitle;
        this.lengthText = lengthText;
        this.viewCountText = viewCountText;
        this.thumbnailUrl = thumbnailUrl;
        this.liveBadge = liveBadge;
        this.videoId = videoId;
        this.endpoint = endpoint;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    /**
     * Get formatted duration or NULL if this is LIVE video.
     * @return formatted duration or null.
     */
    @Nullable
    public String getLengthText() {
        return lengthText;
    }

    public String getViewCountText() {
        return viewCountText;
    }

    public String getTitle() {
        return title;
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

    @Nullable
    public String getLiveBadge() {
        return liveBadge;
    }

    public static final class TypeAdapter implements JsonDeserializer<CompactVideo> {
        private static final String LIVE_BADGE_TYPE = "live_badge";
        @Override
        public CompactVideo deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonObject) {
                JsonObject jsonObj = ((JsonObject) json);
                String title = parseFormattedString(jsonObj.getAsJsonObject("title"));
                String viewCount = parseFormattedString(jsonObj.getAsJsonObject("view_count"));
                String byline = parseFormattedString(jsonObj.getAsJsonObject("short_byline"));
                String length = parseFormattedString(jsonObj.getAsJsonObject("length"));
                String thumbnail = jsonObj.getAsJsonObject("thumbnail_info").get("url").getAsString();
                String videoId = jsonObj.get("encrypted_id").getAsString();
                String endpoint = jsonObj.getAsJsonObject("endpoint").get("url").getAsString();

                String liveBadge = null;
                JsonArray badges = jsonObj.getAsJsonArray("badges");
                if (badges != null && badges.size() > 0) {
                    for (JsonElement element : badges) {
                        JsonObject badgeJsonObj = element.getAsJsonObject();
                        String itemType = safeGet(badgeJsonObj, KEY_ITEM_TYPE, "");
                        if (LIVE_BADGE_TYPE.equalsIgnoreCase(itemType)) {
                            liveBadge = parseFormattedString(badgeJsonObj.getAsJsonObject("label"));
                        }
                    }
                }
                return new CompactVideo(title, byline, length, viewCount, thumbnail, liveBadge,
                        videoId, endpoint);
            } else {
                throw new JsonParseException("Invalid CompactVideo");
            }
        }
    }

}
