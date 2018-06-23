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

import static org.junit.Assert.*;
import org.junit.Test;

public class TestConnectionInformation {

    /**
     * Tests the preset values of the default constructor.
     */
    @Test
    public void defaultConstructor() {
        ConnectionInformation connInfo = new ConnectionInformation();

        assertTrue(connInfo.hostname().equals("localhost"));
        assertTrue(connInfo.db().equals("database"));
        assertTrue(connInfo.user().equals("root"));
        assertTrue(connInfo.password().equals(""));
        assertTrue(connInfo.port() == ConnectionInformation.defaultMySqlPort);
    }

    /**
     * Tests whether the constructor with parameters sets the values properly.
     */
    @Test
    public void constructorWithParameters() {
        ConnectionInformation connInfo = new ConnectionInformation("db-server", "db_one", "foo", "bar", 3307);

        assertTrue(connInfo.hostname().equals("db-server"));
        assertTrue(connInfo.db().equals("db_one"));
        assertTrue(connInfo.user().equals("foo"));
        assertTrue(connInfo.password().equals("bar"));
        assertTrue(connInfo.port() == 3307);
    }

    /**
     * Tests whether isComplete() returns true, if information is complete.
     */
    @Test
    public void testForIsComplete_positive() {
        ConnectionInformation connInfo = new ConnectionInformation();
        assertTrue(connInfo.isComplete());

        connInfo = new ConnectionInformation("db-server", "db_one", "foo", "bar", 3307);
        assertTrue(connInfo.isComplete());

        // empty password - still complete
        connInfo = new ConnectionInformation("db-server", "db_one", "foo", "", 3307);
        assertTrue(connInfo.isComplete());
    }

    /**
     * Tests whether isComplete() returns false, if information is incomplete.
     */
    @Test
    public void testForIsComplete_negative() {
        ConnectionInformation connInfo;

        connInfo = new ConnectionInformation("", "", "", "", 0);
        assertFalse(connInfo.isComplete());

        connInfo = new ConnectionInformation("", "db_one", "foo", "bar", 3307);
        assertFalse(connInfo.isComplete());

        connInfo = new ConnectionInformation("db-server", "", "foo", "bar", 3307);
        assertFalse(connInfo.isComplete());

        connInfo = new ConnectionInformation("db-server", "db_one", "", "bar", 3307);
        assertFalse(connInfo.isComplete());

        connInfo = new ConnectionInformation("db-server", "db_one", "foo", "", 3307);
        // Empty password may be allowed.
        assertTrue(connInfo.isComplete());

        connInfo = new ConnectionInformation("db-server", "db_one", "foo", "bar", 0);
        assertFalse(connInfo.isComplete());

        connInfo = new ConnectionInformation(null, null, null, null, 0);
        assertFalse(connInfo.isComplete());

        connInfo = new ConnectionInformation(null, "db_one", "foo", "bar", 3307);
        assertFalse(connInfo.isComplete());

        connInfo = new ConnectionInformation("db-server", null, "foo", "bar", 3307);
        assertFalse(connInfo.isComplete());

        connInfo = new ConnectionInformation("db-server", "db_one", null, "bar", 3307);
        assertFalse(connInfo.isComplete());

        connInfo = new ConnectionInformation("db-server", "db_one", "foo", null, 3307);
        // Empty password may be allowed.
        assertTrue(connInfo.isComplete());

        //port out of range
        connInfo = new ConnectionInformation("db-server", "db_one", "foo", "bar", -30);
        assertFalse(connInfo.isComplete());

        connInfo = new ConnectionInformation("db-server", "db_one", "foo", "bar", 230000);
        assertFalse(connInfo.isComplete());
    }

}
