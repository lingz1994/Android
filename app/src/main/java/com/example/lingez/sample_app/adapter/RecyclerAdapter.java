package com.example.lingez.sample_app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lingez.sample_app.Data.Item;
import com.example.lingez.sample_app.R;

import java.util.ArrayList;

/**
 * Created by lingez on 1/23/18.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    ArrayList<Item> arrayList = new ArrayList<>();

    public RecyclerAdapter(ArrayList<Item> arrayList){
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_rv,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.itemName.setText(arrayList.get(position).getIt_name());
        holder.itemWeight.setText(arrayList.get(position).getIt_weight());
        holder.itemExpdate.setText(arrayList.get(position).getIt_expdate());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView itemName;
        TextView itemWeight;
        TextView itemExpdate;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.rv_view_item_name);
            itemWeight = itemView.findViewById(R.id.rv_view_item_weight);
            itemExpdate = itemView.findViewById(R.id.rv_view_item_expdate);
        }
    }
}
