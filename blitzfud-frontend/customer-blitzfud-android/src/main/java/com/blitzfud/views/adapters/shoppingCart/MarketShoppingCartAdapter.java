package com.blitzfud.views.adapters.shoppingCart;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.blitzfud.R;
import com.blitzfud.controllers.restapi.services.ShoppingCartService;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.models.market.Market;
import com.blitzfud.models.market.Product;
import com.blitzfud.models.responseAPI.ResponseAPI;
import com.blitzfud.models.shoppingCart.ItemShoppingCart;
import com.blitzfud.models.shoppingCart.ShoppingCart;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketShoppingCartAdapter extends RecyclerView.Adapter<MarketShoppingCartAdapter.ViewHolder> {
    private Context context;
    private List<ShoppingCart> shoppingCarts;
    private OnMarketShoppingListener onMarketShoppingListener;
    private AlertDialog dialog;

    public MarketShoppingCartAdapter(Context context, List<ShoppingCart> shoppingCarts,
                                     OnMarketShoppingListener onMarketShoppingListener) {
        this.context = context;
        this.shoppingCarts = shoppingCarts;
        this.onMarketShoppingListener = onMarketShoppingListener;
        dialog = BlitzfudUtils.initLoading(context);
    }

    @NonNull
    @Override
    public MarketShoppingCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_market_shopping_cart, parent, false);

        return new MarketShoppingCartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MarketShoppingCartAdapter.ViewHolder holder, int position) {
        final ShoppingCart shoppingCart = shoppingCarts.get(position);
        final List<ItemShoppingCart> items = shoppingCart.getItems();
        final Market market = shoppingCart.getMarket();

        holder.txtNameMarket.setText(shoppingCart.getMarket().getName());
        holder.txtTotal.setText(shoppingCart.getTotalString());

        if (market.hasDelivery()) {
            holder.switchCompat.setChecked(true);
            holder.switchCompat.setEnabled(false);
            holder.switchCompat.setText("Delivery");
        }

        if (market.hasPickup()) {
            holder.switchCompat.setEnabled(false);
        }

        if(market.hasBoth()){
            holder.switchCompat.setChecked(shoppingCart.isDelivery());
        }

        final ItemShoppingCartAdapter itemShoppingCartAdapter = new ItemShoppingCartAdapter(context, items, new ItemShoppingCartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemShoppingCart itemShoppingCart, int position) {
                onMarketShoppingListener.onItemClick(shoppingCarts.get(holder.getAdapterPosition()).getMarket(), itemShoppingCart.getProduct());
            }
        });


        final ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (!onMarketShoppingListener.isLoadedFromAPI()) {
                    itemShoppingCartAdapter.notifyDataSetChanged();
                    return;
                }

                final int positionAdapter = viewHolder.getAdapterPosition();
                dialog.show();
                ShoppingCartService.removeItem(shoppingCart.getItems().get(positionAdapter)
                        .getProduct().get_id()).enqueue(new Callback<ResponseAPI>() {
                    @Override
                    public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            onMarketShoppingListener.onItemDeleted(items.get(positionAdapter).getProduct(),
                                    shoppingCart.getMarket(), response.body().getMessage());
                        } else {
                            BlitzfudUtils.showErrorWithCatch(context, response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseAPI> call, Throwable t) {
                        dialog.dismiss();
                        BlitzfudUtils.showFailure(context);
                    }
                });
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(holder.recyclerView);

        holder.setAdapter(itemShoppingCartAdapter);
        holder.btnConfirmStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMarketShoppingListener.onConfirmMarket(shoppingCarts.get(holder.getAdapterPosition()).getMarket().get_id());
            }
        });

        holder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.switchCompat.setText("Delivery");
                } else {
                    holder.switchCompat.setText("Ir yo mismo");
                }

                onMarketShoppingListener.onCheckedDeleted(shoppingCarts.get(holder.getAdapterPosition()).getMarket().get_id(), isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingCarts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNameMarket;
        private Button btnConfirmStore;
        private RecyclerView recyclerView;
        private TextView txtTotal;
        private SwitchCompat switchCompat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNameMarket = itemView.findViewById(R.id.txtNameMarket);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            btnConfirmStore = itemView.findViewById(R.id.btnConfirmMarket);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            switchCompat = itemView.findViewById(R.id.switchMethodDelivery);
        }

        public void setAdapter(ItemShoppingCartAdapter itemShoppingCartAdapter) {
            recyclerView.setAdapter(itemShoppingCartAdapter);
        }

    }

    public interface OnMarketShoppingListener {
        void onConfirmMarket(String marketId);

        void onItemClick(final Market market, final Product product);

        void onItemDeleted(final Product product, final Market market, final String message);

        void onCheckedDeleted(final String marketId, final boolean isChecked);

        boolean isLoadedFromAPI();

    }

}