package com.blitzfud.models.market;

import androidx.annotation.Nullable;

import com.blitzfud.controllers.utilities.BlitzfudConstants;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Market extends RealmObject {

    @PrimaryKey
    private String _id;
    private String name;
    private RealmList<Product> products;
    private String deliveryMethods;
    private double deliveryPrice;
    private String marketStatus;

    public Market() {
    }

    public Market(String _id, String name, String deliveryMethods, String marketStatus) {
        this._id = _id;
        this.name = name;
        this.products = new RealmList<>();
        this.deliveryMethods = deliveryMethods;
        this.marketStatus = marketStatus;
    }

    public String getMarketStatus() {
        return marketStatus;
    }

    public void setMarketStatus(String marketStatus) {
        this.marketStatus = marketStatus;
    }

    public RealmList<Product> getProducts() {
        return products;
    }

    public void setProducts(RealmList<Product> products) {
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

    public String getDeliveryMethods() {
        return deliveryMethods;
    }

    public String getTagDelivery(){
        if(hasBoth())
            return "Recojo en tienda y Delivery";

        if(hasPickup())
            return "Recojo en tienda";

        return "Con Delivery";
    }

    public void setDeliveryMethods(String deliveryMethods) {
        this.deliveryMethods = deliveryMethods;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public String getDeliveryPriceString(){
        return String.format("S/ %.2f",deliveryPrice);
    }

    public void setDeliveryPrice(double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public boolean hasDelivery(){
        return deliveryMethods.equals(BlitzfudConstants.DELIVERY);
    }

    public boolean hasBoth(){
        return deliveryMethods.equals(BlitzfudConstants.BOTH);
    }

    public boolean hasPickup(){
        return deliveryMethods.equals(BlitzfudConstants.PICK_UP);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        final Market market = (Market) obj;
        return this._id.equals(market._id);
    }

}