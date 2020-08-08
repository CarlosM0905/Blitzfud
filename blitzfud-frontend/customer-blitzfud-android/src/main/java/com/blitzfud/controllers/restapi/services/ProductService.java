package com.blitzfud.controllers.restapi.services;

import com.blitzfud.controllers.restapi.API;
import com.blitzfud.controllers.restapi.deserializers.product.getAllDeserializer;
import com.blitzfud.controllers.restapi.interfaces.ProductInterface;
import com.blitzfud.models.market.Product;
import com.blitzfud.models.responseCount.ProductCount;

import retrofit2.Call;

public class ProductService {
    private static final String URL = "markets/";

    public static Call<ProductCount> getAll(final String marketId){
        ProductInterface productInterface = API.createService(ProductInterface.class, ProductCount.class,
                new getAllDeserializer(), URL);

        return productInterface.getAll(marketId);
    }

    public static Call<Product> getById(final String marketId, final String productId){
        ProductInterface productInterface = API.createService(ProductInterface.class, URL);

        return productInterface.getById(marketId, productId);
    }
}
