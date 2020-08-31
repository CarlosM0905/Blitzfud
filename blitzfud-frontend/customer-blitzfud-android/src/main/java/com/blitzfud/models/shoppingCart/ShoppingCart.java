package com.blitzfud.models.shoppingCart;

import com.blitzfud.controllers.localDB.DBConnection;
import com.blitzfud.models.body.ItemPurchaseOrderBody;
import com.blitzfud.models.body.PurchaseOrderBody;
import com.blitzfud.models.market.Market;
import com.blitzfud.models.market.Product;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ShoppingCart extends RealmObject {

    @PrimaryKey
    private long _id;
    private Market market;
    private RealmList<ItemShoppingCart> items;
    private double total;
    private boolean delivery;

    public ShoppingCart() {
        this._id = DBConnection.shoppingCartDBId.incrementAndGet();
    }

    public ShoppingCart(Market market, RealmList<ItemShoppingCart> items) {
        this._id = DBConnection.shoppingCartDBId.incrementAndGet();
        this.market = market;
        this.items = items;
    }

    public ShoppingCart(Market market, Product product, int quantity) {
        this._id = DBConnection.shoppingCartDBId.incrementAndGet();
        this.market = market;
        this.items = new RealmList<>();
        this.items.add(new ItemShoppingCart(product, quantity));
        this.total = product.getPrice() * quantity;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public RealmList<ItemShoppingCart> getItems() {
        return items;
    }

    public void setItems(RealmList<ItemShoppingCart> items) {
        this.items = items;
    }

    public double getTotal() {
        return total + (delivery ? market.getDeliveryPrice() : 0);
    }

    public String getTotalWithoutDeliveryString() {
        return String.format("S/ %.2f", total);
    }

    public void setTotal() {
        if (this.total == 0) {
            for (int i = 0; i < items.size(); i++) {
                ItemShoppingCart item = items.get(i);
                item.setTotal();
                this.total += item.getTotal();
            }
        }
    }

    private void updateTotal() {
        this.total = 0;

        for (int i = 0; i < items.size(); i++) {
            ItemShoppingCart item = items.get(i);
            item.setTotal();
            this.total += item.getTotal();
        }
    }

    public String getTotalString() {
        return String.format("S/ %.2f", getTotal());
    }

    public int getQuantityItem(final String productId) {
        final int position = findItemShoppingCart(productId);

        return position == -1 ? 0 : items.get(position).getQuantity();
    }

    public double getSubtotalItem(final String productId) {
        for (int i = 0; i < items.size(); i++) {
            ItemShoppingCart item = items.get(i);

            if (item.getProduct().get_id().equals(productId)) {
                return item.getTotal();
            }
        }

        return 0;
    }

    public void addItemShoppingCart(final Product product, final int quantity) {
        items.add(new ItemShoppingCart(product, quantity));
        total += product.getPrice() * quantity;
    }

    public void updateItemShoppingCart(final Product product, final int quantity) {
        final int position = findItemShoppingCart(product.get_id());

        if (position != -1) {
            total -= getSubtotalItem(product.get_id());
            items.set(position, new ItemShoppingCart(product, quantity));
            total += product.getPrice() * quantity;
        }
    }

    public void removeItemShoppingCart(final Product product) {
        final int position = findItemShoppingCart(product.get_id());

        if (position != -1) {
            total -= getSubtotalItem(product.get_id());
            items.remove(position);
        }
    }

    public int findItemShoppingCart(final String productId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProduct().get_id().equals(productId)) {
                return i;
            }
        }

        return -1;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;

        updateTotal();
    }

    public List<PurchaseOrderBody> toOrderList() {
        List<PurchaseOrderBody> orderBodies = new ArrayList<>();

        orderBodies.add(new PurchaseOrderBody(itemsToItemOrder(), delivery));

        return orderBodies;
    }

    public PurchaseOrderBody toOrder() {
        return new PurchaseOrderBody(itemsToItemOrder(), delivery);
    }

    public List<ItemPurchaseOrderBody> itemsToItemOrder() {
        List<ItemPurchaseOrderBody> itemOrderBodies = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            itemOrderBodies.add(new ItemPurchaseOrderBody(items.get(i).getProduct().get_id(), items.get(i).getQuantity()));
        }

        return itemOrderBodies;
    }

}
