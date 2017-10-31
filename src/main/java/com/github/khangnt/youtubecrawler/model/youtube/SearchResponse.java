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

public class SearchResponse extends AbstractResponse {
    private SearchResult searchResult;

    public SearchResponse(String result, long timestamp, SearchResult searchResult) {
        super(result, timestamp);
        this.searchResult = searchResult;
    }

    @Nullable
    public SearchResult getSearchResult() {
        return searchResult;
    }

    @Override
    public boolean hasContinuation() {
        return searchResult != null && searchResult.getContent() instanceof Continuation
                && ((Continuation) searchResult.getContent()).getContinuationToken() != null;
    }

    @Override
    public Continuation getContinuation() {
        return hasContinuation() ? ((Continuation) searchResult.getContent()) : null;
    }

    public static final class TypeAdapter implements JsonDeserializer<SearchResponse> {

        @Override
        public SearchResponse deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonObject) {
                JsonObject jsonObj = json.getAsJsonObject();
                String result = safeGet(jsonObj, "result", "failed");
                long timestamp = safeGet(jsonObj, "timestamp", 0).longValue();
                if (RESULT_OK.equalsIgnoreCase(result)) {
                    JsonElement content = jsonObj.get("content");
                    if (content instanceof JsonObject && ((JsonObject) content).size() > 0) {
                        SearchResult searchResult = context.deserialize(content, SearchResult.class);
                        return new SearchResponse(result, timestamp, searchResult);
                    }
                }
                return new SearchResponse(result, timestamp, null);
            } else {
                throw new JsonParseException("Invalid SearchResponse");
            }
        }
    }

}
