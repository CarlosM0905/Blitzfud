package com.blitzfud.views.fragments.market;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blitzfud.R;
import com.blitzfud.controllers.localDB.providers.ShoppingCartDBProvider;
import com.blitzfud.controllers.restapi.services.ProductService;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.databinding.FragmentProductsBinding;
import com.blitzfud.models.dialog.ItemShoppingCartDialog;
import com.blitzfud.models.market.Category;
import com.blitzfud.models.market.Market;
import com.blitzfud.models.market.Product;
import com.blitzfud.models.responseAPI.ProductSet;
import com.blitzfud.models.responseAPI.ShoppingCartSet;
import com.blitzfud.views.adapters.market.ProductFilterAdapter;
import com.blitzfud.views.pages.MainActivity;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.blitzfud.controllers.utilities.BlitzfudConstants.ITEMS_PER_PAGE;

public class ProductsFragment extends Fragment implements View.OnClickListener {

    public static Market market;
    public static Category category;

    private FragmentProductsBinding binding;
    private Realm realm;
    private ProductSet productSet;
    private AlertDialog dialog;
    private ShoppingCartSet shoppingCartSet;
    private ProductFilterAdapter productFilterAdapter;
    private ItemShoppingCartDialog itemShoppingCartDialog;
    private ItemShoppingCartDialog.Builder dialogBuilder;
    private int offset;
    private boolean loadingProducts;
    private boolean noMoreProducts;

    public ProductsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        initConfig();
        loadData();
        bindListeners();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
            case R.id.imgBack2:
                ((MainActivity) getActivity()).closeFragment();
                break;
            default : break;
        }
    }

    private void initConfig() {
        realm = Realm.getDefaultInstance();
        dialog = BlitzfudUtils.initLoading(getContext());
        productSet = new ProductSet();
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

    private void loadData() {
        ProductService.getProducts(market.get_id(), offset, ITEMS_PER_PAGE, (category != null) ?
                category.get_id() : null).enqueue(new Callback<ProductSet>() {
            @Override
            public void onResponse(Call<ProductSet> call, Response<ProductSet> response) {
                if (response.isSuccessful()) {
                    final ProductSet body = response.body();

                    productSet.setCount(body.getCount());
                    productSet.addProducts(body.getProducts());
                    offset = productSet.getCount();

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

    private void showProducts() {

        productFilterAdapter = new ProductFilterAdapter(getContext(), productSet.getProducts(), new ProductFilterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                showDialog(market, product);
            }
        });

        binding.recyclerProduct.setHasFixedSize(true);
        binding.recyclerProduct.setAdapter(productFilterAdapter);

        binding.pantallaLoading.setVisibility(View.GONE);

        if (productSet.getProducts().isEmpty()) {
            binding.pantallaEmpty.setVisibility(View.VISIBLE);
            binding.txtTitleToolbar2.setText(category.getName());
            binding.imgToolbar2.setImageResource(Category.getDrawableId(category.getName()));
        } else {
            setToolbar();
            binding.pantallaPrincipal.setVisibility(View.VISIBLE);
        }

    }

    private void setToolbar() {
        if (category == null) {
            binding.txtTitleToolbar.setText("Todos los productos");
            binding.imgToolbar.setVisibility(View.GONE);
        } else {
            binding.txtTitleToolbar.setText(category.getName());
            binding.imgToolbar.setImageResource(Category.getDrawableId(category.getName()));
        }
    }

    private void bindListeners() {
        binding.imgBack.setOnClickListener(this);
        binding.imgBack2.setOnClickListener(this);
        binding.recyclerProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) { //1 for down
                    if (noMoreProducts) return;
                    if (!loadingProducts) loadMoreProducts();
                }
            }
        });
    }

    private void showDialog(final Market market, final Product product) {
        itemShoppingCartDialog = dialogBuilder.setShoppingCartSet(shoppingCartSet)
                .setMarket(market)
                .setProduct(product)
                .build();
    }

    private void loadMoreProducts() {
        loadingProducts = true;
        binding.loadingProducts.setVisibility(View.VISIBLE);
        binding.recyclerProduct.scrollToPosition(productSet.getProducts().size()-1);

        ProductService.getProducts(market.get_id(), offset, ITEMS_PER_PAGE, (category != null) ?
                category.get_id() : null).enqueue(new Callback<ProductSet>() {
            @Override
            public void onResponse(Call<ProductSet> call, Response<ProductSet> response) {
                if (response.isSuccessful()) {
                    final ProductSet body = response.body();
                    final int newElements = body.getCount();

                    productSet.setCount(newElements);
                    productSet.addProducts(body.getProducts());
                    offset += newElements;

                    if (newElements != ITEMS_PER_PAGE) noMoreProducts = true;

                    addNewProducts(newElements);

                    loadingProducts = false;
                } else {
                    BlitzfudUtils.showError(getContext(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ProductSet> call, Throwable t) {
                BlitzfudUtils.showFailure(getContext());
            }
        });
    }

    private void addNewProducts(int newElements) {
        int lastItem = productSet.getProducts().size() -
                newElements;
        productFilterAdapter.notifyItemRangeInserted(lastItem, newElements);

        ((LinearLayoutManager) binding.recyclerProduct.getLayoutManager())
                .scrollToPositionWithOffset(lastItem - 1, 20);

        binding.loadingProducts.setVisibility(View.GONE);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
        binding = null;
    }


}
