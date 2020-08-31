package com.blitzfud.models.responseAPI;

import com.blitzfud.models.market.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductSet {
    private int count;
    private List<Product> products;

    public ProductSet() {
        this.products = new ArrayList<>();
    }

    public ProductSet(int count, List<Product> products) {
        this.count = count;
        this.products = products;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProducts(List<Product> products){
        this.products.addAll(products);
    }
}
