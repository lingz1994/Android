package com.example.lingez.sample_app.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lingez.sample_app.R;
import com.example.lingez.sample_app.activity.NewItemActivity;

public class ItemFragment extends Fragment{

    TextView viewItem_weight;

    private Button add_NewItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.items_layout, container, false);

        add_NewItem = myView.findViewById(R.id.new_item_add);
        add_NewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewItemActivity.class);
                startActivity(intent);
            }
        });

        viewItem_weight = myView.findViewById(R.id.view_item_weight);
        return myView;
    }
}
