package com.blitzfud.controllers.restapi.services;

import com.blitzfud.controllers.restapi.API;
import com.blitzfud.controllers.restapi.deserializers.favoriteMarket.getAllDeserializer;
import com.blitzfud.controllers.restapi.interfaces.FavoriteMarketInterface;
import com.blitzfud.models.ResponseAPI;
import com.blitzfud.models.responseCount.FavoriteMarketCount;

import retrofit2.Call;

public class FavoriteMarketService {

    private static final String URL = "favoriteMarkets/";

    public static Call<FavoriteMarketCount> getAll() {
        FavoriteMarketInterface favoriteMarketInterface = API.createService(FavoriteMarketInterface.class, FavoriteMarketCount[].class,
                new getAllDeserializer(), URL);

        return favoriteMarketInterface.getAll(AuthService.getToken());
    }

    public static Call<ResponseAPI> add(final String marketId) {
        FavoriteMarketInterface favoriteMarketInterface = API.createService(FavoriteMarketInterface.class, URL);

        return favoriteMarketInterface.add(AuthService.getToken(), marketId);
    }

    public static Call<ResponseAPI> remove(final String marketId){
        FavoriteMarketInterface favoriteMarketInterface = API.createService(FavoriteMarketInterface.class, URL);

        return favoriteMarketInterface.remove(AuthService.getToken(), marketId);
    }
}
