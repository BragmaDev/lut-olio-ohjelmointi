package com.example.ht;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EntryFragment extends Fragment {

    EntryManager em;
    View view;
    SeekBar seek_beef;
    SeekBar seek_fish;
    SeekBar seek_pork;
    SeekBar seek_dairy;
    SeekBar seek_cheese;
    SeekBar seek_rice;
    SeekBar seek_wsalad;
    EditText edit_restaurant;
    EditText edit_eggs;
    Button button_add;
    int[] cons = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    public EntryFragment(EntryManager em) { this.em = em; }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_entry, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        (seek_beef = (SeekBar) view.findViewById(R.id.seekBeef)).setMax(200);
        (seek_fish = (SeekBar) view.findViewById(R.id.seekFish)).setMax(200);
        (seek_pork = (SeekBar) view.findViewById(R.id.seekPork)).setMax(200);
        (seek_dairy = (SeekBar) view.findViewById(R.id.seekDairy)).setMax(200);
        (seek_cheese = (SeekBar) view.findViewById(R.id.seekCheese)).setMax(200);
        (seek_rice = (SeekBar) view.findViewById(R.id.seekRice)).setMax(200);
        (seek_wsalad = (SeekBar) view.findViewById(R.id.seekWsalad)).setMax(200);
        edit_restaurant = (EditText) view.findViewById(R.id.editRestaurant);
        edit_eggs = (EditText) view.findViewById(R.id.editEggs);
        resetInputs();

        button_add = (Button) view.findViewById(R.id.buttonAdd);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { addEntry(); }
        });
    }

    // Updating the array of consumption values
    private void updateConsumptionValues() {
        cons[0] = seek_beef.getProgress();
        cons[1] = seek_fish.getProgress();
        cons[2] = seek_pork.getProgress();
        cons[3] = seek_dairy.getProgress();
        cons[4] = seek_cheese.getProgress();
        cons[5] = seek_rice.getProgress();
        cons[6] = seek_wsalad.getProgress();
        cons[7] = Integer.parseInt(edit_restaurant.getText().toString());
        cons[8] = Integer.parseInt(edit_eggs.getText().toString());
    }

    // Resetting the seekbars and other input fields
    private void resetInputs() {
        seek_beef.setProgress(100);
        seek_fish.setProgress(100);
        seek_pork.setProgress(100);
        seek_dairy.setProgress(100);
        seek_cheese.setProgress(100);
        seek_rice.setProgress(100);
        seek_wsalad.setProgress(100);
        edit_restaurant.setText("");
        edit_eggs.setText("");
    }

    private void addEntry() {
        updateConsumptionValues();
        em.getResponse(cons);
        resetInputs();
    }
}