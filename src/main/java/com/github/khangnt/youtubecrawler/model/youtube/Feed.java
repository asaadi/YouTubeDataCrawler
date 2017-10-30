package com.github.khangnt.youtubecrawler.model.youtube;

import com.github.khangnt.youtubecrawler.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

import okhttp3.HttpUrl;

import static com.github.khangnt.youtubecrawler.internal.Preconditions.notNull;
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
    private String nextUrl;

    public Feed(String feedName, Content content, @Nullable String nextUrl) {
        this.feedName = feedName;
        this.content = content;
        this.nextUrl = nextUrl;
    }

    public String getFeedName() {
        return feedName;
    }

    public Content getContent() {
        return content;
    }

    /**
     * @return Return url to fetch next page or NULL if this is the last page.
     */
    @Nullable
    public String getNextUrl() {
        return nextUrl;
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
                String nextUrl = null;
                if (content instanceof Continuation
                        && ((Continuation) content).getContinuationToken() != null) {
                    Continuation continuation = ((Continuation) content);
                    nextUrl = safeGet(jsonObj, "next_url", (String) null);
                    HttpUrl httpUrl = notNull(HttpUrl.parse(Utils.getYouTubeFullUrl(nextUrl)))
                            .newBuilder()
                            .setQueryParameter("ctoken", continuation.getContinuationToken())
                            .setQueryParameter("itct", continuation.getClickTrackingParams())
                            .build();
                    nextUrl = httpUrl.toString();
                }
                return new Feed(feedName, content, nextUrl);
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
