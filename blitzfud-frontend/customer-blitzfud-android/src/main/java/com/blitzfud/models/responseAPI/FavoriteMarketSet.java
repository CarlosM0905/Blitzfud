package com.blitzfud.models.responseAPI;

import com.blitzfud.models.market.FavoriteMarket;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FavoriteMarketSet extends RealmObject {

    @PrimaryKey
    private int _id = 1;
    private int count;
    private RealmList<FavoriteMarket> markets;

    public FavoriteMarketSet() {
    }

    public FavoriteMarketSet(int count, RealmList<FavoriteMarket> markets) {
        this.count = count;
        this.markets = markets;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public RealmList<FavoriteMarket> getMarkets() {
        return markets;
    }

    public void setMarkets(RealmList<FavoriteMarket> markets) {
        this.markets = markets;
    }

    public boolean existsMarket(final String marketId) {
        return findMarket(marketId) != -1;
    }

    private int findMarket(final String marketId){
        for (int i = 0; i < markets.size(); i++) {
            if(markets.get(i).get_id().equals(marketId)){
                return i;
            }
        }

        return -1;
    }

    public void removeMarket(final FavoriteMarket market){
        int position = findMarket(market.get_id());

        if(position!= -1){
            markets.remove(position);
        }
    }

}
