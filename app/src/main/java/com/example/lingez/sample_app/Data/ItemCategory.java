package com.example.lingez.sample_app.Data;

/**
 * Created by lingez on 1/26/18.
 */

public class ItemCategory {
    private String category;
    private String itemname;

    public ItemCategory() {
    }

    public ItemCategory(String category, String itemname) {
        this.category = category;
        this.itemname = itemname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }
}
