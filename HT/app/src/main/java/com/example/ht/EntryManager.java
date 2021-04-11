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

        for (int i = 0; i < s_arr.length; i++) {

            // Getting the float value of each line
            String s = s_arr[i].split(":", 2)[1];
            s = s.replace("}", "");
            double value = Double.parseDouble(s);
            System.out.println(value);
        }
        /*
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(response.toString())));
            doc.getDocumentElement().normalize();

            NodeList nodes = doc.getElementsByTagName("FoodCalculatorController.FoodCalculationResult");
            if (nodes.getLength() > 0) {
                Element err = (Element) nodes.item(0);
                System.out.println(err.getElementsByTagName("Dairy").item(0).getTextContent());
                System.out.println("##############################");
                System.out.println("##############################");
                System.out.println("##############################");
                System.out.println("##############################");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
         */
    }
}
