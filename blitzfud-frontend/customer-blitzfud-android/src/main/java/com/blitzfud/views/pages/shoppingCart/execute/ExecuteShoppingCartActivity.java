package com.blitzfud.views.pages.shoppingCart.execute;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import com.blitzfud.R;
import com.blitzfud.views.adapters.MarketResumeAdapter;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.databinding.ActivityExecuteShoppingCartBinding;
import com.blitzfud.views.pages.order.OrderSuccessfullActivity;
import com.blitzfud.views.pages.shoppingCart.ShoppingCartActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ExecuteShoppingCartActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityExecuteShoppingCartBinding binding;
    private MarketResumeAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExecuteShoppingCartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initConfig();
        loadData();
        bindListeners();
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
            case R.id.btnExecuteOrder:
                clickExecute();
                break;
        }
    }

    private void initConfig() {
        BlitzfudUtils.initToolbar(this, "Pago final", true);
        dialog = BlitzfudUtils.initLoading(this);
    }

    private void loadData() {
        adapter = new MarketResumeAdapter(this, ShoppingCartActivity.getshoppingCarts());
        linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setAdapter(adapter);
        binding.txtTotal.setText(String.format("Total S/%.2f", ShoppingCartActivity.getTotal()));
    }

    private void bindListeners() {
        binding.btnExecuteOrder.setOnClickListener(this);
    }

    private void clickExecute(){
        new SweetAlertDialog(ExecuteShoppingCartActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("¿Estás seguro?")
                .setConfirmText("Sí")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        confirm();
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

    private void confirm() {
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                final Intent intent = new Intent(ExecuteShoppingCartActivity.this, OrderSuccessfullActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }, 700);
    }

}
