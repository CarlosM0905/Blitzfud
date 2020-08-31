package com.blitzfud.models.body;

import com.blitzfud.controllers.restapi.services.AuthService;
import com.blitzfud.controllers.utilities.BlitzfudConstants;

import java.util.List;

public class PurchaseOrderBody {
    private final List<ItemPurchaseOrderBody> items;
    private final String deliveryMethod;
    private final LocationAPI deliveryPoint;

    public PurchaseOrderBody(List<ItemPurchaseOrderBody> items, boolean deliveryMethod) {
        this.items = items;
        this.deliveryMethod = deliveryMethod ? BlitzfudConstants.DELIVERY : BlitzfudConstants.PICK_UP;
        if(deliveryMethod) deliveryPoint = AuthService.getUser().getLocation();
        else deliveryPoint = null;
    }

    public List<ItemPurchaseOrderBody> getItems() {
        return items;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public LocationAPI getDeliveryPoint() {
        return deliveryPoint;
    }
}
