package com.example.lingez.sample_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lingez.sample_app.Data.ShopListParent;
import com.example.lingez.sample_app.R;
import com.example.lingez.sample_app.activity.ItemShopListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingez on 1/28/18.
 */

public class ShopListHomeRecyclerAdapter extends RecyclerView.Adapter<ShopListHomeRecyclerAdapter.MyViewHolder> {
    private Context context;
    private List<ShopListParent> arrayList = new ArrayList<>();

    public ShopListHomeRecyclerAdapter(Context context,List arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoplist_home_viewlist, parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.listName.setText(arrayList.get(position).getListName());
        holder.listDate.setText(arrayList.get(position).getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ItemShopListActivity.class);
                intent.putExtra("parentID", arrayList.get(position).getParentID());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView listName;
        TextView listDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.shoplist_name);
            listDate = itemView.findViewById(R.id.shoplist_date);
        }
    }
}
