package com.github.khangnt.youtubecrawler.model.youtube;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

import static com.github.khangnt.youtubecrawler.model.youtube.TypeAdapterUtils.safeGet;

/**
 * Created by Khang NT on 10/31/17.
 * Email: khang.neon.1997@gmail.com
 */

public class SearchResult {
    private Content content;
    private String searchType;

    public SearchResult(Content content, String searchType) {
        this.content = content;
        this.searchType = searchType;
    }

    public Content getContent() {
        return content;
    }

    @Nullable
    public String getSearchType() {
        return searchType;
    }

    public static final class TypeAdapter implements JsonDeserializer<SearchResult> {
        private static final String CONTINUATION_CONTENTS = "continuation_contents";
        private static final String SEARCH_RESULTS = "search_results";

        @Override
        public SearchResult deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonObject) {
                JsonObject jsonObj = json.getAsJsonObject();
                String searchType = safeGet(jsonObj, "search_type", (String) null);
                Content content = null;
                if (jsonObj.has(SEARCH_RESULTS)) {
                    content = TypeAdapterUtils.parse(jsonObj.getAsJsonObject(SEARCH_RESULTS), context);
                } else if (jsonObj.has(CONTINUATION_CONTENTS)) {
                    content = TypeAdapterUtils.parse(jsonObj.getAsJsonObject(CONTINUATION_CONTENTS), context);
                }
                return new SearchResult(content, searchType);
            } else {
                throw new JsonParseException("Invalid SearchResult structure");
            }
        }

    }
}
