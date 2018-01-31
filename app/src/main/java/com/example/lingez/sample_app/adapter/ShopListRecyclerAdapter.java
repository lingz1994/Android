package com.example.lingez.sample_app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.lingez.sample_app.Data.ShopList;
import com.example.lingez.sample_app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingez on 1/28/18.
 */

public class ShopListRecyclerAdapter extends RecyclerView.Adapter<ShopListRecyclerAdapter.MyViewHolder> {
    private Context context;
    private List<ShopList> arrayList = new ArrayList<>();

    public ShopListRecyclerAdapter(Context context,List arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoplistitem_parent, parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.itemName.setText(arrayList.get(position).getItemName());
        holder.itemCategory.setText(arrayList.get(position).getItemCategory());
        holder.itemQuantity.setText(arrayList.get(position).getItemQuantity());
        holder.unitPrice.setText(arrayList.get(position).getItemTotalPrice());

        holder.checkitem.setOnCheckedChangeListener(null);
        holder.checkitem.setChecked(arrayList.get(position).getItemCheck());
        holder.checkitem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.checkitem.isChecked()){
                    Log.d("Check", "onCheckedChanged: " + holder.getAdapterPosition() + " " + holder.checkitem.isChecked() + " " + holder.itemName.getText());
                }
                else {
                    Log.d("Check", "onCheckedChanged: " + holder.getAdapterPosition() + " " + holder.checkitem.isChecked() + " " + holder.itemName.getText());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView itemName;
        TextView itemCategory;
        TextView itemQuantity;
        TextView unitPrice;
        CheckBox checkitem;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemCategory = itemView.findViewById(R.id.itemCategory);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            unitPrice = itemView.findViewById(R.id.itemUnitPrice);
            checkitem = itemView.findViewById(R.id.checkBoxItem);
        }
    }
}
