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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.lingez.sample_app.Data.Item;
import com.example.lingez.sample_app.R;
import com.example.lingez.sample_app.RequestQueueSingleton;
import com.example.lingez.sample_app.activity.NewItemActivity;
import com.example.lingez.sample_app.adapter.RecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItemFragment extends Fragment{

    private TextView rvItemName;
    private TextView rvItemWeight;
    private TextView rvItemExpDate;

    private RecyclerView itrecyclerview;
    private RecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    String url = "http://192.168.1.118:3000/items";

    private List<Item> listItem = new ArrayList<>();

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

        itrecyclerview = myView.findViewById(R.id.new_item_rv);
        itrecyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new RecyclerAdapter((ArrayList<Item>) listItem);
        itrecyclerview.setAdapter(adapter);

        initData();

        return myView;
    }

    private void initData() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        /*Item item = new Item(jsonObject.getString("item_name"),
                                jsonObject.getString("item_weight"),
                                jsonObject.getString("item_exp_date"));
                        listItem.add(item);*/
                        Item item = new Item();
                        item.setIt_name(jsonObject.getString("item_name"));
                        item.setIt_weight(jsonObject.getString("item_weight"));
                        item.setIt_expdate(jsonObject.getString("item_exp_date"));
                        listItem.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getActivity(), response.toString(),Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSONArray", "onErrorResponse: " + error);
            }
        });
        RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest);
    }
}
