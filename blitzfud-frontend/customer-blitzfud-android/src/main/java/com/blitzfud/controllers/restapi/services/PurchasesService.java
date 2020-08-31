package com.blitzfud.controllers.restapi.services;

import com.blitzfud.controllers.restapi.API;
import com.blitzfud.controllers.restapi.interfaces.PurchasesInterface;
import com.blitzfud.models.responseAPI.PurchasesSet;

import java.util.Date;

import retrofit2.Call;

import static com.blitzfud.controllers.utilities.BlitzfudConstants.SDF_DATE_PURCHASE;

public class PurchasesService {

    private static final String URL = "purchases/";
    private static PurchasesInterface purchasesInterface;

    public static Call<PurchasesSet> get(String from, String to) {
        return getInstance().get(AuthService.getToken(), from, to);
    }

    public static Call<PurchasesSet> getToday() {

        return getInstance().getToday(AuthService.getToken(), SDF_DATE_PURCHASE.format(new Date()));
    }

    private static PurchasesInterface getInstance() {
        if (purchasesInterface == null) purchasesInterface = API.createService(PurchasesInterface.class, URL);

        return purchasesInterface;
    }
}
