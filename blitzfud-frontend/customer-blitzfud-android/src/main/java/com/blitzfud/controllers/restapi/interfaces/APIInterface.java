package com.blitzfud.controllers.restapi.interfaces;

import com.blitzfud.models.responseAPI.ResponseAPI;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {
    @GET(".")
    Call<ResponseAPI> wakeup();
}
