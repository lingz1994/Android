package com.example.lingez.sample_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lingez.sample_app.R;
import com.example.lingez.sample_app.RequestQueueSingleton;

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

public class NewItemActivity extends AppCompatActivity {

    EditText item_weight, item_expdate, item_unitprice, item_quantity;
    AutoCompleteTextView item_category, item_name;
    Spinner item_triglvl;
    CheckBox item_importance;
    ArrayAdapter<String> autocompleteAdapter;

    String item = "http://192.168.1.118:3000/items";
    String itemcategory = "http://192.168.1.118:3000/itemcategories";

    MqttAndroidClient client;
    String topic;
    String mqttServer = "tcp://iot.eclipse.org:1883";

    private Button new_save, new_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        Intent intent = getIntent();
        final String itemID = intent.getStringExtra("ItemID");
        Log.d("ItemID", "onCreate in newitemactivity: " + itemID);
        topic = intent.getStringExtra("topic");

        item_category = findViewById(R.id.new_item_category_field);
        item_name = findViewById(R.id.new_item_name_field);
        item_weight = findViewById(R.id.new_item_weight_field);
        item_expdate = findViewById(R.id.new_item_expdate_field);
        item_unitprice = findViewById(R.id.new_item_unitprice_field);
        item_quantity = findViewById(R.id.new_item_quantity_field);
        item_triglvl = findViewById(R.id.newtrigspinner);
        item_importance = findViewById(R.id.new_item_importance_checkBox);


        new_save = findViewById(R.id.new_item_save);
        new_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newItemSave();
            }
        });

        if (itemID != null){
            getItemData(itemID);

            new_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editItemSave(itemID);
                }
            });

        } else {
            new_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newItemSave();
                }
            });
        }

        new_cancel = findViewById(R.id.new_item_cancel);
        new_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newItemCancel();
            }
        });




        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), mqttServer, clientId);

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
                item_weight.setText(new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });




        newItemCategory();
        newItemName();
    }

    private void editItemSave(String itemID) {
        String splititemID[] = itemID.split("\"");
        item = item.concat("/"+splititemID[3]);
        JSONObject json = new JSONObject();

        String category = item_category.getText().toString();
        String name = item_name.getText().toString();
        String unitprice = item_unitprice.getText().toString();
        String quantity = item_quantity.getText().toString();
        double weight = Double.parseDouble(item_weight.getText().toString());
        String expdate = item_expdate.getText().toString();

        String[] trigperc = item_triglvl.getSelectedItem().toString().split("%");
        double trigpercint = Integer.parseInt(trigperc[0]);
        double triggerlvl = trigpercint/100*weight;

        boolean chckbox = item_importance.isChecked();

        try {
            json.put("item_category", category);
            json.put("item_name", name);
            json.put("item_unitprice", unitprice);
            json.put("item_quantity", quantity);
            json.put("item_weight", weight);
            json.put("item_exp_date",expdate);
            json.put("item_trig_lvl", triggerlvl);
            json.put("item_imp", chckbox);
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, item, json,
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
        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

        finish();
    }

    private void setSub(){
        try{
            client.subscribe(topic, 1);
        } catch (MqttException e){
            e.printStackTrace();
        }
    }

    public void getItemData(final String itemID){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, item, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        if (itemID.equals(jsonObject.getString("_id"))) {

                            item_category.setText(jsonObject.getString("item_category"));
                            item_name.setText(jsonObject.getString("item_name"));
                            item_unitprice.setText(jsonObject.getString("item_unitprice"));
                            item_quantity.setText(jsonObject.getString("item_quantity"));
                            item_weight.setText(jsonObject.getString("item_weight"));
                            item_expdate.setText(jsonObject.getString("item_exp_date"));

                            item_importance.setChecked(jsonObject.getBoolean("item_imp"));
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

        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }

    public void newItemSave(){

        JSONObject json = new JSONObject();

        String category = item_category.getText().toString();
        String name = item_name.getText().toString();
        String unitprice = item_unitprice.getText().toString();
        String quantity = item_quantity.getText().toString();
        String weightStr = item_weight.getText().toString();
        double weight = Double.parseDouble(weightStr);

        String expdate = item_expdate.getText().toString();

        String[] trigperc = item_triglvl.getSelectedItem().toString().split("%");
        double trigpercint = Integer.parseInt(trigperc[0]);
        double triggerlvl = trigpercint/100*weight;

        boolean chckbox = item_importance.isChecked();

        try {
            json.put("item_category", category);
            json.put("item_name", name);
            json.put("item_unitprice", unitprice);
            json.put("item_quantity", quantity);
            json.put("item_weight", weight);
            json.put("item_exp_date",expdate);
            json.put("item_trig_lvl", triggerlvl);
            json.put("item_imp", chckbox);
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, item, json,
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
        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

        finish();
    }

    public void newItemCategory(){
        final List<String> itemCategories = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, itemcategory, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        itemCategories.add(jsonObject.getString("category"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                autocompleteAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.select_dialog_item,itemCategories);
                item_category.setAdapter(autocompleteAdapter);

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSONCat", "onErrorResponse: ");
            }
        });

        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }

    public void newItemName(){
        final List<String> itemName = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, itemcategory, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        itemName.add(jsonObject.getString("item"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                autocompleteAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.select_dialog_item,itemName);
                item_name.setAdapter(autocompleteAdapter);

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSONCat", "onErrorResponse: ");
            }
        });

        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }

    public void newItemCancel(){
        finish();
    }
}
