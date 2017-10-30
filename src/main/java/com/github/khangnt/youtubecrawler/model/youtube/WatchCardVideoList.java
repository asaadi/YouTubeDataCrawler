package com.github.khangnt.youtubecrawler.model.youtube;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

import static com.github.khangnt.youtubecrawler.model.youtube.TypeAdapterUtils.parse;
import static com.github.khangnt.youtubecrawler.model.youtube.TypeAdapterUtils.parseFormattedString;

/**
 * Created by Khang NT on 10/30/17.
 * Email: khang.neon.1997@gmail.com
 */

public class WatchCardVideoList extends Content {
    static final String ITEM_TYPE = "watch_card_video_list";

    private String title;
    private String endpoint;
    private List<Video> videoList;

    public WatchCardVideoList(String title, String endpoint, List<Video> videoList) {
        super(ITEM_TYPE);
        this.title = title;
        this.endpoint = endpoint;
        this.videoList = videoList;
    }

    public String getTitle() {
        return title;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public List<Video> getVideoList() {
        return videoList;
    }

    public static final class TypeAdapter implements JsonDeserializer<WatchCardVideoList> {

        @Override
        public WatchCardVideoList deserialize(JsonElement json, Type typeOfT,
                                              JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonObject) {
                JsonObject jsonObj = ((JsonObject) json);
                String title = parseFormattedString(jsonObj.get("title"));
                String endpoint = jsonObj.getAsJsonObject("view_all_endpoint").get("url").getAsString();
                List<Video> videoList = parse(jsonObj.getAsJsonArray("videos"), Video::new);
                return new WatchCardVideoList(title, endpoint, videoList);
            } else {
                throw new JsonParseException("Invalid WatchCardVideoList");
            }
        }
    }

    public static class Video {
        private String title;
        private String durationText;
        private String thumbnailUrl;
        private String endpoint;

        public Video(JsonElement jsonElement) {
            JsonObject jsonObj = jsonElement.getAsJsonObject();
            title = parseFormattedString(jsonObj.get("title"));
            durationText = parseFormattedString(jsonObj.get("duration"));
            thumbnailUrl = jsonObj.getAsJsonObject("thumbnail").get("url").getAsString();
            endpoint = jsonObj.getAsJsonObject("navigation_endpoint").get("url").getAsString();
        }

        public String getTitle() {
            return title;
        }

        public String getDurationText() {
            return durationText;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public String getEndpoint() {
            return endpoint;
        }
    }

}
