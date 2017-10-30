package com.github.khangnt.youtubecrawler.model.youtube;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

import static com.github.khangnt.youtubecrawler.model.youtube.TypeAdapterUtils.parse;

/**
 * Created by Khang NT on 10/27/17.
 * Email: khang.neon.1997@gmail.com
 */

public class ItemSection extends MultipleItemContent {
    public static final String ITEM_TYPE = "item_section";

    public ItemSection(List<Content> contents) {
        super(ITEM_TYPE, contents);
    }

    public static final class TypeAdapter implements JsonDeserializer<ItemSection> {

        @Override
        public ItemSection deserialize(JsonElement json, Type typeOfT,
                                       JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonObject) {
                JsonObject jsonObj = json.getAsJsonObject();
                JsonArray contentsJsonArr = jsonObj.getAsJsonArray("contents");
                List<Content> contentList = parse(contentsJsonArr, context);
                return new ItemSection(contentList);
            } else {
                throw new JsonParseException("Invalid ItemSection structure");
            }
        }
    }
}
