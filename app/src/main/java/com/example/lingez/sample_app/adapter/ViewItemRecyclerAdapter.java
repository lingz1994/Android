package com.example.lingez.sample_app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lingez.sample_app.Data.Item;
import com.example.lingez.sample_app.R;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingez on 1/23/18.
 */

public class ViewItemRecyclerAdapter extends RecyclerView.Adapter<ViewItemRecyclerAdapter.MyViewHolder> {

    private Context context;
    private List<Item> arrayList = new ArrayList<>();

    MqttAndroidClient client;
    String topic = "SBSGTS";
    String mqttServer = "tcp://iot.eclipse.org:1883";

    public ViewItemRecyclerAdapter(Context context,List arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_rv, parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.itemName.setText(arrayList.get(position).getIt_name());
//        holder.itemWeight.setText(arrayList.get(position).getIt_weight());
        holder.itemExpdate.setText(arrayList.get(position).getIt_expdate());

        mqttSensor(holder);

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

    //MQTT
    private void setSub(){
        try{
            client.subscribe(topic, 1);
        } catch (MqttException e){
            e.printStackTrace();
        }
    }

    private void mqttSensor(final MyViewHolder myViewHolder){
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
                myViewHolder.itemWeight.setText(new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}
