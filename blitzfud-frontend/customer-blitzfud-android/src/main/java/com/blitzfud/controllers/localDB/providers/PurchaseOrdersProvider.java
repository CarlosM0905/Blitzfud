package com.blitzfud.controllers.localDB.providers;

import com.blitzfud.models.responseAPI.PurchaseOrdersSet;

import io.realm.Realm;

public class PurchaseOrdersProvider {

    public static void save(final Realm realm, final PurchaseOrdersSet purchaseOrdersSet) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(purchaseOrdersSet);
        realm.commitTransaction();
    }

    public static PurchaseOrdersSet getPurchaseOrders(final Realm realm){
        return realm.where(PurchaseOrdersSet.class).equalTo("_id", 1).findFirst();
    }

}
