package io.github.divios.wards.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.github.divios.core_lib.misc.LocationUtils;
import io.github.divios.wards.wards.Ward;

import java.io.IOException;

public class WardSerializer extends StdSerializer<Ward> {


    public WardSerializer() {
        this(Ward.class);
    }

    public WardSerializer(Class<Ward> t) {
        super(t);
    }

    @Override
    public void serialize(Ward ward, JsonGenerator gen, SerializerProvider provider) throws IOException {

        gen.writeStartObject();
        gen.writeStringField("owner", ward.getOwner().toString());
        gen.writeStringField("location", LocationUtils.toString(ward.getCenter()));
        gen.writeStringField("type", ward.getType().getId());
        gen.writeNumberField("time", ward.getTimer());
        gen.writeEndObject();

    }
}
