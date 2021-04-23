package com.example.ht;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class EntryManager {

    private Entry entry = null;
    private String req_url_tail = "";

    private final static EntryManager em = new EntryManager();
    private EntryManager() {}
    public static EntryManager getInstance() { return em; }

    UserManager um = UserManager.getInstance();

    // This method adds the parameters to the request URL based on the user's inputs
    private void constructTail(int[] cons) {
        req_url_tail = ("?query.diet=" + um.getUser().getDiet()
                + "&query.lowCarbonPreference=" + um.getUser().getLowCarbon()
                + "&query.beefLevel=" + cons[0]
                + "&query.fishLevel=" + cons[1]
                + "&query.porkPoultryLevel=" + cons[2]
                + "&query.dairyLevel=" + cons[3]
                + "&query.cheeseLevel=" + cons[4]
                + "&query.riceLevel=" + cons[5]
                + "&query.winterSaladLevel=" + cons[6]
                + "&query.restaurantSpending=" + cons[7]
                + "&query.eggLevel=" + cons[8]
        );
    }

    /* This method sends the http request to the climate diet API and gets the response. Afterwards,
    it calls the "parseResponse" method */
    public void getResponse(int[] cons) {
        URL url;

        constructTail(cons);
        try {
            String req_url_head = "https://ilmastodieetti.ymparisto.fi/ilmastodieetti/calculatorapi/v1/FoodCalculator";
            url = new URL(req_url_head + req_url_tail);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String input_line;
            StringBuffer response = new StringBuffer();
            while ((input_line = in.readLine()) != null) {
                response.append(input_line);
            }
            in.close();
            parseResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* This method parses the response passed by the "getResponse" method by splitting the string
    and modifying it to leave only the wanted number values. The values are put into an array,
    which is used to create a new climate diet entry */
    private void parseResponse(StringBuffer response) {
        String[] s_arr = response.toString().split(",", 5);
        double[] d_arr = {0.0, 0.0, 0.0, 0.0, 0.0};

        for (int i = 0; i < s_arr.length; i++) {
            // Getting the double value of each line
            String s = s_arr[i].split(":", 2)[1];
            s = s.replace("}", "");
            double value = Double.parseDouble(s);
            d_arr[i] = value;
        }

        entry = new ClimateDietEntry(d_arr[0], d_arr[1], d_arr[2], d_arr[3], d_arr[4]);
    }

    // Methods for getting and setting the current entry
    public Entry getEntry() { return entry; }
    public void setEntry(Entry entry) { this.entry = entry; }

    public void sortEntries(ArrayList<Entry> entries) {
        Collections.sort(entries, new Comparator<Entry>() {
            @Override
            public int compare(Entry o1, Entry o2) {
                return o1.getDate().getTime() > o2.getDate().getTime() ? 1 : -1;
            }
        });
    }

    /* This method takes in the arraylist_id parameter and writes either the climate diet log or the
    weight log JSON file based on the id (0 = climate diet, anything else = weight). It overwrites
    the previously written file. */
    public void writeJSON(int arraylist_id, Context context) {
        JSONObject obj = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String filename_tail;
        if (arraylist_id == 0) {
            filename_tail = "_climatediet_log.json";
            for (Entry entry : um.getUser().getEntries(0)) {
                ClimateDietEntry cde = (ClimateDietEntry) entry;
                try {
                    JSONArray arr = new JSONArray();
                    arr.put("Dairy: " + cde.getEmissions()[0] + " kg");
                    arr.put("Meat: " + cde.getEmissions()[1] + "kg");
                    arr.put("Plant: " + cde.getEmissions()[2] + "kg");
                    arr.put("Restaurant: " + cde.getEmissions()[3] + "kg");
                    arr.put("Total: " + cde.getEmissions()[4] + "kg");
                    obj.put(sdf.format(entry.getDate()), arr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            filename_tail = "_weight_log.json";
            for (Entry entry : um.getUser().getEntries(1)) {
                WeightEntry we = (WeightEntry) entry;
                try {
                    obj.put(sdf.format(entry.getDate()), we.getWeight() + " kg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            FileOutputStream fos = context.openFileOutput(um.getUser().getName() + filename_tail, Context.MODE_PRIVATE);
            byte b[] = obj.toString(4).getBytes();
            fos.write(b);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
