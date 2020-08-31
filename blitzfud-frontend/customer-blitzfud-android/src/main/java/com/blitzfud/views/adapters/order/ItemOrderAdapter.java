package com.blitzfud.views.adapters.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blitzfud.R;
import com.blitzfud.models.order.ItemOrder;

import java.util.List;

public class ItemOrderAdapter extends RecyclerView.Adapter<ItemOrderAdapter.ViewHolder> {
    private Context context;
    private List<ItemOrder> itemOrders;

    public ItemOrderAdapter(Context context, List<ItemOrder> itemOrders) {
        this.context = context;
        this.itemOrders = itemOrders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_item_oder, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ItemOrder itemOrder = itemOrders.get(position);

        holder.txtNameProduct.setText(itemOrder.getName());
        holder.txtPriceProduct.setText(itemOrder.getPriceString());
        holder.txtQuantity.setText(String.valueOf(itemOrder.getQuantity()));
        holder.txtTotal.setText(itemOrder.getItemPriceString());

    }

    @Override
    public int getItemCount() {
        return itemOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgProduct;
        private TextView txtNameProduct;
        private TextView txtPriceProduct;
        private TextView txtQuantity;
        private TextView txtTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtNameProduct = itemView.findViewById(R.id.txtNameProduct);
            txtPriceProduct = itemView.findViewById(R.id.txtPriceProduct);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtTotal = itemView.findViewById(R.id.txtTotal);
        }

    }

}