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
import static com.github.khangnt.youtubecrawler.model.youtube.TypeAdapterUtils.safeGet;

/**
 * Created by Khang NT on 10/30/17.
 * Email: khang.neon.1997@gmail.com
 */

public class ArtistWatchCard extends MultipleItemContent {
    static final String ITEM_TYPE = "artist_watch_card";

    private String title;
    private String subtitle;
    private String thumbnailUrl;
    private String callToAction;
    private String callToActionEndpoint;
    private String relatedDataTitle;
    private List<Artist> relatedData;
    private String channelEndpoint;

    public ArtistWatchCard(String title, String subtitle, String thumbnailUrl, String callToAction,
                           String callToActionEndpoint, String relatedDataTitle,
                           List<Artist> relatedData, String channelEndpoint, List<Content> lists) {
        super(ITEM_TYPE, lists);
        this.title = title;
        this.subtitle = subtitle;
        this.thumbnailUrl = thumbnailUrl;
        this.callToAction = callToAction;
        this.callToActionEndpoint = callToActionEndpoint;
        this.relatedDataTitle = relatedDataTitle;
        this.relatedData = relatedData;
        this.channelEndpoint = channelEndpoint;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getCallToAction() {
        return callToAction;
    }

    public String getCallToActionEndpoint() {
        return callToActionEndpoint;
    }

    public String getRelatedDataTitle() {
        return relatedDataTitle;
    }

    public List<Artist> getRelatedData() {
        return relatedData;
    }

    public String getChannelEndpoint() {
        return channelEndpoint;
    }

    public static final class TypeAdapter implements JsonDeserializer<ArtistWatchCard> {

        @Override
        public ArtistWatchCard deserialize(JsonElement json, Type typeOfT,
                                           JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonObject) {
                JsonObject jsonObj = json.getAsJsonObject();
                String title = parseFormattedString(jsonObj.get("title"));
                String subtitle = parseFormattedString(jsonObj.get("collapsed_label"));

                JsonObject ctaObj = jsonObj.getAsJsonObject("call_to_action");
                String ctaLabel = parseFormattedString(ctaObj.get("label"));
                String ctaEndpoint = ctaObj.getAsJsonObject("navigation_endpoint")
                        .get("url").getAsString();
                String thumbnail = safeGet(ctaObj.getAsJsonObject("left_thumbnail"),
                        "url", (String) null);

                JsonObject relatedDataObj = jsonObj.getAsJsonObject("related_data");
                String relatedDataTitle = parseFormattedString(relatedDataObj.get("title"));
                List<Artist> relatedData = parse(relatedDataObj.getAsJsonArray("entities"), Artist::new);

                List<Content> lists = parse(jsonObj.getAsJsonArray("lists"), context);
                String channelEndpoint = jsonObj.getAsJsonObject("navigation_endpoint")
                        .get("url").getAsString();

                return new ArtistWatchCard(title, subtitle, thumbnail, ctaLabel, ctaEndpoint,
                        relatedDataTitle, relatedData, channelEndpoint, lists);
            } else {
                throw new JsonParseException("Invalid ArtistWatchCard");
            }
        }
    }

    public static class Artist {
        private String title;
        private String thumbnailUrl;
        private String endpoint;

        public Artist(JsonElement jsonElement) {
            JsonObject jsonObj = ((JsonObject) jsonElement);
            this.title = parseFormattedString(jsonObj.get("title"));
            this.thumbnailUrl = jsonObj.getAsJsonObject("thumbnail").get("url").getAsString();
            this.endpoint = jsonObj.getAsJsonObject("navigation_endpoint").get("url").getAsString();
        }

        public String getTitle() {
            return title;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public String getEndpoint() {
            return endpoint;
        }
    }

}
