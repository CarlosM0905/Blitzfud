package com.blitzfud.views.adapters.market;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blitzfud.R;
import com.blitzfud.models.market.Market;
import com.blitzfud.models.market.Product;

import java.util.List;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ViewHolder> {
    private Context context;
    private List<Market> markets;
    private OnMarketAdapterListener onMarketAdapterListener;

    public MarketAdapter(Context context, List<Market> markets, OnMarketAdapterListener onMarketAdapterListener) {
        this.context = context;
        this.markets = markets;
        this.onMarketAdapterListener = onMarketAdapterListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_market, parent, false);

        return new MarketAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Market market = markets.get(position);

        final ProductAdapter productAdapter = new ProductAdapter(context, market.getProducts(), new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                onMarketAdapterListener.onProductClickListener(markets.get(holder.getAdapterPosition()), product);
            }
        });
        holder.setAdapter(productAdapter);

        holder.txtName.setText(market.getName());

        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMarketAdapterListener.onMoreClickListener(markets.get(holder.getAdapterPosition()));
            }
        });

        if (market.hasPickup())
            holder.imgHasDelivery.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return markets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName;
        private ImageView imgMore;
        private ImageView imgHasDelivery;
        private RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtNameMarket);
            imgMore = itemView.findViewById(R.id.imgMore);
            imgHasDelivery = itemView.findViewById(R.id.imgHasDelivery);
            recyclerView = itemView.findViewById(R.id.recyclerView);

            prepareRecycler();
        }

        private void prepareRecycler() {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        }

        public void setAdapter(ProductAdapter productAdapter) {
            recyclerView.setAdapter(productAdapter);
        }

    }

    public interface OnMarketAdapterListener {
        void onMoreClickListener(Market market);

        void onProductClickListener(Market market, Product product);
    }

}