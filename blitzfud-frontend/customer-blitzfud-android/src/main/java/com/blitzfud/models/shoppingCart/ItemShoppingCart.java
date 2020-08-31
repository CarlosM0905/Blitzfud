package com.blitzfud.models.shoppingCart;

import com.blitzfud.controllers.localDB.DBConnection;
import com.blitzfud.models.market.Product;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ItemShoppingCart extends RealmObject {

    @PrimaryKey
    private long _id;
    private Product product;
    private int quantity;
    private double total;

    public ItemShoppingCart() {
        this._id = DBConnection.itemShoppingCartDBId.incrementAndGet();
    }

    public ItemShoppingCart(Product product, int quantity) {
        this._id = DBConnection.itemShoppingCartDBId.incrementAndGet();
        this.product = product;
        this.quantity = quantity;
        this.setTotal();
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
        return total;
    }

    public void setTotal() {
        if(this.total == 0){
            this.total = product.getPrice() * quantity;
        }
    }

    public String getTotalString(){
        return String.format("S/ %.2f", total);
    }
    /*
    public ItemShoppingCartDB toDB(){
        return new ItemShoppingCartDB(product.toDB(),quantity,total);
    }*/
}
