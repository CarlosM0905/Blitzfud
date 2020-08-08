package com.blitzfud.controllers.restapi.interfaces;

import com.blitzfud.models.responseCount.MarketCount;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchInterface {
    @GET("products")
    Call<MarketCount> searchProducts(@Query("lat") final double latitude,
                                     @Query("lon") final double longitude,
                                     @Query("keyword") final String keyword);
}
