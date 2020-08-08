package com.blitzfud.views.fragments.market;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blitzfud.views.adapters.market.FavoriteMarketAdapter;
import com.blitzfud.controllers.restapi.services.FavoriteMarketService;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.databinding.FragmentFavoriteMarketsBinding;
import com.blitzfud.models.market.Market;
import com.blitzfud.models.responseCount.FavoriteMarketCount;
import com.blitzfud.views.pages.market.MarketActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteMarketsFragment extends Fragment {

    public static final int NONE_ACTION = -1;
    public static final int UNSUBSCRIBE_ACTION = 0;
    public static final int SUBSCRIBE_ACTION = 1;
    private static ArrayList<Market> markets;
    private static int count;

    private FragmentFavoriteMarketsBinding binding;
    private FavoriteMarketAdapter adapter;
    private GridLayoutManager layoutManager;

    public FavoriteMarketsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoriteMarketsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        prepareRecycler();
        loadData();

        return view;
    }

    private void prepareRecycler() {
        layoutManager = new GridLayoutManager(getContext(), 2);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(layoutManager);
    }

    private void loadData() {
        if (markets != null) {
            showMarkets();
            return;
        }

        FavoriteMarketService.getAll().enqueue(new Callback<FavoriteMarketCount>() {
            @Override
            public void onResponse(Call<FavoriteMarketCount> call, Response<FavoriteMarketCount> response) {
                if (response.isSuccessful()) {
                    final FavoriteMarketCount favoriteMarketCount = response.body();
                    count = favoriteMarketCount.getCount();
                    markets = favoriteMarketCount.getMarkets();
                    showMarkets();
                } else {
                    BlitzfudUtils.showError(getContext(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<FavoriteMarketCount> call, Throwable t) {
                BlitzfudUtils.showFailure(getContext());
            }
        });
    }

    private void showMarkets() {
        binding.pantallaLoading.setVisibility(View.GONE);

        if (markets.isEmpty()) {
            binding.pantallaEmpty.setVisibility(View.VISIBLE);
        } else {
            adapter = new FavoriteMarketAdapter(getContext(), markets, new FavoriteMarketAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Market market, int position) {
                    MarketActivity.setMarket(market);
                    final Intent intent = new Intent(getContext(), MarketActivity.class);
                    startActivity(intent);
                }
            });

            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (MarketsFragment.getSubscribeAction() == SUBSCRIBE_ACTION) {
            if (adapter == null) {
                adapter = new FavoriteMarketAdapter(getContext(), markets, new FavoriteMarketAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Market market, int position) {
                        MarketActivity.setMarket(market);
                        final Intent intent = new Intent(getContext(), MarketActivity.class);
                        startActivity(intent);
                    }
                });
                binding.recyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }

            if (binding.recyclerView.getVisibility() == View.GONE)
                binding.recyclerView.setVisibility(View.VISIBLE);

            if (binding.pantallaEmpty.getVisibility() == View.VISIBLE)
                binding.pantallaEmpty.setVisibility(View.GONE);

            MarketsFragment.setSubscribeAction(-1);
        } else if (MarketsFragment.getSubscribeAction() == UNSUBSCRIBE_ACTION) {
            if (FavoriteMarketsFragment.getMarkets().isEmpty()) {
                binding.recyclerView.setVisibility(View.GONE);
                binding.pantallaEmpty.setVisibility(View.VISIBLE);
            } else {
                if (binding.recyclerView.getVisibility() == View.GONE) {
                    binding.pantallaEmpty.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                }

                adapter.notifyDataSetChanged();
            }
            MarketsFragment.setSubscribeAction(NONE_ACTION);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static boolean existsMarket(final String marketId) {
        for (int i = 0; i < markets.size(); i++)
            if (markets.get(i).get_id().equals(marketId))
                return true;

        return false;
    }

    public static void addMarket(final Market market) {
        markets.add(market);
        count++;
    }

    public static void removeMarket(final Market market) {
        final int position = findPosition(market);

        if (position != -1) {
            markets.remove(position);
            count--;
        }
    }

    private static int findPosition(final Market market) {
        int position = -1;

        for (int i = 0; i < markets.size(); i++) {
            final Market marketItem = markets.get(i);

            if (marketItem.equals(market)) {
                position = i;
                break;
            }
        }

        return position;
    }

    public static boolean itemsInitialized() {
        return markets != null;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        FavoriteMarketsFragment.count = count;
    }

    public static ArrayList<Market> getMarkets() {
        return markets;
    }

    public static void setMarkets(ArrayList<Market> markets) {
        FavoriteMarketsFragment.markets = markets;
    }

}
