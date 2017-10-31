package com.github.khangnt.youtubecrawler.model.youtube;

import com.github.khangnt.youtubecrawler.internal.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

import static com.github.khangnt.youtubecrawler.model.youtube.TypeAdapterUtils.parse;
import static com.github.khangnt.youtubecrawler.model.youtube.TypeAdapterUtils.safeGet;

/**
 * Created by Khang NT on 10/31/17.
 * Email: khang.neon.1997@gmail.com
 */

public class Channel {

    private @Nullable Header header;
    private Content content;

    public Channel(@Nullable Header header, @NotNull Content content) {
        this.header = header;
        this.content = Preconditions.notNull(content);
    }

    /**
     * Only available for first load, any continuation load will return NULL.
     */
    @Nullable
    public Header getHeader() {
        return header;
    }

    public Content getContent() {
        return content;
    }

    public static final class TypeAdapter implements JsonDeserializer<Channel> {

        @Override
        public Channel deserialize(JsonElement json, Type typeOfT,
                                   JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonObject) {
                JsonObject jsonObj = ((JsonObject) json);
                Header header = null;
                Content content = null;
                if (jsonObj.has("header")) {
                    header = new Header(jsonObj.get("header"));
                }
                JsonObject tabSettings = jsonObj.getAsJsonObject("tab_settings");
                if (tabSettings != null) {
                    JsonArray availableTabs = tabSettings.getAsJsonArray("available_tabs");
                    for (JsonElement tabElement : availableTabs) {
                        JsonObject tabObj = tabElement.getAsJsonObject();
                        // only handle selected tab
                        if (safeGet(tabObj, "selected", false)) {
                            content = parse(tabObj.getAsJsonObject("content"), context);
                        }
                    }
                } else {
                    JsonObject continuationContent = jsonObj.getAsJsonObject("continuation_contents");
                    content = parse(continuationContent, context);
                }
                if (content == null) {
                    throw new JsonParseException("Channel content == null");
                }
                return new Channel(header, content);
            } else {
                throw new JsonParseException("Invalid channel");
            }
        }
    }

    public static final class Header {
        private String title;
        private String avatarUrl;
        private String channelEndpoint;
        private String bannerUrl;
        private String hdBannerUrl;

        Header(JsonElement jsonElement) {
            JsonObject jsonObj = jsonElement.getAsJsonObject();
            title = jsonObj.get("title").getAsString();
            avatarUrl = jsonObj.getAsJsonObject("avatar").get("url").getAsString();
            channelEndpoint = jsonObj.get("channel_url").getAsString();
            bannerUrl = jsonObj.getAsJsonObject("banner_image").get("url").getAsString();
            hdBannerUrl = jsonObj.getAsJsonObject("banner_image_hd").get("url").getAsString();
        }

        public String getTitle() {
            return title;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public String getChannelEndpoint() {
            return channelEndpoint;
        }

        public String getBannerUrl() {
            return bannerUrl;
        }

        public String getHdBannerUrl() {
            return hdBannerUrl;
        }
    }
}
