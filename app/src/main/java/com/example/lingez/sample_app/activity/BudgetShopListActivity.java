package com.example.lingez.sample_app.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lingez.sample_app.Data.ShopListParent;
import com.example.lingez.sample_app.R;
import com.example.lingez.sample_app.RequestQueueSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class BudgetShopListActivity extends AppCompatActivity {

    private Button create;
    private Button cancel;
    private EditText listname,datepick,budget;
    private String shopListParentURL = "http://192.168.1.118:3000/shop_list_parents";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_shop_list);

        getIntent();

        listname = findViewById(R.id.mb_listname);
        datepick = findViewById(R.id.mb_datepicker);
        budget = findViewById(R.id.mb_budget);
        create = findViewById(R.id.mb_create);
        cancel = findViewById(R.id.mb_cancel);

        datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(BudgetShopListActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        datepick.setText( dayOfMonth + "/" + month + "/" + year);
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbCreate();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbCancel();
            }
        });
    }

    public void mbCreate(){

        String name = listname.getText().toString();
        String date = datepick.getText().toString();
        String bdgt = budget.getText().toString();

        JSONObject json = new JSONObject();

        try {
            json.put("sl_listname", name);
            json.put("sl_date", date);
            json.put("sl_budget", bdgt);
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, shopListParentURL, json,
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

        Intent intent = new Intent(BudgetShopListActivity.this, ItemShopListActivity.class);
        /*intent.putExtra("listname", name);
        intent.putExtra("listdate", date);
        intent.putExtra("listbudget", bdgt);*/
//        startActivity(intent);
        finish();
    }

    public void mbCancel(){
        finish();
    }
}
