package com.github.khangnt.youtubecrawler.model.youtube;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.functions.Func1;

/**
 * Created by Khang NT on 10/27/17.
 * Email: khang.neon.1997@gmail.com
 */

class TypeAdapterUtils {
    static final String UNKNOWN_NAME = "Unknown";
    static final String KEY_ITEM_TYPE = "item_type";
    static final String NEXT_CONTINUATION_DATA_TYPE = "next_continuation_data";

    private static final Map<String, Class<? extends Content>> ITEM_TYPE_MAP;
    private static final Set<String> BLACK_LIST_ITEM_TYPE;

    private static final String TYPE_FORMATTED_STRING = "formatted_string";

    static {
        ITEM_TYPE_MAP = new HashMap<>();
        ITEM_TYPE_MAP.put(ArtistWatchCard.ITEM_TYPE, ArtistWatchCard.class);
        ITEM_TYPE_MAP.put(CompactChannel.ITEM_TYPE, CompactChannel.class);
        ITEM_TYPE_MAP.put(CompactPlaylist.ITEM_TYPE, CompactPlaylist.class);
        ITEM_TYPE_MAP.put(CompactRadio.ITEM_TYPE, CompactRadio.class);
        ITEM_TYPE_MAP.put(CompactVideo.ITEM_TYPE, CompactVideo.class);
        ITEM_TYPE_MAP.put(ItemSection.ITEM_TYPE, ItemSection.class);
        ITEM_TYPE_MAP.put(SectionList.ITEM_TYPE, SectionList.class);
        ITEM_TYPE_MAP.put(Shelf.ITEM_TYPE, Shelf.class);
        ITEM_TYPE_MAP.put(VerticalList.ITEM_TYPE, VerticalList.class);
        ITEM_TYPE_MAP.put(VideoWithContext.ITEM_TYPE, VideoWithContext.class);
        ITEM_TYPE_MAP.put(WatchCardAlbumList.ITEM_TYPE, WatchCardAlbumList.class);
        ITEM_TYPE_MAP.put(WatchCardVideoList.ITEM_TYPE, WatchCardVideoList.class);

        BLACK_LIST_ITEM_TYPE = new HashSet<>();
        BLACK_LIST_ITEM_TYPE.add("promoted_video");
    }

    @Nullable
    static Content parse(JsonObject jsonObj,
                         JsonDeserializationContext context) throws JsonParseException {
        String itemType = safeGet(jsonObj, KEY_ITEM_TYPE, "");
        if (BLACK_LIST_ITEM_TYPE.contains(itemType)) {
            return null;
        }
        Class<? extends Content> contentClass = ITEM_TYPE_MAP.get(itemType);
        if (contentClass == null) {
            throw new JsonParseException("Unknown item_type: " + itemType);
        }
        return context.deserialize(jsonObj, contentClass);
    }

    static List<Content> parse(JsonArray contentsJsonArr,
                               JsonDeserializationContext context) throws JsonParseException {
        List<Content> contents = new ArrayList<>();
        for (JsonElement element : contentsJsonArr) {
            Content sectionItem = parse(element.getAsJsonObject(), context);
            if (sectionItem != null) {
                contents.add(sectionItem);
            }
        }
        return contents;
    }

    static <T> List<T> parse(JsonArray elements, Func1<JsonElement, T> creator) {
        List<T> result = new ArrayList<>();
        for (JsonElement element : elements) {
            result.add(creator.call(element));
        }
        return result;
    }

    static String parseFormattedString(JsonElement content) {
        if (content instanceof JsonObject) {
            String itemType = safeGet(((JsonObject) content), KEY_ITEM_TYPE, "");
            if (itemType.equalsIgnoreCase(TYPE_FORMATTED_STRING)) {
                JsonArray runs = ((JsonObject) content).getAsJsonArray("runs");
                if (runs.size() > 0) {
                    return safeGet(runs.get(0).getAsJsonObject(), "text", (String) null);
                }
            }
        }
        return null;
    }

    static String safeGet(JsonObject jsonObject, String key, String defaultValue) {
        if (jsonObject == null) return defaultValue;
        JsonElement jsonElement = jsonObject.get(key);
        if (!(jsonElement instanceof JsonPrimitive)
                || !((JsonPrimitive) jsonElement).isString()) {
            return defaultValue;
        } else {
            return jsonElement.getAsString();
        }
    }

    static Number safeGet(JsonObject jsonObject, String key, Number defaultValue) {
        if (jsonObject == null) return defaultValue;
        JsonElement jsonElement = jsonObject.get(key);
        if (!(jsonElement instanceof JsonPrimitive)
                || !((JsonPrimitive) jsonElement).isNumber()) {
            return defaultValue;
        } else {
            return jsonElement.getAsNumber();
        }
    }

    static boolean safeGet(JsonObject jsonObject, String key, boolean defaultValue) {
        if (jsonObject == null) return defaultValue;
        JsonElement jsonElement = jsonObject.get(key);
        if (!(jsonElement instanceof JsonPrimitive)
                || !((JsonPrimitive) jsonElement).isBoolean()) {
            return defaultValue;
        } else {
            return jsonElement.getAsBoolean();
        }
    }

}
