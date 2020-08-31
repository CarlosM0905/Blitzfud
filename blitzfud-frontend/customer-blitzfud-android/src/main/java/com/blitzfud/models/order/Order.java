package com.blitzfud.models.order;

import com.blitzfud.controllers.utilities.BlitzfudConstants;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Order extends RealmObject {

    @PrimaryKey
    private String _id;
    private String status;
    private double totalAmount;
    private RealmList<ItemOrder> items;
    private MarketName market;
    private String deliveryMethod;
    private double deliveryPrice;

    public Order() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStatus() {
        return status.equals(BlitzfudConstants.PREPROCESSING) ? BlitzfudConstants.PREPROCESSING_SPANISH :
                BlitzfudConstants.IN_PROGRESS_SPANISH;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getTotalAmountString() {
        return String.format("S/ %.2f", totalAmount);
    }

    public String getTotalWithDelivery(){
        return String.format("S/ %.2f", totalAmount+deliveryPrice);
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public RealmList<ItemOrder> getItems() {
        return items;
    }

    public void setItems(RealmList<ItemOrder> items) {
        this.items = items;
    }

    public MarketName getMarket() {
        return market;
    }

    public void setMarket(MarketName market) {
        this.market = market;
    }

    public String getDeliveryMethod() {
        return deliveryMethod.equals(BlitzfudConstants.DELIVERY) ? BlitzfudConstants.DELIVERY_SPANISH :
                BlitzfudConstants.PICK_UP_SPANISH;
    }

    public boolean isDeliveryMethod() {
        return deliveryMethod.equals(BlitzfudConstants.DELIVERY);
    }

    public boolean isInProgress() {
        return status.equals(BlitzfudConstants.IN_PROGRESS);
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public String getDeliveryPriceString() {
        return String.format("S/ %.2f", deliveryPrice);
    }

    public void setDeliveryPrice(double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }
}
