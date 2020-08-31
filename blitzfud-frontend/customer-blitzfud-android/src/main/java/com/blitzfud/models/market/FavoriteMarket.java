package com.blitzfud.models.market;

import androidx.annotation.Nullable;

import com.blitzfud.controllers.utilities.BlitzfudConstants;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FavoriteMarket extends RealmObject {

    @PrimaryKey
    private String _id;
    private String name;
    private String marketStatus;
    private String deliveryMethods;

    public FavoriteMarket() {
    }

    public FavoriteMarket(String _id) {
        this._id = _id;
    }

    public FavoriteMarket(String _id, String name, String deliveryMethods) {
        this._id = _id;
        this.name = name;
        this.marketStatus = BlitzfudConstants.MARKET_OPEN;
        this.deliveryMethods = deliveryMethods;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarketStatus() {
        return marketStatus;
    }

    public void setMarketStatus(String marketStatus) {
        this.marketStatus = marketStatus;
    }

    public String getDeliveryMethods() {
        return deliveryMethods;
    }

    public void setDeliveryMethods(String deliveryMethods) {
        this.deliveryMethods = deliveryMethods;
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

    public boolean isOpen(){return marketStatus.equals(BlitzfudConstants.MARKET_OPEN);}

    @Override
    public boolean equals(@Nullable Object obj) {
        final FavoriteMarket market = (FavoriteMarket) obj;
        return this._id.equals(market._id);
    }
}
