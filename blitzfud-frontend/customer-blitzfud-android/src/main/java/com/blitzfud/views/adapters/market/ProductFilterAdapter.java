package com.blitzfud.views.adapters.market;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blitzfud.R;
import com.blitzfud.models.market.Product;

import java.util.List;

public class ProductFilterAdapter extends RecyclerView.Adapter<ProductFilterAdapter.ViewHolder> {
    private Context context;
    private List<Product> products;
    private OnItemClickListener itemClickListener;

    public ProductFilterAdapter(Context context, List<Product> products, OnItemClickListener itemClickListener) {
        this.context = context;
        this.products = products;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_filter, parent, false);

        return new ProductFilterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Product product = products.get(position);

        holder.txtNameProduct.setText(product.getName()+" "+product.getInformation());
        holder.txtPriceProduct.setText(product.getPriceString());

        holder.bindListener(itemClickListener);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgProduct;
        private TextView txtNameProduct;
        private TextView txtPriceProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtNameProduct = itemView.findViewById(R.id.txtNameProduct);
            txtPriceProduct = itemView.findViewById(R.id.txtPriceProduct);
        }

        public void bindListener(final OnItemClickListener onItemClickListener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(products.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }
}