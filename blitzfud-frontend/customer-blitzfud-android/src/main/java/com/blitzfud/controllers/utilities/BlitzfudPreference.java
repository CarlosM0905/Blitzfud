package com.blitzfud.controllers.utilities;

import android.content.SharedPreferences;

import com.blitzfud.controllers.restapi.services.AuthService;
import com.blitzfud.models.auth.User;
import com.blitzfud.views.fragments.market.FavoriteMarketsFragment;
import com.blitzfud.views.fragments.market.MarketsFragment;
import com.blitzfud.views.fragments.shoppingCart.ShoppingCartFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

public class BlitzfudPreference {
    public static final String PREFERENCE_NAME = "BLITzFUD_PREFERENCE";
    private static final double latDefault = -12.0460038;
    private static final double lngDefault = -77.0327398;
    public static final LatLng defaultUbication = new LatLng(-12.0460038, -77.0327398);
    public static final String defaultAddress = "Plaza de Armas de Lima, Cercado de Lima";

    public static boolean firstTime = true;
    private static final Gson gson = new Gson();


    public static void savePreferences(SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstTime", firstTime);
        editor.putString("token", AuthService.getToken());
        editor.putString("user", gson.toJson(AuthService.getUser()));
        editor.apply();
    }

    public static void loadPreferences(SharedPreferences prefs) {
        firstTime = prefs.getBoolean("firstTime", true);
        AuthService.setToken(prefs.getString("token", "none"));

        final String user = prefs.getString("user", "none");
        if (!user.equals("none")) {
            AuthService.setUser(gson.fromJson(user, User.class));
        }
    }

    public static void saveUser(SharedPreferences prefs){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("user", gson.toJson(AuthService.getUser()));
        editor.apply();
    }

    public static void clearPreferences(SharedPreferences prefs) {
        prefs.edit().clear().apply();
        MarketsFragment.loadedFromAPI = false;
        FavoriteMarketsFragment.loadedFromAPI = false;
        ShoppingCartFragment.loadedFromAPI = false;
        AuthService.setUser(null);
        AuthService.setToken("none");
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstTime", firstTime);
        editor.apply();
    }
}
