package com.github.khangnt.youtubecrawler.model.youtube;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

import static com.github.khangnt.youtubecrawler.model.youtube.TypeAdapterUtils.parseFormattedString;
import static com.github.khangnt.youtubecrawler.model.youtube.TypeAdapterUtils.safeGet;

/**
 * Created by Khang NT on 10/27/17.
 * Email: khang.neon.1997@gmail.com
 */

public class Shelf extends NestedContent {
    public static final String ITEM_TYPE = "shelf";

    private String thumbnailUrl;
    private String title;
    private String subtitle;
    private String titleAnnotation;
    private String endpoint;

    public Shelf(Content subContent, String thumbnailUrl, String title,
                 String subtitle, String titleAnnotation, String endpoint) {
        super(ITEM_TYPE, subContent);
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.endpoint = endpoint;
        this.titleAnnotation = titleAnnotation;
        this.subtitle = subtitle;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    @Nullable
    public String getEndpoint() {
        return endpoint;
    }

    public String getTitleAnnotation() {
        return titleAnnotation;
    }

    @Nullable
    public String getSubtitle() {
        return subtitle;
    }

    public static final class TypeAdapter implements JsonDeserializer<Shelf> {

        @Override
        public Shelf deserialize(JsonElement json, Type typeOfT,
                                 JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonObject) {
                JsonObject jsonObj = ((JsonObject) json);
                String title = parseFormattedString(jsonObj.getAsJsonObject("title"));
                String subtitle = parseFormattedString(jsonObj.getAsJsonObject("subtitle"));
                String titleAnnotation = parseFormattedString(jsonObj.getAsJsonObject("title_annotation"));
                String endpoint = safeGet(jsonObj.getAsJsonObject("endpoint"), "url", (String) null);
                String thumbnail = safeGet(jsonObj.getAsJsonObject("thumbnail"), "url", (String) null);
                Content subContent = TypeAdapterUtils.parse(jsonObj.getAsJsonObject("content"), context);
                return new Shelf(subContent, thumbnail, title, subtitle, titleAnnotation, endpoint);
            } else {
                throw new JsonParseException("Invalid Shelf");
            }
        }
    }

}
