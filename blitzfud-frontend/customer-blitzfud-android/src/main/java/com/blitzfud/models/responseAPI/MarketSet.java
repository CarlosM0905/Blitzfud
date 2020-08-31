package com.blitzfud.models.responseAPI;

import com.blitzfud.models.market.Market;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MarketSet extends RealmObject {

    @PrimaryKey
    private int _id = 1;
    private int count;
    private RealmList<Market> markets;

    public MarketSet() {
    }

    public MarketSet(int count, RealmList<Market> markets) {
        this.count = count;
        this.markets = markets;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public RealmList<Market> getMarkets() {
        return markets;
    }

    public void setMarkets(RealmList<Market> markets) {
        this.markets = markets;
    }
}
