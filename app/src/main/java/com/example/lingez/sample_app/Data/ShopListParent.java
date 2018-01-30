package com.example.lingez.sample_app.Data;

/**
 * Created by lingez on 1/29/18.
 */

public class ShopListParent {
    private String parentID;
    private String listName;
    private String date;
    private String budget;

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

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public ShopListParent(String listName, String date, String parentID, String budget) {
        this.listName = listName;
        this.date = date;
        this.parentID = parentID;
        this.budget = budget;
    }

    public ShopListParent() {
    }
}
