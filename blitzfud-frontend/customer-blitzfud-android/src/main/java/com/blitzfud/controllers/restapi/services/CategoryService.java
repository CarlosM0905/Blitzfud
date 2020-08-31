package com.blitzfud.controllers.restapi.services;

import com.blitzfud.controllers.restapi.API;
import com.blitzfud.controllers.restapi.interfaces.CategoryInterface;
import com.blitzfud.models.responseAPI.CategorySet;

import retrofit2.Call;

public class CategoryService {

    private static final String URL = "categories/";
    private static CategoryInterface marketInterface;

    public static Call<CategorySet> getAll() {
        return getInstance().getAll();
    }

    private static CategoryInterface getInstance() {
        if (marketInterface == null)
            marketInterface = API.createService(CategoryInterface.class, URL);

        return marketInterface;
    }
}
