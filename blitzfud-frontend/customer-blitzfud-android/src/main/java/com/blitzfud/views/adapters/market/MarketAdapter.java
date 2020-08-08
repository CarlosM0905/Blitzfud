package com.blitzfud.views.adapters.market;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blitzfud.R;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.models.market.Market;
import com.blitzfud.models.market.Product;
import com.blitzfud.views.pages.shoppingCart.ItemShoppingCartActivity;
import com.blitzfud.views.pages.market.MarketActivity;

import java.util.ArrayList;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Market> stores;

    public MarketAdapter(Context context, ArrayList<Market> stores) {
        this.context = context;
        this.stores = stores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_market, parent, false);

        return new MarketAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Market market = stores.get(position);

        final ProductAdapter productAdapter = new ProductAdapter(context, market.getProducts(), new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product, int position) {
                MarketActivity.setMarket(market);
                ItemShoppingCartActivity.setProduct(product);
                final Intent intent = new Intent(context, ItemShoppingCartActivity.class);
                context.startActivity(intent);
            }
        });
        holder.setAdapter(productAdapter);

        holder.txtName.setText(market.getName());

        holder.txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarketActivity.setMarket(market);
                final Intent intent = new Intent(context, MarketActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName;
        private TextView txtMore;
        private RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtNameMarket);
            txtMore = itemView.findViewById(R.id.txtMore);
            recyclerView = itemView.findViewById(R.id.recyclerView);

            prepareRecycler();
        }

        private void prepareRecycler() {
            recyclerView.setHasFixedSize(true);
            //recyclerView.setLayoutManager(BlitzfudUtils.getStaggeredGrid(context, 2, 4));
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        }

        public void setAdapter(ProductAdapter productAdapter) {
            recyclerView.setAdapter(productAdapter);
        }

    }

}