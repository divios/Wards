package io.github.divios.wards.file;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.github.divios.wards.serializers.WardDeserializer;
import io.github.divios.wards.serializers.WardSerializer;
import io.github.divios.wards.utils.utils;
import io.github.divios.wards.wards.Ward;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class jsonDatabase {

    private final StdSerializer<Ward> serializer = new WardSerializer();
    private final StdDeserializer<Ward> deserializer = new WardDeserializer();

    private final File file;

    public jsonDatabase(File file) {
        this.file = file;
    }

    public File getFile() { return file; }

    public void serialize(Collection<Ward> wards) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Ward.class, serializer);
        mapper.registerModule(module);

        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

        try {
            writer.writeValue(file, wards);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Ward> deserialize() {

        if (utils.isEmpty(file)) return Collections.emptyList();

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("CustomWardSerializer",
                new Version(1, 0, 0, null, null, null));
        module.addDeserializer(Ward.class, deserializer);
        mapper.registerModule(module);

        List<Ward> wards = new ArrayList<>();
        try {
            wards = Arrays.asList(mapper.readValue(file, Ward[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return wards;
    }

}
