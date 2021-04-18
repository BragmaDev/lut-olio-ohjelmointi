package com.example.ht;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CO2Fragment extends Fragment {

    View view;
    MainActivity main = null;
    EntryManager em = EntryManager.getInstance();
    UserManager um = UserManager.getInstance();
    ArrayList<String> date_log = new ArrayList<>();
    ArrayList<Double> emission_log = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

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

        // Resetting the log
        date_log.clear();
        emission_log.clear();

        if (um.getUser().getEntries(0).size() > 0) {
            for (Entry e : um.getUser().getEntries(0)) {
                if (e instanceof ClimateDietEntry) {
                    date_log.add(sdf.format(e.getDate()));
                    emission_log.add(((ClimateDietEntry) e).getEmissions()[4]);
                }
            }
        }

        // Graph setup
        graph = (GraphView) view.findViewById(R.id.graph);
        GridLabelRenderer glr = graph.getGridLabelRenderer();
        glr.setHorizontalAxisTitle("Date");
        glr.setHorizontalAxisTitleColor(getResources().getColor(R.color.grey_50, null));
        glr.setVerticalAxisTitle("Emission");
        glr.setVerticalAxisTitleColor(getResources().getColor(R.color.grey_50, null));
        glr.setVerticalLabelsVisible(false);
        glr.setHorizontalLabelsVisible(false);
        glr.setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxY(4000.0);
        DataPoint[] datapoints = new DataPoint[um.getUser().getEntries(0).size()];
        for (int i = 0; i < um.getUser().getEntries(0).size(); i++) {
            ClimateDietEntry e = (ClimateDietEntry) um.getUser().getEntries(0).get(i);
            datapoints[i] = new DataPoint(e.getDate().getTime() / 1000000000.0, e.getEmissions()[4]);
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(datapoints);
        series.setColor(getResources().getColor(R.color.green_500, null));
        graph.addSeries(series);

        recycler_log = (RecyclerView) view.findViewById(R.id.recyclerLogCO2);
        button_new_entry = (Button) view.findViewById(R.id.buttonChange);
        button_new_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { switchToEntryFragment(); }
        });
        setRecyclerAdapter();
        System.out.println("Date log: " + date_log.size() + " Emission log: " + emission_log.size());
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