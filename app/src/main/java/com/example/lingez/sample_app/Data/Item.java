package com.example.lingez.sample_app.Data;

/**
 * Created by lingez on 1/22/18.
 */

public class Item {
    private String it_name;
    private String it_weight;
    private String it_expdate;

    public Item(){

    }

    public Item(String item, String weight, String expdate){
        this.it_name = item;
        this.it_weight = weight;
        this.it_expdate = expdate;
    }

    public String getIt_name() {
        return it_name;
    }

    public void setIt_name(String it_name) {
        this.it_name = it_name;
    }

    public String getIt_weight() {
        return it_weight;
    }

    public void setIt_weight(String it_weight) {
        this.it_weight = it_weight;
    }

    public String getIt_expdate() {
        return it_expdate;
    }

    public void setIt_expdate(String it_expdate) {
        this.it_expdate = it_expdate;
    }
}
