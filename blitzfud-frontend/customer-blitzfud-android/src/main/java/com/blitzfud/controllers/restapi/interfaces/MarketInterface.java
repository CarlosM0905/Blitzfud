package com.blitzfud.controllers.restapi.interfaces;

import com.blitzfud.models.responseCount.MarketCount;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MarketInterface {
    @GET(".")
    Call<MarketCount> getAll(@Query("lat") double latitude, @Query("lon") double longitude);
}
