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

public class VerticalList extends MultipleItemContent {
    public static final String ITEM_TYPE = "vertical_list";

    public VerticalList(List<Content> items) {
        super(ITEM_TYPE, items);
    }

    public static final class TypeAdapter implements JsonDeserializer<VerticalList> {

        @Override
        public VerticalList deserialize(JsonElement json, Type typeOfT,
                                       JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonObject) {
                JsonObject jsonObj = json.getAsJsonObject();
                JsonArray contentsJsonArr = jsonObj.getAsJsonArray("items");
                List<Content> contentList = parse(contentsJsonArr, context);
                return new VerticalList(contentList);
            } else {
                throw new JsonParseException("Invalid VerticalList structure");
            }
        }
    }
}
