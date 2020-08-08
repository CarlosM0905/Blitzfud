package com.blitzfud.controllers.restapi.deserializers.market;

import com.blitzfud.models.market.Market;
import com.blitzfud.models.responseCount.MarketCount;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class getAllDeserializer implements JsonDeserializer<MarketCount> {
    @Override
    public MarketCount deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = json.getAsJsonObject();
        final Gson gson = new Gson();

        final int count = jo.get("count").getAsInt();
        final String docs = jo.get("markets").getAsJsonArray().toString();
        final ArrayList<Market> markets = new ArrayList<>(Arrays.asList(gson.fromJson(docs, Market[].class)));

        return new MarketCount(count, markets);
    }
}
