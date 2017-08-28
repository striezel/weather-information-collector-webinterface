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

import static org.junit.Assert.*;

import java.util.Date;
import java.sql.Timestamp;
import org.junit.Test;

public class TestWeather {

	@Test
	public void emptyAfterConstruction() {
		Weather weather = new Weather();
		// no data should be set
		assertFalse(weather.hasDataTime());
		assertFalse(weather.hasTemperatureCelsius());
		assertFalse(weather.hasHumidity());
		assertFalse(weather.hasPressure());
	}

	@Test
	public void setHasGetDataTime() {
		Weather weather = new Weather();
		final Timestamp dt = new Timestamp(System.currentTimeMillis());
		weather.setDataTime(dt);
		assertTrue(weather.hasDataTime());
		assertEquals("Weather's dataTime does not match!", dt, weather.dataTime());
	}

	@Test
	public void setHasGetTemperatureCelsius() {
		Weather weather = new Weather();
		weather.setTemperatureCelsius(25.25f);
		assertTrue(weather.hasTemperatureCelsius());
		assertTrue(weather.temperatureCelsius() == 25.25);
	}

	@Test
	public void setHasGetRelativeHumidity() {
		Weather weather = new Weather();
		weather.setHumidity(65);
		assertTrue(weather.hasHumidity());
		assertTrue(weather.humidity() == 65);
	}

	@Test
	public void outOfRangeHumidity() {
		Weather weather = new Weather();
		weather.setHumidity(-5);
		assertFalse(weather.hasHumidity());
		assertTrue(weather.humidity() == -1);

		weather.setHumidity(101);
		assertFalse(weather.hasHumidity());
		assertTrue(weather.humidity() == -1);
	}

	@Test
	public void setHasGetAirPressure() {
		Weather weather = new Weather();
		weather.setPressure(1013);
		assertTrue(weather.hasPressure());
		assertTrue(weather.pressure() == 1013);
	}

	@Test
	public void outOfRangeAirPressure() {
		Weather weather = new Weather();
		weather.setPressure(-500);
		assertFalse(weather.hasPressure());
		assertTrue(weather.pressure() == -1);

		weather.setPressure(0);
		assertFalse(weather.hasPressure());
		assertTrue(weather.pressure() == -1);
	}

}
