package com.example.lingez.sample_app.adapter;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingez on 1/23/18.
 */

public class ViewItemRecyclerAdapter extends RecyclerView.Adapter<ViewItemRecyclerAdapter.MyViewHolder> {

    private Context context;
    private List<Item> arrayList = new ArrayList<>();
    String item = "http://192.168.0.182:3000/items";

    MqttAndroidClient client;
    String topic = "SBSGTS";
    String mqttServer = "tcp://iot.eclipse.org:1883";

    public ViewItemRecyclerAdapter(Context context,List arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_layout, parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.itemName.setText(arrayList.get(position).getIt_name());
//        holder.itemWeight.setText(arrayList.get(position).getIt_weight());
        holder.itemExpdate.setText(arrayList.get(position).getIt_expdate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewItemActivity.class);
                intent.putExtra("ItemID", arrayList.get(position).getIt_id());
                v.getContext().startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setMessage("Do you want to delete this item?")
                        .setCancelable(false)
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteItemData(arrayList.get(position).getIt_id(),holder);
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

        mqttSensor(holder, position);

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
//            itemName = itemView.findViewById(R.id.rv_view_item_name);
//            itemWeight = itemView.findViewById(R.id.rv_view_item_weight);
//            itemExpdate = itemView.findViewById(R.id.rv_view_item_expdate);
        }
    }

    //MQTT
    private void setSub(int size){

        for (int i=1; i<=size; i++){
            try{
                client.subscribe(topic+i, 1);
            } catch (MqttException e){
                e.printStackTrace();
            }
        }
    }

    private void mqttSensor(final MyViewHolder myViewHolder, final int size){
        String clientId = MqttClient.generateClientId();
        client =
                new MqttAndroidClient(myViewHolder.itemView.getContext(), mqttServer,
                        clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d("MQTT", "onSuccess");
                    setSub(size);
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
                Log.d("mqtt123", "messageArrived: "+topic + " " + new String(message.getPayload()));
                myViewHolder.itemWeight.setText(new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void deleteItemData(String itemID, MyViewHolder holder){
        String splititemID[] = itemID.split("\"");
        item = item.concat("/"+splititemID[3]);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, item, null,
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
}
