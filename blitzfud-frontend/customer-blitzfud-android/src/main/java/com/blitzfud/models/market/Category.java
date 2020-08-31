package com.blitzfud.models.market;

import com.blitzfud.R;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Category extends RealmObject {

    private static final Map<String, Integer> categories = new HashMap<>();

    @PrimaryKey
    private String _id;
    private String name;

    public Category() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public static int getDrawableId(final String name) {
        if (categories.isEmpty()) initialize();

        return categories.get(name);
    }

    private static void initialize() {
        categories.put("Lacteos y huevos", R.drawable.ic_dairy_product);
        categories.put("Limpieza", R.drawable.ic_liquid_soap);
        categories.put("Cuidado personal", R.drawable.ic_shower);
        categories.put("Frutas y verduras", R.drawable.ic_healthy_food);
        categories.put("Carnes y aves", R.drawable.ic_meat);
        categories.put("Abarrotes", R.drawable.ic_flour);
        categories.put("Bebidas", R.drawable.ic_plastic_bottle);
        categories.put("Panes", R.drawable.ic_bread);
        categories.put("Mascotas", R.drawable.ic_dog);
        categories.put("Embutidos", R.drawable.ic_sausages);
        categories.put("Confiteria", R.drawable.ic_candy);
        categories.put("Congelados", R.drawable.ic_seafood);
        categories.put("Baby", R.drawable.ic_baby);
        categories.put("Otros", R.drawable.ic_others);
    }
}
