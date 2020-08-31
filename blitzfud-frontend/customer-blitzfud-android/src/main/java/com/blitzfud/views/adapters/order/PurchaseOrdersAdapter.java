package com.blitzfud.views.adapters.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blitzfud.R;
import com.blitzfud.models.order.PurchaseOrders;

import java.util.List;

public class PurchaseOrdersAdapter extends RecyclerView.Adapter<PurchaseOrdersAdapter.ViewHolder> {

    private Context context;
    private List<PurchaseOrders> purchaseOrders;
    private OrderAdapter.OnOrderClickListener onOrderClickListener;

    public PurchaseOrdersAdapter(Context context, List<PurchaseOrders> purchaseOrders, OrderAdapter.OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.purchaseOrders = purchaseOrders;
        this.onOrderClickListener = onOrderClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_purchase_orders, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final PurchaseOrders purchaseOrder = purchaseOrders.get(position);

        final OrderAdapter orderAdapter = new OrderAdapter(context, purchaseOrder.getOrders(),
                purchaseOrder.getCreatedAtDate(), onOrderClickListener);

        holder.txtCreatedAt.setText(purchaseOrder.getCreatedAt());
        holder.recyclerView.setAdapter(orderAdapter);
    }

    @Override
    public int getItemCount() {
        return purchaseOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtCreatedAt;
        private RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            recyclerView = itemView.findViewById(R.id.recyclerPurchase);
            txtCreatedAt = itemView.findViewById(R.id.txtCreatedAt);
        }

    }

}