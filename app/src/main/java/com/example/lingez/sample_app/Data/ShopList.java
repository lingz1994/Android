package com.example.lingez.sample_app.Data;

/**
 * Created by lingez on 1/28/18.
 */

public class ShopList {
    private Boolean itemCheck;
    private String itemName;
    private String itemCategory;
    private String itemQuantity;
    private String itemUnitPrice;

    public ShopList(Boolean itemCheck, String itemName, String itemCategory, String itemQuantity, String itemUnitPrice) {
        this.itemCheck = itemCheck;
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemQuantity = itemQuantity;
        this.itemUnitPrice = itemUnitPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemUnitPrice() {
        return itemUnitPrice;
    }

    public void setItemUnitPrice(String itemUnitPrice) {
        this.itemUnitPrice = itemUnitPrice;
    }

    public Boolean getItemCheck() {
        return itemCheck;
    }

    public void setItemCheck(Boolean itemCheck) {
        this.itemCheck = itemCheck;
    }
}
