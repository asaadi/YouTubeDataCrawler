package com.github.khangnt.youtubecrawler.model.youtube;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.List;

import static com.github.khangnt.youtubecrawler.model.youtube.TypeAdapterUtils.KEY_ITEM_TYPE;
import static com.github.khangnt.youtubecrawler.model.youtube.TypeAdapterUtils.parse;
import static com.github.khangnt.youtubecrawler.model.youtube.TypeAdapterUtils.safeGet;

/**
 * Created by Khang NT on 10/27/17.
 * Email: khang.neon.1997@gmail.com
 */

public class SectionList extends MultipleItemContent implements Continuation {
    static final String ITEM_TYPE = "section_list";

    private String continuationToken;
    private String clickTrackingParams;

    public SectionList(List<Content> items, String continuationToken, String clickTrackingParams) {
        super(ITEM_TYPE, items);
        this.continuationToken = continuationToken;
        this.clickTrackingParams = clickTrackingParams;
    }

    @Nullable
    @Override
    public String getContinuationToken() {
        return continuationToken;
    }

    @Nullable
    @Override
    public String getClickTrackingParams() {
        return clickTrackingParams;
    }

    public static final class TypeAdapter implements JsonDeserializer<SectionList> {
        private static final String NEXT_CONTINUATION_DATA_TYPE = "next_continuation_data";

        @Override
        public SectionList deserialize(JsonElement json, Type typeOfT,
                                       JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonObject) {
                JsonObject jsonObj = json.getAsJsonObject();
                JsonArray contentsJsonArr = jsonObj.getAsJsonArray("contents");
                List<Content> contents = parse(contentsJsonArr, context);
                String continuationToken = null;
                String clickTrackingParams = null;
                JsonArray continuations = jsonObj.getAsJsonArray("continuations");
                if (continuations != null) {
                    for (JsonElement jsonElement : continuations) {
                        JsonObject continuationObj = jsonElement.getAsJsonObject();
                        String itemType = safeGet(continuationObj, KEY_ITEM_TYPE, "");
                        if (NEXT_CONTINUATION_DATA_TYPE.equalsIgnoreCase(itemType)) {
                            continuationToken = continuationObj.get("continuation").getAsString();
                            clickTrackingParams = continuationObj.get("click_tracking_params")
                                    .getAsString();
                            break;
                        }
                    }
                }
                return new SectionList(contents, continuationToken, clickTrackingParams);
            } else {
                throw new JsonParseException("Invalid SectionList structure");
            }
        }
    }

}
