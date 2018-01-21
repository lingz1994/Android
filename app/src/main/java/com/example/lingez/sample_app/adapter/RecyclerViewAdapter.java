package com.example.lingez.sample_app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lingez.sample_app.R;

import java.util.ArrayList;

/**
 * Created by lingez on 22/01/2018.
 */

class RecyclerViewHolder extends RecyclerView.ViewHolder{
    public TextView itemName;
    public TextView itemWeight;
    public TextView itemExpDate;

    public RecyclerViewHolder(View view){
        super(view);
        itemName = view.findViewById(R.id.rv_view_item_name);
        itemWeight = view.findViewById(R.id.rv_view_item_weight);
        itemExpDate = view.findViewById(R.id.rv_view_item_expdate);
    }
}
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from((parent.getContext()));
        View view = inflater.inflate(R.layout.items_rv,parent,false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
