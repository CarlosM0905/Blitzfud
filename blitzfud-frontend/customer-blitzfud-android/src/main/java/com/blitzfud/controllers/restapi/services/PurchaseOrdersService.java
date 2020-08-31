package com.blitzfud.controllers.restapi.services;

import com.blitzfud.controllers.restapi.API;
import com.blitzfud.controllers.restapi.interfaces.PurchaseOrdersInterface;
import com.blitzfud.models.body.PurchaseOrderBody;
import com.blitzfud.models.responseAPI.PurchaseOrdersSet;
import com.blitzfud.models.responseAPI.ResponseAPI;

import java.util.List;

import retrofit2.Call;

public class PurchaseOrdersService {

    private static final String URL = "purchaseOrders/";
    private static PurchaseOrdersInterface purchaseOrdersInterface;

    public static Call<ResponseAPI> create(List<PurchaseOrderBody> orderBodies) {
        return getInstance().create(AuthService.getToken(), orderBodies);
    }

    public static Call<PurchaseOrdersSet> getAll() {
        return getInstance().getAll(AuthService.getToken());
    }

    public static Call<PurchaseOrdersSet> getActive() {
        return getInstance().getActive(AuthService.getToken(), "active");
    }

    private static PurchaseOrdersInterface getInstance() {
        if (purchaseOrdersInterface == null)
            purchaseOrdersInterface = API.createService(PurchaseOrdersInterface.class, URL);

        return purchaseOrdersInterface;
    }

}
