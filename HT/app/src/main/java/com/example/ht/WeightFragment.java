package com.example.ht;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

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
import java.util.Calendar;
import java.util.Date;

public class WeightFragment extends Fragment {

    View view;
    MainActivity main = null;
    EntryManager em = EntryManager.getInstance();
    UserManager um = UserManager.getInstance();
    ArrayList<String> date_log = new ArrayList<>();
    ArrayList<Double> weight_log = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    GraphView graph;
    EditText edit_weight;
    EditText edit_date;
    TextView text_reminder;
    Button button_new_entry;
    RecyclerView recycler_log;
    Button button_export;

    double weight;
    Date selected_date;

    public WeightFragment(MainActivity main) { this.main = main; }
    public WeightFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_weight, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        updateLog();

        graph = (GraphView) view.findViewById(R.id.graph);
        updateGraph();
        edit_weight = (EditText) view.findViewById(R.id.editWeight);
        edit_date = (EditText) view.findViewById(R.id.editDateWeight);
        edit_date.setShowSoftInputOnFocus(false);
        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        recycler_log = (RecyclerView) view.findViewById(R.id.recyclerLogWeight);
        text_reminder = (TextView) view.findViewById(R.id.textReminderWeight);
        button_new_entry = (Button) view.findViewById(R.id.buttonAddWeight);
        button_new_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weight = Double.parseDouble(edit_weight.getText().toString());
                if (weight > 0 && weight < 450) {
                    if (selected_date != null) {
                        em.setEntry(new WeightEntry(weight));
                        em.getEntry().setDate(selected_date);
                        um.getUser().addEntry(em.getEntry());
                        em.sortEntries(um.getUser().getEntries(1));
                        updateLog();
                        updateGraph();
                        setRecyclerAdapter();
                        resetInputs();
                        text_reminder.setText("");
                        um.saveUsers();
                    } else {
                        text_reminder.setText("Please select a date");
                    }
                } else {
                    text_reminder.setText("Please set a valid weight");
                }
            }
        });
        button_export = (Button) view.findViewById(R.id.buttonExport);
        button_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportLog();
            }
        });
        setRecyclerAdapter();
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        DatePickerDialog.OnDateSetListener dsl = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                edit_date.setText(sdf.format(calendar.getTime()));
                selected_date = calendar.getTime();
            }
        };

        new DatePickerDialog(getContext(), dsl, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void setRecyclerAdapter() {
        RecyclerAdapter adapter = new RecyclerAdapter(date_log, weight_log);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        recycler_log.setLayoutManager(lm);
        recycler_log.setItemAnimator(new DefaultItemAnimator());
        recycler_log.setAdapter(adapter);
    }

    private void updateLog() {
        date_log.clear();
        weight_log.clear();

        if (um.getUser().getEntries(1).size() > 0) {
            for (Entry e : um.getUser().getEntries(1)) {
                if (e instanceof WeightEntry) {
                    date_log.add(sdf.format(e.getDate()));
                    weight_log.add(((WeightEntry) e).getWeight());
                }
            }
        }
    }

    private void updateGraph() {
        graph.removeAllSeries();
        DataPoint[] datapoints = new DataPoint[um.getUser().getEntries(1).size()];
        for (int i = 0; i < um.getUser().getEntries(1).size(); i++) {
            WeightEntry e = (WeightEntry) um.getUser().getEntries(1).get(i);
            datapoints[i] = new DataPoint(e.getDate().getTime() / 1000000000.0, e.getWeight());
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(datapoints);
        series.setColor(getResources().getColor(R.color.green_500, null));
        GridLabelRenderer glr = graph.getGridLabelRenderer();
        glr.setHorizontalAxisTitle("Date");
        glr.setHorizontalAxisTitleColor(getResources().getColor(R.color.grey_50, null));
        glr.setVerticalAxisTitle("Weight");
        glr.setVerticalAxisTitleColor(getResources().getColor(R.color.grey_50, null));
        glr.setVerticalLabelsVisible(false);
        glr.setHorizontalLabelsVisible(false);
        glr.setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0.0);
        if (datapoints.length > 0) {
            graph.getViewport().setMinX(datapoints[0].getX());
            graph.getViewport().setMaxX(datapoints[datapoints.length - 1].getX());
            for (DataPoint datapoint : datapoints) {
                if (graph.getViewport().getMaxY(false) < datapoint.getY()) {
                    graph.getViewport().setMaxY(datapoint.getY());
                }
            }
        } else {
            graph.getViewport().setMaxX(1);
            graph.getViewport().setMaxY(1);
        }
        graph.addSeries(series);
    }

    private void resetInputs() {
        edit_date.setText("");
        edit_weight.setText("");
    }

    private void exportLog() {
        em.writeJSON();
    }


}