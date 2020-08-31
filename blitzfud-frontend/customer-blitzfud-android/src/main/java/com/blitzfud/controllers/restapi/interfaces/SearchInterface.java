package com.blitzfud.controllers.restapi.interfaces;

import com.blitzfud.models.responseAPI.MarketSet;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchInterface {
    @GET("products")
    Call<MarketSet> searchProducts(@Query("lat") final double latitude,
                                   @Query("lon") final double longitude,
                                   @Query("keyword") final String keyword);
}
