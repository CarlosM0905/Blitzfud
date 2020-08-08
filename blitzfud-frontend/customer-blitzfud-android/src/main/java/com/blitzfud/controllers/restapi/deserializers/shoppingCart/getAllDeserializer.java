package com.blitzfud.controllers.restapi.deserializers.shoppingCart;

import com.blitzfud.models.responseCount.ShoppingCartCount;
import com.blitzfud.models.shoppingCart.ShoppingCart;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class getAllDeserializer implements JsonDeserializer<ShoppingCartCount> {

    @Override
    public ShoppingCartCount deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = json.getAsJsonObject();
        final Gson gson = new Gson();

        final int count = jo.get("count").getAsInt();
        final String docs = jo.get("subcarts").getAsJsonArray().toString();
        final ArrayList<ShoppingCart> markets = new ArrayList<>(Arrays.asList(gson.fromJson(docs, ShoppingCart[].class)));

        return new ShoppingCartCount(count, markets);
    }

}