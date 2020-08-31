package com.blitzfud.models.order;

import com.blitzfud.models.market.Market;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DeliveryOrder extends RealmObject {
    @PrimaryKey
    private String _id;
    private double totalAmount;
    private String status;
    private Market market;

    public DeliveryOrder() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }
}
