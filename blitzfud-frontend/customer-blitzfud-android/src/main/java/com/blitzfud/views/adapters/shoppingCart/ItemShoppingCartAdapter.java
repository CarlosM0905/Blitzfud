package com.blitzfud.views.adapters.shoppingCart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blitzfud.R;
import com.blitzfud.models.shoppingCart.ItemShoppingCart;

import java.util.ArrayList;

public class ItemShoppingCartAdapter extends RecyclerView.Adapter<ItemShoppingCartAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ItemShoppingCart> itemsShoppingCart;
    private ItemShoppingCartAdapter.OnItemClickListener itemClickListener;

    public ItemShoppingCartAdapter(Context context, ArrayList<ItemShoppingCart> itemsShoppingCart, ItemShoppingCartAdapter.OnItemClickListener itemClickListener) {
        this.context = context;
        this.itemsShoppingCart = itemsShoppingCart;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ItemShoppingCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_item_shopping_cart, parent, false);

        return new ItemShoppingCartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemShoppingCartAdapter.ViewHolder holder, int position) {
        final ItemShoppingCart itemShoppingCart = itemsShoppingCart.get(position);

        holder.txtNameProduct.setText(itemShoppingCart.getProduct().getName());
        holder.txtPriceProduct.setText(itemShoppingCart.getProduct().getPriceString());
        holder.txtQuantity.setText(String.valueOf(itemShoppingCart.getQuantity()));
        holder.txtTotal.setText(itemShoppingCart.getTotalString());

        if(itemClickListener!=null) holder.bindListener(itemShoppingCart, itemClickListener);
    }

    @Override
    public int getItemCount() {
        return itemsShoppingCart.size();
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

        public void bindListener(final ItemShoppingCart itemShoppingCart, final ItemShoppingCartAdapter.OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(itemShoppingCart, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ItemShoppingCart itemShoppingCart, int position);
    }
}