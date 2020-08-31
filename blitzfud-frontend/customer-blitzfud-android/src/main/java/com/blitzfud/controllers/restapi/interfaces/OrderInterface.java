package com.blitzfud.controllers.restapi.interfaces;

import com.blitzfud.models.order.Order;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface OrderInterface {

    @GET("{orderId}")
    Call<Order> getById(@Header("authorization") String token,
                        @Path("orderId") String orderId);

}
