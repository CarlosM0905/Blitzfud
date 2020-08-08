package com.blitzfud.controllers.restapi.services;

import com.blitzfud.controllers.restapi.API;
import com.blitzfud.controllers.restapi.deserializers.search.getProductsDeserializer;
import com.blitzfud.controllers.restapi.interfaces.SearchInterface;
import com.blitzfud.models.responseCount.MarketCount;
import com.google.android.gms.maps.model.LatLng;

import retrofit2.Call;

public class SearchService {
    private static final String URL = "search/";

    public static Call<MarketCount> getProducts(final LatLng position, final String keyword){
        SearchInterface searchInterface = API.createService(SearchInterface.class, MarketCount.class,
                new getProductsDeserializer(), URL);

        return searchInterface.searchProducts(position.latitude, position.longitude, keyword);
    }

}
