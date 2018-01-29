package com.example.lingez.sample_app.Data;

/**
 * Created by lingez on 1/29/18.
 */

public class ShopListParent {
    private String listName;
    private String date;
    private int budget;

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public ShopListParent(String listName, String date, int budget) {
        this.listName = listName;
        this.date = date;
        this.budget = budget;
    }

    public ShopListParent() {
    }
}
