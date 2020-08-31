package com.blitzfud.views.fragments.market;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.blitzfud.controllers.localDB.providers.FavoriteMarketsDBProvider;
import com.blitzfud.controllers.restapi.services.FavoriteMarketService;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.databinding.FragmentFavoriteMarketsBinding;
import com.blitzfud.models.dialog.ConfirmDialog;
import com.blitzfud.models.market.FavoriteMarket;
import com.blitzfud.models.responseAPI.FavoriteMarketSet;
import com.blitzfud.models.responseAPI.ResponseAPI;
import com.blitzfud.views.adapters.market.FavoriteMarketAdapter;
import com.blitzfud.views.pages.MainActivity;
import com.google.gson.JsonSyntaxException;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.blitzfud.controllers.utilities.BlitzfudConstants.FRAGMENT_MARKET;

public class FavoriteMarketsFragment extends Fragment {

    public static boolean loadedFromAPI = false;

    private FragmentFavoriteMarketsBinding binding;
    private Realm realm;
    private RealmChangeListener<RealmModel> listenerFavoritesMarkets;
    private FavoriteMarketSet favoriteMarketSet;
    private FavoriteMarketAdapter adapter;
    private ConfirmDialog confirmDialog;
    private String marketIdSelected;
    private AlertDialog alertDialog;

    public FavoriteMarketsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoriteMarketsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        initConfig();
        initListenerDB();
        prepareRecycler();
        loadData();

        return view;
    }

    private void initConfig() {
        realm = Realm.getDefaultInstance();
        alertDialog = BlitzfudUtils.initLoading(getContext());
        confirmDialog = new ConfirmDialog.Builder(getContext())
                .setTitle("¿Estás seguro")
                .setContent("Dejarás de seguir a esta tienda")
                .setOnConfirmAction("Sí", new ConfirmDialog.OnConfirmDialog() {
                    @Override
                    public void onClickListener() {
                        unsubscribe();
                    }
                })
                .build();
    }

    private void initListenerDB() {
        listenerFavoritesMarkets = new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel realmModel) {
                if (binding.pantallaEmpty.getVisibility() == View.VISIBLE) {
                    binding.pantallaEmpty.setVisibility(View.GONE);
                    binding.pantallaPrincipal.setVisibility(View.VISIBLE);
                }

                if (favoriteMarketSet.getMarkets().isEmpty()) {
                    binding.pantallaPrincipal.setVisibility(View.GONE);
                    binding.pantallaEmpty.setVisibility(View.VISIBLE);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        };
    }

    private void prepareRecycler() {
        binding.recyclerView.setHasFixedSize(true);
    }

    private void loadData() {
        if (!loadedFromAPI) {
            FavoriteMarketService.getAll().enqueue(new Callback<FavoriteMarketSet>() {
                @Override
                public void onResponse(Call<FavoriteMarketSet> call, Response<FavoriteMarketSet> response) {
                    if (response.isSuccessful()) {
                        FavoriteMarketsDBProvider.save(realm, response.body());
                        favoriteMarketSet = FavoriteMarketsDBProvider.getFavoriteMarkets(realm);
                        loadedFromAPI = true;

                        showMarkets();
                    } else {
                        try {
                            BlitzfudUtils.showError(getContext(), response.errorBody());
                        } catch (JsonSyntaxException ex) {
                            loadLocalFavoriteMarkets();
                        }
                    }
                }

                @Override
                public void onFailure(Call<FavoriteMarketSet> call, Throwable t) {
                    loadLocalFavoriteMarkets();
                }
            });
        } else {
            favoriteMarketSet = FavoriteMarketsDBProvider.getFavoriteMarkets(realm);
            showMarkets();
        }
    }

    private void loadLocalFavoriteMarkets() {
        favoriteMarketSet = FavoriteMarketsDBProvider.getFavoriteMarkets(realm);
        if (favoriteMarketSet != null) {
            showMarkets();
            BlitzfudUtils.showFailureSnackbar(favoriteMarketSet.getMarkets()
                    .isEmpty() ? binding.pantallaEmpty : binding.pantallaPrincipal);
        } else {
            BlitzfudUtils.showFailure(getContext());
        }
    }

    private void showMarkets() {
        favoriteMarketSet.addChangeListener(listenerFavoritesMarkets);
        binding.pantallaLoading.setVisibility(View.GONE);

        adapter = new FavoriteMarketAdapter(getContext(), favoriteMarketSet.getMarkets(), new FavoriteMarketAdapter.OnFavoriteMarketClickListener() {
            @Override
            public void onItemClick(FavoriteMarket market) {
                if (!loadedFromAPI) {
                    BlitzfudUtils.showFailureSnackbar(binding.pantallaPrincipal);
                    return;
                }

                ((MainActivity) getActivity()).newFragment(new MarketFragment(market), FRAGMENT_MARKET);
            }

            @Override
            public void onUnsubscribeClick(FavoriteMarket market) {
                if (!loadedFromAPI) {
                    BlitzfudUtils.showFailureSnackbar(binding.pantallaPrincipal);
                    return;
                }

                marketIdSelected = market.get_id();
                confirmDialog.show();
            }
        });
        binding.recyclerView.setAdapter(adapter);

        if (favoriteMarketSet.getMarkets().isEmpty()) {
            binding.pantallaEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.pantallaPrincipal.setVisibility(View.VISIBLE);
        }
    }

    private void unsubscribe() {
        alertDialog.show();
        FavoriteMarketService.remove(marketIdSelected).enqueue(new Callback<ResponseAPI>() {
            @Override
            public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                alertDialog.dismiss();
                if (response.isSuccessful()) {
                    FavoriteMarketsDBProvider.remove(realm, favoriteMarketSet, new FavoriteMarket(marketIdSelected));
                    BlitzfudUtils.showSnackbar(binding.pantallaPrincipal, response.body().getMessage());
                } else {
                    try {
                        BlitzfudUtils.showError(getContext(), response.errorBody());
                    } catch (JsonSyntaxException ex) {
                        BlitzfudUtils.showFailure(getContext());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseAPI> call, Throwable t) {
                alertDialog.dismiss();
                BlitzfudUtils.showFailure(getContext());
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (favoriteMarketSet != null)
            favoriteMarketSet.removeChangeListener(listenerFavoritesMarkets);
        realm.close();
        binding = null;
    }

}
