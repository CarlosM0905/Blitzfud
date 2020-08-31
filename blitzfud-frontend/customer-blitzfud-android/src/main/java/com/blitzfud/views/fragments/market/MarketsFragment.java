package com.blitzfud.views.fragments.market;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blitzfud.R;
import com.blitzfud.controllers.localDB.providers.MarketsDBProvider;
import com.blitzfud.controllers.localDB.providers.ShoppingCartDBProvider;
import com.blitzfud.controllers.restapi.services.AuthService;
import com.blitzfud.controllers.restapi.services.MarketService;
import com.blitzfud.controllers.restapi.services.SearchService;
import com.blitzfud.controllers.restapi.services.ShoppingCartService;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.databinding.FragmentMarketsBinding;
import com.blitzfud.models.dialog.ItemShoppingCartDialog;
import com.blitzfud.models.market.Market;
import com.blitzfud.models.market.Product;
import com.blitzfud.models.responseAPI.MarketSet;
import com.blitzfud.models.responseAPI.ShoppingCartSet;
import com.blitzfud.views.adapters.market.MarketAdapter;
import com.blitzfud.views.fragments.shoppingCart.ShoppingCartFragment;
import com.blitzfud.views.pages.MainActivity;
import com.blitzfud.views.pages.map.MapActivity;
import com.google.gson.JsonSyntaxException;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.blitzfud.controllers.utilities.BlitzfudConstants.FRAGMENT_MARKET;
import static com.blitzfud.controllers.utilities.BlitzfudConstants.MAP_ACTIVITY_RESULT;

public class MarketsFragment extends Fragment implements TextView.OnEditorActionListener,
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static boolean loadedFromAPI = false;

    private FragmentMarketsBinding binding;
    private Realm realm;
    private RealmChangeListener<RealmModel> listenerShoppingCart;
    private MarketSet marketSet;
    private ShoppingCartSet shoppingCartSet;
    private MarketAdapter marketAdapter;
    private boolean search;
    private ItemShoppingCartDialog itemShoppingCartDialog;
    private ItemShoppingCartDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    public MarketsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMarketsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        initConfig();
        initListenerDB();
        loadData();
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
        switch (v.getId()) {
            case R.id.txtMyPosition:
            case R.id.txtMyPosition2:
                openMapActivity();
                break;
            default : break;
        }
    }

    @Override
    public void onRefresh() {
        loadedFromAPI = false;
        reloadMarkets(false);
        binding.swipeLayout.setRefreshing(false);
        binding.swipeLayout2.setRefreshing(false);
    }

    private void initConfig() {
        realm = Realm.getDefaultInstance();
        dialog = BlitzfudUtils.initLoading(getContext());
        dialogBuilder = new ItemShoppingCartDialog.Builder(getContext(), new ItemShoppingCartDialog.OnConfirmClickListener() {
            @Override
            public void onAddItem(int quantity, Product product, Market market) {
                if (!loadedFromAPI) {
                    BlitzfudUtils.showFailureSnackbar(binding.pantallaPrincipal);
                    return;
                }

                itemShoppingCartDialog.addItem(realm, binding.pantallaPrincipal, dialog,
                        quantity, product, market);
            }

            @Override
            public void onUpdateItem(int quantity, Product product, Market market) {
                if (!loadedFromAPI) {
                    BlitzfudUtils.showFailureSnackbar(binding.pantallaPrincipal);
                    return;
                }

                itemShoppingCartDialog.updateItem(realm, binding.pantallaPrincipal, dialog,
                        quantity, product, market);
            }

            @Override
            public void onRemoveItem(Product product, Market market) {
                if (!loadedFromAPI) {
                    BlitzfudUtils.showFailureSnackbar(binding.pantallaPrincipal);
                    return;
                }

                itemShoppingCartDialog.removeItem(realm, binding.pantallaPrincipal, dialog,
                        product, market);
            }
        });
        prepareRecycler();
    }

    private void initListenerDB() {
        listenerShoppingCart = new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel realmModel) {

            }
        };
    }

    private void prepareRecycler() {
        binding.recyclerView.setHasFixedSize(true);
    }

    private void loadData() {
        binding.txtMyPosition.setText(AuthService.getUser().getLocation().getAddress());
        binding.txtMyPosition2.setText(AuthService.getUser().getLocation().getAddress());

        if (!ShoppingCartFragment.loadedFromAPI) {
            ShoppingCartService.getAll().enqueue(new Callback<ShoppingCartSet>() {
                @Override
                public void onResponse(Call<ShoppingCartSet> call, Response<ShoppingCartSet> response) {
                    if (response.isSuccessful()) {
                        ShoppingCartDBProvider.save(realm, response.body());
                        shoppingCartSet = ShoppingCartDBProvider.getShoppingCartSet(realm);
                        ShoppingCartFragment.loadedFromAPI = true;

                        if (loadedFromAPI) showMarkets();
                    } else {
                        try {
                            BlitzfudUtils.showError(getContext(), response.errorBody());
                        } catch (JsonSyntaxException ex) {
                            loadLocalShoppingCart();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ShoppingCartSet> call, Throwable t) {
                    loadLocalShoppingCart();
                }
            });
        } else {
            shoppingCartSet = ShoppingCartDBProvider.getShoppingCartSet(realm);
        }

        if (!loadedFromAPI) {
            MarketService.getAll(AuthService.getUser().getLocationLatLng()).enqueue(new Callback<MarketSet>() {
                @Override
                public void onResponse(Call<MarketSet> call, Response<MarketSet> response) {
                    if (response.isSuccessful()) {
                        MarketsDBProvider.save(realm, response.body());
                        marketSet = MarketsDBProvider.getMarketSet(realm);
                        loadedFromAPI = true;

                        if (ShoppingCartFragment.loadedFromAPI) showMarkets();
                    } else {
                        try {
                            BlitzfudUtils.showError(getContext(), response.errorBody());
                        } catch (JsonSyntaxException ex) {
                            loadLocalMarket();
                        }
                    }
                }

                @Override
                public void onFailure(Call<MarketSet> call, Throwable t) {
                    loadLocalMarket();
                }
            });
        } else {
            marketSet = MarketsDBProvider.getMarketSet(realm);
            showMarkets();
        }
    }

    private void loadLocalShoppingCart() {
        shoppingCartSet = ShoppingCartDBProvider.getShoppingCartSet(realm);
        if (marketSet != null && shoppingCartSet != null) {
            showMarkets();
            BlitzfudUtils.showFailureSnackbar(marketSet.getMarkets().isEmpty() ? binding.pantallaEmpty : binding.pantallaPrincipal);
        }
    }

    private void loadLocalMarket() {
        marketSet = MarketsDBProvider.getMarketSet(realm);
        if (marketSet != null && shoppingCartSet != null) {
            showMarkets();
            BlitzfudUtils.showFailureSnackbar(marketSet.getMarkets().isEmpty() ? binding.pantallaEmpty : binding.pantallaPrincipal);
        } else {
            BlitzfudUtils.showFailure(getContext());
        }
    }

    public void reloadMarkets(final boolean newPosition) {
        final boolean pantallaEmpty = binding.pantallaEmpty.getVisibility() == View.VISIBLE;
        final boolean searchEmpty = binding.searchEmpty.getVisibility() == View.VISIBLE;

        if (newPosition) {
            binding.txtMyPosition.setText(AuthService.getUser().getLocation().getAddress());
            binding.txtMyPosition2.setText(AuthService.getUser().getLocation().getAddress());
        }

        if (pantallaEmpty) {
            binding.pantallaEmpty.setVisibility(View.GONE);
            binding.pantallaLoading.setVisibility(View.VISIBLE);
        } else {
            if (searchEmpty) {
                binding.searchEmpty.setVisibility(View.GONE);
            } else {
                binding.recyclerView.setVisibility(View.GONE);
            }
            binding.searchLoading.setVisibility(View.VISIBLE);
        }

        MarketService.getAll(AuthService.getUser().getLocationLatLng()).enqueue(new Callback<MarketSet>() {
            @Override
            public void onResponse(Call<MarketSet> call, Response<MarketSet> response) {
                if (response.isSuccessful()) {
                    MarketsDBProvider.update(realm, marketSet, response.body());
                    loadedFromAPI = true;
                    binding.pantallaLoading.setVisibility(View.GONE);

                    if (marketSet.getMarkets().isEmpty()) {
                        binding.pantallaEmpty.setVisibility(View.VISIBLE);
                        binding.pantallaPrincipal.setVisibility(View.GONE);
                    } else {
                        marketAdapter.notifyDataSetChanged();

                        binding.searchLoading.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);

                        if (pantallaEmpty) {
                            binding.pantallaPrincipal.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    BlitzfudUtils.showError(getContext(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<MarketSet> call, Throwable t) {
                if (pantallaEmpty) {
                    binding.pantallaEmpty.setVisibility(View.VISIBLE);
                    binding.pantallaLoading.setVisibility(View.GONE);
                    BlitzfudUtils.showFailureSnackbar(binding.pantallaEmpty);
                } else {
                    if (searchEmpty) {
                        binding.searchEmpty.setVisibility(View.VISIBLE);
                    } else {
                        binding.recyclerView.setVisibility(View.VISIBLE);
                    }
                    binding.searchLoading.setVisibility(View.GONE);
                    BlitzfudUtils.showFailureSnackbar(binding.pantallaPrincipal);
                }
            }
        });
    }

    private void showMarkets() {
        shoppingCartSet.addChangeListener(listenerShoppingCart);
        binding.pantallaLoading.setVisibility(View.GONE);

        marketAdapter = new MarketAdapter(getContext(), marketSet.getMarkets(), new MarketAdapter.OnMarketAdapterListener() {
            @Override
            public void onMoreClickListener(Market market) {
                if (!loadedFromAPI) {
                    BlitzfudUtils.showFailureSnackbar(binding.pantallaPrincipal);
                    return;
                }

                ((MainActivity) getActivity()).newFragment(new MarketFragment(market), FRAGMENT_MARKET);
            }

            @Override
            public void onProductClickListener(Market market, Product product) {
                showDialog(market, product);
            }
        });
        binding.recyclerView.setAdapter(marketAdapter);

        if (marketSet.getMarkets().isEmpty()) {
            binding.pantallaEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.pantallaPrincipal.setVisibility(View.VISIBLE);
        }

    }

    private void showDialog(final Market market, final Product product) {
        itemShoppingCartDialog = dialogBuilder.setShoppingCartSet(shoppingCartSet)
                .setMarket(market)
                .setProduct(product)
                .build();
    }

    private void bindListeners() {
        binding.txtKeyword.setOnEditorActionListener(this);
        binding.swipeLayout.setOnRefreshListener(this);
        binding.swipeLayout2.setOnRefreshListener(this);
        binding.txtMyPosition.setOnClickListener(this);
        binding.txtMyPosition2.setOnClickListener(this);
    }

    private void openMapActivity() {
        if (!loadedFromAPI) {
            BlitzfudUtils.showFailureSnackbar(shoppingCartSet.getSubcarts().isEmpty() ? binding.pantallaEmpty : binding.pantallaPrincipal);
            return;
        }

        Intent i = new Intent(getContext(), MapActivity.class);
        getActivity().startActivityForResult(i, MAP_ACTIVITY_RESULT);
    }

    private void searchProducts() {
        final String keyword = binding.txtKeyword.getText().toString();

        if (keyword.trim().isEmpty()) {
            if (search) {
                search = false;
                resetMarkets();
                return;
            } else {
                return;
            }
        }

        binding.searchLoading.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
        binding.searchEmpty.setVisibility(View.GONE);

        SearchService.getProducts(AuthService.getUser().getLocationLatLng(), binding.txtKeyword.getText().toString()).enqueue(new Callback<MarketSet>() {
            @Override
            public void onResponse(Call<MarketSet> call, Response<MarketSet> response) {
                if (response.isSuccessful()) {
                    MarketsDBProvider.update(realm, marketSet, response.body());
                    search = true;
                    updateSearchView();
                } else {
                    BlitzfudUtils.showError(getContext(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<MarketSet> call, Throwable t) {
                binding.searchLoading.setVisibility(View.GONE);

                if (marketSet.getMarkets().isEmpty()) {
                    binding.searchEmpty.setVisibility(View.VISIBLE);
                } else {
                    binding.recyclerView.setVisibility(View.VISIBLE);
                }

                BlitzfudUtils.showFailureSnackbar(binding.pantallaPrincipal);
            }
        });
    }

    private void resetMarkets() {
        binding.searchLoading.setVisibility(View.VISIBLE);
        binding.searchEmpty.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.GONE);

        MarketService.getAll(AuthService.getUser().getLocationLatLng()).enqueue(new Callback<MarketSet>() {
            @Override
            public void onResponse(Call<MarketSet> call, Response<MarketSet> response) {
                if (response.isSuccessful()) {
                    MarketsDBProvider.update(realm, marketSet, response.body());
                    marketAdapter.notifyDataSetChanged();

                    binding.searchLoading.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                } else {
                    BlitzfudUtils.showError(getContext(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<MarketSet> call, Throwable t) {
                binding.searchLoading.setVisibility(View.GONE);

                if (marketSet.getMarkets().isEmpty()) {
                    binding.searchEmpty.setVisibility(View.VISIBLE);
                } else {
                    binding.recyclerView.setVisibility(View.VISIBLE);
                }

                BlitzfudUtils.showFailureSnackbar(binding.pantallaPrincipal);
            }
        });
    }

    private void updateSearchView() {
        binding.searchLoading.setVisibility(View.GONE);

        if (marketSet.getMarkets().isEmpty()) {
            binding.searchEmpty.setVisibility(View.VISIBLE);
        } else {
            marketAdapter.notifyDataSetChanged();
            binding.recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (shoppingCartSet != null)
            shoppingCartSet.removeChangeListener(listenerShoppingCart);

        realm.close();
        binding = null;
    }

}
