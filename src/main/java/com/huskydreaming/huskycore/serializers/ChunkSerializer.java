package com.huskydreaming.huskycore.serializers;

import com.google.gson.*;
import com.huskydreaming.huskycore.data.ChunkData;

import java.lang.reflect.Type;

public class ChunkSerializer implements JsonSerializer<ChunkData>, JsonDeserializer<ChunkData> {

    @Override
    public ChunkData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return ChunkData.deserialize(jsonElement.getAsString().split(":"));
    }

    @Override
    public JsonElement serialize(ChunkData data, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(data);
    }
}