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

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import io.github.striezel.weather_information_collector.webinterface.data.Location;
import io.github.striezel.weather_information_collector.webinterface.data.Weather;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 * Provides basics to connect to a MySQL database.
 * 
 * @author Dirk Stolle
 */
public class SourceMySQL {

	private ConnectionInformation connInfo;
	private DataSource source;

	/**
	 * constructor
	 * 
	 * @param ci
	 *            connection information (server, database name, user, etc.)
	 */
	public SourceMySQL(ConnectionInformation ci) {
		connInfo = ci;
		source = getMySQLDataSource();
	}

	private DataSource getMySQLDataSource() {
		MysqlDataSource ds = null;
		try {
			ds = new MysqlDataSource();
			// general URL syntax: "jdbc:mysql://hostname:port/databasename"
			ds.setURL("jdbc:mysql://" + connInfo.hostname() + ":" + String.valueOf(connInfo.port()) + "/"
					+ connInfo.db());
			ds.setUser(connInfo.user());
			ds.setPassword(connInfo.password());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ds;
	}

	/**
	 * Lists all named locations that are present in the database.
	 * 
	 * @return Returns a list of locations in case of success. Returns null, if an
	 *         error occurred.
	 */
	public List<Location> listLocations() {
		if (null == source)
			return null;
		Connection conn;
		try {
			conn = source.getConnection();
			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(10);
			if (!stmt.execute("SELECT * FROM location WHERE NOT ISNULL(name) ORDER BY name ASC;"))
				return null;
			ResultSet res = stmt.getResultSet();
			List<Location> locations = new ArrayList<>();
			while (res.next()) {
				Location loc = new Location();
				loc.setName(res.getString("name"));
				locations.add(loc);
			} // while
			res.close();
			stmt.close();
			conn.close();
			return locations;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets all available weather data for a location.
	 * 
	 * @param loc
	 *            the location
	 * @return Returns a list of weather data in case of success. Returns null, if
	 *         an error occurred.
	 */
	public List<Weather> fetchData(Location loc) {
		if ((null == loc) || !loc.hasName())
			return null;

		Connection conn;
		try {
			conn = source.getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM location WHERE name = ? LIMIT 1;");
			stmt.setQueryTimeout(10);
			stmt.setString(1, loc.name());
			if (!stmt.execute())
				return null;
			ResultSet res = stmt.getResultSet();
			int locationId = -1;
			if (res.next()) {
				locationId = res.getInt("locationID");
			} else {
				stmt.close();
				conn.close();
				return null;
			}
			res.close();
			stmt.close();
			stmt = conn.prepareStatement(
					"SELECT DISTINCT dataTime, temperature_C, humidity, rain, pressure FROM weatherdata"
							+ " WHERE locationID = ? AND apiID = 1 ORDER BY dataTime ASC");
			stmt.setInt(1, locationId);
			if (!stmt.execute())
				return null;
			res = stmt.getResultSet();
			List<Weather> data = new ArrayList<>();
			while (res.next()) {
				Timestamp dt = res.getTimestamp("dataTime");
				if (res.wasNull())
					dt = null;
				float tempC = res.getFloat("temperature_C");
				if (res.wasNull())
					tempC = Float.NaN;
				int humidity = res.getInt("humidity");
				if (res.wasNull())
					humidity = -1;
				float rain = res.getFloat("rain");
				if (res.wasNull())
					rain = Float.NaN;
				int pressure = res.getInt("pressure");
				if (res.wasNull())
					pressure = 0;

				Weather w = new Weather();
				w.setDataTime(dt);
				w.setTemperatureCelsius(tempC);
				w.setHumidity(humidity);
				w.setRain(rain);
				w.setPressure(pressure);
				data.add(w);
			} // while
			res.close();
			stmt.close();
			conn.close();

			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
