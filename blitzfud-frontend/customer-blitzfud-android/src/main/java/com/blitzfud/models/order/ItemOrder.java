package com.blitzfud.models.order;

import com.blitzfud.controllers.localDB.DBConnection;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ItemOrder extends RealmObject {
    @PrimaryKey
    private long _id;
    private String name;
    private double price;
    private int quantity;
    private double itemPrice;
    private String productId;

    public ItemOrder() {
        this._id = DBConnection.itemOrderId.incrementAndGet();
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public String getPriceString() {
        return String.format("S/ %.2f", price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemPriceString() {
        return String.format("S/ %.2f", itemPrice);
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

}
