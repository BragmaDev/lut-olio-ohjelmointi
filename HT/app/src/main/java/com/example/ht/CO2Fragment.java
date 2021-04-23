package com.example.ht;

import android.graphics.Color;
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
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class CO2Fragment extends Fragment {

    View view;
    MainActivity main = null;
    EntryManager em = EntryManager.getInstance();
    UserManager um = UserManager.getInstance();
    ArrayList<String> date_log = new ArrayList<>();
    ArrayList<Double> emission_log = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    GraphView graph;
    Button button_toggle;
    Button button_new_entry;
    RecyclerView recycler_log;
    Button button_export;

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
        graph = (GraphView) view.findViewById(R.id.graph);
        recycler_log = (RecyclerView) view.findViewById(R.id.recyclerLogWeight);
        button_toggle = (Button) view.findViewById(R.id.buttonToggle);
        button_new_entry = (Button) view.findViewById(R.id.buttonAddWeight);
        button_export = (Button) view.findViewById(R.id.buttonExport);

        // Resetting the helper arraylists used for populating the log's recycler view
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
        Collections.reverse(date_log);
        Collections.reverse(emission_log);

        GraphManager gm = new GraphManager(main);
        gm.setUpGraph(graph);

        button_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gm.toggleAdvanced(graph);
            }
        });
        button_new_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { main.changeFragment("entry"); }
        });
        button_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                em.writeJSON(0, main.getApplicationContext());
            }
        });
        setRecyclerAdapter();
    }

    // This method sets up the recycler adapter for the log's recycler view
    private void setRecyclerAdapter() {
        RecyclerAdapter adapter = new RecyclerAdapter(date_log, emission_log);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        recycler_log.setLayoutManager(lm);
        recycler_log.setItemAnimator(new DefaultItemAnimator());
        recycler_log.setAdapter(adapter);
    }

}