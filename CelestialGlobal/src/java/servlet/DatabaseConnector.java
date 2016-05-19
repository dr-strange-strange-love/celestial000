/*
 * Copyright (C) 2016 Amadeus
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package servlet;

import java.sql.Connection;
import java.util.Properties;

/**
 *
 * @author Amadeus
 */
public class DatabaseConnector {
    protected static Connection   connection;
    protected static String       url;
    protected static Properties   props;
    
    public DatabaseConnector() {
        connection = null;
        url = "jdbc:postgresql://localhost:5432/celestial000";
        props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "spaceLife485");
    }
    
    public static Connection getConnection() {
        return connection;
    }
    public static String getURL() {
        return url;
    }
    public static Properties getProperties() {
        return props;
    }
}
