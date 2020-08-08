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
import com.blitzfud.models.market.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Category> categories;
    private OnItemClickListener itemClickListener;

    public CategoryAdapter(Context context, ArrayList<Category> categories, OnItemClickListener itemClickListener) {
        this.context = context;
        this.categories = categories;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);

        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryAdapter.ViewHolder holder, int position) {
        final Category category = categories.get(position);

        holder.txtNameCategory.setText(category.getName());
        holder.imgCategory.setImageResource(Category.getDrawableId(category.getName()));
//        Picasso.with(context).load(Category.getDrawableId(category.getName())).into(holder.imgCategory);

        holder.bindListener(category, itemClickListener);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgCategory;
        private TextView txtNameCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCategory = itemView.findViewById(R.id.imgCategory);
            txtNameCategory = itemView.findViewById(R.id.txtNameCategory);
        }

        public void bindListener(final Category category, final CategoryAdapter.OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(category, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Category category, int position);
    }
}