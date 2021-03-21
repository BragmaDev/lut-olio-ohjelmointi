package com.example.viikko9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    Context context = null;
    String url_areas_xml = "https://www.finnkino.fi/xml/TheatreAreas/";
    String url_head = "https://www.finnkino.fi/xml/Schedule/?area=";
    String url_final = "";
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    Date current_date = new Date();
    Date desired_date = new Date();
    Date after = current_date, before = current_date;
    TheaterManager manager = new TheaterManager();
    ArrayList<String> theater_names = new ArrayList<String>();
    ArrayList<String> showings = new ArrayList<String>();
    String theater_name = "";
    String movie_title = "";

    EditText edit_date;
    EditText edit_after;
    EditText edit_focused = null;
    EditText edit_before;
    EditText edit_movie;
    Spinner spin_theaters;
    RecyclerView recy_movies;
    Document doc_movies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        edit_date = (EditText) findViewById(R.id.editDate);
        edit_after = (EditText) findViewById(R.id.editAfter);
        edit_before = (EditText) findViewById(R.id.editBefore);
        edit_movie = (EditText) findViewById(R.id.editMovie);
        spin_theaters = (Spinner) findViewById(R.id.spinTheater);
        recy_movies = (RecyclerView) findViewById(R.id.recyMovies);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Initial values for the time pickers
        after = setTime(0, 0);
        before = setTime(23, 59);

        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(edit_date);
            }
        });

        edit_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_focused = edit_after;
                DialogFragment time_picker = new TimePickerFragment();
                time_picker.show(getSupportFragmentManager(), "time picker");
            }
        });

        edit_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_focused = edit_before;
                DialogFragment time_picker = new TimePickerFragment();
                time_picker.show(getSupportFragmentManager(), "time picker");
            }
        });

        // Disabling keyboard input on the date and time pickers
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21
            edit_date.setShowSoftInputOnFocus(false);
            edit_after.setShowSoftInputOnFocus(false);
            edit_before.setShowSoftInputOnFocus(false);
        } else { // API 11-20
            edit_date.setTextIsSelectable(true);
            edit_after.setTextIsSelectable(true);
            edit_before.setTextIsSelectable(true);
        }

        edit_movie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                movie_title = edit_movie.getText().toString();

            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Setting up the recycler view
        setRecyAdapter();

        // Loading areas to the list
        Document areas_xml = manager.loadXML(url_areas_xml);
        manager.readAreasXML(areas_xml);

        // Setting up the spinner for theater selection
        theater_names = manager.getTheaterNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, theater_names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_theaters.setAdapter(adapter);
        spin_theaters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                theater_name = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        edit_focused.setText(time_formatter.format(calendar.getTime()));
        if (edit_focused == edit_after) {
            after = setTime(hourOfDay, minute);
        } else if (edit_focused == edit_before) {
            before = setTime(hourOfDay, minute);
        }
    }

    private void showDateDialog(EditText edit_date) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dsl = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                edit_date.setText(sdf.format(calendar.getTime()));
                desired_date = calendar.getTime();

                // Updating the date for the time interval
                after.setYear(desired_date.getYear());
                after.setMonth(desired_date.getMonth());
                after.setDate(desired_date.getDate());
                before.setYear(desired_date.getYear());
                before.setMonth(desired_date.getMonth());
                before.setDate(desired_date.getDate());
            }
        };

        new DatePickerDialog(context, dsl, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void setRecyAdapter() {
        RecyclerAdapter adapter = new RecyclerAdapter(showings);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(context);
        recy_movies.setLayoutManager(lm);
        recy_movies.setItemAnimator(new DefaultItemAnimator());
        recy_movies.setAdapter(adapter);
    }

    private Date setTime(int hour, int minute) {
        Date date;
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_MONTH, desired_date.getDate());
        calendar.set(Calendar.MONTH, desired_date.getMonth());
        calendar.set(Calendar.YEAR, desired_date.getYear() + 1900);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        date = calendar.getTime();
        return date;
    }

    public void updateRecycler(View v) {
        if (!movie_title.equals("") && manager.getTheaterId(theater_name).equals("1029")) {
            // Searching from all theaters
            showings = manager.readShowingsFromAllAreas(desired_date, after, before, movie_title);
            setRecyAdapter();
        } else {
            url_final = url_head + manager.getTheaterId(theater_name) + "&dt=" + sdf.format(desired_date);
            doc_movies = manager.loadXML(url_final);
            showings = manager.readShowingsXML(doc_movies, after, before, movie_title);
            setRecyAdapter();
        }
    }
}