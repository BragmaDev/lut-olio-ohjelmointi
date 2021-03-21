package com.example.viikko9;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class TheaterManager {
    private ArrayList<Theater> theaters = new ArrayList<Theater>();
    private String[] id_array = {"1014", "1015", "1016", "1017", "1041", "1018", "1019", "1021", "1022"};
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

    public Document loadXML(String url) {
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

    public ArrayList<String> readShowingsFromAllAreas(Date date, Date after, Date before, String movie_title) {
        SimpleDateFormat url_formatter = new SimpleDateFormat("dd.MM.yyyy");
        String date_string = "&dt=" + url_formatter.format(date);
        ArrayList<String> movie_titles = new ArrayList<String>();

        for (int i = 0; i < id_array.length; i++) {
            String url = "https://www.finnkino.fi/xml/Schedule/?area=" + id_array[i] + date_string;
            Document doc = loadXML(url);
            System.out.println(i + " ## " + id_array[i] + " ## " + url);
            ArrayList<String> temp_list = readShowingsXML(doc, after, before, movie_title);
            for (int j = 0; j < temp_list.size(); j++) {
                movie_titles.add(temp_list.get(j));
            }
        }
        return movie_titles;
    }

    public ArrayList<String> readShowingsXML(Document doc, Date after, Date before, String movie_title) {
        ArrayList<String> movie_titles = new ArrayList<String>();
        NodeList node_list = doc.getDocumentElement().getElementsByTagName("Show");

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

                    if (title.equals(movie_title) || movie_title.equals("")) {
                        String auditorium = element.getElementsByTagName("TheatreAndAuditorium").item(0).getTextContent();
                        String start_time = sdfFormatter.format(start_time_date);

                        movie_titles.add(title + "\n" + auditorium + " | " + start_time);
                    }
                }
            }
        }
        return movie_titles;
    }
}
