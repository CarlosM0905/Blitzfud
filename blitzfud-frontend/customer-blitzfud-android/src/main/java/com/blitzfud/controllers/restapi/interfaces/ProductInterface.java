package com.blitzfud.controllers.restapi.interfaces;

import com.blitzfud.models.market.Product;
import com.blitzfud.models.responseCount.ProductCount;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductInterface {
    @GET("{marketId}/products")
    Call<ProductCount> getAll(@Path("marketId") String marketId);

    @GET("{marketId}/products/{productId}")
    Call<Product> getById(@Path("marketId") String marketId,
                          @Path("productId") String productId);
}
