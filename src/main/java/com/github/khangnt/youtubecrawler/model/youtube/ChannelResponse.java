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

public class ChannelResponse extends AbstractResponse {
    private Channel channel;

    public ChannelResponse(String result, long timestamp, Channel channel) {
        super(result, timestamp);
        this.channel = channel;
    }

    @Nullable
    public Channel getChannel() {
        return channel;
    }

    @Override
    public boolean hasContinuation() {
        return channel != null && channel.getContent() instanceof Continuation
                && ((Continuation) channel.getContent()).getContinuationToken() != null;
    }

    @Override
    public Continuation getContinuation() {
        return hasContinuation() ? ((Continuation) channel.getContent()) : null;
    }


    public static final class TypeAdapter implements JsonDeserializer<ChannelResponse> {

        @Override
        public ChannelResponse deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonObject) {
                JsonObject jsonObj = json.getAsJsonObject();
                String result = safeGet(jsonObj, "result", "failed");
                long timestamp = safeGet(jsonObj, "timestamp", 0).longValue();
                if (RESULT_OK.equalsIgnoreCase(result)) {
                    JsonElement content = jsonObj.get("content");
                    if (content instanceof JsonObject && ((JsonObject) content).size() > 0) {
                        Channel channel = context.deserialize(content, Channel.class);
                        return new ChannelResponse(result, timestamp, channel);
                    }
                }
                return new ChannelResponse(result, timestamp, null);
            } else {
                throw new JsonParseException("Invalid ChannelResponse");
            }
        }
    }

}
