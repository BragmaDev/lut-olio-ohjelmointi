package com.example.ht;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class EntryManager {

    private Entry entry = null;

    private int beef_cons;
    private int fish_cons;

    private String req_url_head = "https://ilmastodieetti.ymparisto.fi/ilmastodieetti/calculatorapi/v1/FoodCalculator";
    private String req_url_tail = "?query.diet=omnivore";

    public void getResponse() {
        URL url = null;
        try {
            url = new URL(req_url_head + req_url_tail);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //int response_code = conn.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String input_line = "";
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

    public void parseResponse(StringBuffer response) {
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
}
