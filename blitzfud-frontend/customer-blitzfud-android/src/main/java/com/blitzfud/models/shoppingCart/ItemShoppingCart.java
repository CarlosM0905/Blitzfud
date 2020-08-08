package com.blitzfud.models.shoppingCart;

import com.blitzfud.models.market.Market;
import com.blitzfud.models.market.Product;

public class ItemShoppingCart {
    private Product product;
    private int quantity;
    private double total;

    public ItemShoppingCart() {
    }

    public ItemShoppingCart(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal(){
        if(total == 0){
            total = product.getPrice() * quantity;
        }

        return total;
    }

    public String getTotalString(){
        return String.format("S/ %.2f", getTotal());
    }
}
