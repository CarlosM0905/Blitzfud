package com.blitzfud.controllers.restapi.interfaces;

import com.blitzfud.models.responseAPI.PurchasesSet;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface PurchasesInterface {
    @GET(".")
    Call<PurchasesSet> get(@Header("authorization") String token,
                           @Query("from") String from,
                           @Query("to") String to);

    @GET(".")
    Call<PurchasesSet> getToday(@Header("authorization") String token,
                                @Query("from") String from);
}
