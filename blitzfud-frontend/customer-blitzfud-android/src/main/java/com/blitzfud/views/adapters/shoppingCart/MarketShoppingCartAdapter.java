package com.blitzfud.views.adapters.shoppingCart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blitzfud.R;
import com.blitzfud.controllers.restapi.services.ShoppingCartService;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.models.shoppingCart.ItemShoppingCart;
import com.blitzfud.models.shoppingCart.ShoppingCart;
import com.blitzfud.models.ResponseAPI;
import com.blitzfud.views.pages.market.MarketActivity;
import com.blitzfud.views.pages.shoppingCart.ShoppingCartActivity;
import com.blitzfud.views.pages.shoppingCart.ItemShoppingCartActivity;
import com.blitzfud.views.pages.shoppingCart.execute.ExecuteMarketActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketShoppingCartAdapter extends RecyclerView.Adapter<MarketShoppingCartAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ShoppingCart> shoppingCarts;
    private OnItemDeletedListener itemClickListener;
    private AlertDialog dialog;

    public MarketShoppingCartAdapter(Context context, ArrayList<ShoppingCart> shoppingCarts, MarketShoppingCartAdapter.OnItemDeletedListener itemClickListener) {
        this.context = context;
        this.shoppingCarts = shoppingCarts;
        this.itemClickListener = itemClickListener;
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
        final ArrayList<ItemShoppingCart> items = shoppingCart.getItems();

        holder.txtNameMarket.setText(shoppingCart.getMarket().getName());
        holder.txtTotal.setText(shoppingCart.getTotalString());

        final ItemShoppingCartAdapter itemShoppingCartAdapter = new ItemShoppingCartAdapter(context, items, new ItemShoppingCartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemShoppingCart itemShoppingCart, int position) {
                MarketActivity.setMarket(shoppingCart.getMarket());
                ItemShoppingCartActivity.setProduct(itemShoppingCart.getProduct());
                context.startActivity(new Intent(context, ItemShoppingCartActivity.class));
            }
        });

        final ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                dialog.show();

                final int position = viewHolder.getAdapterPosition();

                ShoppingCartService.removeItem(shoppingCart.getItems().get(position)
                        .getProduct().get_id()).enqueue(new Callback<ResponseAPI>() {
                    @Override
                    public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                        dialog.dismiss();
                        if(response.isSuccessful()){
                            final double priceTotal = items.get(position).getTotal();
                            shoppingCart.removeItemShoppingCart(items.get(position).getProduct());
                            itemShoppingCartAdapter.notifyItemRemoved(position);
                            holder.txtTotal.setText(shoppingCart.getTotalString());

                            itemClickListener.onItemDeleted(priceTotal, shoppingCart.getMarket().get_id(), shoppingCart.getItems().size());

                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }else{
                            BlitzfudUtils.showError(context, response.errorBody());
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
                final Intent intent = new Intent(context, ExecuteMarketActivity.class);
                intent.putExtra("delivery", holder.switchCompat.isChecked());
                intent.putExtra("marketId", shoppingCart.getMarket().get_id());
                context.startActivity(intent);
            }
        });

        holder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ShoppingCartActivity.updateMarketDelivery(shoppingCart.getMarket().get_id(), isChecked);

                if(isChecked){
                    holder.switchCompat.setText("Delivery");
                }else{
                    holder.switchCompat.setText("Ir yo mismo");
                }
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
        private LinearLayoutManager linearLayoutManager;
        private TextView txtTotal;
        private SwitchCompat switchCompat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNameMarket = itemView.findViewById(R.id.txtNameMarket);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            btnConfirmStore = itemView.findViewById(R.id.btnConfirmMarket);
            linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            switchCompat = itemView.findViewById(R.id.switchMethodDelivery);
        }

        public void setAdapter(ItemShoppingCartAdapter itemShoppingCartAdapter) {
            recyclerView.setAdapter(itemShoppingCartAdapter);
        }

    }

    public interface OnItemDeletedListener {
        void onItemDeleted(double total, String marketId, int size);
    }

}