package com.example.lingez.sample_app.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lingez.sample_app.Data.Item;
import com.example.lingez.sample_app.R;
import com.example.lingez.sample_app.RequestQueueSingleton;
import com.example.lingez.sample_app.activity.NewItemActivity;

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

    List<Item> itemList;

    CardView cv1, cv2;
    TextView item_name1, item_weight1, item_expdate1;
    TextView item_name2, item_weight2, item_expdate2;
    Button btn;

    String url = "http://192.168.0.182:3000/items";

    MqttAndroidClient client;
    String topic = "SBSGTS";
    String mqttServer = "tcp://iot.eclipse.org:1883";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.items_layout, container, false);

        itemList = new ArrayList<>();

        btn = myView.findViewById(R.id.button);
        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyWeight(getActivity());
            }
        });*/

        cv1 = myView.findViewById(R.id.card1);
        item_name1 = myView.findViewById(R.id.rv_view_item_name1);
        item_weight1 = myView.findViewById(R.id.rv_view_item_weight1);
        item_expdate1 = myView.findViewById(R.id.rv_view_item_expdate1);
        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewItemActivity.class);
                try {
                    intent.putExtra("ItemID", itemList.get(0).getIt_id());
                } catch (Exception e){
                    e.printStackTrace();
                }
                intent.putExtra("topic", "SBSGTS1");
                startActivity(intent);
            }
        });
        cv1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you want to delete this item?")
                        .setCancelable(false)
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteItemData(itemList.get(0).getIt_id(),getActivity());
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });


        cv2 = myView.findViewById(R.id.card2);
        item_name2 = myView.findViewById(R.id.rv_view_item_name2);
        item_weight2 = myView.findViewById(R.id.rv_view_item_weight2);
        item_expdate2 = myView.findViewById(R.id.rv_view_item_expdate2);
        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewItemActivity.class);
                try {
                    intent.putExtra("ItemID", itemList.get(1).getIt_id());
                } catch (Exception e){
                    e.printStackTrace();
                }
                intent.putExtra("topic", "SBSGTS2");
                startActivity(intent);
            }
        });
        cv2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you want to delete this item?")
                        .setCancelable(false)
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteItemData(itemList.get(1).getIt_id(),getActivity());
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });

        fetchData();
        mqttSensor(item_weight1, item_weight2);

        return myView;
    }

    //MQTT
    private void setSub(){
        try{
            client.subscribe(topic.concat("1"), 1);
            client.subscribe(topic.concat("2"), 1);
        } catch (MqttException e){
            e.printStackTrace();
        }
    }

    private void mqttSensor(final TextView item_weight1, final TextView item_weight2){
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(getActivity(), mqttServer, clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d("MQTT", "onSuccess");
                    setSub();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d("MQTT", "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                double trig1, trig2;
                double w1, w2;
                if(topic.equals("SBSGTS1"))
                {
                    item_weight1.setText(new String(message.getPayload()));
                    w1 = Double.parseDouble(new String(message.getPayload()));
                    trig1 = Double.parseDouble(itemList.get(0).getIt_triglvl());
                    if(w1 <= trig1){
                        notifyWeight(w1, itemList.get(0), getActivity());
                    }
                }
                if(topic.equals("SBSGTS2"))
                {
                    item_weight2.setText(new String(message.getPayload()));
                    Log.d("Weight", "messageArrived: "+itemList.get(1).getIt_triglvl());
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void notifyWeight(final double w, final Item it_id, final Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please replace the item back if it's in use and DISMISS else reset if new item is to be placed")
                .setCancelable(true)
                .setPositiveButton("DISMISS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (w <= Double.parseDouble(it_id.getIt_triglvl())){
                            Log.d("Notification", "Need to push!");
                            getItemData(it_id.getIt_id(), activity);
                        }
                    }
                })
                .setNegativeButton("RESET", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItemData(it_id.getIt_id(),activity);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void fetchData() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {

                    Item item = new Item();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        if(i == 0) {
                            item_name1.setText(jsonObject.getString("item_name"));
                            item.setIt_triglvl(jsonObject.getString("item_trig_lvl"));
                            item_expdate1.setText(jsonObject.getString("item_exp_date"));
                        }

                        if(i == 1) {
                            item_name2.setText(jsonObject.getString("item_name"));
                            item.setIt_triglvl(jsonObject.getString("item_trig_lvl"));
                            item_expdate2.setText(jsonObject.getString("item_exp_date"));
                        }
                        item.setIt_id(jsonObject.getString("_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    itemList.add(item);
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSONArray", "onErrorResponse: ");
            }
        });

        RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest);
    }

    public void deleteItemData(String itemID, Activity activity){
        String splititemID[] = itemID.split("\"");
        url = url.concat("/"+splititemID[3]);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
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

    public void getItemData(final String itemID, final Activity activity){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {

                    Item item = new Item();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        if (itemID.equals(jsonObject.getString("_id"))) {

                            item.setIt_cat(jsonObject.getString("item_category"));
                            item.setIt_name(jsonObject.getString("item_name"));
                            item.setIt_untpr(jsonObject.getString("item_unitprice"));
                            item.setIt_qty(jsonObject.getString("item_quantity"));
                            item.setIt_imp(jsonObject.getBoolean("item_imp"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    trigPushItem(item, activity);

                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSONArray", "onErrorResponse: ");
            }
        });

        RequestQueueSingleton.getInstance(activity).addToRequestQueue(jsonArrayRequest);
    }

    public void trigPushItem(Item it_id, Activity activity){

        String shopListURL = "http://192.168.0.182:3000/shop_list_parents/5a755f79f4aed714017194ec/shoppinglists";

        String name = it_id.getIt_name();
        String category = it_id.getIt_cat();
        int quantity = Integer.parseInt(it_id.getIt_qty());
        double totalprice = quantity * Double.parseDouble(it_id.getIt_untpr());

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
