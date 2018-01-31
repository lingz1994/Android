package com.example.lingez.sample_app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lingez.sample_app.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by lingez on 1/2/18.
 */

public class StatisticsFragment extends Fragment{

    LineGraphSeries<DataPoint> series;
    GraphView expenditure;

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.statistics_layout, container, false);

        double y,x;
        x = 0;

        expenditure = myView.findViewById(R.id.expenditure_graph);
        series = new LineGraphSeries<>();
        for(int i = 0; i < 500; i++){
            x = x + 0.1;
            y = Math.sin(x);
            series.appendData(new DataPoint(x, y), true, 500);
        }
//        expenditure.addSeries(series);

        return myView;
    }
}
