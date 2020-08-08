package com.blitzfud.views.pages.shoppingCart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.blitzfud.R;
import com.blitzfud.models.ResponseAPI;
import com.blitzfud.views.adapters.shoppingCart.MarketShoppingCartAdapter;
import com.blitzfud.controllers.restapi.services.ShoppingCartService;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.databinding.ActivityShoppingCartBinding;
import com.blitzfud.models.market.Market;
import com.blitzfud.models.shoppingCart.ShoppingCart;
import com.blitzfud.models.market.Product;
import com.blitzfud.models.responseCount.ShoppingCartCount;
import com.blitzfud.views.pages.shoppingCart.execute.ExecuteShoppingCartActivity;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShoppingCartActivity extends AppCompatActivity {

    private static ArrayList<ShoppingCart> shoppingCarts;
    private static int count;
    private static double total;

    private ActivityShoppingCartBinding binding;
    private MarketShoppingCartAdapter marketShoppingCartAdapter;
    private LinearLayoutManager linearLayoutManager;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShoppingCartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initConfig();
        prepareRecycler();
        loadData();
        bindListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_client_order, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.optClearOrder:
                clickClearShoppingCart();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initConfig(){
        BlitzfudUtils.initToolbar(this, "Mi carrito", true);
        dialog = BlitzfudUtils.initLoading(this);
    }

    private void prepareRecycler() {
        linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void loadData() {
        if (shoppingCarts != null) {
            showItems();
            return;
        }

        ShoppingCartService.getAll().enqueue(new Callback<ShoppingCartCount>() {
            @Override
            public void onResponse(Call<ShoppingCartCount> call, Response<ShoppingCartCount> response) {
                if (response.isSuccessful()) {
                    final ShoppingCartCount shoppingCartCount = response.body();
                    total = shoppingCartCount.getTotal();
                    count = shoppingCartCount.getCount();
                    shoppingCarts = shoppingCartCount.getSubcarts();

                    showItems();
                } else {
                    BlitzfudUtils.showError(ShoppingCartActivity.this, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ShoppingCartCount> call, Throwable t) {
                BlitzfudUtils.showFailure(ShoppingCartActivity.this);
            }
        });
    }

    private void showItems() {
        binding.pantallaLoading.setVisibility(View.GONE);

        if (shoppingCarts.isEmpty()) {
            binding.pantallaEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.txtTotal.setText(String.format("Total: S/ %.2f", total));
            marketShoppingCartAdapter = new MarketShoppingCartAdapter(ShoppingCartActivity.this,
                    shoppingCarts, new MarketShoppingCartAdapter.OnItemDeletedListener() {
                @Override
                public void onItemDeleted(double totalValue, String marketId, int size) {
                    total -= totalValue;
                    binding.txtTotal.setText(String.format("Total: S/ %.2f", total));

                    if(size == 0){
                        final int position = findMarket(marketId);

                        if(position != -1){
                            shoppingCarts.remove(position);
                            marketShoppingCartAdapter.notifyItemRemoved(position);

                            if(shoppingCarts.isEmpty()){
                                binding.pantallaEmpty.setVisibility(View.VISIBLE);
                                binding.pantallaPrincipal.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            });
            binding.recyclerView.setAdapter(marketShoppingCartAdapter);
            binding.pantallaPrincipal.setVisibility(View.VISIBLE);
        }
    }

    private void bindListener() {
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(ShoppingCartActivity.this, ExecuteShoppingCartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void clickClearShoppingCart() {
        new SweetAlertDialog(ShoppingCartActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("¿Estás seguro?")
                .setContentText("No podrás revertir la acción")
                .setConfirmText("Sí")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        clearShoppingCart();
                    }
                })
                .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    private void clearShoppingCart(){
        dialog.show();
        ShoppingCartService.clear().enqueue(new Callback<ResponseAPI>() {
            @Override
            public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    shoppingCarts.clear();
                    count = 0;
                    total = 0;
                    Toast.makeText(ShoppingCartActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    BlitzfudUtils.showError(ShoppingCartActivity.this, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseAPI> call, Throwable t) {
                dialog.dismiss();
                BlitzfudUtils.showFailure(ShoppingCartActivity.this);
            }
        });
    }

    public static int getQuantity(final String productId, final String marketId) {
        if (shoppingCarts == null)
            return 0;

        return findQuantityProduct(productId, marketId);
    }

    private static int findQuantityProduct(final String productId, final String marketId) {
        final int position = findMarket(marketId);

        if(position==-1) return 0;

        return shoppingCarts.get(position).getQuantityItem(productId);
    }

    public static void addItem(final int quantity, final Product product, final Market market) {
        int positionMarket = findMarket(market.get_id());

        if(positionMarket==-1){
            shoppingCarts.add(new ShoppingCart(market, product, quantity));
        }else{
            shoppingCarts.get(positionMarket).addItemShoppingCart(product, quantity);
        }

        total += quantity * product.getPrice();
    }

    public static void updateItem(final int quantity, final Product product, final Market market) {
        final int position = findMarket(market.get_id());

        if (position != -1) {
            ShoppingCart shoppingCart = shoppingCarts.get(position);
            total -= shoppingCart.getSubtotalItem(product.get_id());
            shoppingCarts.get(position).updateItemShoppingCart(product, quantity);
            total += quantity * product.getPrice();
        }
    }

    public static void removeItem(final Product product, final Market market) {
        final int position = findMarket(market.get_id());

        if (position != -1) {
            ShoppingCart shoppingCart = shoppingCarts.get(position);
            total -= shoppingCart.getSubtotalItem(product.get_id());
            shoppingCart.removeItemShoppingCart(product);
        }
    }

    private static int findMarket(final String marketId) {
        int position = -1;

        for (int i = 0; i < shoppingCarts.size(); i++) {
            final ShoppingCart item = shoppingCarts.get(i);

            if (item.getMarket().get_id().equals(marketId)) {
                position = i;
                break;
            }
        }

        return position;
    }

    public static ShoppingCart getShoppingCart(final String marketId){

        for (int i = 0; i < shoppingCarts.size(); i++) {
            final ShoppingCart item = shoppingCarts.get(i);

            if (item.getMarket().get_id().equals(marketId)) {
                return item;
            }
        }

        return null;
    }

    public static void updateMarketDelivery(final String marketId, final boolean isChecked){
        final int position = findMarket(marketId);

        if(position != 1) shoppingCarts.get(position).setDelivery(isChecked);
    }

    public static double getTotal() {
        return total;
    }

    public static void setTotal(double total) {
        ShoppingCartActivity.total = total;
    }

    public static boolean itemsInitialized() {
        return shoppingCarts != null;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        ShoppingCartActivity.count = count;
    }

    public static ArrayList<ShoppingCart> getshoppingCarts() {
        return shoppingCarts;
    }

    public static void setshoppingCarts(ArrayList<ShoppingCart> shoppingCarts) {
        ShoppingCartActivity.shoppingCarts = shoppingCarts;
    }

}
