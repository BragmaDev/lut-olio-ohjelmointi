package com.example.viikko9;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TheaterManager {
    private ArrayList<Theater> theaters = new ArrayList<Theater>();
    SimpleDateFormat sdfParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    SimpleDateFormat sdfFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public TheaterManager() {}

    public ArrayList<String> getTheaterNames() {
        ArrayList<String> list = new ArrayList<String>();
        list.clear();
        for (int i = 0; i < theaters.size(); i++) {
            list.add(theaters.get(i).getName());
        }
        return list;
    }

    public String getTheaterId(String name) {
        for (int i = 0; i < theaters.size(); i++) {
            if (theaters.get(i).getName().equals(name)) {
                return theaters.get(i).getId();
            }
        }
        return "";
    }

    public void readAreasXML(Document doc) {
        NodeList node_list = doc.getDocumentElement().getElementsByTagName("TheatreArea");

        for (int i = 0; i < node_list.getLength(); i++) {
            Node node = node_list.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String name = element.getElementsByTagName("Name").item(0).getTextContent();
                String id = element.getElementsByTagName("ID").item(0).getTextContent();
                Theater theater = new Theater(name, id);
                theaters.add(theater);
            }

        }
    }

    public ArrayList<String> readShowingsXML(Document doc, Date after, Date before) {
        ArrayList<String> movie_titles = new ArrayList<String>();
        NodeList node_list = doc.getDocumentElement().getElementsByTagName("Show");

        System.out.println(sdfFormatter.format(after) + " - " + sdfFormatter.format(before));

        for (int i = 0; i < node_list.getLength(); i++) {
            Node node = node_list.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                Date start_time_date = null;
                try {
                    start_time_date = sdfParser.parse(element.getElementsByTagName("dttmShowStart").item(0).getTextContent());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (start_time_date.after(after) && start_time_date.before(before)) {
                    String title = element.getElementsByTagName("Title").item(0).getTextContent();
                    String auditorium = element.getElementsByTagName("TheatreAndAuditorium").item(0).getTextContent();
                    String start_time = sdfFormatter.format(start_time_date);

                    movie_titles.add(title + "\n" + auditorium + " | " + start_time);
                }
            }
        }
        return movie_titles;
    }
}
