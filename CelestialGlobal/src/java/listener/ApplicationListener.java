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
package listener;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import servlet.DatabaseConnector;
import servlet.LoadOneTLE;

/**
 *
 * @author Amadeus
 */
public class ApplicationListener implements ServletContextListener {

    private ServletContext sc = null;

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        this.sc = arg0.getServletContext();
        DatabaseConnector dbc = new DatabaseConnector();
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // Shutdown code
    }

}
