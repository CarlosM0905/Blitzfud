package com.blitzfud.controllers.restapi.services;

import com.blitzfud.controllers.restapi.API;
import com.blitzfud.controllers.restapi.interfaces.MarketInterface;
import com.blitzfud.models.responseAPI.MarketSet;
import com.google.android.gms.maps.model.LatLng;

import retrofit2.Call;

public class MarketService {

    private static final String URL = "markets/";
    private static MarketInterface marketInterface;

    public static Call<MarketSet> getAll(final LatLng myPosition) {
        return getInstance().getAll(myPosition.latitude, myPosition.longitude);
    }

    private static MarketInterface getInstance() {
        if (marketInterface == null)
            marketInterface = API.createService(MarketInterface.class, URL);

        return marketInterface;
    }

}
