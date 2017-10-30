package com.github.khangnt.youtubecrawler.model.youtube;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

import static com.github.khangnt.youtubecrawler.model.youtube.TypeAdapterUtils.parseFormattedString;

/**
 * Created by Khang NT on 10/27/17.
 * Email: khang.neon.1997@gmail.com
 */

public class CompactChannel extends Content {
    public static final String ITEM_TYPE = "compact_channel";

    private String title;
    private String thumbnailUrl;
    private String videoCountText;
    private String subscriberCount;
    private String endpoint;

    public CompactChannel(String title, String thumbnailUrl, String videoCountText,
                          String subscriberCount, String endpoint) {
        super(ITEM_TYPE);
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.videoCountText = videoCountText;
        this.subscriberCount = subscriberCount;
        this.endpoint = endpoint;
    }

    public String getTitle() {
        return title;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getVideoCountText() {
        return videoCountText;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    /**
     * @return Return subscriber count as String or NULL if the channel hide it.
     */
    @Nullable
    public String getSubscriberCount() {
        return subscriberCount;
    }

    public static final class TypeAdapter implements JsonDeserializer<CompactChannel> {

        @Override
        public CompactChannel deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonObject) {
                JsonObject jsonObj = ((JsonObject) json);
                String title = parseFormattedString(jsonObj.get("title"));
                String thumbnail = jsonObj.getAsJsonObject("thumbnail_info").get("url").getAsString();
                String videoCount = parseFormattedString(jsonObj.get("video_count"));
                String subscriberCount = parseFormattedString(jsonObj.get("subscriber_count"));
                String endpoint = jsonObj.getAsJsonObject("endpoint").get("url").getAsString();
                return new CompactChannel(title, thumbnail, videoCount, subscriberCount, endpoint);
            } else {
                throw new JsonParseException("Invalid CompactChannel");
            }
        }
    }

}
