package com.blitzfud.controllers.restapi.interfaces;

import com.blitzfud.models.body.PurchaseOrderBody;
import com.blitzfud.models.responseAPI.PurchaseOrdersSet;
import com.blitzfud.models.responseAPI.ResponseAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PurchaseOrdersInterface {
    @GET(".")
    Call<PurchaseOrdersSet> getAll(@Header("authorization") String token);

    @GET(".")
    Call<PurchaseOrdersSet> getActive(@Header("authorization") String token,
                                @Query("status") String status);

    @POST(".")
    Call<ResponseAPI> create(@Header("authorization") String token,
                             @Body List<PurchaseOrderBody> orderBodies);
}
