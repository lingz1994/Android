package com.example.lingez.sample_app.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.lingez.sample_app.Data.ShopListParent;
import com.example.lingez.sample_app.R;
import com.example.lingez.sample_app.RequestQueueSingleton;
import com.example.lingez.sample_app.activity.BudgetShopListActivity;
import com.example.lingez.sample_app.activity.ItemShopListActivity;
import com.example.lingez.sample_app.adapter.ShopListHomeRecyclerAdapter;
import com.example.lingez.sample_app.adapter.ViewItemRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingez on 1/2/18.
 */

public class ShopListFragment extends Fragment{

    private Button newlist;

    TextView systemList;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    List<ShopListParent> itemList;

    private String shopListParentURL = "http://192.168.0.182:3000/shop_list_parents";

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.shoplist_layout, container, false);

        systemList = myView.findViewById(R.id.systemlist);
        systemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ItemShopListActivity.class);
                String systemListID = "{\"$oid\":\"5a755f79f4aed714017194ec\"}";
                intent.putExtra("parentID", systemListID);
                startActivity(intent);
            }
        });

        newlist = myView.findViewById(R.id.shoplist_new);
        newlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),BudgetShopListActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = myView.findViewById(R.id.shoplist_user_rv);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        itemList = new ArrayList<>();

        fetchData();

        return myView;
    }

    private void fetchData() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, shopListParentURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 1; i < response.length(); i++) {

                    ShopListParent shopListParent = new ShopListParent();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        shopListParent.setListName(jsonObject.getString("sl_listname"));
                        shopListParent.setDate(jsonObject.getString("sl_date"));
                        shopListParent.setBudget(jsonObject.getString("sl_budget"));
                        shopListParent.setParentID(jsonObject.getString("_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    itemList.add(shopListParent);

                }

                adapter = new ShopListHomeRecyclerAdapter(getActivity(), itemList);
                recyclerView.setAdapter(adapter);

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSONArray", "onErrorResponse: ");
            }
        });

        RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest);
    }
}
