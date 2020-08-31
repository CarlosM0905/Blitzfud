package com.blitzfud.models.responseAPI;

import com.blitzfud.models.body.PurchaseOrderBody;
import com.blitzfud.models.market.Market;
import com.blitzfud.models.market.Product;
import com.blitzfud.models.shoppingCart.ShoppingCart;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ShoppingCartSet extends RealmObject {

    @PrimaryKey
    private int _id = 1;
    private int count;
    private RealmList<ShoppingCart> subcarts;
    private double total;

    public ShoppingCartSet() {
    }

    public ShoppingCartSet(int count, RealmList<ShoppingCart> subcarts) {
        this.count = count;
        this.subcarts = subcarts;
    }

    public int get_id() {
        return _id;
    }

    public void updateTotal() {
//        if (total == 0) {
        total = 0;
        for (int i = 0; i < subcarts.size(); i++) {
            final ShoppingCart subcart = subcarts.get(i);
            subcart.setTotal();
            total += subcart.getTotal();
        }
//        }
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void recoverDelivery(ShoppingCartSet dataCopy) {
        if (subcarts != null) {
            for (int i = 0; i < subcarts.size(); i++) {
                ShoppingCart subcart = subcarts.get(i);

                for (int j = 0; j < dataCopy.getSubcarts().size(); j++) {
                    ShoppingCart item = dataCopy.getSubcarts().get(j);

                    if(subcart.getMarket().get_id().equals(item.getMarket().get_id())){
                        subcart.setDelivery(item.isDelivery());
                        break;
                    }
                }
            }
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public RealmList<ShoppingCart> getSubcarts() {
        return subcarts;
    }

    public void setSubcarts(RealmList<ShoppingCart> subcarts) {
        this.subcarts = subcarts;
    }

    public double getTotal() {
        return total;
    }

    public String getTotalString() {
        return String.format("S/ %.2f", total);
    }

    public ShoppingCart getShoppingCart(final String marketId) {
        final int position = findMarket(marketId);

        if (position == -1) return null;

        return subcarts.get(position);
    }

    public int getQuantity(final String productId, final String marketId) {
        if (subcarts == null) return 0;

        return findQuantityProduct(productId, marketId);
    }

    public void removeShoppingCart(final String marketId) {
        final int position = findMarket(marketId);

        if (position != -1) {
            total -= subcarts.get(position).getTotal();
            subcarts.remove(position);
        }
    }

    private int findQuantityProduct(final String productId, final String marketId) {
        final int position = findMarket(marketId);

        if (position == -1) return 0;

        return subcarts.get(position).getQuantityItem(productId);
    }

    private int findMarket(final String marketId) {
        int position = -1;

        for (int i = 0; i < subcarts.size(); i++) {
            final ShoppingCart item = subcarts.get(i);

            if (item.getMarket().get_id().equals(marketId)) {
                position = i;
                break;
            }
        }

        return position;
    }

    public void addItem(final int quantity, final Product product, final Market market) {
        int positionMarket = findMarket(market.get_id());

        if (positionMarket == -1) {
            subcarts.add(new ShoppingCart(market, product, quantity));
        } else {
            subcarts.get(positionMarket).addItemShoppingCart(product, quantity);
        }

        total += quantity * product.getPrice();
    }

    public void updateItem(final int quantity, final Product product, final Market market) {
        final int position = findMarket(market.get_id());

        if (position != -1) {
            ShoppingCart shoppingCart = subcarts.get(position);
            total -= shoppingCart.getSubtotalItem(product.get_id());
            subcarts.get(position).updateItemShoppingCart(product, quantity);
            total += quantity * product.getPrice();
        }
    }

    public void removeItem(final Product product, final Market market) {
        final int position = findMarket(market.get_id());

        if (position != -1) {
            ShoppingCart shoppingCart = subcarts.get(position);
            total -= shoppingCart.getSubtotalItem(product.get_id());
            shoppingCart.removeItemShoppingCart(product);

            if (shoppingCart.getItems().isEmpty()) {
                subcarts.remove(position);
            }
        }
    }

    public void updateDelivery(final String marketId, final boolean delivery) {
        final int position = findMarket(marketId);

        if (position != -1) {
            subcarts.get(position).setDelivery(delivery);
        }
    }

    public List<PurchaseOrderBody> toOrderList() {
        List<PurchaseOrderBody> orderBodies = new ArrayList<>();

        for (int i = 0; i < subcarts.size(); i++) {
            orderBodies.add(subcarts.get(i).toOrder());
        }

        return orderBodies;
    }

}
