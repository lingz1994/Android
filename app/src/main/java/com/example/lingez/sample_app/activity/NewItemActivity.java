package com.example.lingez.sample_app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lingez.sample_app.R;

public class NewItemActivity extends AppCompatActivity {

    private Button new_save, new_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        getIntent();

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
        finish();
    }

    public void newItemCancel(){
        finish();
    }
}
