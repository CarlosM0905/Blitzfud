package com.blitzfud.controllers.localDB.providers;

import com.blitzfud.models.market.FavoriteMarket;
import com.blitzfud.models.responseAPI.FavoriteMarketSet;

import io.realm.Realm;

public class FavoriteMarketsDBProvider {

    public static void save(final Realm realm, final FavoriteMarketSet favoriteMarketSet) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(favoriteMarketSet);
        realm.commitTransaction();
    }

    public static FavoriteMarketSet getFavoriteMarkets(Realm realm) {
        return realm.where(FavoriteMarketSet.class).equalTo("_id", 1).findFirst();
    }

    public static void add(final Realm realm, final FavoriteMarketSet favoriteMarketSet, FavoriteMarket market){
        realm.beginTransaction();
        favoriteMarketSet.getMarkets().add(market);
        realm.commitTransaction();
    }

    public static void remove(final Realm realm, final FavoriteMarketSet favoriteMarketSet, FavoriteMarket market){
        realm.beginTransaction();
        favoriteMarketSet.removeMarket(market);
        realm.commitTransaction();
    }

}
