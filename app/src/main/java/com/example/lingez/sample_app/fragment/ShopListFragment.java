package com.example.lingez.sample_app.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lingez.sample_app.R;
import com.example.lingez.sample_app.activity.BudgetShopListActivity;

/**
 * Created by lingez on 1/2/18.
 */

public class ShopListFragment extends Fragment{

    private Button newlist;

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.shoplist_layout, container, false);

        newlist = myView.findViewById(R.id.shoplist_new);
        newlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),BudgetShopListActivity.class);
                startActivity(intent);
            }
        });
        return myView;
    }
}
