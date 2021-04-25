package com.example.ht;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.AdapterViewHolder> {
    private ArrayList<String> dates;
    private ArrayList<Double> values;

    public RecyclerAdapter(ArrayList<String> dates, ArrayList<Double> values) {
        this.dates = dates;
        this.values = values;
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView text_date;
        private TextView text_value;

        public AdapterViewHolder(final View view) {
            super(view);
            text_date = (TextView) view.findViewById(R.id.textTitle);
            text_value = (TextView) view.findViewById(R.id.textValue);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_log, parent, false);
        return new AdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.AdapterViewHolder holder, int position) {
        // Setting the item texts
        String date = dates.get(position);
        double value = values.get(position);
        holder.text_date.setText(date);
        holder.text_value.setText(String.format("%.2f", value) + " kg");
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }
}
