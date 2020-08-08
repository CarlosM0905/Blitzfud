package com.blitzfud.models;

public class User {
    private String _id;
    private String email;
    private String firstName;
    private String lastName;

    public User(String _id, String email, String firstName, String lastName) {
        this._id = _id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
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
}
