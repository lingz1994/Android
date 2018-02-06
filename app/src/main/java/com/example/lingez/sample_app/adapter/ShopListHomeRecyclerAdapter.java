package com.example.lingez.sample_app.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lingez.sample_app.Data.ShopList;
import com.example.lingez.sample_app.Data.ShopListParent;
import com.example.lingez.sample_app.R;
import com.example.lingez.sample_app.RequestQueueSingleton;
import com.example.lingez.sample_app.activity.BudgetShopListActivity;
import com.example.lingez.sample_app.activity.ItemShopListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingez on 1/28/18.
 */

public class ShopListHomeRecyclerAdapter extends RecyclerView.Adapter<ShopListHomeRecyclerAdapter.MyViewHolder> {

    private String shopListParentURL = "http://192.168.0.182:3000/shop_list_parents";
    private Context context;
    private List<ShopListParent> arrayList = new ArrayList<>();
    private List<ShopList> shopLists =  new ArrayList<>();

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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
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
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("What would you like to do with " + arrayList.get(position).getListName())
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteShopListParentData(arrayList.get(position).getParentID(), (Activity) holder.itemView.getContext());
                            }
                        })
                        .setNeutralButton("DUPLICATE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                duplicateShopListParentData(arrayList.get(position),(Activity) holder.itemView.getContext());
                            }
                        })
                        .setNegativeButton("CANCEL", null);

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
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

    public void deleteShopListParentData(String itemID, Activity activity){
        String splititemID[] = itemID.split("\"");
        shopListParentURL = shopListParentURL.concat("/"+splititemID[3]);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, shopListParentURL, null,
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
        RequestQueueSingleton.getInstance(activity.getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    }

    public void duplicateShopListParentData(ShopListParent shopListParent, final Activity activity){
        String name = shopListParent.getListName();
        String bdgt = shopListParent.getBudget();

        JSONObject json = new JSONObject();

        try {
            json.put("sl_listname", name.concat("(COPY)"));
            json.put("sl_budget", bdgt);
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, shopListParentURL, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(activity,response.toString(),Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSONActivity", error.toString());
            }
        });
        RequestQueueSingleton.getInstance(activity).addToRequestQueue(jsonObjectRequest);
    }

    public void fetchChildData(final String parentID, final Activity activity){

        String splitParentID[] = parentID.split("\"");
        shopListParentURL = shopListParentURL.concat("/"+splitParentID[3]+"/shoppinglists");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, shopListParentURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++){

                    ShopList shopList = new ShopList();
                    double ttl = 0;

                    try{
                        JSONObject jsonObject = response.getJSONObject(i);

                        if(parentID.equals(jsonObject.getString("shoplistparent_id"))){
                            shopList.setItemName(jsonObject.getString("sl_name"));
                            shopList.setItemCategory(jsonObject.getString("sl_category"));
                            shopList.setItemQuantity(jsonObject.getString("sl_quantity"));
                            shopList.setItemTotalPrice(jsonObject.getString("sl_ttlprice"));
                            shopList.setItemID(jsonObject.getString("_id"));

                            ttl = Double.parseDouble(jsonObject.getString("sl_ttlprice")) + ttl;
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                    shopLists.add(shopList);
                    submitChildData(shopLists, activity, shopListParentURL);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSONChild", "onErrorResponse: ");
            }
        });

        RequestQueueSingleton.getInstance(activity).addToRequestQueue(jsonArrayRequest);
    }

    public void submitChildData(List<ShopList> shopLists, Activity activity, String shopListURL){

        for (int i = 0; i < shopLists.size(); i++){

            String name = shopLists.get(i).getItemName();
            String category = shopLists.get(i).getItemCategory();
            int quantity = Integer.parseInt(shopLists.get(i).getItemQuantity());
            double totalprice = quantity * Double.parseDouble(shopLists.get(i).getItemTotalPrice());

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("sl_name", name);
                jsonObject.put("sl_category", category);
                jsonObject.put("sl_quantity", quantity);
                jsonObject.put("sl_ttlprice", totalprice);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, shopListURL, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("JSONChild", "onErrorResponse: Fail");
                }
            });
            RequestQueueSingleton.getInstance(activity).addToRequestQueue(jsonObjectRequest);
        }
    }
}
