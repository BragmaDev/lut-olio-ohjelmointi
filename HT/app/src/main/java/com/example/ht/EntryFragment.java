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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EntryFragment extends Fragment {

    EntryManager em = EntryManager.getInstance();
    UserManager um = UserManager.getInstance();
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
    EditText edit_date;
    Date selected_date;
    TextView text_reminder;
    Button button_add;
    int[] cons = {0, 0, 0, 0, 0, 0, 0, 0, 0};

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
        edit_date = (EditText) view.findViewById(R.id.editDate);
        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        edit_date.setShowSoftInputOnFocus(false);
        (text_reminder = (TextView) view.findViewById(R.id.textReminder)).setText("");
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

    private void addEntry() {
        if (selected_date == null) {
            text_reminder.setText("Please select a date");
        } else {
            updateConsumptionValues();
            em.getResponse(cons);
            em.getEntry().setDate(selected_date);
            um.getUser().addEntry(em.getEntry());
            em.sortEntries(um.getUser().getEntries(0));
            resetInputs();
            text_reminder.setText("");
        }
    }
}