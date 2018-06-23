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

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestLoader {

    /**
     * Setup method for test class: creates an example for a configuration file.
     */
    @BeforeClass
    public static void setUp() {
        List<String> lines = Arrays.asList("# Comment was here!", "", "db.host=some-server.example.com",
                "db.name=weather_information_collector", "db.user=wic", "db.password=secret", "db.port=1234");
        Path file = Paths.get("test-file-configuration.conf");
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            fail("setUp() failed!");
        }
    }

    /**
     * Tear down method for test class: deletes the example configuration file.
     */
    @AfterClass
    public static void tearDown() {
        File f = new File("test-file-configuration.conf");
        if (f.exists()) {
            f.delete();
        }
    }

    /**
     * Tests whether some default configuration file names are defined.
     */
    @Test
    public void defaultConfigurationFileNames() {
        List<String> fileNames = Loader.defaultConfigurationFileNames();

        assertFalse(fileNames.isEmpty());
        for (String fn : fileNames) {
            assertTrue(fn.endsWith(".conf"));
        }
    }

    /**
     * Tests whether the loading mechanism works as expected.
     */
    @Test
    public void loadWithFileName() {
        ConnectionInformation ci = Loader.load("test-file-configuration.conf");

        //Function result must not be null.
        assertNotNull(ci);

        //Check individual values.
        assertEquals("some-server.example.com", ci.hostname());
        assertEquals("weather_information_collector", ci.db());
        assertEquals("wic", ci.user());
        assertEquals("secret", ci.password());
        assertEquals(1234, ci.port());
    }

}
