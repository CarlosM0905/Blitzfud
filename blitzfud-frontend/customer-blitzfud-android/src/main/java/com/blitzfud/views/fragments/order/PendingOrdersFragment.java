package com.blitzfud.views.fragments.order;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blitzfud.views.adapters.PendingOrderAdapter;
import com.blitzfud.databinding.FragmentPendingOrdersBinding;
import com.blitzfud.models.market.Market;
import com.blitzfud.views.pages.order.DetailPendingOrderActivity;

import java.util.ArrayList;

public class PendingOrdersFragment extends Fragment {

    private FragmentPendingOrdersBinding binding;
    private LinearLayoutManager linearLayoutManager;
    private PendingOrderAdapter storeAdapter;
    private ArrayList<Market> stores;


    public PendingOrdersFragment() {
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPendingOrdersBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        populateData();
        loadData();

        return view;
    }

    private void loadData(){
        storeAdapter = new PendingOrderAdapter(getContext(), stores, new PendingOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Market store, int position) {
                final Intent intent = new Intent(getContext(), DetailPendingOrderActivity.class);
                startActivity(intent);
            }
        });

        linearLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setAdapter(storeAdapter);
    }

    private void populateData(){
        stores = new ArrayList<>();

        stores.add(new Market());
        stores.add(new Market());
        stores.add(new Market());
        stores.add(new Market());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
