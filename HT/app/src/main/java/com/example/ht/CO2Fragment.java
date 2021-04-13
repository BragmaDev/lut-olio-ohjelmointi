package com.example.ht;

import android.app.Activity;
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

import java.util.ArrayList;

public class CO2Fragment extends Fragment {

    View view;
    MainActivity main;
    ArrayList<String> date_log = new ArrayList<>();
    ArrayList<Double> emission_log = new ArrayList<>();

    Button button_new_entry;
    RecyclerView recycler_log;

    public CO2Fragment(MainActivity main) { this.main = main; }

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

    public void switchToEntryFragment() {
        main.changeFragment("entry");
    }
}