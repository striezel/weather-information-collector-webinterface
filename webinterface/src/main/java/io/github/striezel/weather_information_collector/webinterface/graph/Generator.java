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
import io.github.striezel.weather_information_collector.webinterface.data.Weather;
import io.github.striezel.weather_information_collector.webinterface.plotly.SingleLocationSingleApiChart;
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
     * @param data list of data points
     * @return Returns a chart that displays temperature and humidity of the
     * given location.
     */
    public static Component simple(Location loc, List<Weather> data) {
        if ((null == loc) || !loc.hasName()) {
            return Utility.errorLabel("Chart error: The given city has no name.");
        }
        if ((null == data) || data.isEmpty()) {
            return Utility.errorLabel("Chart error: There is no data for the city " + loc.name() + ".");
        }

        // Sort data by timestamp, because some chart implementations need it.
        data.sort((Weather a, Weather b) -> {
            return a.dataTime().compareTo(b.dataTime());
        });

        final List<Double> dataTemp = new ArrayList<>(data.size());
        final List<Integer> dataHum = new ArrayList<>(data.size());
        // TODO: add rain data
        final List<Timestamp> dates = new ArrayList<>(data.size());
        for (Weather w : data) {
            dates.add(w.dataTime());
            dataTemp.add((double) w.temperatureCelsius());
            dataHum.add(w.humidity());
        } // for
        Component chart = new SingleLocationSingleApiChart(
                loc, dates, dataTemp, dataHum);
        // TODO: axis for rain, light blue
        return chart;
    }

}
