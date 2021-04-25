package com.example.ht;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EntryFragment extends Fragment {

    MainActivity main = null;
    EntryManager em = EntryManager.getInstance();
    UserManager um = UserManager.getInstance();
    View view;
    TextView text_consumption;
    SeekBar seek_beef;
    SeekBar seek_fish;
    SeekBar seek_pork;
    SeekBar seek_dairy;
    SeekBar seek_cheese;
    SeekBar seek_rice;
    SeekBar seek_wsalad;
    EditText edit_restaurant;
    EditText edit_eggs;
    EditText edit_date;
    Date selected_date;
    TextView text_reminder;
    Button button_add;
    int[] cons = {0, 0, 0, 0, 0, 0, 0, 0, 0}; // Array for consumption inputs

    public EntryFragment(MainActivity main) { this.main = main; }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_entry, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        text_consumption = (TextView) view.findViewById(R.id.textConsumption);
        (seek_beef = (SeekBar) view.findViewById(R.id.seekBeef)).setMax(200);
        (seek_fish = (SeekBar) view.findViewById(R.id.seekFish)).setMax(200);
        (seek_pork = (SeekBar) view.findViewById(R.id.seekPork)).setMax(200);
        (seek_dairy = (SeekBar) view.findViewById(R.id.seekDairy)).setMax(200);
        (seek_cheese = (SeekBar) view.findViewById(R.id.seekCheese)).setMax(200);
        (seek_rice = (SeekBar) view.findViewById(R.id.seekRice)).setMax(200);
        (seek_wsalad = (SeekBar) view.findViewById(R.id.seekWsalad)).setMax(200);
        edit_restaurant = (EditText) view.findViewById(R.id.editRestaurant);
        edit_eggs = (EditText) view.findViewById(R.id.editEggs);
        edit_date = (EditText) view.findViewById(R.id.editDate);
        button_add = (Button) view.findViewById(R.id.buttonAdd);
        (text_reminder = (TextView) view.findViewById(R.id.textReminder)).setText("");
        resetInputs();

        // Setting the seek bars to update the consumption text
        seek_beef.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text_consumption.setText(String.format("Beef: %.2f kg/week", progress * 0.01 * 0.4));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        seek_fish.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text_consumption.setText(String.format("Fish: %.2f kg/week", progress * 0.01 * 0.6));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        seek_pork.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text_consumption.setText(String.format("Pork and poultry: %.2f kg/week", progress * 0.01));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        seek_dairy.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text_consumption.setText(String.format("Dairy: %.2f kg/week", progress * 0.01 * 3.8));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        seek_cheese.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text_consumption.setText(String.format("Cheese: %.2f kg/week", progress * 0.01 * 0.3));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        seek_rice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text_consumption.setText(String.format("Rice: %.2f kg/week", progress * 0.01 * 0.09));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        seek_wsalad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text_consumption.setText(String.format("Winter salad: %.2f kg/week", progress * 0.01 * 1.4));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        edit_date.setShowSoftInputOnFocus(false);

        text_consumption.setText("- kg/week");

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
        if (!edit_restaurant.getText().toString().equals("")) {
            cons[7] = Integer.parseInt(edit_restaurant.getText().toString());
        }
        if (!edit_eggs.getText().toString().equals("")) {
            cons[8] = Integer.parseInt(edit_eggs.getText().toString());
        }
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
        edit_date.setText("");
        selected_date = null;
        for (int i = 0; i < cons.length; i++) {
            cons[i] = 0;
        }
    }

    // This method opens up the date picker
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

    /* This method creates a new entry with the currently inputted values, resets the inputs and
    changes back to the CO2 fragment */
    private void addEntry() {
        if (selected_date == null) {
            text_reminder.setText("Please select a date");
        } else {
            updateConsumptionValues();
            em.getResponse(cons);
            em.getEntry().setDate(selected_date);
            um.getUser().addEntry(em.getEntry());
            em.sortEntries(um.getUser().getEntries(0));
            text_reminder.setText("");
            um.saveUsers(main.getApplicationContext());
            main.changeFragment("co2");
        }
    }
}