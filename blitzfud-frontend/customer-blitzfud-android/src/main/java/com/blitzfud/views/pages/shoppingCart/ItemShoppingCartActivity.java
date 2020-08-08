package com.blitzfud.views.pages.shoppingCart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.blitzfud.R;
import com.blitzfud.controllers.restapi.services.ShoppingCartService;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.databinding.ActivityItemShoppingCartBinding;
import com.blitzfud.models.market.Product;
import com.blitzfud.models.ResponseAPI;
import com.blitzfud.models.responseCount.ShoppingCartCount;
import com.blitzfud.views.pages.market.MarketActivity;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemShoppingCartActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int TYPE_ADD_ITEM = 0;
    private static final int TYPE_UPDATE_ITEM = 1;
    private static final int TYPE_REMOVE_ITEM = 2;
    private static Product product;

    private ActivityItemShoppingCartBinding binding;
    private double total = 0;
    private int quantity = 0;
    private int initialQuantity = 1;
    private boolean alreadyAdded;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemShoppingCartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initConfig();
        bindListeners();
        loadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lblMinus:
                clickMinus();
                break;
            case R.id.lblPlus:
                clickPlus();
                break;
            case R.id.btnAddItem:
                confirm();
                break;
        }
    }

    private void initConfig() {
        BlitzfudUtils.initToolbar(this, "Agregar al carrito", true);
        dialog = BlitzfudUtils.initLoading(this);
    }

    private void bindListeners() {
        binding.lblMinus.setOnClickListener(this);
        binding.lblPlus.setOnClickListener(this);
        binding.btnAddItem.setOnClickListener(this);
    }

    private void clickMinus() {
        if (quantity > 0) {
            quantity--;
            updateView();
        }
    }

    private void clickPlus() {
        if (quantity < 20) {
            quantity++;
            updateView();
        }
    }

    private void updateView() {
        total = quantity * product.getPrice();
        binding.txtQuantity.setText(String.valueOf(quantity));
        binding.txtTotal.setText(String.format("S/ %.2f", total));

        if (alreadyAdded) {
            if (quantity == 0) {
                binding.btnAddItem.setText("Eliminar del carrito");
            } else {
                binding.btnAddItem.setText("Actualizar");
            }
        }
    }

    private void loadData() {
        binding.txtNameProduct.setText(product.getName());
        binding.txtPriceProduct.setText(product.getPriceString());
        binding.txtInformationProduct.setText(product.getInformation());

        if (ShoppingCartActivity.itemsInitialized()) {
            showItemDetail();
        } else {
            ShoppingCartService.getAll().enqueue(new Callback<ShoppingCartCount>() {
                @Override
                public void onResponse(Call<ShoppingCartCount> call, Response<ShoppingCartCount> response) {
                    if (response.isSuccessful()) {
                        final ShoppingCartCount shoppingCartCount = response.body();
                        ShoppingCartActivity.setTotal(shoppingCartCount.getTotal());
                        ShoppingCartActivity.setCount(shoppingCartCount.getCount());
                        ShoppingCartActivity.setshoppingCarts(shoppingCartCount.getSubcarts());

                        showItemDetail();
                    } else {
                        BlitzfudUtils.showError(ItemShoppingCartActivity.this, response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<ShoppingCartCount> call, Throwable t) {
                    BlitzfudUtils.showFailure(ItemShoppingCartActivity.this);
                }
            });
        }
    }

    private void showItemDetail() {
        quantity = ShoppingCartActivity.getQuantity(product.get_id(), MarketActivity.getMarket().get_id());
        initialQuantity = quantity;
        alreadyAdded = (quantity != 0);

        if(quantity==0) quantity = 1;

        total = quantity * product.getPrice();
        binding.txtQuantity.setText(String.valueOf(quantity));
        binding.txtTotal.setText(String.format("S/ %.2f", total));
        binding.pantallaLoading.setVisibility(View.GONE);
        binding.pantallaPrincipal.setVisibility(View.VISIBLE);

        if (alreadyAdded) {
            binding.btnAddItem.setText("Actualizar");
            getSupportActionBar().setTitle("Actualizar");
        }
    }

    private void confirm() {
        if (!alreadyAdded) {
            if (quantity != 0) {
                dialog.show();
                addItem();
            }
        } else {
            if (quantity != 0) {
                if (quantity != initialQuantity) {
                    dialog.show();
                    updateItem();
                }
            } else {
                dialog.show();
                removeItem();
            }
        }
    }

    private void addItem() {
        ShoppingCartService.addItem(product.get_id(), quantity).enqueue(new Callback<ResponseAPI>() {
            @Override
            public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                onResponseBody(response, TYPE_ADD_ITEM);
            }

            @Override
            public void onFailure(Call<ResponseAPI> call, Throwable t) {
                onFailureBody();
            }
        });
    }

    private void updateItem() {
        ShoppingCartService.updateItem(product.get_id(), quantity).enqueue(new Callback<ResponseAPI>() {
            @Override
            public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                onResponseBody(response, TYPE_UPDATE_ITEM);
            }

            @Override
            public void onFailure(Call<ResponseAPI> call, Throwable t) {
                onFailureBody();
            }
        });
    }

    private void removeItem() {
        ShoppingCartService.removeItem(product.get_id()).enqueue(new Callback<ResponseAPI>() {
            @Override
            public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                onResponseBody(response, TYPE_REMOVE_ITEM);
            }

            @Override
            public void onFailure(Call<ResponseAPI> call, Throwable t) {
                onFailureBody();
            }
        });
    }

    private void onResponseBody(Response<ResponseAPI> response, int type) {
        switch (type) {
            case TYPE_ADD_ITEM:
                ShoppingCartActivity.addItem(quantity, product, MarketActivity.getMarket());
                break;
            case TYPE_UPDATE_ITEM:
                ShoppingCartActivity.updateItem(quantity, product, MarketActivity.getMarket());
                break;
            case TYPE_REMOVE_ITEM:
                ShoppingCartActivity.removeItem(product, MarketActivity.getMarket());
        }

        dialog.dismiss();

        if (response.isSuccessful()) {
            Toast toast = Toast.makeText(ItemShoppingCartActivity.this, response.body().getMessage(), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            onBackPressed();
        } else {
            BlitzfudUtils.showError(ItemShoppingCartActivity.this, response.errorBody());
        }
    }

    private void onFailureBody() {
        dialog.dismiss();
        BlitzfudUtils.showFailure(ItemShoppingCartActivity.this);
    }

    public static Product getProduct() {
        return product;
    }

    public static void setProduct(Product product) {
        ItemShoppingCartActivity.product = product;
    }


}
