package com.blitzfud.controllers.restapi.services;

import com.blitzfud.controllers.restapi.API;
import com.blitzfud.controllers.restapi.interfaces.FavoriteMarketInterface;
import com.blitzfud.models.responseAPI.FavoriteMarketSet;
import com.blitzfud.models.responseAPI.ResponseAPI;

import retrofit2.Call;

public class FavoriteMarketService {

    private static final String URL = "favoriteMarkets/";
    private static FavoriteMarketInterface favoriteMarketInterface;

    public static Call<FavoriteMarketSet> getAll() {
        return getInstance().getAll(AuthService.getToken());
    }

    public static Call<ResponseAPI> add(final String marketId) {
        return getInstance().add(AuthService.getToken(), marketId);
    }

    public static Call<ResponseAPI> remove(final String marketId) {
        return getInstance().remove(AuthService.getToken(), marketId);
    }

    private static FavoriteMarketInterface getInstance() {
        if (favoriteMarketInterface == null)
            favoriteMarketInterface = API.createService(FavoriteMarketInterface.class, URL);

        return favoriteMarketInterface;
    }
}
