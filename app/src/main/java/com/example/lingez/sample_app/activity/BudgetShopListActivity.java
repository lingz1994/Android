package com.example.lingez.sample_app.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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

import com.example.lingez.sample_app.R;

import java.util.Calendar;

public class BudgetShopListActivity extends AppCompatActivity {

    private Button create;
    private Button cancel;
    private EditText datepick;
    private DatePickerDialog.OnDateSetListener mDateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_shop_list);

        getIntent();

        datepick = findViewById(R.id.mb_datepicker);
        create = findViewById(R.id.mb_create);
        cancel = findViewById(R.id.mb_cancel);

        datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BudgetShopListActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateListener,
                        day, month, year);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                ++month;
                datepick.setText( dayOfMonth + "/" + month + "/" + year);
                Log.d("date", "onDateSet: " + dayOfMonth + "/" + month + "/" + year);
            }
        };

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
        finish();
    }

    public void mbCancel(){
        finish();
    }
}
