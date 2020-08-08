package com.blitzfud.views.fragments.market;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.blitzfud.views.adapters.market.FavoriteMarketReviewAdapter;
import com.blitzfud.views.adapters.market.MarketAdapter;
import com.blitzfud.controllers.restapi.services.FavoriteMarketService;
import com.blitzfud.controllers.restapi.services.MarketService;
import com.blitzfud.controllers.restapi.services.SearchService;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.controllers.utilities.MyPreference;
import com.blitzfud.databinding.FragmentMarketsBinding;
import com.blitzfud.models.market.Market;
import com.blitzfud.models.responseCount.FavoriteMarketCount;
import com.blitzfud.models.responseCount.MarketCount;
import com.blitzfud.views.pages.MainActivity;
import com.blitzfud.views.pages.market.MarketActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketsFragment extends Fragment implements TextView.OnEditorActionListener, View.OnClickListener {

    private static int subscribeAction = -1;

    private FragmentMarketsBinding binding;
    private LinearLayoutManager layoutManager;
    private MarketAdapter marketAdapter;
    private FavoriteMarketReviewAdapter favoriteMarketReviewAdapter;
    private int count;
    private ArrayList<Market> markets;
    private boolean search;

    public MarketsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMarketsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        prepareRecycler();
        loadData(false);
        bindListeners();

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchProducts();
            InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            return true;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        final MenuItem menuItem = ((MainActivity) getActivity()).getNavigationView().getMenu().getItem(0).getSubMenu().getItem(1);
        ((MainActivity) getActivity()).changeFragment(new FavoriteMarketsFragment(), menuItem);
    }

    private void prepareRecycler() {
        markets = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerSubscribedMarkets.setHasFixedSize(true);
        binding.recyclerSubscribedMarkets.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
    }

    private void loadData(final boolean clearSearch) {
        MarketService.getAll(MyPreference.positionLatLng).enqueue(new Callback<MarketCount>() {
            @Override
            public void onResponse(Call<MarketCount> call, Response<MarketCount> response) {
                if (response.isSuccessful()) {
                    final MarketCount marketCount = response.body();

                    markets.clear();
                    markets.addAll(marketCount.getMarkets());
                    count = marketCount.getCount();

                    if (clearSearch) {
                        updateSearchView();
                    } else {
                        showMarkets();
                    }
                } else {
                    BlitzfudUtils.showError(getContext(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<MarketCount> call, Throwable t) {
                BlitzfudUtils.showFailure(getContext());
            }
        });

        if (FavoriteMarketsFragment.getMarkets() == null) {
            FavoriteMarketService.getAll().enqueue(new Callback<FavoriteMarketCount>() {
                @Override
                public void onResponse(Call<FavoriteMarketCount> call, Response<FavoriteMarketCount> response) {
                    if (response.isSuccessful()) {
                        final FavoriteMarketCount favoriteMarketCount = response.body();

                        FavoriteMarketsFragment.setCount(favoriteMarketCount.getCount());
                        FavoriteMarketsFragment.setMarkets(favoriteMarketCount.getMarkets());

                        showFavoriteMarkets();
                    } else {
                        BlitzfudUtils.showError(getContext(), response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<FavoriteMarketCount> call, Throwable t) {
                    BlitzfudUtils.showFailure(getContext());
                }
            });
        } else {
            showFavoriteMarkets();
        }
    }

    private void showMarkets() {
        binding.loadingMarkets.setVisibility(View.GONE);

        if (markets.isEmpty()) {
            binding.emptyMarkets.setVisibility(View.VISIBLE);
        } else {
            marketAdapter = new MarketAdapter(getContext(), markets);

            binding.recyclerView.setAdapter(marketAdapter);
            binding.pantallaPrincipal.setVisibility(View.VISIBLE);
        }
    }

    private void showFavoriteMarkets() {
        if (FavoriteMarketsFragment.getMarkets().isEmpty()) return;

        favoriteMarketReviewAdapter = new FavoriteMarketReviewAdapter(getContext(), FavoriteMarketsFragment.getMarkets(), new FavoriteMarketReviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Market market, int position) {
                MarketActivity.setMarket(market);
                final Intent intent = new Intent(getContext(), MarketActivity.class);
                startActivity(intent);
            }
        });
        binding.recyclerSubscribedMarkets.setAdapter(favoriteMarketReviewAdapter);
        binding.layoutSubscribe.setVisibility(View.VISIBLE);
    }

    private void bindListeners() {
        binding.txtKeyword.setOnEditorActionListener(this);
        binding.lblViewAllFavoriteMarkets.setOnClickListener(this);
    }

    private void searchProducts() {
        final String keyword = binding.txtKeyword.getText().toString();

        if (keyword.trim().isEmpty()) {
            if (search) {
                binding.searchLoading.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
                binding.seachEmpty.setVisibility(View.GONE);
                loadData(true);
                return;
            }
        }

        binding.searchLoading.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
        binding.seachEmpty.setVisibility(View.GONE);

        SearchService.getProducts(MyPreference.positionLatLng, binding.txtKeyword.getText().toString()).enqueue(new Callback<MarketCount>() {
            @Override
            public void onResponse(Call<MarketCount> call, Response<MarketCount> response) {
                if (response.isSuccessful()) {
                    final MarketCount marketCount = response.body();

                    final ArrayList<Market> marketsFound = marketCount.getMarkets();
                    markets.clear();
                    markets.addAll(marketsFound);
                    count = marketCount.getCount();
                    search = true;

                    updateSearchView();
                } else {
                    BlitzfudUtils.showError(getContext(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<MarketCount> call, Throwable t) {
                BlitzfudUtils.showFailure(getContext());
            }
        });
    }

    private void updateSearchView() {
        binding.searchLoading.setVisibility(View.GONE);

        if (markets.isEmpty()) {
            binding.seachEmpty.setVisibility(View.VISIBLE);
        } else {
            marketAdapter.notifyDataSetChanged();
            binding.recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (subscribeAction == FavoriteMarketsFragment.SUBSCRIBE_ACTION) {
            if (favoriteMarketReviewAdapter == null) {
                favoriteMarketReviewAdapter = new FavoriteMarketReviewAdapter(getContext(), FavoriteMarketsFragment.getMarkets(), new FavoriteMarketReviewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Market market, int position) {
                        MarketActivity.setMarket(market);
                        final Intent intent = new Intent(getContext(), MarketActivity.class);
                        startActivity(intent);
                    }
                });
                binding.recyclerSubscribedMarkets.setAdapter(favoriteMarketReviewAdapter);
            } else {
                favoriteMarketReviewAdapter.notifyDataSetChanged();
            }

            if (binding.layoutSubscribe.getVisibility() == View.GONE)
                binding.layoutSubscribe.setVisibility(View.VISIBLE);

            subscribeAction = FavoriteMarketsFragment.NONE_ACTION;
        } else if (subscribeAction == FavoriteMarketsFragment.UNSUBSCRIBE_ACTION) {
            if (FavoriteMarketsFragment.getMarkets().isEmpty()) {
                binding.layoutSubscribe.setVisibility(View.GONE);
            } else {
                if (binding.layoutSubscribe.getVisibility() == View.GONE)
                    binding.layoutSubscribe.setVisibility(View.VISIBLE);

                favoriteMarketReviewAdapter.notifyDataSetChanged();
            }
            subscribeAction = FavoriteMarketsFragment.NONE_ACTION;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static int getSubscribeAction() {
        return MarketsFragment.subscribeAction;
    }

    public static void setSubscribeAction(int subscribeAction) {
        MarketsFragment.subscribeAction = subscribeAction;
    }

}
