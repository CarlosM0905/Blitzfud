package com.blitzfud.views.pages.market;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.blitzfud.R;
import com.blitzfud.views.adapters.market.CategoryAdapter;
import com.blitzfud.views.adapters.market.ProductAdapter;
import com.blitzfud.controllers.restapi.services.CategoryService;
import com.blitzfud.controllers.restapi.services.FavoriteMarketService;
import com.blitzfud.controllers.restapi.services.ProductService;
import com.blitzfud.controllers.utilities.BlitzfudColors;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.databinding.ActivityMarketBinding;
import com.blitzfud.models.market.Category;
import com.blitzfud.models.market.Market;
import com.blitzfud.models.market.Product;
import com.blitzfud.models.ResponseAPI;
import com.blitzfud.models.responseCount.CategoryCount;
import com.blitzfud.models.responseCount.FavoriteMarketCount;
import com.blitzfud.models.responseCount.ProductCount;
import com.blitzfud.views.fragments.market.FavoriteMarketsFragment;
import com.blitzfud.views.fragments.market.MarketsFragment;
import com.blitzfud.views.pages.shoppingCart.ItemShoppingCartActivity;
import com.blitzfud.views.pages.shoppingCart.ShoppingCartActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketActivity extends AppCompatActivity implements View.OnClickListener {

    private static Market market;
    private static AlertDialog dialog;

    private ActivityMarketBinding binding;
    private LinearLayoutManager categoryLinearLayoutManager;
    private LinearLayoutManager newProductsLinearLayoutManager;
    private RecyclerView.LayoutManager allProductsLayoutManager;
    private CategoryAdapter categoryAdapter;
    private int countCategories;
    private ArrayList<Category> categories;
    private ProductAdapter productAdapter;
    private int countProducts;
    private ArrayList<Product> products;
    private boolean categoriesLoaded;
    private boolean productsLoaded;
    private boolean subscribed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMarketBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initConfig();
        prepareRecycler();
        loadData();
        bindListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.optMyOrder:
                redirectMyShoppingCart();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSubscribe: clickSubscribe(); break;
        }
    }

    private void initConfig(){
        BlitzfudUtils.initToolbar(this,  market.getName(), true);
        dialog = BlitzfudUtils.initLoading(this);
    }

    private void prepareRecycler() {
        categoryLinearLayoutManager = new LinearLayoutManager(MarketActivity.this, RecyclerView.HORIZONTAL, false);
        newProductsLinearLayoutManager = new LinearLayoutManager(MarketActivity.this, RecyclerView.HORIZONTAL, false);
        //allProductsLayoutManager = BlitzfudUtils.getStaggeredGrid(MarketActivity.this, 3, 4);
        allProductsLayoutManager = new LinearLayoutManager(MarketActivity.this, RecyclerView.HORIZONTAL, false);

        binding.recyclerCategory.setHasFixedSize(true);
        binding.recyclerNewProducts.setHasFixedSize(true);
        binding.recyclerAllProducts.setHasFixedSize(true);

        binding.recyclerCategory.setLayoutManager(categoryLinearLayoutManager);
        binding.recyclerNewProducts.setLayoutManager(newProductsLinearLayoutManager);
        binding.recyclerAllProducts.setLayoutManager(allProductsLayoutManager);
    }

    private void loadData() {
        if (!FavoriteMarketsFragment.itemsInitialized()) {
            FavoriteMarketService.getAll().enqueue(new Callback<FavoriteMarketCount>() {
                @Override
                public void onResponse(Call<FavoriteMarketCount> call, Response<FavoriteMarketCount> response) {
                    if (response.isSuccessful()) {
                        final FavoriteMarketCount favoriteMarketCount = response.body();
                        FavoriteMarketsFragment.setCount(favoriteMarketCount.getCount());
                        FavoriteMarketsFragment.setMarkets(favoriteMarketCount.getMarkets());
                        subscribed = FavoriteMarketsFragment.existsMarket(market.get_id());
                        showSusbcribe();
                    } else {
                        BlitzfudUtils.showError(MarketActivity.this, response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<FavoriteMarketCount> call, Throwable t) {
                    BlitzfudUtils.showFailure(MarketActivity.this);
                }
            });
        } else {
            subscribed = FavoriteMarketsFragment.existsMarket(market.get_id());
            showSusbcribe();
        }

        CategoryService.getAll().enqueue(new Callback<CategoryCount>() {
            @Override
            public void onResponse(Call<CategoryCount> call, Response<CategoryCount> response) {
                if (response.isSuccessful()) {
                    final CategoryCount categoryCount = response.body();

                    countCategories = categoryCount.getCount();
                    categories = categoryCount.getCategories();

                    categoriesLoaded = true;
                    showCategories();
                } else {
                    BlitzfudUtils.showError(MarketActivity.this, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<CategoryCount> call, Throwable t) {
                BlitzfudUtils.showFailure(MarketActivity.this);
            }
        });

        ProductService.getAll(market.get_id()).enqueue(new Callback<ProductCount>() {
            @Override
            public void onResponse(Call<ProductCount> call, Response<ProductCount> response) {
                if (response.isSuccessful()) {
                    final ProductCount productCount = response.body();

                    countProducts = productCount.getCount();
                    products = productCount.getProducts();

                    productsLoaded = true;
                    showProducts();
                } else {
                    BlitzfudUtils.showError(MarketActivity.this, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ProductCount> call, Throwable t) {
                BlitzfudUtils.showFailure(MarketActivity.this);
            }
        });
    }

    private void showSusbcribe() {
        if (subscribed) {
            binding.btnSubscribe.setText("SUSCRITO");
            binding.btnSubscribe.setTextColor(BlitzfudColors.colorSecondary);
        }else{
            binding.btnSubscribe.setText("SUSCRIBIRME");
            binding.btnSubscribe.setTextColor(BlitzfudColors.colorPrimary);
        }
    }

    private void showCategories() {
        categoryAdapter = new CategoryAdapter(MarketActivity.this, categories, new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category category, int position) {
                Toast.makeText(MarketActivity.this, "Filtrar por categoría", Toast.LENGTH_SHORT).show();
            }
        });
        binding.recyclerCategory.setAdapter(categoryAdapter);

        if (productsLoaded) {
            binding.pantallaLoading.setVisibility(View.GONE);
            binding.pantallaPrincipal.setVisibility(View.VISIBLE);
        }
    }

    private void showProducts() {
        productAdapter = new ProductAdapter(MarketActivity.this, products, new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product, int position) {
                ItemShoppingCartActivity.setProduct(product);
                startActivity(new Intent(MarketActivity.this, ItemShoppingCartActivity.class));
            }
        });
        binding.recyclerAllProducts.setAdapter(productAdapter);

        if (categoriesLoaded) {
            binding.pantallaLoading.setVisibility(View.GONE);
            binding.pantallaPrincipal.setVisibility(View.VISIBLE);
        }
    }

    private void bindListeners() {
        binding.btnSubscribe.setOnClickListener(this);
    }

    private void clickSubscribe(){
        dialog.show();

        if (!subscribed) {
            subscribe();
        } else {
            unsubscribe();
        }
    }

    private void subscribe(){
        FavoriteMarketService.add(market.get_id()).enqueue(new Callback<ResponseAPI>() {
            @Override
            public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    subscribed = true;
                    MarketsFragment.setSubscribeAction(1);
                    FavoriteMarketsFragment.addMarket(market);
                    showSusbcribe();
                    BlitzfudUtils.showSuccessMessage(MarketActivity.this, response.body().getMessage());
                } else {
                    BlitzfudUtils.showError(MarketActivity.this, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseAPI> call, Throwable t) {
                dialog.dismiss();
                BlitzfudUtils.showFailure(MarketActivity.this);
            }
        });
    }

    private void unsubscribe(){
        FavoriteMarketService.remove(market.get_id()).enqueue(new Callback<ResponseAPI>() {
            @Override
            public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    subscribed = false;
                    MarketsFragment.setSubscribeAction(0);
                    FavoriteMarketsFragment.removeMarket(market);
                    showSusbcribe();
                    BlitzfudUtils.showSuccessMessage(MarketActivity.this, response.body().getMessage());
                } else {
                    BlitzfudUtils.showError(MarketActivity.this, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseAPI> call, Throwable t) {
                dialog.dismiss();
                BlitzfudUtils.showFailure(MarketActivity.this);
            }
        });
    }

    private void redirectMyShoppingCart() {
        startActivity(new Intent(MarketActivity.this, ShoppingCartActivity.class));
    }

    public static Market getMarket() {
        return market;
    }

    public static void setMarket(Market market) {
        MarketActivity.market = market;
    }

}
