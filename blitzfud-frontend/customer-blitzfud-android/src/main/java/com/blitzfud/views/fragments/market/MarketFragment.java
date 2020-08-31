package com.blitzfud.views.fragments.market;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.blitzfud.R;
import com.blitzfud.controllers.localDB.providers.FavoriteMarketsDBProvider;
import com.blitzfud.controllers.localDB.providers.ShoppingCartDBProvider;
import com.blitzfud.controllers.restapi.services.CategoryService;
import com.blitzfud.controllers.restapi.services.FavoriteMarketService;
import com.blitzfud.controllers.restapi.services.ProductService;
import com.blitzfud.controllers.utilities.BlitzfudColors;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.databinding.FragmentMarketBinding;
import com.blitzfud.models.dialog.ItemShoppingCartDialog;
import com.blitzfud.models.market.Category;
import com.blitzfud.models.market.FavoriteMarket;
import com.blitzfud.models.market.Market;
import com.blitzfud.models.market.Product;
import com.blitzfud.models.responseAPI.CategorySet;
import com.blitzfud.models.responseAPI.FavoriteMarketSet;
import com.blitzfud.models.responseAPI.ProductSet;
import com.blitzfud.models.responseAPI.ResponseAPI;
import com.blitzfud.models.responseAPI.ShoppingCartSet;
import com.blitzfud.views.adapters.market.CategoryAdapter;
import com.blitzfud.views.adapters.market.ProductAdapter;
import com.blitzfud.views.pages.MainActivity;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.blitzfud.controllers.utilities.BlitzfudConstants.FRAGMENT_PRODUCTS;

public class MarketFragment extends Fragment implements View.OnClickListener {

    private FragmentMarketBinding binding;
    private Realm realm;
    private CategorySet categorySet;
    private ShoppingCartSet shoppingCartSet;
    private FavoriteMarketSet favoriteMarketSet;
    private ProductSet productSet;
    private Market market;
    private AlertDialog dialog;
    private ItemShoppingCartDialog itemShoppingCartDialog;
    private ItemShoppingCartDialog.Builder dialogBuilder;
    private boolean categoriesLoaded;
    private boolean productsLoaded;
    private boolean subscribed;

    public MarketFragment() {
    }

    public MarketFragment(Market market) {
        this.market = market;
    }

    public MarketFragment(FavoriteMarket market) {
        this.market = new Market(market.get_id(), market.getName(), market.getDeliveryMethods(),
                market.getMarketStatus());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMarketBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        initConfig();
        prepareRecycler();
        loadData();
        bindListeners();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubscribe:
                clickSubscribe();
                break;
            case R.id.imgBack:
                closefragment();
                break;
            case R.id.txtVerTodo:
                redirectProduct(null);
                break;
        }
    }

    private void initConfig() {
        realm = Realm.getDefaultInstance();
        dialog = BlitzfudUtils.initLoading(getContext());
        shoppingCartSet = ShoppingCartDBProvider.getShoppingCartSet(realm);
        dialogBuilder = new ItemShoppingCartDialog.Builder(getContext(), new ItemShoppingCartDialog.OnConfirmClickListener() {
            @Override
            public void onAddItem(int quantity, Product product, Market market) {
                itemShoppingCartDialog.addItem(realm, binding.pantallaPrincipal, dialog,
                        quantity, product, market);
            }

            @Override
            public void onUpdateItem(int quantity, Product product, Market market) {
                itemShoppingCartDialog.updateItem(realm, binding.pantallaPrincipal, dialog,
                        quantity, product, market);
            }

            @Override
            public void onRemoveItem(Product product, Market market) {
                itemShoppingCartDialog.removeItem(realm, binding.pantallaPrincipal, dialog,
                        product, market);
            }
        });
    }

    private void prepareRecycler() {
        binding.recyclerCategory.setHasFixedSize(true);
        binding.recyclerNewProducts.setHasFixedSize(true);
        binding.recyclerAllProducts.setHasFixedSize(true);
    }

    private void loadData() {
        binding.txtNameMarket.setText(market.getName());
        binding.txtDelivery.setText(market.getTagDelivery());

        loadFavoriteMarkets();
        loadCategories();
        loadProducts();
    }

    private void loadFavoriteMarkets(){
        if (!FavoriteMarketsFragment.loadedFromAPI) {
            FavoriteMarketService.getAll().enqueue(new Callback<FavoriteMarketSet>() {
                @Override
                public void onResponse(Call<FavoriteMarketSet> call, Response<FavoriteMarketSet> response) {
                    if (response.isSuccessful()) {
                        FavoriteMarketsDBProvider.save(realm, response.body());
                        favoriteMarketSet = FavoriteMarketsDBProvider.getFavoriteMarkets(realm);
                        FavoriteMarketsFragment.loadedFromAPI = true;
                        showSusbcribe();
                    } else {
                        BlitzfudUtils.showErrorWithCatch(getContext(), response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<FavoriteMarketSet> call, Throwable t) {
                    BlitzfudUtils.showFailure(getContext());
                }
            });
        } else {
            favoriteMarketSet = FavoriteMarketsDBProvider.getFavoriteMarkets(realm);
            showSusbcribe();
        }
    }

    private void loadCategories(){
        CategoryService.getAll().enqueue(new Callback<CategorySet>() {
            @Override
            public void onResponse(Call<CategorySet> call, Response<CategorySet> response) {
                if (response.isSuccessful()) {
                    categorySet = response.body();
                    categoriesLoaded = true;
                    showCategories();
                } else {
                    BlitzfudUtils.showErrorWithCatch(getContext(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<CategorySet> call, Throwable t) {
                BlitzfudUtils.showFailure(getContext());
            }
        });
    }

    private void loadProducts(){
        ProductService.getAll(market.get_id()).enqueue(new Callback<ProductSet>() {
            @Override
            public void onResponse(Call<ProductSet> call, Response<ProductSet> response) {
                if (response.isSuccessful()) {
                    productSet = response.body();
                    productsLoaded = true;
                    showProducts();
                } else {
                    BlitzfudUtils.showErrorWithCatch(getContext(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ProductSet> call, Throwable t) {
                BlitzfudUtils.showFailure(getContext());
            }
        });
    }

    private void showSusbcribe() {
        if(binding == null) return;

        subscribed = favoriteMarketSet.existsMarket(market.get_id());
        binding.btnSubscribe.setVisibility(View.VISIBLE);

        if (subscribed) {
            binding.btnSubscribe.setText("SUSCRITO");
            binding.btnSubscribe.setTextColor(BlitzfudColors.colorSecondary);
        } else {
            binding.btnSubscribe.setText("SUSCRIBIRME");
            binding.btnSubscribe.setTextColor(BlitzfudColors.colorPrimary);
        }
    }

    private void showCategories() {
        if(binding == null) return;

        final CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(),
                categorySet.getCategories(), new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                redirectProduct(category);
            }
        });
        binding.recyclerCategory.setAdapter(categoryAdapter);

        if (productsLoaded) {
            binding.pantallaLoading.setVisibility(View.GONE);
            binding.pantallaPrincipal.setVisibility(View.VISIBLE);
        }
    }

    private void showProducts() {
        if(binding == null) return;

        final ProductAdapter productAdapter = new ProductAdapter(getContext(),
                productSet.getProducts(), new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                showDialog(market, product);
            }
        });
        binding.recyclerAllProducts.setAdapter(productAdapter);

        if (categoriesLoaded) {
            binding.pantallaLoading.setVisibility(View.GONE);
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
        binding.btnSubscribe.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);
        binding.txtVerTodo.setOnClickListener(this);
    }

    private void clickSubscribe() {
        dialog.show();

        if (!subscribed) {
            subscribe();
        } else {
            unsubscribe();
        }
    }

    private void subscribe() {
        FavoriteMarketService.add(market.get_id()).enqueue(new Callback<ResponseAPI>() {
            @Override
            public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    subscribed = true;
                    FavoriteMarketsDBProvider.add(realm, favoriteMarketSet, new FavoriteMarket(market.get_id(), market.getName(),
                            market.getDeliveryMethods()));
                    showSusbcribe();
                    BlitzfudUtils.showSnackbar(binding.pantallaPrincipal, response.body().getMessage());
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

    private void unsubscribe() {
        FavoriteMarketService.remove(market.get_id()).enqueue(new Callback<ResponseAPI>() {
            @Override
            public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    subscribed = false;
                    FavoriteMarketsDBProvider.remove(realm, favoriteMarketSet, new FavoriteMarket(market.get_id()));
                    showSusbcribe();
                    BlitzfudUtils.showSnackbar(binding.pantallaPrincipal, response.body().getMessage());
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

    private void redirectProduct(Category category) {
        ProductsFragment.category = category;
        ProductsFragment.market = market;

        ((MainActivity) getActivity()).newFragment(new ProductsFragment(), FRAGMENT_PRODUCTS);
    }

    private void closefragment() {
        ((MainActivity) getActivity()).closeFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        realm.close();
    }
}
