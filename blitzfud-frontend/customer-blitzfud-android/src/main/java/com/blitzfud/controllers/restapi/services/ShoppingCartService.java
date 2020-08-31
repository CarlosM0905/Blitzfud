package com.blitzfud.controllers.restapi.services;

import com.blitzfud.controllers.restapi.API;
import com.blitzfud.controllers.restapi.interfaces.ShoppingCartInterface;
import com.blitzfud.models.responseAPI.ResponseAPI;
import com.blitzfud.models.responseAPI.ShoppingCartSet;

import retrofit2.Call;

public class ShoppingCartService {
    private static final String URL = "cart/";
    private static ShoppingCartInterface shoppingCartInterface;

    public static Call<ShoppingCartSet> getAll() {
        return getInstance().getAll(AuthService.getToken());
    }

    public static Call<ResponseAPI> addItem(final String productId, final int quantity) {
        return getInstance().addItem(AuthService.getToken(), productId, quantity);
    }

    public static Call<ResponseAPI> updateItem(final String productId, final int quantity) {
        return getInstance().updateItem(AuthService.getToken(), productId, quantity);
    }

    public static Call<ResponseAPI> removeItem(final String productId) {
        return getInstance().removeItem(AuthService.getToken(), productId);
    }

    public static Call<ResponseAPI> clearMarket(final String marketId) {
        return getInstance().clearMarket(AuthService.getToken(), marketId);
    }

    public static Call<ResponseAPI> clear() {
        return getInstance().clear(AuthService.getToken());
    }

    private static ShoppingCartInterface getInstance() {
        if (shoppingCartInterface == null)
            shoppingCartInterface = API.createService(ShoppingCartInterface.class, URL);

        return shoppingCartInterface;
    }

}
