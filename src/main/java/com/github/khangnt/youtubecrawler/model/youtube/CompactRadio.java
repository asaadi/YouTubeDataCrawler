package com.github.khangnt.youtubecrawler.model.youtube;

/**
 * Created by Khang NT on 10/27/17.
 * Email: khang.neon.1997@gmail.com
 */

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import static com.github.khangnt.youtubecrawler.model.youtube.TypeAdapterUtils.parseFormattedString;

/**
 * YouTube mixes - Endless playlist
 */
public class CompactRadio extends Content {
    public static final String ITEM_TYPE = "compact_radio";

    private String title;
    private String videoCountShortText;
    private String thumbnailUrl;
    private String playlistId;
    private String endpoint;

    public CompactRadio(String title, String videoCountShortText, String thumbnailUrl,
                        String playlistId, String endpoint) {
        super(ITEM_TYPE);
        this.title = title;
        this.videoCountShortText = videoCountShortText;
        this.thumbnailUrl = thumbnailUrl;
        this.playlistId = playlistId;
        this.endpoint = endpoint;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public String getTitle() {
        return title;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getVideoCountShortText() {
        return videoCountShortText;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public static final class TypeAdapter implements JsonDeserializer<CompactRadio> {

        @Override
        public CompactRadio deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonObject) {
                JsonObject jsonObj = ((JsonObject) json);
                String title = parseFormattedString(jsonObj.getAsJsonObject("title"));
                String videoCountShortText = parseFormattedString(jsonObj.getAsJsonObject("video_count_short_text"));
                String thumbnail = jsonObj.getAsJsonObject("thumbnail_info").get("url").getAsString();
                String playlistId = jsonObj.get("playlist_id").getAsString();
                String endpoint = jsonObj.getAsJsonObject("navigation_endpoint").get("url").getAsString();
                return new CompactRadio(title, videoCountShortText, thumbnail, playlistId, endpoint);
            } else {
                throw new JsonParseException("Invalid CompactRadio");
            }
        }
    }

}
