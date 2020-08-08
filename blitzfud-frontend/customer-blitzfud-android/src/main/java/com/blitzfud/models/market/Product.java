package com.blitzfud.models.market;

public class Product {
    private String _id;
    private String name;
    private String description;
    private String unitOfMeasurement;
    private int quantityOfProduct;
    private double price;

    public Product() {
    }

    public Product(String _id, String name, String description, String unitOfMeasurement, int quantityOfProduct, double price) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.unitOfMeasurement = unitOfMeasurement;
        this.quantityOfProduct = quantityOfProduct;
        this.price = price;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public int getQuantityOfProduct() {
        return quantityOfProduct;
    }

    public void setQuantityOfProduct(int quantityOfProduct) {
        this.quantityOfProduct = quantityOfProduct;
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
        return quantityOfProduct + " " + unitOfMeasurement;
    }

}
