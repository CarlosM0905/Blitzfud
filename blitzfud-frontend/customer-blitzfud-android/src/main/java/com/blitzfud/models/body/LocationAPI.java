package com.blitzfud.models.body;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LocationAPI extends RealmObject {

    @PrimaryKey
    private String _id;
    private String address;
    private RealmList<Double> coordinates;

    public LocationAPI() {
    }

    public LocationAPI(String address, double latitude, double longitude){
        this.address = address;
        coordinates = new RealmList<>();
        coordinates.add(longitude);
        coordinates.add(latitude);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(RealmList<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
