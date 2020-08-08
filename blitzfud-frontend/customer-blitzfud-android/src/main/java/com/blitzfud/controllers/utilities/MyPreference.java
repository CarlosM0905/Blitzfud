package com.blitzfud.controllers.utilities;

import android.content.SharedPreferences;

import com.blitzfud.controllers.restapi.services.AuthService;
import com.blitzfud.models.User;
import com.blitzfud.views.fragments.market.FavoriteMarketsFragment;
import com.blitzfud.views.pages.market.MarketActivity;
import com.blitzfud.views.pages.shoppingCart.ItemShoppingCartActivity;
import com.blitzfud.views.pages.shoppingCart.ShoppingCartActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

public class MyPreference {
    public static final String PREFERENCE_NAME = "BLITzFUD_PREFERENCE";
    private static final double latDefault = -12.0459541;
    private static final double lngDefault = -77.0327471;
    private static final String nameDefault = "Plaza Mayor de Lima, Cercado de Lima 15001";

    public static boolean firstTime = true;
    private static final Gson gson = new Gson();
    public static LatLng positionLatLng = new LatLng(latDefault, lngDefault);
    public static String position = nameDefault;



    public static void savePreferences(SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstTime", firstTime);
        editor.putString("token", AuthService.getToken());
        editor.putString("user", gson.toJson(AuthService.getUser()));
        editor.apply();
    }

    public static void loadPreferences(SharedPreferences prefs) {
        firstTime = prefs.getBoolean("firstTime", true);
        position = prefs.getString("position", "Plaza Mayor de Lima, Cercado de Lima 15001");
        double positionLat = prefs.getFloat("positionLat", 0);
        double positionLng = prefs.getFloat("positionLng", 0);
        if (!position.equals(nameDefault)) {
            positionLatLng = new LatLng(positionLat, positionLng);
        }
        AuthService.setToken(prefs.getString("token", "none"));
        final String user = prefs.getString("user", "none");
        if (!user.equals("none")) {
            AuthService.setUser(gson.fromJson(user, User.class));
        }
    }

    public static void saveLocation(SharedPreferences prefs, final LatLng positionLatLng,
                                    final String position) {
        MyPreference.position = position;
        MyPreference.positionLatLng = new LatLng(positionLatLng.latitude, positionLatLng.longitude);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat("positionLat", (float) positionLatLng.latitude);
        editor.putFloat("positionLng", (float) positionLatLng.longitude);
        editor.putString("position", position);
        editor.apply();
    }

    public static void clearPreferences(SharedPreferences prefs) {
        prefs.edit().clear().apply();
        ItemShoppingCartActivity.setProduct(null);
        ShoppingCartActivity.setCount(0);
        ShoppingCartActivity.setshoppingCarts(null);
        ShoppingCartActivity.setTotal(0);
        MarketActivity.setMarket(null);
        FavoriteMarketsFragment.setMarkets(null);
        AuthService.setUser(null);
        AuthService.setToken("none");
        positionLatLng = new LatLng(latDefault, lngDefault);
        position = nameDefault;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstTime", firstTime);
        editor.apply();
    }
}
