package com.blitzfud.models.responseAPI;

import com.blitzfud.models.order.Order;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class OrderSet extends RealmObject {
    @PrimaryKey
    private int _id = 1;
    private int count;
    private RealmList<Order> orders;

    public OrderSet() {
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

    public RealmList<Order> getOrders() {
        return orders;
    }

    public void setOrders(RealmList<Order> orders) {
        this.orders = orders;
    }

}
