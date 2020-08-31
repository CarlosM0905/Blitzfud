package com.blitzfud.controllers.restapi.interfaces;

import com.blitzfud.models.market.Product;
import com.blitzfud.models.responseAPI.ProductSet;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductInterface {
    @GET("{marketId}/products")
    Call<ProductSet> getProducts(@Path("marketId") String marketId,
                                 @Query("offset") int offset,
                                 @Query("limit") int limit,
                                 @Query("category") String category);

    @GET("{marketId}/products/{productId}")
    Call<Product> getById(@Path("marketId") String marketId,
                          @Path("productId") String productId);
}
