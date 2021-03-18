package com.example.viikko9;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class TheaterManager {
    private ArrayList<Theater> theaters = new ArrayList<Theater>();

    public TheaterManager() {}

    public ArrayList<String> getTheaterNames() {
        ArrayList<String> list = new ArrayList<String>();
        list.clear();
        for (int i = 0; i < theaters.size(); i++) {
            list.add(theaters.get(i).getName());
        }
        return list;
    }

    public void readXML(Document doc) {
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
}
