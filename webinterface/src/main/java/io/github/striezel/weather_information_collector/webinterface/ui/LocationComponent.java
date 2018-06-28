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

import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import io.github.striezel.weather_information_collector.webinterface.data.Location;
import io.github.striezel.weather_information_collector.webinterface.data.RestApi;
import io.github.striezel.weather_information_collector.webinterface.db.ConnectionInformation;
import io.github.striezel.weather_information_collector.webinterface.db.SourceMySQL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Dirk Stolle
 */
public class LocationComponent extends HorizontalLayout {

    public LocationComponent(ConnectionInformation connInfo) {
        init(connInfo);
    }

    private void init(ConnectionInformation connInfo) {
        if (null == connInfo) {
            addComponent(Utility.errorLabel("Could not find or load configuration file!"));
            return;
        }

        SourceMySQL src = new SourceMySQL(connInfo);
        final List<AbstractMap.SimpleImmutableEntry<Location, RestApi>> locations = src.listLocationsWithApi();
        if (null == locations) {
            addComponent(Utility.errorLabel("Could not load list of locations from database!"));
            return;
        }
        if (locations.isEmpty()) {
            addComponent(Utility.errorLabel("There are no locations in the database yet."));
            return;
        }

        Grid<AbstractMap.SimpleImmutableEntry<Location, RestApi>> grid = new Grid<>();
        grid.addColumn(AbstractMap.SimpleImmutableEntry<Location, RestApi>::getKey).setCaption("City");
        grid.addColumn(AbstractMap.SimpleImmutableEntry<Location, RestApi>::getValue).setCaption("API");

        List<AbstractMap.SimpleImmutableEntry<Location, RestApi>> items = new ArrayList<>(locations.size());
        locations.stream().forEach((l) -> {
            items.add(l);
        });
        grid.setItems(items);

        grid.addSelectionListener((event) -> {
            Optional<AbstractMap.SimpleImmutableEntry<Location, RestApi>> item = (Optional<AbstractMap.SimpleImmutableEntry<Location, RestApi>>) event.getFirstSelectedItem();
            if (item.isPresent()) {

                Notification.show("City changed:", item.get().getKey().name()
                        + ", " + item.get().getValue().name(),
                        Notification.Type.TRAY_NOTIFICATION);
                // TODO: listener etc.
            }
        });
        grid.setCaption("Locations");
        addComponent(grid);
    }
}
