package com.blitzfud.models.order;

import com.blitzfud.controllers.utilities.BlitzfudConstants;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PurchaseOrders extends RealmObject {

    @PrimaryKey
    private String _id;
    private double totalAmount;
    private Date createdAt;
    private RealmList<Order> orders;

    public PurchaseOrders() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCreatedAt() {
        return "Pedido del "+ BlitzfudConstants.SDF_FULL_DATE.format(createdAt);
    }

    public String getCreatedAtDate() {
        return BlitzfudConstants.SDF_DATE.format(createdAt);
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public RealmList<Order> getOrders() {
        return orders;
    }

    public void setOrders(RealmList<Order> orders) {
        this.orders = orders;
    }

}
