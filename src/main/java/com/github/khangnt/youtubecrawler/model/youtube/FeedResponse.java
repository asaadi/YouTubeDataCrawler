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
 * Created by Khang NT on 10/27/17.
 * Email: khang.neon.1997@gmail.com
 */

public class FeedResponse extends AbstractResponse {
    private Feed feed;

    public FeedResponse(String result, long timestamp, Feed feed) {
        super(result, timestamp);
        this.feed = feed;
    }

    @Nullable
    public Feed getFeed() {
        return feed;
    }

    @Override
    public @Nullable String getNextUrl() {
        return feed != null ? feed.getNextUrl() : null;
    }

    public static final class TypeAdapter implements JsonDeserializer<FeedResponse> {

        @Override
        public FeedResponse deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonObject) {
                JsonObject jsonObj = json.getAsJsonObject();
                String result = safeGet(jsonObj, "result", "failed");
                long timestamp = safeGet(jsonObj, "timestamp", 0).longValue();
                if (RESULT_OK.equalsIgnoreCase(result)) {
                    JsonElement content = jsonObj.get("content");
                    if (content instanceof JsonObject && ((JsonObject) content).size() > 0) {
                        Feed feed = context.deserialize(content, Feed.class);
                        return new FeedResponse(result, timestamp, feed);
                    }
                }
                return new FeedResponse(result, timestamp, null);
            } else {
                throw new JsonParseException("Invalid feed response");
            }
        }
    }

}
