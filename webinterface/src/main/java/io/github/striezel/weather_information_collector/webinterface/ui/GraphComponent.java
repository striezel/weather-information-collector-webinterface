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
package io.github.striezel.weather_information_collector.webinterface.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import io.github.striezel.weather_information_collector.webinterface.data.Location;
import io.github.striezel.weather_information_collector.webinterface.data.RestApi;
import io.github.striezel.weather_information_collector.webinterface.data.Weather;
import io.github.striezel.weather_information_collector.webinterface.db.ConnectionInformation;
import io.github.striezel.weather_information_collector.webinterface.db.SourceMySQL;
import io.github.striezel.weather_information_collector.webinterface.graph.Generator;
import java.util.List;

/**
 * Vaadin component to show graph of weather data for a single location.
 *
 * @author Dirk Stolle
 */
public class GraphComponent extends VerticalLayout {

    /**
     * Constructs a component including a weather plot.
     *
     * @param selectedLocation the current location
     * @param api the API whose data shall be displayed
     * @param connInfo database connection information
     */
    public GraphComponent(Location selectedLocation, RestApi api, ConnectionInformation connInfo) {
        initComponents(selectedLocation, api, connInfo);
    }

    private void initComponents(Location selectedLocation, RestApi api, ConnectionInformation connInfo) {
        if (null == selectedLocation) {
            Label l1 = new Label("Please select a location from the list on the left.");
            l1.setIcon(VaadinIcons.LIST_SELECT);
            l1.setCaption("No city has been selected.");

            addComponents(l1);
        } else {
            if (null == connInfo) {
                addComponent(Utility.errorLabel("Could not find or load configuration file!"));
                return;
            }
            if (api == null) {
                addComponent(Utility.errorLabel("API is null!"));
                return;
            }
            SourceMySQL src = new SourceMySQL(connInfo);
            List<Weather> data = src.fetchData(selectedLocation, api);
            if (null == data) {
                addComponent(Utility.errorLabel("Could not load data for " + selectedLocation.name() + " from database!"));
                return;
            }
            addComponent(Generator.simple(selectedLocation, data));
        }
    }
}
