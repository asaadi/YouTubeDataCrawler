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

public class CompactPlaylist extends Content {
    static final String ITEM_TYPE = "compact_playlist";

    private String title;
    private String owner;
    private String videoCountText;
    private String thumbnailUrl;
    private String playlistId;
    private String endpoint;

    public CompactPlaylist(String title, String owner, String videoCountText, String thumbnailUrl,
                           String playlistId, String endpoint) {
        super(ITEM_TYPE);
        this.title = title;
        this.owner = owner;
        this.videoCountText = videoCountText;
        this.thumbnailUrl = thumbnailUrl;
        this.playlistId = playlistId;
        this.endpoint = endpoint;
    }

    public String getTitle() {
        return title;
    }

    public String getOwner() {
        return owner;
    }

    public String getVideoCountText() {
        return videoCountText;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public static final class TypeAdapter implements JsonDeserializer<CompactPlaylist> {

        @Override
        public CompactPlaylist deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonObject) {
                JsonObject jsonObj = ((JsonObject) json);
                String title = parseFormattedString(jsonObj.get("title"));
                String owner = parseFormattedString(jsonObj.get("owner"));
                String videoCount = parseFormattedString(jsonObj.get("video_count_short"));
                String thumbnailUrl = jsonObj.getAsJsonObject("thumbnail_info").get("url").getAsString();
                String playlistId = jsonObj.get("playlist_id").getAsString();
                String endpoint = jsonObj.getAsJsonObject("endpoint").get("url").getAsString();
                return new CompactPlaylist(title, owner, videoCount, thumbnailUrl, playlistId, endpoint);
            } else {
                throw new JsonParseException("Invalid CompactPlaylist");
            }
        }
    }

}
