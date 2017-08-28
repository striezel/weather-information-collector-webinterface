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

import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Provides some utility methods for the UI.
 * @author Dirk Stolle
 */
public class Utility {

	/**
	 * Gets a label with error message.
	 * 
	 * @param value
	 *            the error message
	 * @return Returns a label that indicates an error with the given error message.
	 */
	public static Label errorLabel(String value) {
		Label errorLabel;
		if ((null!= value) && !value.isEmpty())
		{
			errorLabel = new Label(value);	
		}
		else
		{
			errorLabel = new Label("An unspecified error occurred!");
		}
		errorLabel.setStyleName(ValoTheme.LABEL_FAILURE);
		return errorLabel;
	}
	
}
