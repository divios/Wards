package io.github.divios.wards.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.github.divios.core_lib.misc.LocationUtils;
import io.github.divios.wards.Wards;
import io.github.divios.wards.wards.Ward;

import java.io.IOException;

public class WardDeserializer extends StdDeserializer<Ward> {

    public WardDeserializer() {
        this(null);
    }

    protected WardDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Ward deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);

        return new Ward.Builder(node.get("owner").asText())
                .setLocation(LocationUtils.fromString(Wards.getInstance(), node.get("location").asText()))
                .setId(node.get("type").asText())
                .setTimer(node.get("time").asInt())
                .build();
    }
}
