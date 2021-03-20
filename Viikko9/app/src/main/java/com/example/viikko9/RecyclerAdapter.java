package com.example.viikko9;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.AdapterViewHolder> {
    private ArrayList<String> showings;

    public RecyclerAdapter(ArrayList<String> showings) {
        this.showings = showings;
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView text;

        public AdapterViewHolder(final View view) {
            super(view);
            text = (TextView) view.findViewById(R.id.textTitle);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_showings, parent, false);
        return new AdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.AdapterViewHolder holder, int position) {
        String title = showings.get(position);
        holder.text.setText(title);
    }

    @Override
    public int getItemCount() {
        return showings.size();
    }
}
