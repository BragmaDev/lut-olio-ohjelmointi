package com.example.ht;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.AdapterViewHolder> {
    private ArrayList<String> dates;
    private ArrayList<Double> emissions;

    public RecyclerAdapter(ArrayList<String> dates, ArrayList<Double> emissions) {
        this.dates = dates;
        this.emissions = emissions;
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView text_date;
        private TextView text_emission;

        public AdapterViewHolder(final View view) {
            super(view);
            text_date = (TextView) view.findViewById(R.id.textDate);
            text_emission = (TextView) view.findViewById(R.id.textEmission);
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
        String date = dates.get(position);
        double emission = emissions.get(position);
        holder.text_date.setText(date);
        holder.text_emission.setText(String.format("%.2f", emission) + " kg");
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }
}
