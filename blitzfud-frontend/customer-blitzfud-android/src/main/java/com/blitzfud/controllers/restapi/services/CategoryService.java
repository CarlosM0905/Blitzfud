package com.blitzfud.controllers.restapi.services;

import com.blitzfud.controllers.restapi.API;
import com.blitzfud.controllers.restapi.deserializers.category.getAllDeserializer;
import com.blitzfud.controllers.restapi.interfaces.CategoryInterface;
import com.blitzfud.models.responseCount.CategoryCount;
import com.blitzfud.models.responseCount.MarketCount;

import retrofit2.Call;

public class CategoryService {
    private static final String URL = "categories/";

    public static Call<CategoryCount> getAll() {
        CategoryInterface marketInterface = API.createService(CategoryInterface.class, MarketCount.class,
                new getAllDeserializer(), URL);

        return marketInterface.getAll();
    }
}
