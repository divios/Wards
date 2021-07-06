package io.github.divios.wards.file;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import io.github.divios.core_lib.misc.LocationUtils;
import io.github.divios.wards.Wards;
import io.github.divios.wards.utils.utils;
import io.github.divios.wards.wards.Ward;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class jsonDatabase {


    private final File file;

    public jsonDatabase(File file) {
        this.file = file;
    }

    public File getFile() { return file; }

    public void serialize(Collection<Ward> wards) {
        GsonBuilder gsonBuilder = new GsonBuilder();

        JsonSerializer<Ward> serializer = (ward, typeOfSrc, context) -> {
            JsonObject jsonMerchant = new JsonObject();

            jsonMerchant.addProperty("owner", ward.getOwner().toString());
            jsonMerchant.addProperty("location", LocationUtils.toString(ward.getCenter()));
            jsonMerchant.addProperty("type", ward.getType().getId());
            jsonMerchant.addProperty("time", ward.getTimer());

            return jsonMerchant;
        };

        gsonBuilder.registerTypeAdapter(Ward.class, serializer);

        Gson customGson = gsonBuilder.setPrettyPrinting().create();
        try {
            Writer writer = new FileWriter(file);
            customGson.toJson(wards, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Ward> deserialize() {

        if (utils.isEmpty(file)) return Collections.emptyList();

        List<Ward> wards = new ArrayList<>();
        GsonBuilder gsonBuilder = new GsonBuilder();

        JsonDeserializer<List<Ward>> deserializer = (json, typeOfT, context) -> {

            List<Ward> wards1 = new ArrayList<>();

            json.getAsJsonArray().forEach(jsonElement -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                wards1.add(new Ward.Builder(jsonObject.get("owner").getAsString())
                        .setLocation(LocationUtils.fromString(Wards.getInstance(),
                                jsonObject.get("location").getAsString()))
                        .setId(jsonObject.get("type").getAsString())
                        .setTimer(jsonObject.get("time").getAsInt())
                        .build());
            });
            return wards1;
        };
        gsonBuilder.registerTypeAdapter(ArrayList.class, deserializer);

        Gson customGson = gsonBuilder.create();

        try {
            wards = customGson.fromJson(new JsonReader(new FileReader(file)), ArrayList.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return wards;
    }

}
