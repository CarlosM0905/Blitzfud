package com.blitzfud.controllers.restapi.interfaces;

import com.blitzfud.models.responseAPI.CategorySet;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryInterface {
    @GET(".")
    Call<CategorySet> getAll();
}
