package com.blitzfud.models.responseAPI;

import com.blitzfud.models.market.Category;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CategorySet extends RealmObject {

    @PrimaryKey
    private int _id = 1;
    private int count;
    private RealmList<Category> categories;

    public CategorySet() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setCategories(RealmList<Category> categories) {
        this.categories = categories;
    }

    public RealmList<Category> getCategories() {
        return categories;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
