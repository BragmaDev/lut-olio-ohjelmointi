package com.example.ht;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class CO2Fragment extends Fragment {

    View view;
    MainActivity main = null;
    EntryManager em = null;
    ArrayList<String> date_log = new ArrayList<>();
    ArrayList<Double> emission_log = new ArrayList<>();

    GraphView graph;
    Button button_new_entry;
    RecyclerView recycler_log;

    public CO2Fragment(MainActivity main) { this.main = main; }
    public CO2Fragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_co2, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        date_log.add("[22.3.2021]");
        date_log.add("[20.3.2021]");
        date_log.add("[18.3.2021]");
        date_log.add("[16.3.2021]");
        emission_log.add(1234.56);
        emission_log.add(1234.56);
        emission_log.add(1234.56);
        emission_log.add(1234.56);

        // Graph setup
        graph = (GraphView) view.findViewById(R.id.graph);
        GridLabelRenderer grl = graph.getGridLabelRenderer();
        grl.setHorizontalAxisTitle("Date");
        grl.setHorizontalAxisTitleColor(getResources().getColor(R.color.grey_50, null));
        grl.setVerticalAxisTitle("Emission");
        grl.setVerticalAxisTitleColor(getResources().getColor(R.color.grey_50, null));
        grl.setVerticalLabelsVisible(false);
        grl.setHorizontalLabelsVisible(false);
        grl.setGridStyle(GridLabelRenderer.GridStyle.NONE);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        series.setColor(getResources().getColor(R.color.green_500, null));
        graph.addSeries(series);

        recycler_log = (RecyclerView) view.findViewById(R.id.recyclerLogCO2);
        button_new_entry = (Button) view.findViewById(R.id.buttonNewEntry);
        button_new_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { switchToEntryFragment(); }
        });
        setRecyclerAdapter();
    }

    private void setRecyclerAdapter() {
        RecyclerAdapter adapter = new RecyclerAdapter(date_log, emission_log);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        recycler_log.setLayoutManager(lm);
        recycler_log.setItemAnimator(new DefaultItemAnimator());
        recycler_log.setAdapter(adapter);
    }

    private void switchToEntryFragment() {
        main.changeFragment("entry");
    }


}