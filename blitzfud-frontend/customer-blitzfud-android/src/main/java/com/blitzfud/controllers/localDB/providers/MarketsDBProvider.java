package com.blitzfud.controllers.localDB.providers;

import com.blitzfud.models.responseAPI.MarketSet;

import io.realm.Realm;

public class MarketsDBProvider {

    public static void save(final Realm realm, final MarketSet marketSet) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(marketSet);
        realm.commitTransaction();
    }

    public static MarketSet getMarketSet(final Realm realm){
        return realm.where(MarketSet.class).equalTo("_id", 1).findFirst();
    }

    public static void update(final Realm realm, final MarketSet original, final MarketSet copy){
        realm.beginTransaction();
        original.setCount(copy.getCount());
        original.getMarkets().clear();
        original.getMarkets().addAll(copy.getMarkets());
        realm.copyFromRealm(original);
        realm.commitTransaction();
    }

}
