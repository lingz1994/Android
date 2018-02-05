package com.example.lingez.sample_app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lingez.sample_app.Data.ShopList;
import com.example.lingez.sample_app.R;
import com.example.lingez.sample_app.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingez on 1/28/18.
 */

public class ShopListRecyclerAdapter extends RecyclerView.Adapter<ShopListRecyclerAdapter.MyViewHolder> {
    private Context context;
    private List<ShopList> arrayList = new ArrayList<>();
    private String statisticsURL = "http://192.168.0.182:3000/shopliststatistics/";

    public ShopListRecyclerAdapter(Context context, List arrayList){
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
                    String splititemID[] = arrayList.get(position).getItemID().split("\"");
                    String itemID = splititemID[3];
                    try{
                        if (holder.checkitem.isChecked()){
                            newItemCheck(holder, itemID);
                        }
                        else {
                            fetchData(holder, itemID);
//                            Log.d("Check", "onCheckedChanged: statistics" + arrayList.get(position).getItemID());
                        }
                    } catch(Exception e){
                        e.printStackTrace();
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

    public void newItemCheck(MyViewHolder holder, String itemID){

        Log.d("JSONActivity", statisticsURL);

        JSONObject json = new JSONObject();

        String name = holder.itemName.getText().toString();
        String quantity = holder.itemQuantity.getText().toString();
        String ttlprice = holder.unitPrice.getText().toString();

        try {
            json.put("itemname", name);
            json.put("itemquantity", quantity);
            json.put("itemttlprice", ttlprice);
            json.put("shoplistid", itemID);
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, statisticsURL, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSONActivity", error.toString());
            }
        });
        RequestQueueSingleton.getInstance(holder.itemView.getContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void deleteItemCheck(String itemID, MyViewHolder holder){
        String splitStatisticsID[] = itemID.split("\"");
        statisticsURL = statisticsURL.concat("/"+splitStatisticsID[3]+"/");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, statisticsURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("StatisticsID", "Deleted");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("StatisticsID123", error.toString());
            }
        });
        RequestQueueSingleton.getInstance(holder.itemView.getContext()).addToRequestQueue(jsonObjectRequest);

    }

    private void fetchData(final MyViewHolder holder, final String itemID) {
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, statisticsURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        if(itemID.equals(jsonObject.getString("shoplistid"))) {
                            String statisticsID = jsonObject.getString("_id");
                            Log.d("StatisticsID", "onResponse: " + statisticsID);
                            deleteItemCheck(statisticsID, holder);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSONArray", "onErrorResponse: ");
            }
        });

        RequestQueueSingleton.getInstance(holder.itemView.getContext()).addToRequestQueue(jsonArrayRequest);
    }
}
