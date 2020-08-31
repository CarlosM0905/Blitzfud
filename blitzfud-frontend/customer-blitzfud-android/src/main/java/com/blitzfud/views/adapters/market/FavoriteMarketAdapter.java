package com.blitzfud.views.adapters.market;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blitzfud.R;
import com.blitzfud.models.dialog.ConfirmDialog;
import com.blitzfud.models.market.FavoriteMarket;

import java.util.List;

public class FavoriteMarketAdapter extends RecyclerView.Adapter<FavoriteMarketAdapter.ViewHolder> {
    private Context context;
    private List<FavoriteMarket> markets;
    private OnFavoriteMarketClickListener onFavoriteMarketClickListener;
    private ConfirmDialog confirmDialog;

    public FavoriteMarketAdapter(Context context, List<FavoriteMarket> markets, OnFavoriteMarketClickListener onFavoriteMarketClickListener) {
        this.context = context;
        this.markets = markets;
        this.onFavoriteMarketClickListener = onFavoriteMarketClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite_market, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final FavoriteMarket market = markets.get(position);

        holder.txtNameMarket.setText(market.getName());

        holder.bindListener(onFavoriteMarketClickListener);

        if (market.hasPickup()) holder.imgPickup.setVisibility(View.VISIBLE);
        if (market.hasDelivery()) holder.imgDelivery.setVisibility(View.VISIBLE);
        if (market.hasBoth()) {
            holder.imgPickup.setVisibility(View.VISIBLE);
            holder.imgDelivery.setVisibility(View.VISIBLE);
        }

        if (market.isOpen())
            holder.btnStatusOpen.setVisibility(View.VISIBLE);
        else
            holder.btnStatusClose.setVisibility(View.VISIBLE);


    }

    @Override
    public int getItemCount() {
        return markets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private ImageView imgMarket;
        private ImageView imgPickup;
        private ImageView imgDelivery;
        private Button btnStatusOpen;
        private Button btnStatusClose;
        private TextView txtNameMarket;
        private ImageView imgSubscribe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgMarket = itemView.findViewById(R.id.imgMarket);
            txtNameMarket = itemView.findViewById(R.id.txtNameMarket);
            imgPickup = itemView.findViewById(R.id.imgPickup);
            imgDelivery = itemView.findViewById(R.id.imgDelivery);
            btnStatusOpen = itemView.findViewById(R.id.btnStatusOpen);
            btnStatusClose = itemView.findViewById(R.id.btnStatusClose);
            imgSubscribe = itemView.findViewById(R.id.imgSubscribe);
        }

        public void bindListener(final FavoriteMarketAdapter.OnFavoriteMarketClickListener onFavoriteMarketClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFavoriteMarketClickListener.onItemClick(markets.get(getAdapterPosition()));
                }
            });
            imgSubscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFavoriteMarketClickListener.onUnsubscribeClick(markets.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnFavoriteMarketClickListener {
        void onItemClick(FavoriteMarket market);
        void onUnsubscribeClick(FavoriteMarket market);
    }
}