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

/**
 * Holds information to connect to a database server.
 *
 * @author Dirk Stolle
 */
public class ConnectionInformation {

    private String db_host;
    private String db_name;
    private String db_user;
    private String db_pass;
    private int db_port;

    /**
     * default port for MySQL database servers
     */
    final public static int defaultMySqlPort = 3306;

    /**
     * Default constructor.
     */
    public ConnectionInformation() {
        db_host = "localhost";
        db_name = "database";
        db_user = "root";
        db_pass = "";
        db_port = defaultMySqlPort;
    }

    /**
     * Constructs a ConnectionInformation instance with the given initial
     * values.
     *
     * @param host hostname of the database server
     * @param db database name
     * @param user name of the database user
     * @param pass password for the database user
     * @param port port number of the database server
     */
    public ConnectionInformation(final String host, final String db, final String user, final String pass, int port) {
        db_host = host;
        db_name = db;
        db_user = user;
        db_pass = pass;
        setPort(port);
    }

    /**
     * Gets the host name of the database server.
     *
     * @return Returns the host name of the database server.
     */
    public String hostname() {
        return db_host;
    }

    /**
     * Sets the host name for the database connection.
     *
     * @param host the new host name
     */
    public void setHostname(String host) {
        if (host != null && !host.trim().isEmpty()) {
            db_host = host.trim();
        } else {
            db_host = "";
        }
    }

    /**
     * Gets the database name.
     *
     * @return Returns the database name.
     */
    public String db() {
        return db_name;
    }

    /**
     * Sets the database name for the database connection.
     *
     * @param database the new database name
     */
    public void setDb(String database) {
        if (database != null && !database.trim().isEmpty()) {
            db_name = database.trim();
        } else {
            db_name = "";
        }
    }

    /**
     * Gets the name of the database user.
     *
     * @return Returns the name of the database user.
     */
    public String user() {
        return db_user;
    }

    /**
     * Sets the name of the database user.
     *
     * @param username the new user
     */
    public void setUser(String username) {
        if (username != null && !username.trim().isEmpty()) {
            db_user = username.trim();
        } else {
            db_user = "";
        }
    }

    /**
     * Gets the password for the database user.
     *
     * @return Returns the password for the database user.
     */
    public String password() {
        return db_pass;
    }

    /**
     * Sets the password for the database user.
     *
     * @param pw the new password
     */
    public void setPassword(String pw) {
        if (pw != null) {
            db_pass = pw;
        } else {
            db_pass = "";
        }
    }

    /**
     * Gets the port of the database server.
     *
     * @return Returns the port of the database server.
     */
    public int port() {
        return db_port;
    }

    /**
     * Sets the port of the database server.
     *
     * @param p new port number
     */
    public void setPort(int p) {
        if ((p > 0) && (p < 65536)) {
            db_port = p;
        } else {
            p = 0;
        }
    }

    /**
     * Checks whether the connection information is complete.
     *
     * @return Returns true, if the object has all information required to
     * connect to a MySQL database. Returns false otherwise.
     */
    public boolean isComplete() {
        /*
		 * Basically we need a host name, database name, user and port must not be zero.
		 * Password is not strictly required, because some database configurations allow
		 * a connection even without a password. (Little do they know...)
         */
        return (null != db_host) && !db_host.isEmpty()
                && (null != db_name) && !db_name.isEmpty()
                && (null != db_user) && !db_user.isEmpty()
                && (db_port > 0) && (db_port < 65536);
    }

}
