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

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "LoadOneSSR", urlPatterns = {"/LoadOneSSR"})
public class LoadOneSSR extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        String noradID = request.getParameter("noradID");
        String name = request.getParameter("name");
        String objType = request.getParameter("objType");
        String orbType = request.getParameter("orbType");
        String country = request.getParameter("country");
        name = name.replace('_', ' ');

        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(
                DatabaseConnector.getURL(),
                DatabaseConnector.getProperties()
        );
        Statement stmt = null;
        stmt = connection.createStatement();
        String query = String.format("INSERT INTO ssr (\"NORAD_ID\", name, obj_type, orb_type, country) "
                + "VALUES (%d, \'%s\', \'%s\', \'%s\', \'%s\')",
                Integer.parseInt(noradID),
                name,
                objType,
                orbType,
                country
        );
        stmt.executeUpdate(query);
        stmt.close();
        connection.close();
        
        String output = "Success!";
        response.setContentLength(output.length());
        response.getOutputStream().write(output.getBytes());
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Nothing here...
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoadOneTLE.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LoadOneTLE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Loads one SSR into database";
    }// </editor-fold>

}
