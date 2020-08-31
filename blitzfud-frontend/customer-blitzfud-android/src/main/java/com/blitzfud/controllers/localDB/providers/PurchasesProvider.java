package com.blitzfud.controllers.localDB.providers;

import com.blitzfud.models.responseAPI.PurchasesSet;

import io.realm.Realm;

public class PurchasesProvider {
    public static void save(final Realm realm, final PurchasesSet purchasesSet) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(purchasesSet);
        realm.commitTransaction();
    }

    public static PurchasesSet getPurchases(final Realm realm){
        return realm.where(PurchasesSet.class).equalTo("_id", 1).findFirst();
    }
}
