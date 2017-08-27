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

package io.github.striezel.weather_information_collector.webinterface.ui;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
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

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		final VerticalLayout layout = new VerticalLayout();

		// header (Maybe there is a shorter / better text?)
		Label header = new Label("Weather information collector's data");
		header.addStyleName(ValoTheme.LABEL_H1);
		layout.addComponent(header);

		HorizontalLayout hl = new HorizontalLayout();
		// add location list
		hl.addComponent(locationComponent());
		// add graph
		hl.addComponent(graphComponent());
		// add it all to layout
		layout.addComponent(hl);

		setContent(layout);
	}

	/**
	 * Gets a label with error message.
	 * 
	 * @param value
	 *            the error message
	 * @return Returns a label that indicates an error with the given error message.
	 */
	private Label errorLabel(String value) {
		Label errorLabel = new Label(value);
		errorLabel.setStyleName(ValoTheme.LABEL_FAILURE);
		return errorLabel;
	}

	/**
	 * Provides a component that contains the graphical visualization of the data.
	 * 
	 * @return Returns a component for graphical visualization.
	 */
	private Component graphComponent() {
		VerticalLayout layout = new VerticalLayout();

		Label l1 = new Label("Imagine this to be a graph.");
		l1.setIcon(VaadinIcons.CHART_LINE);
		Label l2 = new Label("(The graph is not implemented yet.)");
		l2.setIcon(VaadinIcons.COGS);

		layout.addComponents(l1, l2);
		return layout;
	}

	/**
	 * Provides a component that contains all location names.
	 * 
	 * @return Returns a component with all location names.
	 */
	private Component locationComponent() {
		ConnectionInformation ci = Loader.load();
		if (null == ci) {
			return errorLabel("Could not find or load configuration file!");
		}

		SourceMySQL src = new SourceMySQL(ci);
		List<Location> locations = src.listLocations();
		if (null == locations) {
			return errorLabel("Could not load list of locations from database!");
		}
		if (locations.isEmpty()) {
			return errorLabel("There are no locations in the database yet.");
		}
		ListSelect<String> list = new ListSelect<>();
		List<String> names = new ArrayList<>(locations.size());
		for (Location l : locations) {
			names.add(l.name());
		}
		list.setItems(names);
		return list;
	}

	@WebServlet(urlPatterns = "/*", name = "wicUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = wicUI.class, productionMode = false)
	public static class wicUIServlet extends VaadinServlet {

		private static final long serialVersionUID = 3308539348250205926L;
	}
}
