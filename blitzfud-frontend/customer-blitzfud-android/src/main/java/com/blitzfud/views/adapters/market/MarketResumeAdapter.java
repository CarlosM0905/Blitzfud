package com.blitzfud.views.adapters.market;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.blitzfud.R;
import com.blitzfud.models.shoppingCart.ShoppingCart;

import java.util.List;

public class MarketResumeAdapter extends RecyclerView.Adapter<MarketResumeAdapter.ViewHolder> {
    private Context context;
    private List<ShoppingCart> shoppingCarts;

    public MarketResumeAdapter(Context context, List<ShoppingCart> shoppingCarts) {
        this.context = context;
        this.shoppingCarts = shoppingCarts;
    }

    @NonNull
    @Override
    public MarketResumeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_market_resume, parent, false);

        return new MarketResumeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MarketResumeAdapter.ViewHolder holder, int position) {
        final ShoppingCart shoppingCart = shoppingCarts.get(position);

        holder.txtNameMarket.setText(shoppingCart.getMarket().getName());
        holder.txtMountShopping.setText(shoppingCart.getTotalWithoutDeliveryString());
        holder.txtSubtotal.setText(shoppingCart.getTotalString());

        if (shoppingCart.isDelivery()) {
            holder.layoutDelivery.setVisibility(View.VISIBLE);
            holder.txtDeliveryPrice.setText(shoppingCart.getMarket().getDeliveryPriceString());
        }

    }

    @Override
    public int getItemCount() {
        return shoppingCarts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNameMarket;
        private TextView txtSubtotal;
        private TextView txtMountShopping;
        private LinearLayoutCompat layoutDelivery;
        private TextView txtDeliveryPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNameMarket = itemView.findViewById(R.id.txtNameMarket);
            txtSubtotal = itemView.findViewById(R.id.txtSubtotal);
            txtMountShopping = itemView.findViewById(R.id.txtMountShopping);
            layoutDelivery = itemView.findViewById(R.id.layoutDelivery);
            txtDeliveryPrice = itemView.findViewById(R.id.txtDeliveryPrice);
        }

    }

}