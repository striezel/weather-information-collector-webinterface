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
package io.github.striezel.weather_information_collector.webinterface.data;

/**
 * Holds information about a location / city.
 *
 * @author Dirk Stolle
 */
public class Location {

    private int m_id;
    private String m_name;
    private float m_latitude;
    private float m_longitude;
    private String m_postcode;

    /**
     * Default constructor.
     */
    public Location() {
        m_id = 0;
        m_name = "";
        m_latitude = Float.NaN;
        m_longitude = Float.NaN;
        m_postcode = "";
    }

    /**
     * Gets the API id of the location (if any).
     *
     * @return Returns the API id of the location. Returns zero, if no id is
     * set.
     */
    public int id() {
        return m_id;
    }

    /**
     * Sets the API id of the location.
     *
     * @param newId the new API id of the location
     */
    public void setId(int newId) {
        if (newId > 0) {
            m_id = newId;
        } else {
            m_id = 0;
        }
    }

    /**
     * Checks whether the location has an API id.
     *
     * @return Returns true, if the location has an API id. Returns false
     * otherwise.
     */
    public boolean hasId() {
        return (m_id > 0);
    }

    /**
     * Gets the latitude of the location.
     *
     * @return Returns the latitude of the location (in degrees). Returns NaN,
     * if no latitude is set.
     */
    public float latitude() {
        return m_latitude;
    }

    /**
     * Gets the longitude of the location.
     *
     * @return Returns the longitude of the location (in degrees). Returns NaN,
     * if no longitude is set.
     */
    public float longitude() {
        return m_longitude;
    }

    /**
     * Sets the latitude and longitude of the location.
     *
     * @param lat the new latitude [-90;90] of the location
     * @param lon the new longitude [-180;180] of the location
     */
    public void setCoordinates(float lat, float lon) {
        m_latitude = lat;
        m_longitude = lon;
    }

    /**
     * Checks whether the location has valid latitude and longitude values.
     *
     * @return Returns true, if the location has valid latitude and longitude
     * values. Returns false otherwise.
     */
    public boolean hasCoordinates() {
        return (m_latitude >= -90.0f) && (m_latitude <= 90.0f) && (m_longitude >= -180.0f) && (m_longitude <= 180.0f);
    }

    /**
     * Gets the name of the location (if any).
     *
     * @return Returns the name of the location. Returns an empty string, if no
     * name is set.
     */
    public String name() {
        return m_name;
    }

    /**
     * Sets the name of the location.
     *
     * @param newName the new name of the location
     */
    public void setName(String newName) {
        m_name = newName;
    }

    /**
     * Checks whether the location has a name.
     *
     * @return Returns true, if the location has a name. Returns false
     * otherwise.
     */
    public boolean hasName() {
        return (m_name != null) && !m_name.isEmpty();
    }

    /**
     * Gets the post code of the location (if any).
     *
     * @return Returns the post code of the location. Returns an empty string,
     * if no post code is set.
     */
    public String postcode() {
        return m_postcode;
    }

    /**
     * Sets the post code of the location.
     *
     * @param newPostcode the new post code of the location
     */
    public void setPostcode(String newPostcode) {
        m_postcode = newPostcode;
    }

    /**
     * Checks whether the location has a postcode.
     *
     * @return Returns true, if the location has a postcode. Returns false
     * otherwise.
     */
    public boolean hasPostcode() {
        return m_postcode != null && !m_postcode.isEmpty();
    }

    /**
     * Checks whether this instance has no data at all.
     *
     * @return Returns true, if this instance has no usable data yet. Returns
     * false otherwise.
     */
    public boolean empty() {
        return (!hasId() && !hasName() && !hasCoordinates() && !hasPostcode());
    }

    @Override
    public String toString() {
        if (empty()) {
            return "<Location is empty>";
        }
        if (hasName()) {
            if (hasCoordinates()) {
                return name() + " (" + String.valueOf(latitude()) + "°, "
                        + String.valueOf(longitude()) + "°)";
            }
            // name only
            return name();
        }
        if (hasCoordinates()) {
            return String.valueOf(latitude()) + "°, "
                    + String.valueOf(longitude()) + "°";
        }
        if (hasId()) {
            return "ID " + String.valueOf(id());
        }
        // Postcode? Nah!
        return "unknown location";
    }

}
