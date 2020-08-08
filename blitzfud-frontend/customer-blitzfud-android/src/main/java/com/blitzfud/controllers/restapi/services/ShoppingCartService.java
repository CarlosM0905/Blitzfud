package com.blitzfud.controllers.restapi.services;

import com.blitzfud.controllers.restapi.API;
import com.blitzfud.controllers.restapi.deserializers.shoppingCart.getAllDeserializer;
import com.blitzfud.controllers.restapi.interfaces.ShoppingCartInterface;
import com.blitzfud.models.ResponseAPI;
import com.blitzfud.models.shoppingCart.ShoppingCart;
import com.blitzfud.models.responseCount.ShoppingCartCount;

import retrofit2.Call;

public class ShoppingCartService {
    private static final String URL = "cart/";

    public static Call<ShoppingCartCount> getAll() {
        ShoppingCartInterface shoppingCartInterface = API.createService(ShoppingCartInterface.class, ShoppingCartCount.class,
                new getAllDeserializer(), URL);

        return shoppingCartInterface.getAll(AuthService.getToken());
    }

    public static Call<ResponseAPI> addItem(final String productId, final int quantity) {
        ShoppingCartInterface shoppingCartInterface = API.createService(ShoppingCartInterface.class, URL);

        return shoppingCartInterface.addItem(AuthService.getToken(), productId, quantity);
    }

    public static Call<ResponseAPI> updateItem(final String productId, final int quantity) {
        ShoppingCartInterface shoppingCartInterface = API.createService(ShoppingCartInterface.class, URL);

        return shoppingCartInterface.updateItem(AuthService.getToken(), productId, quantity);
    }

    public static Call<ResponseAPI> removeItem(final String productId){
        ShoppingCartInterface shoppingCartInterface = API.createService(ShoppingCartInterface.class, URL);

        return shoppingCartInterface.removeItem(AuthService.getToken(), productId);
    }

    public static Call<ResponseAPI> clear(){
        ShoppingCartInterface shoppingCartInterface = API.createService(ShoppingCartInterface.class, URL);

        return shoppingCartInterface.clear(AuthService.getToken());
    }

}
