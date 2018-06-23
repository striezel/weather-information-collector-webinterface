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

import static org.junit.Assert.*;
import org.junit.Test;

public class TestLocation {

    /**
     * Tests whether a new Location instance is empty after creation.
     */
    @Test
    public void emptyAfterConstruction() {
        Location loc = new Location();
        assertFalse(loc.hasId());
        assertFalse(loc.hasName());
        assertFalse(loc.hasPostcode());
        assertFalse(loc.hasCoordinates());
        assertTrue(loc.empty());
    }

    /**
     * Tests empty() method's behaviour regarding changes of location id.
     */
    @Test
    public void emptyId() {
        Location loc = new Location();
        assertFalse(loc.hasId());
        loc.setId(5);
        assertFalse(loc.empty());
    }

    /**
     * Tests empty() method's behaviour regarding changes of location
     * coordinates.
     */
    @Test
    public void emptyCoordinates() {
        Location loc = new Location();
        assertFalse(loc.hasCoordinates());
        loc.setCoordinates(5.0f, 120.0f);
        assertFalse(loc.empty());
    }

    /**
     * Tests empty() method's behaviour regarding changes of location name.
     */
    @Test
    public void emptyName() {
        Location loc = new Location();
        assertFalse(loc.hasName());
        loc.setName("Townington");
        assertFalse(loc.empty());
    }

    /**
     * Tests empty() method's behaviour regarding changes of location id.
     */
    @Test
    public void emptyPostcode() {
        Location loc = new Location();
        assertFalse(loc.hasPostcode());
        loc.setPostcode("01234");
        assertFalse(loc.empty());
    }

    /**
     * Tests set/get/has methods for id.
     */
    @Test
    public void setHasGetId() {
        Location loc = new Location();
        loc.setId(12345);
        assertTrue(loc.hasId());
        assertTrue(loc.id() == 12345);
    }

    /**
     * Tests set/get/has methods for latitude and longitude.
     */
    @Test
    public void setHasGetLatitudeAndLongitude() {
        Location loc = new Location();
        loc.setCoordinates(12.5f, 123.4375f);
        assertTrue(loc.hasCoordinates());
        assertTrue(loc.latitude() == 12.5f);
        assertTrue(loc.longitude() == 123.4375f);
    }

    /**
     * Tests whether latitude values outside of the accepted range are handled
     * correctly.
     */
    @Test
    public void outOfRangeLatitude() {
        Location loc = new Location();
        loc.setCoordinates(123.0f, 123.4f);
        assertFalse(loc.hasCoordinates());

        loc.setCoordinates(-91.2f, 123.4f);
        assertFalse(loc.hasCoordinates());
    }

    /**
     * Tests whether longitude values outside of the accepted range are handled
     * correctly.
     */
    @Test
    public void outOfRangeLongitude() {
        Location loc = new Location();
        loc.setCoordinates(12.5f, 191.2f);
        assertFalse(loc.hasCoordinates());

        loc.setCoordinates(-89.2f, -323.2f);
        assertFalse(loc.hasCoordinates());
    }

    /**
     * Tests set/get/has methods for name.
     */
    @Test
    public void setHasGetName() {
        Location loc = new Location();
        loc.setName("Townsvillage");
        assertTrue(loc.hasName());
        assertTrue("Townsvillage".equals(loc.name()));
    }

    /**
     * Tests set/get/has methods for postcode.
     */
    @Test
    public void setHasGetPostcode() {
        Location loc = new Location();
        loc.setPostcode("SW1");
        assertTrue(loc.hasPostcode());
        assertTrue("SW1".equals(loc.postcode()));
    }

}
