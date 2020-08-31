package com.blitzfud.models.market;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Product extends RealmObject {

    @PrimaryKey
    private String _id;
    private String name;
    private String unitOfMeasurement;
    private double content;
    private double price;
    private int maxQuantityPerOrder;

    public Product() {
    }

    public Product(String _id, String name, String unitOfMeasurement, int content, double price) {
        this._id = _id;
        this.name = name;
        this.unitOfMeasurement = unitOfMeasurement;
        this.content = content;
        this.price = price;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getMaxQuantityPerOrder() {
        return maxQuantityPerOrder;
    }

    public void setMaxQuantityPerOrder(int maxQuantityPerOrder) {
        this.maxQuantityPerOrder = maxQuantityPerOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public double getContent() {
        return content;
    }

    public void setContent(double content) {
        this.content = content;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPriceString() {
        return String.format("S/ %.2f", price);
    }

    public String getInformation() {
        return contentToString() + " " + unitOfMeasurement;
    }

    private String contentToString() {
        if (Math.floor(content) == content) {
            return String.valueOf((int) content);
        }

        return String.valueOf(content);
    }

}
