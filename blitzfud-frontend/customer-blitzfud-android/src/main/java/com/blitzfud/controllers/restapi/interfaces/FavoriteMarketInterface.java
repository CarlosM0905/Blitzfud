package com.blitzfud.controllers.restapi.interfaces;

import com.blitzfud.models.responseAPI.FavoriteMarketSet;
import com.blitzfud.models.responseAPI.ResponseAPI;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FavoriteMarketInterface {
    @GET(".")
    Call<FavoriteMarketSet> getAll(@Header("authorization") String token);

    @FormUrlEncoded
    @POST(".")
    Call<ResponseAPI> add(@Header("authorization") String token,
                          @Field("marketId") String marketId);

    @DELETE("{marketId}")
    Call<ResponseAPI> remove(@Header("authorization") String token,
                             @Path("marketId") String marketId);
}
