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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Component for a plot showing multiple line charts.
 *
 * @author Dirk Stolle
 */
@JavaScript({"plotly-1.38.3.min.js", "multitimeseries-plot.js"})
public class MultiTimeSeriesChartComponent extends AbstractJavaScriptComponent {

    /**
     * Constructor for a chart component that shows multiple time series.
     *
     * @param title title of the chart
     * @param dates common dates for the timeseries'
     * @param data map of timeseries data, where key is the name of the series
     * and value is the list of series values
     */
    public MultiTimeSeriesChartComponent(final String title,
            final List<Timestamp> dates,
            final Map<String, List<Double>> data) {
        getState().title = title;
        if (dates != null && !dates.isEmpty() && data != null && !data.isEmpty()) {
            final List<String> names = new ArrayList<>(data.size());
            final List<List<Double>> values = new ArrayList<>(data.size());
            for (String key : data.keySet()) {
                names.add(key);
                values.add(data.get(key));
            }

            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            getState().dates = dates.stream()
                    .map((Timestamp ts) -> format.format(ts))
                    .collect(Collectors.toList());
            getState().names = names;
            getState().values = values;
        } else {
            getState().names = new ArrayList<>();
            getState().values = new ArrayList<>();
        }
    }

    @Override
    protected MultiTimeSeriesChartComponentState getState() {
        return (MultiTimeSeriesChartComponentState) super.getState();
    }
}
