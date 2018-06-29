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

import javax.servlet.annotation.WebServlet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import io.github.striezel.weather_information_collector.webinterface.data.Location;
import io.github.striezel.weather_information_collector.webinterface.data.RestApi;
import io.github.striezel.weather_information_collector.webinterface.db.ConnectionInformation;
import io.github.striezel.weather_information_collector.webinterface.db.Loader;

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
public class wicUI extends UI implements LocationChangeListener {

    private static final long serialVersionUID = -1848954295477812552L;

    private ConnectionInformation connInfo = null;

    private Component graph = null;
    private LocationComponent locations = null;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        connInfo = Loader.load();
        locations = new LocationComponent(connInfo);

        updateGraph(null, null);
        createLayout();

        locations.addListener(this);
    }

    private void updateGraph(Location selectedLocation, RestApi api) {
        graph = new GraphComponent(selectedLocation, api, connInfo);
    }

    private void createLayout() {
        final VerticalLayout layout = new VerticalLayout();

        // header (Maybe there is a shorter / better text?)
        Label header = new Label("Weather information collector's data");
        header.addStyleName(ValoTheme.LABEL_H1);
        layout.addComponent(header);

        HorizontalLayout hl = new HorizontalLayout();
        // add location list
        hl.addComponent(locations);
        // add graph
        hl.addComponent(graph);
        // add it all to layout
        layout.addComponent(hl);

        setContent(layout);
    }

    @Override
    public void changed(Location location, RestApi api) {
        updateGraph(location, api);
        createLayout();
    }

    @WebServlet(urlPatterns = "/*", name = "wicUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = wicUI.class, productionMode = false)
    public static class wicUIServlet extends VaadinServlet {

        private static final long serialVersionUID = 3308539348250205926L;
    }
}
