package com.example.ht;

import android.graphics.Color;

import androidx.core.content.res.ResourcesCompat;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphManager {
    UserManager um = UserManager.getInstance();
    MainActivity main = null;

    LineGraphSeries<DataPoint> series_dairy;
    LineGraphSeries<DataPoint> series_meat;
    LineGraphSeries<DataPoint> series_plant;
    LineGraphSeries<DataPoint> series_restaurant;
    LineGraphSeries<DataPoint> series_total;

    boolean advanced_graph_toggled = false;

    public GraphManager(MainActivity main) { this.main = main; }

    public void setUpGraph(GraphView graph) {
        advanced_graph_toggled = false;
        DataPoint[] datapoints_dairy = new DataPoint[um.getUser().getEntries(0).size()];
        DataPoint[] datapoints_meat = new DataPoint[um.getUser().getEntries(0).size()];
        DataPoint[] datapoints_plant = new DataPoint[um.getUser().getEntries(0).size()];
        DataPoint[] datapoints_restaurant = new DataPoint[um.getUser().getEntries(0).size()];
        DataPoint[] datapoints_total = new DataPoint[um.getUser().getEntries(0).size()];

        for (int i = 0; i < um.getUser().getEntries(0).size(); i++) {
            ClimateDietEntry e = (ClimateDietEntry) um.getUser().getEntries(0).get(i);
            datapoints_dairy[i] = new DataPoint(e.getDate().getTime() / 1000000000.0, e.getEmissions()[0]);
            datapoints_meat[i] = new DataPoint(e.getDate().getTime() / 1000000000.0, e.getEmissions()[1]);
            datapoints_plant[i] = new DataPoint(e.getDate().getTime() / 1000000000.0, e.getEmissions()[2]);
            datapoints_restaurant[i] = new DataPoint(e.getDate().getTime() / 1000000000.0, e.getEmissions()[3]);
            datapoints_total[i] = new DataPoint(e.getDate().getTime() / 1000000000.0, e.getEmissions()[4]);
        }

        series_dairy = new LineGraphSeries<DataPoint>(datapoints_dairy);
        series_meat = new LineGraphSeries<DataPoint>(datapoints_meat);
        series_plant = new LineGraphSeries<DataPoint>(datapoints_plant);
        series_restaurant = new LineGraphSeries<DataPoint>(datapoints_restaurant);
        series_total = new LineGraphSeries<DataPoint>(datapoints_total);

        series_dairy.setColor(main.getResources().getColor(R.color.grey_50, null));
        series_meat.setColor(main.getResources().getColor(R.color.red_600, null));
        series_plant.setColor(main.getResources().getColor(R.color.amber_500, null));
        series_restaurant.setColor(main.getResources().getColor(R.color.teal_700, null));
        series_total.setColor(main.getResources().getColor(R.color.green_500, null));

        series_dairy.setTitle("Dairy");
        series_meat.setTitle("Meat");
        series_plant.setTitle("Plant");
        series_restaurant.setTitle("Restaurant");
        series_total.setTitle("Total");

        GridLabelRenderer glr = graph.getGridLabelRenderer();
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getLegendRenderer().setVisible(false);
        graph.getLegendRenderer().setTextSize(32);
        graph.getLegendRenderer().setTextColor(Color.rgb(236, 239, 241));
        graph.getLegendRenderer().setBackgroundColor(Color.argb(15, 0, 0, 0));

        glr.setHorizontalAxisTitle("Date");
        glr.setHorizontalAxisTitleColor(main.getResources().getColor(R.color.grey_50, null));
        glr.setVerticalAxisTitle("Emission");
        glr.setVerticalAxisTitleColor(main.getResources().getColor(R.color.grey_50, null));
        glr.setVerticalLabelsVisible(false);
        glr.setHorizontalLabelsVisible(false);
        glr.setGridStyle(GridLabelRenderer.GridStyle.NONE);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0.0);

        if (datapoints_total.length > 0) {
            graph.getViewport().setMinX(datapoints_total[0].getX());
            graph.getViewport().setMaxX(datapoints_total[datapoints_total.length - 1].getX());
            for (DataPoint datapoint : datapoints_total) {
                if (graph.getViewport().getMaxY(false) < datapoint.getY()) {
                    graph.getViewport().setMaxY(datapoint.getY());
                }
            }
        } else {
            graph.getViewport().setMaxX(1);
            graph.getViewport().setMaxY(1);
        }
        graph.addSeries(series_total);
    }

    public void toggleAdvanced(GraphView graph) {
        advanced_graph_toggled = !advanced_graph_toggled;
        if (advanced_graph_toggled) {
            graph.addSeries(series_dairy);
            graph.addSeries(series_meat);
            graph.addSeries(series_plant);
            graph.addSeries(series_restaurant);
            graph.getLegendRenderer().setVisible(true);
        } else {
            graph.removeAllSeries();
            graph.addSeries(series_total);
            graph.getLegendRenderer().setVisible(false);
        }
    }
}
