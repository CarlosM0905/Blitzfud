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
import com.blitzfud.models.market.Market;

import java.util.ArrayList;

public class FavoriteMarketAdapter extends RecyclerView.Adapter<FavoriteMarketAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Market> markets;
    private OnItemClickListener itemClickListener;

    public FavoriteMarketAdapter(Context context, ArrayList<Market> markets, OnItemClickListener itemClickListener) {
        this.context = context;
        this.markets = markets;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite_market, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Market market = markets.get(position);

        holder.txtNameMarket.setText(market.getName());

        holder.bindListener(market, itemClickListener);
    }

    @Override
    public int getItemCount() {
        return markets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgMarket;
        private TextView txtNameMarket;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgMarket = itemView.findViewById(R.id.imgMarket);
            txtNameMarket = itemView.findViewById(R.id.txtNameMarket);
        }

        public void bindListener(final Market market, final FavoriteMarketAdapter.OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(market, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Market market, int position);
    }
}