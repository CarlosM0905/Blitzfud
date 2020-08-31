package com.blitzfud.controllers.restapi.services;

import com.blitzfud.controllers.restapi.API;
import com.blitzfud.controllers.restapi.interfaces.SearchInterface;
import com.blitzfud.models.responseAPI.MarketSet;
import com.google.android.gms.maps.model.LatLng;

import retrofit2.Call;

public class SearchService {

    private static final String URL = "search/";
    private static SearchInterface searchInterface;

    public static Call<MarketSet> getProducts(final LatLng position, final String keyword) {
        return getInstance().searchProducts(position.latitude, position.longitude, keyword);
    }

    private static SearchInterface getInstance() {
        if (searchInterface == null)
            searchInterface = API.createService(SearchInterface.class, URL);

        return searchInterface;
    }

}
