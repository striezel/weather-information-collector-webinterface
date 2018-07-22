/*
 -------------------------------------------------------------------------------
    This file is part of the weather information collector webinterface.
    Copyright (C) 2017, 2018  Dirk Stolle

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 -------------------------------------------------------------------------------
 */
package io.github.striezel.weather_information_collector.webinterface.graph;

import com.vaadin.ui.Component;
import io.github.striezel.weather_information_collector.webinterface.data.Location;
import io.github.striezel.weather_information_collector.webinterface.data.RestApi;
import io.github.striezel.weather_information_collector.webinterface.data.Weather;
import io.github.striezel.weather_information_collector.webinterface.plotly.SingleLocationSingleApiChart;
import io.github.striezel.weather_information_collector.webinterface.plotly.SingleLocationSingleApiNHoursChart;
import io.github.striezel.weather_information_collector.webinterface.ui.Utility;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates graphs / charts.
 *
 * @author Dirk Stolle
 */
public class Generator {

    /**
     * Creates a simple chart with temperature and humidity data.
     *
     * @param loc the location / city
     * @param api the API which provided the data
     * @param data list of data points
     * @param showGap whether to show gaps in the data as such
     * @return Returns a chart that displays temperature and humidity of the
     * given location.
     */
    public static Component simple(Location loc, RestApi api, List<Weather> data, boolean showGap) {
        if ((null == loc) || !loc.hasName()) {
            return Utility.errorLabel("Chart error: The given city has no name.");
        }
        if ((null == data) || data.isEmpty()) {
            return Utility.errorLabel("Chart error: There is no data for the city " + loc.name() + ".");
        }

        final List<Double> dataTemp = new ArrayList<>(data.size());
        final List<Integer> dataHum = new ArrayList<>(data.size());
        // TODO: add rain data
        final List<Timestamp> dates = new ArrayList<>(data.size());
        preprocessing(data, dataTemp, dataHum, dates, showGap);
        Component chart = new SingleLocationSingleApiChart(
                loc, api, dates, dataTemp, dataHum);
        // TODO: axis for rain, light blue
        return chart;
    }

    /**
     * Creates a simple chart with temperature and humidity data limited to a
     * certain number of hours (i.e. potentially showing only the last e.g. 48
     * hours).
     *
     * @param loc the location / city
     * @param api the API which provided the data
     * @param hours timespan of the weather data in hours
     * @param data list of data points
     * @param showGap whether to show gaps in the data as such
     * @return Returns a chart that displays temperature and humidity of the
     * given location.
     */
    public static Component simpleNHours(Location loc, RestApi api, int hours, List<Weather> data, boolean showGap) {
        if ((null == loc) || !loc.hasName()) {
            return Utility.errorLabel("Chart error: The given city has no name.");
        }
        if ((null == data) || data.isEmpty()) {
            return Utility.errorLabel("Chart error: There is no data for the city " + loc.name() + ".");
        }
        final List<Double> dataTemp = new ArrayList<>(data.size());
        final List<Integer> dataHum = new ArrayList<>(data.size());
        // TODO: add rain data
        final List<Timestamp> dates = new ArrayList<>(data.size());
        preprocessing(data, dataTemp, dataHum, dates, showGap);
        Component chart = new SingleLocationSingleApiNHoursChart(
                loc, api, hours, dates, dataTemp, dataHum);
        // TODO: axis for rain, light blue
        return chart;
    }

    /**
     * Does some common preprocessing for the chart data.
     *
     * @param data the original weather data from the database
     * @param dataTemp empty list for temperature data
     * @param dataHum empty list to fill with humidity data
     * @param dates empty list to fill with dates
     * @param showGap whether to show gaps in the data as such
     */
    private static void preprocessing(List<Weather> data,
            final List<Double> dataTemp, final List<Integer> dataHum,
            final List<Timestamp> dates, boolean showGap) {
        // Sort data by timestamp, because some chart implementations need it.
        data.sort((Weather a, Weather b) -> {
            return a.dataTime().compareTo(b.dataTime());
        });
        Timestamp previous = !data.isEmpty() ? data.get(0).dataTime() : null;
        // Limit: 6 hours + 15 seconds
        final long limit = 1000 * 3600 * 6 + 15000;
        for (Weather w : data) {
            if (showGap) {
                // If there is a gap greater than the limit in the data, add a null
                // data point so that the gap shows in the Plotly graph.
                long diffMilliseconds = w.dataTime().getTime() - previous.getTime();
                if (diffMilliseconds > limit) {
                    // Add data point with null values halfway between both data points.
                    dates.add(new Timestamp(previous.getTime() + (diffMilliseconds / 2)));
                    dataTemp.add(null);
                    dataHum.add(null);
                }
                previous = w.dataTime();
            } // if showGap
            dates.add(w.dataTime());
            dataTemp.add((double) w.temperatureCelsius());
            dataHum.add(w.humidity());
        } // for
    }

}
