package com.example.lingez.sample_app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.lingez.sample_app.Data.ShopList;
import com.example.lingez.sample_app.R;

import java.util.ArrayList;
import java.util.List;

public class ItemShopListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    TextView listname, date, budget;

    List<ShopList> shopList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_shop_list);

        Intent intent = getIntent();

        recyclerView = findViewById(R.id.isl_itemlist_rv);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        shopList = new ArrayList<>();

        listname = findViewById(R.id.isl_listname);
        String lname = intent.getStringExtra("listname");
        listname.setText(lname);

        date = findViewById(R.id.isl_date);
        String ldate = intent.getStringExtra("listdate");
        date.setText(ldate);

        budget = findViewById(R.id.isl_budget);
        String lbdgt = intent.getStringExtra("listbudget");
        budget.setText(lbdgt);
    }
}
