package com.blitzfud.views.pages.order;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.blitzfud.R;
import com.blitzfud.controllers.restapi.services.OrderService;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.databinding.ActivityDetailPendingOrderBinding;
import com.blitzfud.models.order.Order;
import com.blitzfud.views.adapters.order.ItemOrderAdapter;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPendingOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DetailPendingOrderActiv";

    private ActivityDetailPendingOrderBinding binding;
    private String orderId;
    private String createAtDate;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailPendingOrderBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initConfig();
        loadData();
        bindListeners();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBack: onBackPressed(); break;
            default: break;
        }
    }

    private void initConfig() {
        orderId = getIntent().getStringExtra("orderId");
        createAtDate = getIntent().getStringExtra("createdAtDate");
    }

    private void loadData() {
        OrderService.getById(orderId).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    order = response.body();
                    showData();
                } else {
                    try {
                        Log.w(TAG, "onResponse: failed "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    BlitzfudUtils.showErrorWithCatch(DetailPendingOrderActivity.this, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                BlitzfudUtils.showFailure(DetailPendingOrderActivity.this);
            }
        });
    }

    private void showData() {
        binding.pantallaLoading.setVisibility(View.GONE);

        binding.txtInformationMarket.setText(createAtDate+" "+order.getMarket().getName());
        binding.txtStatus.setText(order.getStatus());

        if(order.isInProgress()) binding.imgStatus.setImageResource(R.drawable.ic_in_progress_status);
        else binding.imgStatus.setImageResource(R.drawable.ic_pre_processing_status);

        final ItemOrderAdapter itemOrderAdapter = new ItemOrderAdapter(this, order.getItems());
        binding.recyclerViewItems.setAdapter(itemOrderAdapter);

        if(order.isDeliveryMethod()){
            binding.imgDetail.setImageResource(R.drawable.delivery_default);
            binding.txtDetail.setText("Detalle del delivery");
            binding.txtTitleDetail.setText("Nombre del repartidor");
            binding.txtContentDetail.setText("Tu chamo de preferencia");
        }else{
            binding.imgDetail.setImageResource(R.drawable.market_profile);
            binding.txtDetail.setText("Detalle del recojo");
            binding.txtTitleDetail.setText("Dirección de la tienda");
            binding.txtContentDetail.setText("Avenida Buenavista Calle casimiro");
        }

        binding.txtTotal.setText("Total: "+order.getTotalWithDelivery());

        binding.pantallaPrincipal.setVisibility(View.VISIBLE);
    }

    private void bindListeners() {
        binding.imgBack.setOnClickListener(this);
    }

}
