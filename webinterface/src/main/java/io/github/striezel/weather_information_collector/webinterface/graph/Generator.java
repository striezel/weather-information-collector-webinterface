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

import java.time.Instant;
import java.util.List;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.AxisType;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.PlotOptionsLine;
import com.vaadin.addon.charts.model.PlotOptionsSpline;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.charts.model.style.Style;
import com.vaadin.ui.Component;

import io.github.striezel.weather_information_collector.webinterface.data.Location;
import io.github.striezel.weather_information_collector.webinterface.data.Weather;
import io.github.striezel.weather_information_collector.webinterface.ui.Utility;

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

        Chart chart = new Chart();
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Weather data for " + loc.name());
        conf.getChart().setType(ChartType.LINE);

        conf.getTooltip().setFormatter(
                "' ' + Highcharts.dateFormat('%Y-%m-%d %H:%M', this.x) + '<br/>\' + this.series.name + ': <b>' + this.y + '</b>'");

        // X axis
        conf.getxAxis().setType(AxisType.LINEAR);
        conf.getxAxis().getLabels().setFormat("{value:%Y-%m-%d %H:%M}");

        {
            // Y axis for temperature
            YAxis temperatureAxis = new YAxis();
            temperatureAxis.setTitle("Temperature [°C]");
            Style style = new Style();
            style.setColor(SolidColor.RED);
            temperatureAxis.getTitle().setStyle(style);
            conf.addyAxis(temperatureAxis);
        }

        {
            // Y axis for humidity
            YAxis humidityAxis = new YAxis();
            humidityAxis.setTitle("Humidity [%]");
            humidityAxis.setOpposite(true);
            Style style = new Style();
            style.setColor(SolidColor.BLUE);
            humidityAxis.getTitle().setStyle(style);
            conf.addyAxis(humidityAxis);
        }

        DataSeries dataTemp = new DataSeries("Temperature");
        DataSeries dataHum = new DataSeries("Humidity");
        DataSeries dataRain = new DataSeries("Rain");

        for (Weather w : data) {
            Instant wInstant = w.dataTime().toInstant();
            dataTemp.add(new DataSeriesItem(wInstant, w.temperatureCelsius()));
            dataHum.add(new DataSeriesItem(wInstant, w.humidity()));
            if (w.hasRain()) {
                dataRain.add(new DataSeriesItem(wInstant, w.rain()));
            }
        } // for

        dataTemp.setyAxis(0);
        {
            // set same color as temperature axis (red) for data
            PlotOptionsLine tempOpts = new PlotOptionsLine();
            tempOpts.setColor(SolidColor.RED);
            dataTemp.setPlotOptions(tempOpts);
        }

        dataHum.setyAxis(1);
        {
            // set same color as humidity axis (blue) for data
            PlotOptionsSpline humOpts = new PlotOptionsSpline();
            humOpts.setColor(SolidColor.BLUE);
            dataHum.setPlotOptions(humOpts);
        }

        if (dataRain.size() > 1) {
            // Y axis for rain
            YAxis rainAxis = new YAxis();
            rainAxis.setTitle("Rain [mm]");
            rainAxis.setOpposite(true);
            Style style = new Style();
            style.setColor(SolidColor.LIGHTBLUE);
            rainAxis.getTitle().setStyle(style);
            conf.addyAxis(rainAxis);

            dataRain.setyAxis(2);
            PlotOptionsColumn rainOpts = new PlotOptionsColumn();
            rainOpts.setColor(SolidColor.LIGHTBLUE);
            dataRain.setPlotOptions(rainOpts);

            conf.addSeries(dataRain);
        } // if there is rain data

        conf.addSeries(dataTemp);
        conf.addSeries(dataHum);

        return chart;
    }

}
