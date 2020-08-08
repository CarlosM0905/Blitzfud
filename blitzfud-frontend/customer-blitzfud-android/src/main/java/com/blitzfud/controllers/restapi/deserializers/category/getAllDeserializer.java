package com.blitzfud.controllers.restapi.deserializers.category;

import com.blitzfud.models.market.Category;
import com.blitzfud.models.responseCount.CategoryCount;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class getAllDeserializer implements JsonDeserializer<CategoryCount> {
    @Override
    public CategoryCount deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = json.getAsJsonObject();
        final Gson gson = new Gson();

        final int count = jo.get("count").getAsInt();
        final String docs = jo.get("categories").getAsJsonArray().toString();
        final ArrayList<Category> categories = new ArrayList<>(Arrays.asList(gson.fromJson(docs, Category[].class)));

        return new CategoryCount(count, categories);
    }
}