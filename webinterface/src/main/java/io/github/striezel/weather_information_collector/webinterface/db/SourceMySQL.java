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
package io.github.striezel.weather_information_collector.webinterface.db;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import io.github.striezel.weather_information_collector.webinterface.data.Location;
import io.github.striezel.weather_information_collector.webinterface.data.RestApi;
import io.github.striezel.weather_information_collector.webinterface.data.Weather;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 * Provides basics to connect to a MySQL database.
 *
 * @author Dirk Stolle
 */
public class SourceMySQL {

    private static final Logger LOG = Logger.getLogger(SourceMySQL.class.getName());

    private final ConnectionInformation connInfo;
    private final DataSource source;
    private final Map<RestApi, Integer> knownApis;

    /**
     * Constructor.
     *
     * @param ci connection information (server, database name, user, etc.)
     */
    public SourceMySQL(ConnectionInformation ci) {
        connInfo = ci;
        source = getMySQLDataSource();
        knownApis = listApis();
    }

    private DataSource getMySQLDataSource() {
        try {
            MysqlDataSource ds = new MysqlDataSource();
            // general URL syntax: "jdbc:mysql://hostname:port/databasename"
            ds.setURL("jdbc:mysql://" + connInfo.hostname() + ":" + String.valueOf(connInfo.port()) + "/"
                    + connInfo.db());
            ds.setUser(connInfo.user());
            ds.setPassword(connInfo.password());
            return ds;
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Could not create data source for MySQL database.", e);
            return null;
        }
    }

    /**
     * Lists all named locations that are present in the database.
     *
     * @return Returns a list of locations in case of success. Returns null, if
     * an error occurred.
     */
    public List<Location> listLocations() {
        if (null == source) {
            return null;
        }
        Connection conn;
        try {
            conn = source.getConnection();
            Statement stmt = conn.createStatement();
            stmt.setQueryTimeout(10);
            if (!stmt.execute("SELECT * FROM location WHERE NOT ISNULL(name) ORDER BY name ASC;")) {
                return null;
            }
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
            LOG.log(Level.WARNING, "Could not get list of locations from the database!", e);
            return null;
        }
    }

    /**
     * Lists all named locations together with the APIs from which data for that
     * locations are present in the database.
     *
     * @return Returns a list of locations in case of success. Returns null, if
     * an error occurred.
     */
    public List<AbstractMap.SimpleImmutableEntry<Location, RestApi>> listLocationsWithApi() {
        if (null == source || null == knownApis) {
            return null;
        }
        Connection conn;
        try {
            conn = source.getConnection();
            Statement stmt = conn.createStatement();
            stmt.setQueryTimeout(10);
            if (!stmt.execute("SELECT DISTINCT location.name AS locName, latitude, longitude, api.name AS apiName, api.apiID AS theApiId"
                    + " FROM weatherdata"
                    + " LEFT JOIN api ON weatherdata.apiID = api.apiID"
                    + " LEFT JOIN location ON location.locationID = weatherdata.locationID"
                    + " WHERE NOT ISNULL(location.name)"
                    + "   AND NOT ISNULL(api.name)"
                    + " ORDER BY locName ASC, apiName ASC;")) {
                return null;
            }
            ResultSet res = stmt.getResultSet();
            List<AbstractMap.SimpleImmutableEntry<Location, RestApi>> locations;
            locations = new ArrayList<>();
            while (res.next()) {
                Location loc = new Location();
                loc.setName(res.getString("locName"));
                Float lat = res.getFloat("latitude");
                if (!res.wasNull()) {
                    Float lon = res.getFloat("longitude");
                    if (!res.wasNull()) {
                        loc.setCoordinates(lat, lon);
                    }
                }

                final int apiId = res.getInt("theApiId");
                final Optional<RestApi> api = knownApis.entrySet().stream()
                        .filter(e -> (e.getValue() == apiId))
                        .map(e -> e.getKey())
                        .findFirst();
                if (api.isPresent()) {
                    locations.add(
                            new AbstractMap.SimpleImmutableEntry<>(loc, api.get())
                    );
                }
            } // while
            res.close();
            stmt.close();
            conn.close();
            return locations;
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Could not get list of locations from the database!", e);
            return null;
        }
    }

    /**
     * Lists all APIs that are present in the database.
     *
     * @return Returns a map where key is the API itself and the corresponding
     * value is the id of the API within the database. Returns null, if an error
     * occurred.
     */
    private Map<RestApi, Integer> listApis() {
        if (null == source) {
            return null;
        }
        Connection conn;
        try {
            conn = source.getConnection();
            Statement stmt = conn.createStatement();
            stmt.setQueryTimeout(10);
            if (!stmt.execute("SELECT * FROM api WHERE NOT ISNULL(name) ORDER BY name ASC;")) {
                return null;
            }
            ResultSet res = stmt.getResultSet();
            Map<RestApi, Integer> apis;
            apis = new HashMap<>();
            while (res.next()) {
                final String name = res.getString("name");
                switch (name.toLowerCase()) {
                    case "apixu":
                        apis.put(RestApi.Apixu, res.getInt("apiID"));
                        break;
                    case "darksky":
                        apis.put(RestApi.DarkSky, res.getInt("apiID"));
                        break;
                    case "openweathermap":
                        apis.put(RestApi.OpenWeatherMap, res.getInt("apiID"));
                        break;
                    default:
                        LOG.log(Level.WARNING, "Could not find suitable API enumeration value for {0} - going to ignore it!", name);
                        break;
                }
            } // while
            res.close();
            stmt.close();
            conn.close();
            return apis;
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Could not execute SQL query to get APIs!", e);
            return null;
        }
    }

    /**
     * Gets the internal database id of a given location.
     *
     * @param conn an open connection to the database
     * @param loc the location to look for
     * @return Returns the id of the location, if it was found. Returns -1, if
     * the location was not found or an error occurred.
     * @throws SQLException if the SQL query failed or the connection is closed
     */
    private int getLocationId(Connection conn, Location loc) throws SQLException {
        if ((conn == null) || (null == loc) || !loc.hasName()) {
            return -1;
        }
        if (conn.isClosed()) {
            return -1;
        }
        int locationId;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM location WHERE name = ? LIMIT 1;")) {
            stmt.setQueryTimeout(10);
            stmt.setString(1, loc.name());
            if (!stmt.execute()) {
                return -1;
            }
            ResultSet res = stmt.getResultSet();
            locationId = -1;
            if (res.next()) {
                locationId = res.getInt("locationID");
            }
            res.close();
            stmt.close();
        }
        return locationId;
    }

    /**
     * Gets all available weather data collected for a location under a given
     * API.
     *
     * @param loc the location
     * @param api the API
     * @return Returns a list of weather data in case of success. Returns null,
     * if an error occurred.
     */
    public List<Weather> fetchData(Location loc, RestApi api) {
        if ((null == loc) || !loc.hasName() || api == null) {
            return null;
        }
        if (knownApis == null || !knownApis.containsKey(api)) {
            return null;
        }
        final Integer apiId = knownApis.get(api);

        Connection conn;
        try {
            conn = source.getConnection();
            int locationId = this.getLocationId(conn, loc);
            if (locationId < 0) {
                conn.close();
                return null;
            }
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT DISTINCT dataTime, temperature_C, humidity, rain, pressure FROM weatherdata"
                    + " WHERE locationID = ? AND apiID = ? ORDER BY dataTime ASC");
            stmt.setInt(1, locationId);
            stmt.setInt(2, apiId);
            if (!stmt.execute()) {
                conn.close();
                return null;
            }
            ResultSet res = stmt.getResultSet();
            List<Weather> data = new ArrayList<>();
            while (res.next()) {
                Timestamp dt = res.getTimestamp("dataTime");
                if (res.wasNull()) {
                    dt = null;
                }
                float tempC = res.getFloat("temperature_C");
                if (res.wasNull()) {
                    tempC = Float.NaN;
                }
                int humidity = res.getInt("humidity");
                if (res.wasNull()) {
                    humidity = -1;
                }
                float rain = res.getFloat("rain");
                if (res.wasNull()) {
                    rain = Float.NaN;
                }
                int pressure = res.getInt("pressure");
                if (res.wasNull()) {
                    pressure = 0;
                }

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
            LOG.log(Level.WARNING, "Could not fetch weather data for the location " + loc.name() + "!", e);
            return null;
        }
    }

    /**
     * Gets the available weather data collected for a location under a given
     * API for the last n hours.
     *
     * @param loc the location
     * @param api the API
     * @param hours number of hours (must be greater than zero)
     * @return Returns a list of weather data in case of success. Returns null,
     * if an error occurred.
     */
    public List<Weather> fetchDataNHours(Location loc, RestApi api, int hours) {
        if ((null == loc) || !loc.hasName() || api == null || hours < 1) {
            return null;
        }
        if (knownApis == null || !knownApis.containsKey(api)) {
            return null;
        }
        final Integer apiId = knownApis.get(api);

        Connection conn;
        try {
            conn = source.getConnection();
            int locationId = getLocationId(conn, loc);
            if (locationId < 0) {
                conn.close();
                return null;
            }
            // Find the latest date in the weather data.
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT MAX(dataTime) AS mdt FROM weatherdata"
                    + " WHERE locationID = ? AND apiID = ?;");
            stmt.setInt(1, locationId);
            stmt.setInt(2, apiId);
            if (!stmt.execute()) {
                conn.close();
                return null;
            }
            ResultSet res = stmt.getResultSet();
            final Timestamp maxDataTime;
            if (res.next()) {
                maxDataTime = res.getTimestamp("mdt");
                if (res.wasNull()) {
                    res.close();
                    stmt.close();
                    return null;
                }
            } else {
                maxDataTime = Timestamp.from(Instant.now());
            }
            res.close();
            stmt.close();
            // Get the weather data.
            stmt = conn.prepareStatement(
                    "SELECT DISTINCT dataTime, temperature_C, humidity, rain, pressure FROM weatherdata"
                    + " WHERE locationID = ? AND apiID = ?"
                    + "  AND dataTime >= DATE_SUB(?, INTERVAL ? HOUR)"
                    + " ORDER BY dataTime ASC");
            stmt.setInt(1, locationId);
            stmt.setInt(2, apiId);
            stmt.setTimestamp(3, maxDataTime);
            stmt.setInt(4, hours);
            if (!stmt.execute()) {
                conn.close();
                return null;
            }
            res = stmt.getResultSet();
            List<Weather> data = new ArrayList<>();
            while (res.next()) {
                Timestamp dt = res.getTimestamp("dataTime");
                if (res.wasNull()) {
                    dt = null;
                }
                float tempC = res.getFloat("temperature_C");
                if (res.wasNull()) {
                    tempC = Float.NaN;
                }
                int humidity = res.getInt("humidity");
                if (res.wasNull()) {
                    humidity = -1;
                }
                float rain = res.getFloat("rain");
                if (res.wasNull()) {
                    rain = Float.NaN;
                }
                int pressure = res.getInt("pressure");
                if (res.wasNull()) {
                    pressure = 0;
                }

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
            LOG.log(Level.WARNING, "Could not fetch weather data for the location " + loc.name() + "!", e);
            return null;
        }
    }

}
