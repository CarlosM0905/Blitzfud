package com.blitzfud.views.fragments.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blitzfud.controllers.localDB.providers.PurchaseOrdersProvider;
import com.blitzfud.controllers.restapi.services.PurchaseOrdersService;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.databinding.FragmentOrdersBinding;
import com.blitzfud.models.order.Order;
import com.blitzfud.models.responseAPI.PurchaseOrdersSet;
import com.blitzfud.views.adapters.order.OrderAdapter;
import com.blitzfud.views.adapters.order.PurchaseOrdersAdapter;
import com.blitzfud.views.pages.order.DetailPendingOrderActivity;
import com.google.gson.JsonSyntaxException;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrdersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public static boolean loadedFromAPI = false;

    private FragmentOrdersBinding binding;
    private Realm realm;
    private PurchaseOrdersSet purchaseOrdersSet;
    private RealmChangeListener<RealmModel> listenerPurcharseOrders;
    private PurchaseOrdersAdapter adapter;

    public OrdersFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        initConfig();
        initListenerDB();
        loadData();
        bindListeners();

        return view;
    }

    @Override
    public void onRefresh() {
        loadedFromAPI = false;
        reloadOrders();
        binding.swipeLayout.setRefreshing(false);
        binding.swipeLayout2.setRefreshing(false);
    }

    private void initConfig() {
        realm = Realm.getDefaultInstance();
    }

    private void initListenerDB() {
        listenerPurcharseOrders = new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel realmModel) {
                if (binding.pantallaEmpty.getVisibility() == View.VISIBLE) {
                    binding.pantallaEmpty.setVisibility(View.GONE);
                    binding.pantallaPrincipal.setVisibility(View.VISIBLE);
                }

                if (purchaseOrdersSet.getPurchaseOrders().isEmpty()) {
                    binding.pantallaPrincipal.setVisibility(View.GONE);
                    binding.pantallaEmpty.setVisibility(View.VISIBLE);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        };
    }

    private void loadData() {
        if (!loadedFromAPI) {
            PurchaseOrdersService.getActive().enqueue(new Callback<PurchaseOrdersSet>() {
                @Override
                public void onResponse(Call<PurchaseOrdersSet> call, Response<PurchaseOrdersSet> response) {
                    if (response.isSuccessful()) {
                        PurchaseOrdersProvider.save(realm, response.body());
                        purchaseOrdersSet = PurchaseOrdersProvider.getPurchaseOrders(realm);
                        loadedFromAPI = true;

                        showOrders();
                    } else {
                        try {
                            BlitzfudUtils.showError(getContext(), response.errorBody());
                        } catch (JsonSyntaxException ex) {
                            loadLocalPurchaseOrders();
                        }
                    }
                }

                @Override
                public void onFailure(Call<PurchaseOrdersSet> call, Throwable t) {
                    loadLocalPurchaseOrders();
                }
            });
        } else {
            purchaseOrdersSet = PurchaseOrdersProvider.getPurchaseOrders(realm);
            showOrders();
        }
    }

    private void loadLocalPurchaseOrders() {
        purchaseOrdersSet = PurchaseOrdersProvider.getPurchaseOrders(realm);
        if (purchaseOrdersSet != null) {
            showOrders();
            BlitzfudUtils.showFailureSnackbar(purchaseOrdersSet.getPurchaseOrders().isEmpty() ?
                    binding.pantallaEmpty : binding.pantallaPrincipal);
        } else {
            BlitzfudUtils.showFailure(getContext());
        }
    }

    private void showOrders() {
        purchaseOrdersSet.addChangeListener(listenerPurcharseOrders);
        binding.pantallaLoading.setVisibility(View.GONE);

        adapter = new PurchaseOrdersAdapter(getContext(), purchaseOrdersSet.getPurchaseOrders(), new OrderAdapter.OnOrderClickListener() {
            @Override
            public void onItemClick(String createdAtDate, Order order, int position) {
                final Intent intent = new Intent(getContext(), DetailPendingOrderActivity.class);
                intent.putExtra("orderId", order.get_id());
                intent.putExtra("createdAtDate", createdAtDate);
                getActivity().startActivity(intent);
            }
        });
        binding.recyclerView.setAdapter(adapter);

        if (purchaseOrdersSet.getPurchaseOrders().isEmpty()) {
            binding.pantallaEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.pantallaPrincipal.setVisibility(View.VISIBLE);
        }
    }

    private void reloadOrders() {
        binding.pantallaLoading.setVisibility(View.VISIBLE);
        binding.pantallaPrincipal.setVisibility(View.GONE);
        binding.pantallaEmpty.setVisibility(View.GONE);

        PurchaseOrdersService.getActive().enqueue(new Callback<PurchaseOrdersSet>() {
            @Override
            public void onResponse(Call<PurchaseOrdersSet> call, Response<PurchaseOrdersSet> response) {
                if (response.isSuccessful()) {
                    PurchaseOrdersProvider.save(realm, response.body());
                    loadedFromAPI = true;
                    adapter.notifyDataSetChanged();

                    binding.pantallaLoading.setVisibility(View.GONE);
                    if (purchaseOrdersSet.getPurchaseOrders().isEmpty()) {
                        binding.pantallaEmpty.setVisibility(View.VISIBLE);
                    } else {
                        binding.pantallaPrincipal.setVisibility(View.VISIBLE);
                    }
                } else {
                    try {
                        BlitzfudUtils.showError(getContext(), response.errorBody());
                    } catch (JsonSyntaxException ex) {
                        loadLocalPurchaseOrders();
                    }
                }
            }

            @Override
            public void onFailure(Call<PurchaseOrdersSet> call, Throwable t) {
                loadLocalPurchaseOrders();
            }
        });
    }

    private void bindListeners(){
        binding.swipeLayout.setOnRefreshListener(this);
        binding.swipeLayout2.setOnRefreshListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (purchaseOrdersSet != null)
            purchaseOrdersSet.removeChangeListener(listenerPurcharseOrders);

        realm.close();
        binding = null;
    }

}
