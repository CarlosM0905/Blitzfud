package com.blitzfud.models.responseCount;

import com.blitzfud.models.market.Product;

import java.util.ArrayList;

public class ProductCount {
    private int count;
    private ArrayList<Product> products;

    public ProductCount() {
    }

    public ProductCount(int count, ArrayList<Product> products) {
        this.count = count;
        this.products = products;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
