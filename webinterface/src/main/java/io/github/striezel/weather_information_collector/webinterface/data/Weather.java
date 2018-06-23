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
package io.github.striezel.weather_information_collector.webinterface.data;

import java.sql.Timestamp;

/**
 * Holds information about one weather data point.
 *
 * @author Dirk Stolle
 */
public class Weather {

    private Timestamp dataTime;
    private float tempC;
    private int humidity;
    private float rain;
    private int pressure;

    /**
     * Default constructor.
     */
    public Weather() {
        dataTime = new Timestamp(0);
        tempC = Float.NaN;
        humidity = -1;
        rain = Float.NaN;
        pressure = -1;
    }

    /**
     * Checks whether this instance has time when the data was received /
     * measured.
     *
     * @return Returns true, if the instance has the time. Returns false
     * otherwise.
     */
    public boolean hasDataTime() {
        return (dataTime.getTime() != 0);
    }

    /**
     * Gets the time when the data was received / measured.
     *
     * @return Returns the time when the data was received / measured. Returns
     * the epoch, if no time is set.
     */
    public Timestamp dataTime() {
        return dataTime;
    }

    /**
     * Sets the time when the data was received / measured.
     *
     * @param dt the new time when the data was received / measured
     */
    public void setDataTime(Timestamp dt) {
        if (null != dt) {
            dataTime = dt;
        } else {
            dataTime = new Timestamp(0);
        }
    }

    /**
     * Checks whether this instance has a temperature in °C.
     *
     * @return
     */
    public boolean hasTemperatureCelsius() {
        return !Float.isNaN(tempC);
    }

    /**
     * Gets the temperature in °C, if it was set.
     *
     * @return Returns the temperature in °C. Returns NaN, if no temperature is
     * set for that unit.
     */
    public float temperatureCelsius() {
        return tempC;
    }

    /**
     * Sets the temperature in °C.
     *
     * @param newTempC the new temperature in °C
     */
    public void setTemperatureCelsius(final float newTempC) {
        tempC = newTempC;
    }

    /**
     * Checks whether this instance has a relative humidity.
     *
     * @return Returns true, if the instance has humidity information. Returns
     * false otherwise.
     */
    public boolean hasHumidity() {
        return (0 <= humidity) && (humidity <= 100);
    }

    /**
     * Gets the relative humidity in percent, if it was set.
     *
     * @return Returns the relative humidity in percent. Returns -1, if no
     * humidity is set.
     */
    public int humidity() {
        return humidity;
    }

    /**
     * Sets the relative humidity in percent.
     *
     * @param newHumidity the new humidity in percent
     */
    public void setHumidity(final int newHumidity) {
        if ((newHumidity < 0) || (newHumidity > 100)) {
            humidity = -1;
        } else {
            humidity = newHumidity;
        }
    }

    /**
     * Checks whether this instance has information about rain.
     *
     * @return Returns true, if the instance has rain information. Returns false
     * otherwise.
     */
    public boolean hasRain() {
        return !Float.isNaN(rain);
    }

    /**
     * Gets the amount of rain in millimeters per square meter, if it was set.
     *
     * @return Returns the amount of rain in millimeters. Returns NaN, if no
     * rain information is set.
     */
    public float rain() {
        return rain;
    }

    /**
     * Sets the amount of rain in millimeters per square meter.
     *
     * @param newRainMm new amount of rain in millimeters per square meter
     */
    public void setRain(float newRainMm) {
        if ((newRainMm >= 0.0f) && !Float.isInfinite(newRainMm)) {
            rain = newRainMm;
        } else {
            rain = Float.NaN;
        }
    }

    /**
     * Checks whether this instance has air pressure information.
     *
     * @return Returns true, if the instance has air pressure. Returns false
     * otherwise.
     */
    public boolean hasPressure() {
        return (pressure > 0);
    }

    /**
     * Gets the air pressure in hPa, if it was set.
     *
     * @return Returns the pressure in hPa. Returns -1, if no pressure is set.
     */
    public int pressure() {
        return pressure;
    }

    /**
     * Sets the pressure in hPa (1 hPa = 1 mbar).
     *
     * @param newPressure_hPa the new pressure in hPa
     */
    public void setPressure(final int newPressure_hPa) {
        if (newPressure_hPa > 0) {
            pressure = newPressure_hPa;
        } else {
            pressure = -1;
        }
    }

}
