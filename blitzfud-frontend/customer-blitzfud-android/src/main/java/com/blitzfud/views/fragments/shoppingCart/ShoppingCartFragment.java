package com.blitzfud.views.fragments.shoppingCart;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.blitzfud.R;
import com.blitzfud.controllers.localDB.providers.ShoppingCartDBProvider;
import com.blitzfud.controllers.restapi.services.ShoppingCartService;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.databinding.FragmentShoppingCartBinding;
import com.blitzfud.models.dialog.ConfirmDialog;
import com.blitzfud.models.dialog.ItemShoppingCartDialog;
import com.blitzfud.models.market.Market;
import com.blitzfud.models.market.Product;
import com.blitzfud.models.responseAPI.ResponseAPI;
import com.blitzfud.models.responseAPI.ShoppingCartSet;
import com.blitzfud.views.adapters.shoppingCart.MarketShoppingCartAdapter;
import com.blitzfud.views.fragments.shoppingCart.execute.ExecuteMarketFragment;
import com.blitzfud.views.fragments.shoppingCart.execute.ExecuteShoppingCartFragment;
import com.blitzfud.views.pages.MainActivity;
import com.google.gson.JsonSyntaxException;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.blitzfud.controllers.utilities.BlitzfudConstants.FRAGMENT_EXECUTE_MARKET;
import static com.blitzfud.controllers.utilities.BlitzfudConstants.FRAGMENT_EXECUTE_SHOPPING_CART;

public class ShoppingCartFragment extends Fragment {

    public static boolean loadedFromAPI = false;

    private Realm realm;
    private RealmChangeListener<RealmModel> listenerDB;
    private ShoppingCartSet shoppingCartSet;
    private ItemShoppingCartDialog itemShoppingCartDialog;
    private ItemShoppingCartDialog.Builder dialogBuilder;
    private ConfirmDialog confirmDialog;
    private FragmentShoppingCartBinding binding;
    private MarketShoppingCartAdapter marketShoppingCartAdapter;
    private AlertDialog dialog;

    public ShoppingCartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShoppingCartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        initConfig();
        initListenerDB();
        loadData();
        bindListener();

        return view;
    }

    private void initConfig() {
        realm = Realm.getDefaultInstance();
        dialog = BlitzfudUtils.initLoading(getContext());
        dialogBuilder = new ItemShoppingCartDialog.Builder(getContext(), new ItemShoppingCartDialog.OnConfirmClickListener() {
            @Override
            public void onAddItem(int quantity, Product product, Market market) {

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

        confirmDialog = new ConfirmDialog.Builder(getContext())
                .setImage(R.drawable.ic_delete)
                .setTitle("¿Estás seguro?")
                .setContent("No podrás revertir la acción")
                .setOnConfirmAction("Sí", new ConfirmDialog.OnConfirmDialog() {
                    @Override
                    public void onClickListener() {
                        clearShoppingCart();
                    }
                }).build();
    }

    private void initListenerDB() {
        listenerDB = new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel realmModel) {
                if (binding.pantallaEmpty.getVisibility() == View.VISIBLE) {
                    binding.pantallaEmpty.setVisibility(View.GONE);
                    binding.pantallaPrincipal.setVisibility(View.VISIBLE);
                }

                if (shoppingCartSet.getSubcarts().isEmpty()) {
                    binding.pantallaPrincipal.setVisibility(View.GONE);
                    binding.pantallaEmpty.setVisibility(View.VISIBLE);
                } else {
                    marketShoppingCartAdapter.notifyDataSetChanged();
                    binding.txtTotal.setText(String.format("Total: S/ %.2f", shoppingCartSet.getTotal()));
                }
            }
        };
    }

    private void loadData() {
        if (!loadedFromAPI) {
            ShoppingCartService.getAll().enqueue(new Callback<ShoppingCartSet>() {
                @Override
                public void onResponse(Call<ShoppingCartSet> call, Response<ShoppingCartSet> response) {
                    if (response.isSuccessful()) {
                        ShoppingCartDBProvider.save(realm, response.body());
                        shoppingCartSet = ShoppingCartDBProvider.getShoppingCartSet(realm);
                        loadedFromAPI = true;
                        showItems();
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
            showItems();
        }
    }

    private void loadLocalShoppingCart() {
        shoppingCartSet = ShoppingCartDBProvider.getShoppingCartSet(realm);
        if (shoppingCartSet != null) {
            showItems();
            BlitzfudUtils.showFailureSnackbar(shoppingCartSet.getSubcarts().isEmpty() ? binding.pantallaEmpty : binding.pantallaPrincipal);
        } else {
            BlitzfudUtils.showFailure(getContext());
        }
    }

    private void showDialog(final Market market, final Product product) {
        itemShoppingCartDialog = dialogBuilder.setShoppingCartSet(shoppingCartSet)
                .setMarket(market)
                .setProduct(product)
                .build();
    }

    private void showItems() {
        shoppingCartSet.addChangeListener(listenerDB);
        binding.pantallaLoading.setVisibility(View.GONE);
        marketShoppingCartAdapter = new MarketShoppingCartAdapter(getContext(),
                shoppingCartSet.getSubcarts(), new MarketShoppingCartAdapter.OnMarketShoppingListener() {
            @Override
            public void onConfirmMarket(String marketId) {
                if (!loadedFromAPI) {
                    BlitzfudUtils.showFailureSnackbar(binding.pantallaPrincipal);
                    return;
                }

                ((MainActivity) getActivity()).newFragment(new ExecuteMarketFragment(marketId), FRAGMENT_EXECUTE_MARKET);
            }

            @Override
            public void onItemClick(Market market, Product product) {
                showDialog(market, product);
            }

            @Override
            public void onItemDeleted(Product product, Market market, String message) {
                ShoppingCartDBProvider.removeItem(realm, shoppingCartSet, product, market);

                BlitzfudUtils.showSnackbar(shoppingCartSet.getSubcarts().isEmpty() ? binding.pantallaEmpty : binding.pantallaPrincipal, message);
            }

            @Override
            public void onCheckedDeleted(String marketId, boolean isChecked) {
                ShoppingCartDBProvider.updateDelivery(realm, shoppingCartSet, marketId, isChecked);
            }

            @Override
            public boolean isLoadedFromAPI() {
                return loadedFromAPI;
            }
        });
        binding.recyclerView.setAdapter(marketShoppingCartAdapter);

        if (shoppingCartSet.getSubcarts().isEmpty()) {
            binding.pantallaEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.txtTotal.setText("Total: " + shoppingCartSet.getTotalString());
            binding.pantallaPrincipal.setVisibility(View.VISIBLE);
        }
    }

    private void updateView() {
        if (shoppingCartSet.getSubcarts().isEmpty()) {
            binding.pantallaPrincipal.setVisibility(View.GONE);
            binding.pantallaEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.pantallaPrincipal.setVisibility(View.VISIBLE);
            binding.pantallaEmpty.setVisibility(View.GONE);
        }
    }

    private void bindListener() {
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
        binding.imgClearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickClearShoppingCart();
            }
        });
    }

    private void confirm() {
        ((MainActivity) getActivity()).newFragment(new ExecuteShoppingCartFragment(), FRAGMENT_EXECUTE_SHOPPING_CART);
    }

    private void clickClearShoppingCart() {
        if (!loadedFromAPI) {
            BlitzfudUtils.showFailureSnackbar(binding.pantallaPrincipal);
            return;
        }

        confirmDialog.show();
    }

    private void clearShoppingCart() {
        dialog.show();
        ShoppingCartService.clear().enqueue(new Callback<ResponseAPI>() {
            @Override
            public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    ShoppingCartDBProvider.clear(realm, shoppingCartSet);
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    updateView();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (shoppingCartSet != null)
            shoppingCartSet.removeChangeListener(listenerDB);

        realm.close();
        binding = null;
    }

}
