package com.blitzfud.controllers.restapi.interfaces;

import com.blitzfud.models.ResponseAPI;
import com.blitzfud.models.responseCount.ShoppingCartCount;

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
    Call<ShoppingCartCount> getAll(@Header("authorization") String token);

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

    @DELETE(".")
    Call<ResponseAPI> clear(@Header("authorization") String token);
}
