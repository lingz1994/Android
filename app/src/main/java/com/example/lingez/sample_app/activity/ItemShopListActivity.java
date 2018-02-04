package com.example.lingez.sample_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lingez.sample_app.Data.ShopList;
import com.example.lingez.sample_app.R;
import com.example.lingez.sample_app.RequestQueueSingleton;
import com.example.lingez.sample_app.adapter.ShopListRecyclerAdapter;

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
    EditText budget, itemname, itemcategory, itemquantity, itemunitprice;
    CheckBox checklist;

    List<ShopList> shopLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_shop_list);

        Intent intent = getIntent();
        final String parentID = intent.getStringExtra("parentID");

        recyclerView = findViewById(R.id.isl_itemlist_rv);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        shopLists = new ArrayList<>();

        listname = findViewById(R.id.isl_listname);
        date = findViewById(R.id.isl_date);
        budget = findViewById(R.id.isl_budget_field);
        itemname = findViewById(R.id.isl_itemname_field);
        itemcategory = findViewById(R.id.isl_itemcategory_field);
        itemquantity = findViewById(R.id.isl_quantity_field);
        itemunitprice = findViewById(R.id.isl_unitprice_field);
        checklist = findViewById(R.id.checkBoxItem);

        fetchParentData(parentID);
        fetchChildData(parentID);

        itemunitprice.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    submitChildData();
                    return true;
                }
                return false;
            }
        });
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

    private void fetchChildData(final String parentID){
        String splitParentID[] = parentID.split("\"");
        shopListURL = shopListURL.concat("/"+splitParentID[3]+"/shoppinglists");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, shopListURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++){

                    ShopList shopList = new ShopList();

                    try{
                        JSONObject jsonObject = response.getJSONObject(i);

                        if(parentID.equals(jsonObject.getString("shoplistparent_id"))){
                            shopList.setItemName(jsonObject.getString("sl_name"));
                            shopList.setItemCategory(jsonObject.getString("sl_category"));
                            shopList.setItemQuantity(jsonObject.getString("sl_quantity"));
                            shopList.setItemTotalPrice(jsonObject.getString("sl_ttlprice"));
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                    shopLists.add(shopList);
                }
                adapter = new ShopListRecyclerAdapter(getApplicationContext(), shopLists);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSONChild", "onErrorResponse: ");
            }
        });

        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }

    public void submitChildData(){

        String name = itemname.getText().toString();
        String category = itemcategory.getText().toString();
        int quantity = Integer.parseInt(itemquantity.getText().toString());
        double totalprice = quantity * Double.parseDouble(itemunitprice.getText().toString());

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("sl_name", name);
            jsonObject.put("sl_category", category);
            jsonObject.put("sl_quantity", quantity);
            jsonObject.put("sl_ttlprice", totalprice);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, shopListURL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSONChild", "onErrorResponse: Fail");
            }
        });
        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}
