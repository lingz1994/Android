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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.lingez.sample_app.Data.Item;
import com.example.lingez.sample_app.R;
import com.example.lingez.sample_app.RequestQueueSingleton;
import com.example.lingez.sample_app.activity.NewItemActivity;
import com.example.lingez.sample_app.adapter.ViewItemRecyclerAdapter;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItemFragment extends Fragment{

    private Button add_NewItem;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    List<Item> itemList;

    String url = "http://192.168.0.182:3000/items";

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

        recyclerView = myView.findViewById(R.id.new_item_rv);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        itemList = new ArrayList<>();

        fetchData();

        return myView;
    }

    private void fetchData() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {

                    Item item = new Item();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        item.setIt_name(jsonObject.getString("item_name"));
                        item.setIt_weight(jsonObject.getString("item_weight"));
                        item.setIt_expdate(jsonObject.getString("item_exp_date"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    itemList.add(item);

                }

                adapter = new ViewItemRecyclerAdapter(getActivity(), itemList);
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
