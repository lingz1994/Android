package com.example.lingez.sample_app.Data;

/**
 * Created by lingez on 1/22/18.
 */

public class Item {
    private String it_id;
    private String it_name;
    private String it_weight;
    private String it_expdate;
    private String it_triglvl;
    private String it_cat;
    private String it_qty;
    private String it_untpr;
    private boolean it_imp;

    public Item(){}

    public Item(String item, String weight, String expdate){
        this.it_name = item;
        this.it_weight = weight;
        this.it_expdate = expdate;
    }

    public boolean isIt_imp() {
        return it_imp;
    }

    public void setIt_imp(boolean it_imp) {
        this.it_imp = it_imp;
    }

    public String getIt_cat() {
        return it_cat;
    }

    public void setIt_cat(String it_cat) {
        this.it_cat = it_cat;
    }

    public String getIt_qty() {
        return it_qty;
    }

    public void setIt_qty(String it_qty) {
        this.it_qty = it_qty;
    }

    public String getIt_untpr() {
        return it_untpr;
    }

    public void setIt_untpr(String it_untpr) {
        this.it_untpr = it_untpr;
    }

    public String getIt_triglvl() {
        return it_triglvl;
    }

    public void setIt_triglvl(String it_triglvl) {
        this.it_triglvl = it_triglvl;
    }

    public String getIt_id() {
        return it_id;
    }

    public void setIt_id(String it_id) {
        this.it_id = it_id;
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
