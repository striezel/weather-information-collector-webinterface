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

import com.vaadin.shared.ui.JavaScriptComponentState;
import java.util.List;

/**
 * Component state for SingleLocationSingleApiChart.
 *
 * @author Dirk Stolle
 *
 */
public class SingleLocationSingleApiChartState extends JavaScriptComponentState {

    /**
     * title of the plot
     */
    public String title;

    /**
     * dates in the series
     */
    public List<String> dates;
    
    /**
     * temperature values in °C
     */
    public List<Double> temperature;
    
    /**
     * relative humidity in percent
     */
    public List<Integer> humidity;
}
