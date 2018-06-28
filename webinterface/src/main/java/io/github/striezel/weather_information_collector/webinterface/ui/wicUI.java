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

import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import io.github.striezel.weather_information_collector.webinterface.data.Location;
import io.github.striezel.weather_information_collector.webinterface.db.ConnectionInformation;
import io.github.striezel.weather_information_collector.webinterface.db.Loader;
import io.github.striezel.weather_information_collector.webinterface.db.SourceMySQL;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("wictheme")
public class wicUI extends UI {

    private static final long serialVersionUID = -1848954295477812552L;

    private ConnectionInformation connInfo = null;
    private Location selectedLocation = null;
    private List<Location> availableLocations = null;

    private Component graph = null;

    /**
     * Loads available locations from the database.
     */
    private void loadAvailableLocations() {
        if (null == connInfo) {
            availableLocations = null;
            return;
        }
        SourceMySQL src = new SourceMySQL(connInfo);
        availableLocations = src.listLocations();
    }

    /**
     * Finds a location by name.
     *
     * @param name the name of the location
     * @return Returns the first matching location, if successful. Returns null,
     * if no location was found.
     */
    private Location findLocationByName(String name) {
        if ((null == name) || name.isEmpty()) {
            return null;
        }
        if (availableLocations == null) {
            return null;
        }
        // Remove square brackets, if they are present.
        if (name.startsWith("[") && name.endsWith("]")) {
            name = name.substring(1, name.length() - 1);
        }
        for (Location loc : availableLocations) {
            if (loc.name().equals(name)) {
                return loc;
            }
        } // for

        // No matching location was found.
        return null;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        connInfo = Loader.load();
        loadAvailableLocations();

        updateGraph();
        createLayout();
    }

    private void updateGraph() {
        graph = new GraphComponent(selectedLocation, connInfo);
    }

    private void createLayout() {
        final VerticalLayout layout = new VerticalLayout();

        // header (Maybe there is a shorter / better text?)
        Label header = new Label("Weather information collector's data");
        header.addStyleName(ValoTheme.LABEL_H1);
        layout.addComponent(header);

        HorizontalLayout hl = new HorizontalLayout();
        // add location list
        hl.addComponent(locationComponent());
        // add graph
        hl.addComponent(graph);
        // add it all to layout
        layout.addComponent(hl);

        setContent(layout);
    }

    /**
     * Provides a component that contains all location names.
     *
     * @return Returns a component with all location names.
     */
    private Component locationComponent() {
        if (null == connInfo) {
            return Utility.errorLabel("Could not find or load configuration file!");
        }

        SourceMySQL src = new SourceMySQL(connInfo);
        List<Location> locations = src.listLocations();
        if (null == locations) {
            return Utility.errorLabel("Could not load list of locations from database!");
        }
        if (locations.isEmpty()) {
            return Utility.errorLabel("There are no locations in the database yet.");
        }
        ListSelect<String> list = new ListSelect<>();
        List<String> names = new ArrayList<>(locations.size());
        locations.stream().forEach((l) -> {
            names.add(l.name());
        });
        list.setItems(names);

        list.addSelectionListener(event -> {
            Notification.show("City changed:", String.valueOf(event.getNewSelection()), Type.TRAY_NOTIFICATION);
            if (!event.getOldSelection().toString().equals(event.getNewSelection().toString())) {
                selectedLocation = this.findLocationByName(String.valueOf(event.getNewSelection()));
                updateGraph();
                createLayout();
            }
        });
        list.setCaption("Location:");
        return list;
    }

    @WebServlet(urlPatterns = "/*", name = "wicUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = wicUI.class, productionMode = false)
    public static class wicUIServlet extends VaadinServlet {

        private static final long serialVersionUID = 3308539348250205926L;
    }
}
