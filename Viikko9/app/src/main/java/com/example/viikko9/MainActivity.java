package com.example.viikko9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    Context context = null;
    String url_areas_xml = "https://www.finnkino.fi/xml/TheatreAreas/";
    String url_head = "https://www.finnkino.fi/xml/Schedule/?area=";
    String url_final = "";
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    Date current_date = new Date();
    TheaterManager manager = new TheaterManager();
    ArrayList<String> theater_names = new ArrayList<String>();
    ArrayList<String> showings = new ArrayList<String>();
    Spinner spin_theaters;
    RecyclerView recy_movies;
    Document doc_movies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        spin_theaters = (Spinner) findViewById(R.id.spinTheater);
        recy_movies = (RecyclerView) findViewById(R.id.recyMovies);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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