package com.blitzfud.models.order;

import com.blitzfud.models.body.LocationAPI;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MarketName extends RealmObject {

    @PrimaryKey
    private String _id;
    private String name;
    private LocationAPI location;

    public MarketName() {
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

    public LocationAPI getLocation() {
        return location;
    }

    public void setLocation(LocationAPI location) {
        this.location = location;
    }
}
