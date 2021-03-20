package com.example.viikko9;

import androidx.annotation.NonNull;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    Context context = null;
    String url_areas_xml = "https://www.finnkino.fi/xml/TheatreAreas/";
    String url_head = "https://www.finnkino.fi/xml/Schedule/?area=";
    String url_final = "";
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    Date current_date = new Date();
    TheaterManager manager = new TheaterManager();
    ArrayList<String> theater_names = new ArrayList<String>();
    ArrayList<String> showings = new ArrayList<String>();

    EditText edit_date;
    EditText edit_after;
    EditText edit_focused = null;
    EditText edit_before;
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
        spin_theaters = (Spinner) findViewById(R.id.spinTheater);
        recy_movies = (RecyclerView) findViewById(R.id.recyMovies);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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

        // Setting up the recycler view
        setRecyAdapter();

        // Loading areas to the list
        Document areas_xml = loadXML(url_areas_xml);
        manager.readAreasXML(areas_xml);

        // Setting up the spinner for theater selection
        theater_names = manager.getTheaterNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, theater_names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_theaters.setAdapter(adapter);
        spin_theaters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Forming the URL for the selected theater
                String theater_name = (String) parent.getItemAtPosition(position);
                url_final = url_head + manager.getTheaterId(theater_name) + "&dt=" + sdf.format(current_date);
                System.out.println(url_final);
                doc_movies = loadXML(url_final);
                showings = manager.readShowingsXML(doc_movies);
                setRecyAdapter();
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

    Document loadXML(String url) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(url);
            doc.getDocumentElement().normalize();

            return doc;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}