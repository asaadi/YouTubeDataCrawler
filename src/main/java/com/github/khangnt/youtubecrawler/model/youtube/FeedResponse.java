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

public class FeedResponse {
    private static final String RESULT_OK = "ok";
    private String result;
    private long timestamp;
    private Feed feed;

    public FeedResponse(String result, long timestamp, Feed feed) {
        this.result = result;
        this.timestamp = timestamp;
        this.feed = feed;
    }

    public boolean isSuccess() {
        return RESULT_OK.equalsIgnoreCase(result);
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Nullable
    public Feed getFeed() {
        return feed;
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
                    Feed feed = context.deserialize(jsonObj.get("content"), Feed.class);
                    return new FeedResponse(result, timestamp, feed);
                } else {
                    return new FeedResponse(result, timestamp, null);
                }
            } else {
                throw new JsonParseException("Invalid feed response");
            }
        }
    }

}
