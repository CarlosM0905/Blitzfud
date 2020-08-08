package com.blitzfud.controllers.restapi.deserializers.product;

import com.blitzfud.models.market.Product;
import com.blitzfud.models.responseCount.ProductCount;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class getAllDeserializer implements JsonDeserializer<ProductCount> {
    @Override
    public ProductCount deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = json.getAsJsonObject();
        final Gson gson = new Gson();

        final int count = jo.get("count").getAsInt();
        final String docs = jo.get("products").getAsJsonArray().toString();
        final ArrayList<Product> products = new ArrayList<>(Arrays.asList(gson.fromJson(docs, Product[].class)));

        return new ProductCount(count, products);
    }
}