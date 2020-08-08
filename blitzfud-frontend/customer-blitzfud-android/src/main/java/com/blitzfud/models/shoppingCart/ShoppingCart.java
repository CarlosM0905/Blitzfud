package com.blitzfud.models.shoppingCart;

import com.blitzfud.models.market.Market;
import com.blitzfud.models.market.Product;

import java.util.ArrayList;

public class ShoppingCart {
    private Market market;
    private ArrayList<ItemShoppingCart> items;
    private double total;
    private boolean delivery;

    public ShoppingCart() {
    }

    public ShoppingCart(Market market, ArrayList<ItemShoppingCart> items) {
        this.market = market;
        this.items = items;
    }

    public ShoppingCart(Market market, Product product, int quantity) {
        this.market = market;
        this.items = new ArrayList<>();
        this.items.add(new ItemShoppingCart(product, quantity));
        this.total = product.getPrice() * quantity;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public ArrayList<ItemShoppingCart> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemShoppingCart> items) {
        this.items = items;
    }

    public double getTotal() {
        if (total == 0) {
            for (int i = 0; i < items.size(); i++) {
                ItemShoppingCart item = items.get(i);

                total += item.getTotal();
            }
        }

        return total;
    }

    public String getTotalString() {
        return String.format("S/ %.2f", getTotal());
    }

    public int getQuantityItem(final String productId){
        final int position = findItemShoppingCart(productId);

        return position == -1? 0: items.get(position).getQuantity();
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

    public void removeItemShoppingCart(final Product product){
        final int position = findItemShoppingCart(product.get_id());

        if(position != -1){
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
    }
}
