package com.blitzfud.controllers.utilities;

import java.text.SimpleDateFormat;

public class BlitzfudConstants {
    /* FRAGMENTS */
    public static final String FRAGMENT_HOME = "home";
    public static final String FRAGMENT_FAVORITES = "favorites";
    public static final String FRAGMENT_ORDERS = "orders";
    public static final String FRAGMENT_CART = "cart";
    public static final String FRAGMENT_ACCOUNT = "account";
    public static final String FRAGMENT_MARKET = "market";
    public static final String FRAGMENT_PRODUCTS = "products";
    public static final String FRAGMENT_EXECUTE_MARKET = "executeMarket";
    public static final String FRAGMENT_EXECUTE_SHOPPING_CART = "shoppingCart";
    public static final String FRAGMENT_MY_PROFILE = "myProfile";

    /* ACTIVITIES RESULTS */
    public static final int MAP_ACTIVITY_RESULT = 137;

    /* DELIVERY METHODS IN ORDER */
    public static final String PICK_UP = "pickup";
    public static final String PICK_UP_SPANISH = "En tienda";
    public static final String DELIVERY = "delivery";
    public static final String DELIVERY_SPANISH = "Delivery";
    public static final String BOTH = "both";

    /* STATUS OF ORDER */
    public static final String PREPROCESSING = "preprocessing";
    public static final String PREPROCESSING_SPANISH = "Esperando respuesta";
    public static final String IN_PROGRESS = "in-progress";
    public static final String IN_PROGRESS_SPANISH = "Aceptado";

    /* DATE FORMAT */
    public static final SimpleDateFormat SDF_FULL_DATE = new SimpleDateFormat("dd/MM/yy hh:mm a");
    public static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("dd/MM/yy");
    public static final SimpleDateFormat SDF_DATE_PURCHASE = new SimpleDateFormat("MM-dd-yyyy");

    /* STATUS MARKET */
    public static final String MARKET_OPEN = "open";
    public static final String MARKET_CLOSE = "close";

    /* SHOPPING CART */
    public static final int ADD_ITEM_SHOPPING_CART = 0;
    public static final int UPDATE_ITEM_SHOPPING_CART = 1;
    public static final int REMOVE_ITEM_SHOPPING_CART = 2;

    /* OTHERS */
    public static final int ITEMS_PER_PAGE = 5;

    /* TOKEN EXPIRED */
    public static final String TOKEN_FAILED = "Autenticación ha fallado, inicie sesión";

}
