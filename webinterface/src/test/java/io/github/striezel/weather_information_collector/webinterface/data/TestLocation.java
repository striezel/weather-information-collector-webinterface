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

import org.junit.Test;

import io.github.striezel.weather_information_collector.webinterface.data.Location;

public class TestLocation {

	@Test
	public void emptyAfterConstruction() {
		Location loc = new Location();
		assertFalse(loc.hasId());
		assertFalse(loc.hasName());
		assertFalse(loc.hasPostcode());
		assertFalse(loc.hasCoordinates());
		assertTrue(loc.empty());
	}

	@Test
	public void emptyId() {
		Location loc = new Location();
		assertFalse(loc.hasId());
		loc.setId(5);
		assertFalse(loc.empty());
	}

	@Test
	public void emptyCoordinates() {
		Location loc = new Location();
		assertFalse(loc.hasCoordinates());
		loc.setCoordinates(5.0f, 120.0f);
		assertFalse(loc.empty());
	}

	@Test
	public void emptyName() {
		Location loc = new Location();
		assertFalse(loc.hasName());
		loc.setName("Townington");
		assertFalse(loc.empty());
	}

	@Test
	public void emptyPostcode() {
		Location loc = new Location();
		assertFalse(loc.hasPostcode());
		loc.setPostcode("01234");
		assertFalse(loc.empty());
	}

	@Test
	public void setHasGetId() {
		Location loc = new Location();
		loc.setId(12345);
		assertTrue(loc.hasId());
		assertTrue(loc.id() == 12345);
	}

	@Test
	public void setHasGetLatitudeAndLongitude() {
		Location loc = new Location();
		loc.setCoordinates(12.5f, 123.4375f);
		assertTrue(loc.hasCoordinates());
		assertTrue(loc.latitude() == 12.5f);
		assertTrue(loc.longitude() == 123.4375f);
	}

	@Test
	public void outOfRangeLatitude() {
		Location loc = new Location();
		loc.setCoordinates(123.0f, 123.4f);
		assertFalse(loc.hasCoordinates());

		loc.setCoordinates(-91.2f, 123.4f);
		assertFalse(loc.hasCoordinates());
	}

	@Test
	public void outOfRangeLongitude() {
		Location loc = new Location();
		loc.setCoordinates(12.5f, 191.2f);
		assertFalse(loc.hasCoordinates());

		loc.setCoordinates(-89.2f, -323.2f);
		assertFalse(loc.hasCoordinates());
	}

	@Test
	public void setHasGetName() {
		Location loc = new Location();
		loc.setName("Townsvillage");
		assertTrue(loc.hasName());
		assertTrue(loc.name() == "Townsvillage");
	}

	@Test
	public void setHasGetPostcode() {
		Location loc = new Location();
		loc.setPostcode("SW1");
		assertTrue(loc.hasPostcode());
		assertTrue(loc.postcode() == "SW1");
	}

}
