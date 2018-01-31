package com.example.lingez.sample_app.Data;

/**
 * Created by lingez on 1/28/18.
 */

public class ShopList {
    private boolean itemCheck;
    private String itemName;
    private String itemCategory;
    private String itemQuantity;
    private String itemTotalPrice;

    public ShopList(Boolean itemCheck, String itemName, String itemCategory, String itemQuantity, String itemTotalPrice) {
        this.itemCheck = itemCheck;
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemQuantity = itemQuantity;
        this.itemTotalPrice = itemTotalPrice;
    }

    public ShopList() {
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

    public String getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(String itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }

    public Boolean getItemCheck() {
        return itemCheck;
    }

    public void setItemCheck(boolean itemCheck) {
        this.itemCheck = itemCheck;
    }
}
