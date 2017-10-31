package com.github.khangnt.youtubecrawler.model.youtube;

import com.github.khangnt.youtubecrawler.C;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

/**
 * Created by Khang NT on 10/31/17.
 * Email: khang.neon.1997@gmail.com
 */

public class SearchResult {
    private Content content;
    private String searchType;
    private @Nullable String nextUrl;

    public SearchResult(Content content, String searchType, @Nullable String nextUrl) {
        this.content = content;
        this.searchType = searchType;
        this.nextUrl = nextUrl;
    }

    public Content getContent() {
        return content;
    }

    @Nullable
    public String getSearchType() {
        return searchType;
    }

    public @Nullable String getNextUrl() {
        return nextUrl;
    }

    public static final class TypeAdapter implements JsonDeserializer<SearchResult> {
        private static final String CONTINUATION_CONTENTS = "continuation_contents";
        private static final String SEARCH_RESULTS = "search_results";

        @Override
        public SearchResult deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonObject) {
                JsonObject jsonObj = json.getAsJsonObject();
                String searchType = jsonObj.get("search_type").getAsString();
                Content content = null;
                if (jsonObj.has(SEARCH_RESULTS)) {
                    content = TypeAdapterUtils.parse(jsonObj.getAsJsonObject(SEARCH_RESULTS), context);
                } else if (jsonObj.has(CONTINUATION_CONTENTS)) {
                    content = TypeAdapterUtils.parse(jsonObj.getAsJsonObject(CONTINUATION_CONTENTS), context);
                }

                String nextUrl = null;
                if (content instanceof Continuation
                        && ((Continuation) content).getContinuationToken() != null) {
                    Continuation continuation = ((Continuation) content);
                    nextUrl = "https://m.youtube.com/results?ajax=1&action_continuation=1&layout=tablet" +
                            "&ctoken=" + continuation.getContinuationToken() +
                            "&itct=" + continuation.getClickTrackingParams() +
                            "&utcoffset=" + C.UTC_OFFSET;
                }
                return new SearchResult(content, searchType, nextUrl);
            } else {
                throw new JsonParseException("Invalid SearchResult structure");
            }
        }

    }
}
