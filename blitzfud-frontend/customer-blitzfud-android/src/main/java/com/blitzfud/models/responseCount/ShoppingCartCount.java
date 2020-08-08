package com.blitzfud.models.responseCount;

import com.blitzfud.models.shoppingCart.ItemShoppingCart;
import com.blitzfud.models.shoppingCart.ShoppingCart;

import java.util.ArrayList;

public class ShoppingCartCount {
    private int count;
    private ArrayList<ShoppingCart> subcarts;
    private double total;

    public ShoppingCartCount() {
    }

    public ShoppingCartCount(int count, ArrayList<ShoppingCart> subcarts) {
        this.count = count;
        this.subcarts = subcarts;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<ShoppingCart> getSubcarts() {
        return subcarts;
    }

    public void setSubcarts(ArrayList<ShoppingCart> subcarts) {
        this.subcarts = subcarts;
    }

    public double getTotal(){
        if(total == 0){
            for (int i = 0; i < subcarts.size(); i++) {
                final ShoppingCart subcart = subcarts.get(i);

                total += subcart.getTotal();
            }
        }

        return total;
    }
}
