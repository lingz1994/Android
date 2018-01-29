package com.example.lingez.sample_app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lingez.sample_app.R;
import com.example.lingez.sample_app.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewItemActivity extends AppCompatActivity {

    EditText item_weight, item_expdate, item_unitprice, item_quantity;
    AutoCompleteTextView item_category, item_name;
    Spinner item_triglvl;
    CheckBox item_importance;
    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<String> autocompleteAdapter;

    String item = "http://192.168.0.182:3000/items";
    String itemcategory = "http://192.168.0.182:3000/itemcategories";

    private Button new_save, new_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        getIntent();

        item_category = findViewById(R.id.new_item_category_field);
        item_name = findViewById(R.id.new_item_name_field);
        item_weight = findViewById(R.id.new_item_weight_field);
        item_expdate = findViewById(R.id.new_item_expdate_field);
        item_unitprice = findViewById(R.id.new_item_unitprice_field);
        item_quantity = findViewById(R.id.new_item_quantity_field);

        item_triglvl = findViewById(R.id.newtrigspinner);
        adapter = ArrayAdapter.createFromResource(this,R.array.triggerpercentage,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        item_triglvl.setAdapter(adapter);

        item_importance = findViewById(R.id.new_item_importance_checkBox);

        new_save = findViewById(R.id.new_item_save);
        new_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newItemSave();
            }
        });

        new_cancel = findViewById(R.id.new_item_cancel);
        new_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newItemCancel();
            }
        });

        newItemCategory();
        newItemName();
    }

    public void newItemSave(){

        JSONObject json = new JSONObject();

        String category = item_category.getText().toString();
        String name = item_name.getText().toString();
        String unitprice = item_unitprice.getText().toString();
        String quantity = item_quantity.getText().toString();
        String weight = item_weight.getText().toString();
        String expdate = item_expdate.getText().toString();

        try {
            json.put("item_category", category);
            json.put("item_name", name);
            json.put("item_unitprice", unitprice);
            json.put("item_quantity", quantity);
            json.put("item_weight", weight);
            json.put("item_exp_date",expdate);
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, item, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplication(),response.toString(),Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSONActivity", error.toString());
            }
        });
        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

        finish();
    }

    public void newItemCategory(){
        final List<String> itemCategories = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, itemcategory, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        itemCategories.add(jsonObject.getString("category"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                autocompleteAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.select_dialog_item,itemCategories);
                item_category.setAdapter(autocompleteAdapter);

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSONCat", "onErrorResponse: ");
            }
        });

        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }

    public void newItemName(){
        final List<String> itemName = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, itemcategory, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        itemName.add(jsonObject.getString("item"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                autocompleteAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.select_dialog_item,itemName);
                item_name.setAdapter(autocompleteAdapter);

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSONCat", "onErrorResponse: ");
            }
        });

        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }

    public void newItemCancel(){
        finish();
    }
}
