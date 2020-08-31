package com.blitzfud.controllers.localDB.providers;

import com.blitzfud.models.market.Market;
import com.blitzfud.models.market.Product;
import com.blitzfud.models.responseAPI.ShoppingCartSet;

import io.realm.Realm;

public class ShoppingCartDBProvider {

    public static void clear(final Realm realm, final ShoppingCartSet shoppingCartSet) {
        realm.beginTransaction();
        shoppingCartSet.setCount(0);
        shoppingCartSet.getSubcarts().clear();
        shoppingCartSet.setTotal(0);
        realm.commitTransaction();
    }

    public static void save(final Realm realm, final ShoppingCartSet shoppingCartSet) {
        realm.beginTransaction();
        final ShoppingCartSet data = getShoppingCartSet(realm);

        if (data != null) {
            shoppingCartSet.recoverDelivery(data);
        }

        shoppingCartSet.updateTotal();
        realm.copyToRealmOrUpdate(shoppingCartSet);

        realm.commitTransaction();
    }

    public static void clearMarket(final Realm realm, final String marketId) {
        realm.beginTransaction();
        final ShoppingCartSet data = getShoppingCartSet(realm);
        data.removeShoppingCart(marketId);
        realm.commitTransaction();
    }

    public static ShoppingCartSet getShoppingCartSet(Realm realm) {
        return realm.where(ShoppingCartSet.class).equalTo("_id", 1).findFirst();
    }

    public static void addItem(final Realm realm, final ShoppingCartSet shoppingCartSet, final int quantity, final Product product, final Market market) {
        realm.beginTransaction();
        shoppingCartSet.addItem(quantity, product, market);
        realm.copyToRealmOrUpdate(shoppingCartSet);
        realm.commitTransaction();
    }

    public static void updateItem(final Realm realm, final ShoppingCartSet shoppingCartSet, final int quantity, final Product product, final Market market) {
        realm.beginTransaction();
        shoppingCartSet.updateItem(quantity, product, market);
        realm.copyToRealmOrUpdate(shoppingCartSet);
        realm.commitTransaction();
    }

    public static void removeItem(final Realm realm, final ShoppingCartSet shoppingCartSet, final Product product, final Market market) {
        realm.beginTransaction();
        shoppingCartSet.removeItem(product, market);
        realm.copyToRealmOrUpdate(shoppingCartSet);
        realm.commitTransaction();
    }

    public static void updateDelivery(final Realm realm, final ShoppingCartSet shoppingCartSet, final String marketId, final boolean delivery) {
        realm.beginTransaction();
        shoppingCartSet.updateDelivery(marketId, delivery);
        shoppingCartSet.updateTotal();
        realm.copyToRealmOrUpdate(shoppingCartSet);
        realm.commitTransaction();
    }

}
