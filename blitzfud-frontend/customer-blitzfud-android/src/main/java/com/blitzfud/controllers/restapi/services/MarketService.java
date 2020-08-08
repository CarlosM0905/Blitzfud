package com.blitzfud.controllers.restapi.services;

import com.blitzfud.controllers.restapi.API;
import com.blitzfud.controllers.restapi.deserializers.market.getAllDeserializer;
import com.blitzfud.controllers.restapi.interfaces.MarketInterface;
import com.blitzfud.models.responseCount.MarketCount;
import com.google.android.gms.maps.model.LatLng;

import retrofit2.Call;

public class MarketService {

    private static final String URL = "markets/";

    public static Call<MarketCount> getAll(final LatLng myPosition) {
        MarketInterface marketInterface = API.createService(MarketInterface.class, MarketCount.class,
                new getAllDeserializer(), URL);

        return marketInterface.getAll(myPosition.latitude, myPosition.longitude);
    }

}
