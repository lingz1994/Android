package com.example.lingez.sample_app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lingez.sample_app.R;

/**
 * Created by lingez on 1/2/18.
 */

public class ShopListFragment extends Fragment{

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.shoplist_layout, container, false);
        return myView;
    }
}
