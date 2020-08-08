package com.blitzfud.models.responseCount;

import com.blitzfud.models.market.Category;

import java.util.ArrayList;

public class CategoryCount {
    private int count;
    private ArrayList<Category> categories;

    public CategoryCount() {
    }

    public CategoryCount(int count, ArrayList<Category> categories) {
        this.count = count;
        this.categories = categories;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}
