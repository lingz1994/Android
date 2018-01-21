package com.example.lingez.sample_app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lingez.sample_app.R;
import com.example.lingez.sample_app.RequestQueueSingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class NewItemActivity extends AppCompatActivity {

    EditText item_name, item_weight, item_expdate;
    Spinner item_triglvl;
    CheckBox item_importance;
    ArrayAdapter<CharSequence> adapter;

    String url = "http://192.168.0.127:3000/items";

    private Button new_save, new_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        getIntent();


        item_name = findViewById(R.id.new_item_name_field);
        item_weight = findViewById(R.id.new_item_weight_field);
        item_expdate = findViewById(R.id.new_item_expdate_field);

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
    }

    public void newItemSave(){

        JSONObject json = new JSONObject();

        String name = item_name.getText().toString();
        String weight = item_weight.getText().toString();
        String expdate = item_expdate.getText().toString();

        try {
            json.put("item_name", name);
            json.put("item_weight", weight);
            json.put("item_expdate",expdate);
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
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

    public void newItemCancel(){
        finish();
    }
}
