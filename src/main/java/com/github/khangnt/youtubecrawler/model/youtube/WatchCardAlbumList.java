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

public class WatchCardAlbumList extends Content {
    static final String ITEM_TYPE = "watch_card_album_list";

    private String title;
    private List<Album> albumList;

    public WatchCardAlbumList(String title, List<Album> albumList) {
        super(ITEM_TYPE);
        this.title = title;
        this.albumList = albumList;
    }

    public String getTitle() {
        return title;
    }

    public List<Album> getAlbumList() {
        return albumList;
    }

    public static final class TypeAdapter implements JsonDeserializer<WatchCardAlbumList> {

        @Override
        public WatchCardAlbumList deserialize(JsonElement json, Type typeOfT,
                                              JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonObject) {
                JsonObject jsonObj = ((JsonObject) json);
                String title = parseFormattedString(jsonObj.get("title"));
                List<Album> albumList = parse(jsonObj.getAsJsonArray("albums"), Album::new);
                return new WatchCardAlbumList(title, albumList);
            } else {
                throw new JsonParseException("Invalid WatchCardAlbumList");
            }
        }
    }

    public static class Album {
        private String title;
        private String year;
        private String thumbnailUrl;
        private String endpoint;

        public Album(JsonElement jsonElement) {
            JsonObject jsonObj = jsonElement.getAsJsonObject();
            title = parseFormattedString(jsonObj.get("title"));
            year = parseFormattedString(jsonObj.get("year"));
            thumbnailUrl = jsonObj.getAsJsonObject("thumbnail").get("url").getAsString();
            endpoint = jsonObj.getAsJsonObject("navigation_endpoint").get("url").getAsString();
        }

        public String getTitle() {
            return title;
        }

        public String getYear() {
            return year;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public String getEndpoint() {
            return endpoint;
        }
    }

}
