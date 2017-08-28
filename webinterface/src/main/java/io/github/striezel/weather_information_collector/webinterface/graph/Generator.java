/*
 -------------------------------------------------------------------------------
    This file is part of the weather information collector webinterface.
    Copyright (C) 2017  Dirk Stolle

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

import java.util.List;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.AxisType;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
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
 * Generates graphs / Vaadin charts.
 * 
 * @author Dirk Stolle
 */
public class Generator {

	/**
	 * Creates a simple chart with temperature and humidity data.
	 * 
	 * @param loc
	 *            the location / city
	 * @param data
	 *            list of data points
	 * @return Returns a chart that displays temperature and humidity of the given
	 *         location.
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

		/*
		 * { // Y axis for pressure YAxis axis = new YAxis();
		 * axis.setTitle("Pressure [hPa]"); axis.setOpposite(true); Style style = new
		 * Style(); style.setColor(SolidColor.GREEN); axis.getTitle().setStyle(style);
		 * conf.addyAxis(axis); }
		 */

		DataSeries dataTemp = new DataSeries("Temperature");
		DataSeries dataHum = new DataSeries("Humidity");
		// DataSeries dataPress = new DataSeries("Pressure");

		for (Weather w : data) {
			dataTemp.add(new DataSeriesItem(w.dataTime().toInstant(), w.temperatureCelsius()));
			dataHum.add(new DataSeriesItem(w.dataTime().toInstant(), w.humidity()));
			// dataPress.add(new DataSeriesItem(w.dataTime().toInstant(), w.pressure()));
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

		/*
		 * dataPress.setyAxis(2); { //set same color as pressure axis (green) for data
		 * PlotOptionsSpline pressureOpts = new PlotOptionsSpline();
		 * pressureOpts.setColor(SolidColor.GREEN);
		 * dataPress.setPlotOptions(pressureOpts); }
		 */

		conf.addSeries(dataTemp);
		conf.addSeries(dataHum);
		// conf.addSeries(dataPress);

		return chart;
	}

}
