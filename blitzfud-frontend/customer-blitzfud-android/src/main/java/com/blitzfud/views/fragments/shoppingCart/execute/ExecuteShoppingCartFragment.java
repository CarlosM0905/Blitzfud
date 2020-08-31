package com.blitzfud.views.fragments.shoppingCart.execute;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.blitzfud.R;
import com.blitzfud.controllers.localDB.providers.PurchaseOrdersProvider;
import com.blitzfud.controllers.localDB.providers.ShoppingCartDBProvider;
import com.blitzfud.controllers.restapi.services.PurchaseOrdersService;
import com.blitzfud.controllers.restapi.services.ShoppingCartService;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.databinding.FragmentExecuteShoppingCartBinding;
import com.blitzfud.models.dialog.ConfirmDialog;
import com.blitzfud.models.responseAPI.PurchaseOrdersSet;
import com.blitzfud.models.responseAPI.ResponseAPI;
import com.blitzfud.models.responseAPI.ShoppingCartSet;
import com.blitzfud.views.adapters.market.MarketResumeAdapter;
import com.blitzfud.views.pages.MainActivity;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExecuteShoppingCartFragment extends Fragment implements View.OnClickListener {

    private FragmentExecuteShoppingCartBinding binding;
    private Realm realm;
    private ShoppingCartSet shoppingCartSet;
//    private MarketResumeAdapter adapter;
    private AlertDialog dialog;
    private ConfirmDialog confirmDialog;

    public ExecuteShoppingCartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExecuteShoppingCartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        initConfig();
        loadData();
        bindListeners();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnExecuteOrder:
                clickExecute();
                break;
            case R.id.imgBack:
                closefragment();
                break;
            default: break;
        }
    }

    private void initConfig() {
        realm = Realm.getDefaultInstance();
        dialog = BlitzfudUtils.initLoading(getContext());
        confirmDialog = new ConfirmDialog.Builder(getContext())
                .setTitle("¿Estás seguro?")
                .setContent("Se le avisará a la tienda sobre tu pedido")
                .setOnConfirmAction("Sí", new ConfirmDialog.OnConfirmDialog() {
                    @Override
                    public void onClickListener() {
                        confirm();
                    }
                })
                .build();
    }

    private void loadData() {
        shoppingCartSet = ShoppingCartDBProvider.getShoppingCartSet(realm);
        final MarketResumeAdapter adapter = new MarketResumeAdapter(getContext(), shoppingCartSet.getSubcarts());
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);
        binding.txtTotal.setText("Total: " + shoppingCartSet.getTotalString());
    }

    private void bindListeners() {
        binding.btnExecuteOrder.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);
    }

    private void clickExecute() {
        confirmDialog.show();
    }

    private void confirm() {
        dialog.show();
        PurchaseOrdersService.create(shoppingCartSet.toOrderList()).enqueue(new Callback<ResponseAPI>() {
            @Override
            public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                if (response.isSuccessful()) {
                    clearShoppingCart(response.body().getMessage());
                } else {
                    dialog.dismiss();
                    BlitzfudUtils.showErrorWithCatch(getContext(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseAPI> call, Throwable t) {
                dialog.dismiss();
                BlitzfudUtils.showFailure(getContext());
            }
        });
    }

    private void clearShoppingCart(final String message) {
        ShoppingCartService.clear().enqueue(new Callback<ResponseAPI>() {
            @Override
            public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                if (response.isSuccessful()) {
                    refreshPurchaseOrders(message);
                } else {
                    BlitzfudUtils.showErrorWithCatch(getContext(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseAPI> call, Throwable t) {
                dialog.dismiss();
                BlitzfudUtils.showFailure(getContext());
            }
        });
    }

    public void refreshPurchaseOrders(final String message) {
        PurchaseOrdersService.getActive().enqueue(new Callback<PurchaseOrdersSet>() {
            @Override
            public void onResponse(Call<PurchaseOrdersSet> call, Response<PurchaseOrdersSet> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    PurchaseOrdersProvider.save(realm, response.body());
                    ShoppingCartDBProvider.clear(realm, shoppingCartSet);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    ((MainActivity) getActivity()).closeFragment();
                } else {
                    BlitzfudUtils.showErrorWithCatch(getContext(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PurchaseOrdersSet> call, Throwable t) {
                dialog.dismiss();
                BlitzfudUtils.showFailure(getContext());
            }
        });
    }

    private void closefragment() {
        ((MainActivity) getActivity()).closeFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
        binding = null;
    }
}
