package com.blitzfud.models.market;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Market {

    private String _id;
    private String name;
    private ArrayList<Product> products;

    public Market() {
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        final Market market = (Market) obj;
        return this._id.equals(market._id);
    }
}