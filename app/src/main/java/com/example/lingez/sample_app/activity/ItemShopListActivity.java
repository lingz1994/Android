package com.example.lingez.sample_app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lingez.sample_app.Data.ShopList;
import com.example.lingez.sample_app.Data.ShopListParent;
import com.example.lingez.sample_app.R;
import com.example.lingez.sample_app.RequestQueueSingleton;
import com.example.lingez.sample_app.adapter.ShopListRecyclerAdapter;
import com.example.lingez.sample_app.fragment.ShopListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItemShopListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    private String shopListURL = "http://192.168.0.182:3000/shop_list_parents";

    TextView listname, date;
    EditText budget;

    List<ShopListParent> shopListParentL;
    List<ShopList> shopList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_shop_list);

        Intent intent = getIntent();
        String parentID = intent.getStringExtra("parentID");
        Log.d("getadapt", "onCreate: " + parentID);

        recyclerView = findViewById(R.id.isl_itemlist_rv);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        shopList = new ArrayList<>();

        listname = findViewById(R.id.isl_listname);

        date = findViewById(R.id.isl_date);

        budget = findViewById(R.id.isl_budget_field);

        fetchParentData(parentID);
    }

    private void fetchParentData(final String parentID) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, shopListURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        if(parentID.equals(jsonObject.getString("_id"))){

                            listname.setText(jsonObject.getString("sl_listname"));
                            date.setText(jsonObject.getString("sl_date"));
                            budget.setText(jsonObject.getString("sl_budget"));
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSONArray", "onErrorResponse: ");
            }
        });

        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }
}
