package com.blitzfud.controllers.restapi.interfaces;

import com.blitzfud.models.responseCount.CategoryCount;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryInterface {
    @GET(".")
    Call<CategoryCount> getAll();
}
