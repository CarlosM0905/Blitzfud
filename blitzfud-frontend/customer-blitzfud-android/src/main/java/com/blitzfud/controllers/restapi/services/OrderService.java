package com.blitzfud.controllers.restapi.services;

import com.blitzfud.controllers.restapi.API;
import com.blitzfud.controllers.restapi.interfaces.OrderInterface;
import com.blitzfud.models.order.Order;

import retrofit2.Call;

public class OrderService {

    private static final String URL = "orders/";
    private static OrderInterface orderInterface;

    public static Call<Order> getById(String orderId) {
        return getInstance().getById(AuthService.getToken(), orderId);
    }

    private static OrderInterface getInstance() {
        if (orderInterface == null) orderInterface = API.createService(OrderInterface.class, URL);

        return orderInterface;
    }

}
