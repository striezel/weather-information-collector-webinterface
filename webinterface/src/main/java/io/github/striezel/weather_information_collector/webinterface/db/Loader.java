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

package io.github.striezel.weather_information_collector.webinterface.db;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Class that loads database connection information.
 * 
 * @author Dirk Stolle
 */
public class Loader {

	/**
	 * Gets a list of default configuration file locations.
	 * 
	 * @return Returns a list of locations where the configuration file might be
	 *         located.
	 */
	public static List<String> defaultConfigurationFileNames() {
		List<String> li = new ArrayList<>();
		li.add("/etc/weather-information-collector/weather-information-collector.conf");
		li.add("/etc/weather-information-collector/wic.conf");
		li.add("/etc/wic/wic.conf");
		String home = System.getProperty("user.home");
		if ((home != null) && !home.isEmpty()) {
			Path p = Paths.get(home);
			li.add(p.resolve(
					".weather-information-collector" + File.separator + "weather-information-collector.conf")
					.toString());
			li.add(p.resolve(".weather-information-collector" + File.separator + "wic.conf").toString());
			li.add(p.resolve(".wic" + File.separator + "wic.conf").toString());
		}
		li.add("." + File.separator + "weather-information-collector.conf");
		li.add("." + File.separator + "wic.conf");
		return li;
	}

	/**
	 * Loads database connection information from the given file.
	 * 
	 * @param fileName
	 *            name of the file
	 * @return Returns ConnectionInformation, if successful. Returns null, if an
	 *         error occurred.
	 */
	public static ConnectionInformation load(String fileName) {
		BufferedInputStream stream;
		try {
			stream = new BufferedInputStream(new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		Properties props = new Properties();
		try {
			props.load(stream);
			stream.close();
			stream = null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		ConnectionInformation ci = new ConnectionInformation("", "", "", "", 0);
		// host
		if (props.containsKey("db.host")) {
			ci.setHostname(props.getProperty("db.host"));
		} else if (props.containsKey("database.host")) {
			ci.setHostname(props.getProperty("database.host"));
		}
		// database name
		if (props.containsKey("db.name")) {
			ci.setDb(props.getProperty("db.name"));
		} else if (props.containsKey("database.name")) {
			ci.setDb(props.getProperty("database.name"));
		}
		// database user
		if (props.containsKey("db.user")) {
			ci.setUser(props.getProperty("db.user"));
		} else if (props.containsKey("database.user")) {
			ci.setUser(props.getProperty("database.user"));
		}
		// database password
		if (props.containsKey("db.password")) {
			ci.setPassword(props.getProperty("db.password"));
		} else if (props.containsKey("database.password")) {
			ci.setPassword(props.getProperty("database.password"));
		}
		// database port
		try {
			if (props.containsKey("db.port")) {
				String p = props.getProperty("db.port");
				ci.setPort(Integer.parseInt(p));
			} else if (props.containsKey("database.port")) {
				String p = props.getProperty("database.port");
				ci.setPort(Integer.parseInt(p));
			}
		} catch (Exception e) {
			e.printStackTrace();
			ci.setPort(ConnectionInformation.defaultMySqlPort);
		}
		if (ci.port() <= 0) {
			ci.setPort(ConnectionInformation.defaultMySqlPort);
		}

		return ci;
	}

	/**
	 * Loads database connection information from the configuration file. This
	 * methods looks for the configuration file in some predefined places.
	 *
	 * @return Returns ConnectionInformation, if successful. Returns null, if an
	 *         error occurred.
	 */
	public static ConnectionInformation load() {
		List<String> places = defaultConfigurationFileNames();

		for (String fileName : places) {
			// If the file exists, then load configuration from it.
			File f = new File(fileName);
			if (f.exists() && f.isFile()) {
				return load(fileName);
			}
		} // for
		
		// No matching file found - return null;
		return null;
	}

}
