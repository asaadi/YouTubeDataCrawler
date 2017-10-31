package com.github.khangnt.youtubecrawler.model.youtube;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import static com.github.khangnt.youtubecrawler.model.youtube.TypeAdapterUtils.UNKNOWN_NAME;
import static com.github.khangnt.youtubecrawler.model.youtube.TypeAdapterUtils.parse;
import static com.github.khangnt.youtubecrawler.model.youtube.TypeAdapterUtils.safeGet;

/**
 * Created by Khang NT on 10/27/17.
 * Email: khang.neon.1997@gmail.com
 */

public class Feed {
    private String feedName;
    private Content content;

    public Feed(String feedName, Content content) {
        this.feedName = feedName;
        this.content = content;
    }

    public String getFeedName() {
        return feedName;
    }

    public Content getContent() {
        return content;
    }

    public static final class TypeAdapter implements JsonDeserializer<Feed> {
        private static final String SINGLE_COLUMN_BROWSE_RESULTS = "single_column_browse_results";
        private static final String CONTINUATION_CONTENTS = "continuation_contents";

        @Override
        public Feed deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonObject) {
                JsonObject jsonObj = json.getAsJsonObject();
                String feedName = safeGet(jsonObj, "feed_name", UNKNOWN_NAME);
                Content content;
                if (jsonObj.has(SINGLE_COLUMN_BROWSE_RESULTS)) {
                    JsonObject singleColumnBrowseResults =
                            jsonObj.getAsJsonObject(SINGLE_COLUMN_BROWSE_RESULTS);
                    content = handleTabContent(singleColumnBrowseResults, context);
                } else if (jsonObj.has(CONTINUATION_CONTENTS)) {
                    JsonObject continuationContents = jsonObj.getAsJsonObject(CONTINUATION_CONTENTS);
                    content = handleContinuationContents(continuationContents, context);
                } else {
                    throw new JsonParseException("Unknown feed structure: " + feedName);
                }
                return new Feed(feedName, content);
            } else {
                throw new JsonParseException("Invalid Feed structure");
            }
        }

        private Content handleTabContent(JsonObject singleColumnBrowseResults,
                                         JsonDeserializationContext context) {
            // find selected tab
            JsonArray tabs = singleColumnBrowseResults.getAsJsonArray("tabs");
            for (JsonElement tab : tabs) {
                JsonObject jsonObject = tab.getAsJsonObject();
                if (safeGet(jsonObject, "selected", false)) {
                    // only care about tab's content
                    JsonObject content = jsonObject.getAsJsonObject("content");
                    return parse(content, context);
                }
            }
            throw new JsonParseException("No tab is selected");
        }

        private Content handleContinuationContents(JsonObject continuationContents,
                                                   JsonDeserializationContext context) {
            return parse(continuationContents, context);
        }
    }

}
