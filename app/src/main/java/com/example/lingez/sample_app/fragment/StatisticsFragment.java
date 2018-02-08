package com.example.lingez.sample_app.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.lingez.sample_app.Data.Statistic;
import com.example.lingez.sample_app.R;
import com.example.lingez.sample_app.RequestQueueSingleton;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingez on 1/2/18.
 */

public class StatisticsFragment extends Fragment{

    String statisticsURL = "http://192.168.1.118:3000/shopliststatistics";

    List<Statistic> statisticList;

    LineChart chart;
    ArrayList<Entry> entries;
    ArrayList<String> labels;
    LineDataSet set;
    LineData data;

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.statistics_layout, container, false);

        statisticList = new ArrayList<>();

        chart = myView.findViewById(R.id.expendituregraph);
        fetchData();

        entries = new ArrayList<>();

        labels = new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        labels.add("July");
        labels.add("August");
        labels.add("September");
        labels.add("October");
        labels.add("November");
        labels.add("December");

        return myView;
    }

    public void fetchData(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, statisticsURL, null, new Response.Listener<JSONArray>() {
            float price = 0;
            int date;

            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {

                    Statistic statistic = new Statistic();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        price = Float.parseFloat(jsonObject.getString("itemttlprice"));
                        statistic.setPrice(price);

                        String[] splitDatetoMonth = jsonObject.getString("created_at").split("-");
//                        String date = splitDatetoMonth[1];
                        date = Integer.parseInt(splitDatetoMonth[1]);
                        statistic.setMonth(date);

                        /*if(date == 2){
                            entries.add(new Entry(2, price));
                        }*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    statisticList.add(statistic);
                }

                Log.d("Statistics", "onResponse: Size " + statisticList.size());

                for (int j=1; j<=12; j++){
                    float c = 0;
                    for (int i=0; i<statisticList.size(); i++){
                        if (statisticList.get(i).getMonth() == j){
                            c = statisticList.get(i).getPrice() + c;
                        }
                    }
                    entries.add(new Entry(j, c));
                }

                set = new LineDataSet(entries, "Expenses");

                data = new LineData(set);
                chart.setData(data);
                chart.notifyDataSetChanged();
                chart.invalidate();
                set.setColor(Color.RED);
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
