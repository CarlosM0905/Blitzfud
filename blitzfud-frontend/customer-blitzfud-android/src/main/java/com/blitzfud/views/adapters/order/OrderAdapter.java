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
import com.blitzfud.models.order.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Context context;
    private List<Order> orders;
    private String createdAtDate;
    private OnOrderClickListener onOrderClickListener;

    public OrderAdapter(Context context, List<Order> orders, String createdAtDate, OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.orders = orders;
        this.createdAtDate = createdAtDate;
        this.onOrderClickListener = onOrderClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Order order = orders.get(position);

        holder.txtNameMarket.setText(order.getMarket().getName());
        holder.txtDeliveryMethod.setText(order.getDeliveryMethod());
        holder.txtStatus.setText(order.getStatus());
        holder.txtTotal.setText(order.getTotalAmountString());

        if (order.isDeliveryMethod())
            holder.imgDeliveryMethod.setImageResource(R.drawable.ic_delivery_method);

        if (order.isInProgress())
            holder.imgStatus.setImageResource(R.drawable.ic_in_progress_status);

        holder.bindListener(onOrderClickListener);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNameMarket;
        private TextView txtDeliveryMethod;
        private ImageView imgDeliveryMethod;
        private TextView txtTotal;
        private TextView txtStatus;
        private ImageView imgStatus;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDeliveryMethod = itemView.findViewById(R.id.txtDeliveryMethod);
            txtNameMarket = itemView.findViewById(R.id.txtNameMarket);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            imgDeliveryMethod = itemView.findViewById(R.id.imgDeliveryMethod);
            imgStatus = itemView.findViewById(R.id.imgStatus);
        }

        public void bindListener(final OnOrderClickListener onItemClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(createdAtDate, orders.get(getAdapterPosition()),
                            getAdapterPosition());
                }
            });
        }
    }

    public interface OnOrderClickListener {
        void onItemClick(String createdAtDate, Order order, int position);
    }
}