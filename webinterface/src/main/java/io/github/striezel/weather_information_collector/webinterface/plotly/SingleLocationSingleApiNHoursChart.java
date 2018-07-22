/*
 -------------------------------------------------------------------------------
    This file is part of the weather information collector webinterface.
    Copyright (C) 2018  Dirk Stolle

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
package io.github.striezel.weather_information_collector.webinterface.plotly;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import io.github.striezel.weather_information_collector.webinterface.data.Location;
import io.github.striezel.weather_information_collector.webinterface.data.RestApi;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Component for a plot showing weather data of a single location, limited to a
 * certain number of hours (i.e. potentially showing only the last e.g. 48
 * hours).
 *
 * @author Dirk Stolle
 */
@JavaScript({"plotly-1.38.3.min.js", "singlelocationsingleapinhourschart.js"})
public final class SingleLocationSingleApiNHoursChart extends AbstractJavaScriptComponent {

    /**
     * Constructor for a chart component that shows multiple time series.
     *
     * @param loc location for which the weather is shown
     * @param api the API which provided the data
     * @param hours timespan of the plot data in hours
     * @param dates common dates for the weather data
     * @param temperature list of temperature values (in °C) for the dates
     * @param relHumidity list of relative humidity values (in %) for the dates
     */
    public SingleLocationSingleApiNHoursChart(final Location loc,
            final RestApi api,
            final int hours,
            final List<Timestamp> dates,
            final List<Double> temperature,
            final List<Integer> relHumidity) {
        String plotTitle;
        if (loc != null && loc.hasName()) {
            plotTitle = "Weather of " + loc.name();
        } else if (loc != null) {
            plotTitle = "Weather of " + loc.toString();
        } else {
            plotTitle = "Weather of unknown location";
        }
        if (hours > 1) {
            plotTitle = plotTitle + " for the last " + String.valueOf(hours) + " hours";
        } else if (hours == 1) {
            plotTitle = plotTitle + " for the last hour";
        }
        if (null != api) {
            plotTitle = plotTitle + ", data provided by " + api.name();
        }
        getState().title = plotTitle;
        if (dates != null && !dates.isEmpty()
                && temperature != null && !temperature.isEmpty()
                && relHumidity != null && !relHumidity.isEmpty()) {
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            getState().dates = dates.stream()
                    .map((Timestamp ts) -> format.format(ts))
                    .collect(Collectors.toList());
            getState().temperature = temperature;
            getState().humidity = relHumidity;
        } else {
            getState().dates = new ArrayList<>();
            getState().temperature = new ArrayList<>();
            getState().humidity = new ArrayList<>();
        }
    }

    @Override
    protected SingleLocationSingleApiChartState getState() {
        return (SingleLocationSingleApiChartState) super.getState();
    }
}
