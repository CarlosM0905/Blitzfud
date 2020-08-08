package com.blitzfud.models;

import java.util.ArrayList;

public class StoreProduct {
    private String name;
    private ArrayList<ProductOrder> productOrders;

    public StoreProduct(){}

    public StoreProduct(final String name, final int size){
        this.name = name;
        this.productOrders = new ArrayList<>();
        populateFakeData(size);
    }

    private void populateFakeData(final int size){
        for (int i = 0; i < size; i++) {
            productOrders.add(new ProductOrder());
        }
    }

    public String getName() {
        return name;
    }

    public ArrayList<ProductOrder> getProductOrders() {
        return productOrders;
    }
}
