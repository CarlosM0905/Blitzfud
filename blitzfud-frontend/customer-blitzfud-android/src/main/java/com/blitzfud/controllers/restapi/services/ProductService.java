package com.blitzfud.controllers.restapi.services;

import com.blitzfud.controllers.restapi.API;
import com.blitzfud.controllers.restapi.interfaces.ProductInterface;
import com.blitzfud.models.market.Product;
import com.blitzfud.models.responseAPI.ProductSet;

import retrofit2.Call;

public class ProductService {

    private static final String URL = "markets/";
    private static ProductInterface productInterface;

    public static Call<ProductSet> getAll(final String marketId) {
        return getInstance().getProducts(marketId, 0, 10, null );
    }

    public static Call<ProductSet> getProducts(final String marketId, int offset, int limit,
                                               String categoryId) {
        return getInstance().getProducts(marketId, offset, limit, categoryId );
    }

    public static Call<Product> getById(final String marketId, final String productId) {
        return getInstance().getById(marketId, productId);
    }

    private static ProductInterface getInstance() {
        if (productInterface == null)
            productInterface = API.createService(ProductInterface.class, URL);

        return productInterface;
    }
}
