package com.blitzfud.models.responseAPI;

import com.blitzfud.models.order.PurchaseOrders;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PurchasesSet extends RealmObject {
    @PrimaryKey
    private int _id = 1;
    private int count;
    private RealmList<PurchaseOrders> purchases;

    public PurchasesSet() {
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

    public RealmList<PurchaseOrders> getPurchaseOrders() {
        return purchases;
    }

    public void setPurchaseOrders(RealmList<PurchaseOrders> purchases) {
        this.purchases = purchases;
    }
}
