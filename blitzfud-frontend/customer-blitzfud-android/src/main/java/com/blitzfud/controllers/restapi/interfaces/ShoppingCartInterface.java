package com.blitzfud.controllers.restapi.interfaces;

import com.blitzfud.models.responseAPI.ResponseAPI;
import com.blitzfud.models.responseAPI.ShoppingCartSet;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ShoppingCartInterface {
    @GET(".")
    Call<ShoppingCartSet> getAll(@Header("authorization") String token);

    @FormUrlEncoded
    @POST(".")
    Call<ResponseAPI> addItem(@Header("authorization") String token,
                                 @Field("productId") String productId,
                                 @Field("quantity") int quantity);

    @FormUrlEncoded
    @PATCH("{productId}")
    Call<ResponseAPI> updateItem(@Header("authorization") String token,
                                 @Path("productId") String productId,
                                 @Field("quantity") int quantity);

    @DELETE("{productId}")
    Call<ResponseAPI> removeItem(@Header("authorization") String token,
                                    @Path("productId") String productId);

    @DELETE("market/{marketId}")
    Call<ResponseAPI> clearMarket(@Header("authorization") String token,
                                  @Path("marketId") String market);

    @DELETE(".")
    Call<ResponseAPI> clear(@Header("authorization") String token);
}
