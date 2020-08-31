package com.blitzfud.models.auth;

import com.blitzfud.models.body.LocationAPI;
import com.google.android.gms.maps.model.LatLng;

public class User {
    private String _id;
    private String phoneNumber;
    private String email;
    private String firstName;
    private String lastName;
    private final String password;
    private LocationAPI location;

    public User() {
        this.password = "";
    }

//    public User(String _id, String email, String firstName, String lastName) {
//        this._id = _id;
//        this.email = email;
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }

    public User(String phoneNumber, String email, String firstName, String lastName, String password, LocationAPI location) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.location = location;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocationAPI getLocation() {
        return location;
    }

    public LatLng getLocationLatLng(){
        return new LatLng(location.getCoordinates().get(1), location.getCoordinates().get(0));
    }

    public void setLocation(LocationAPI location) {
        this.location = location;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String get_id() {
        return _id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
