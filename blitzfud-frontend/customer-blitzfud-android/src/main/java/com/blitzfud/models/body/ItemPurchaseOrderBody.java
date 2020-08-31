package com.blitzfud.models.body;

public class ItemPurchaseOrderBody {
    private final String product;
    private final int quantity;

    public ItemPurchaseOrderBody(String product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public String getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
