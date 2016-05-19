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

@WebServlet(name = "LoadOneTLE", urlPatterns = {"/LoadOneTLE"})
public class LoadOneTLE extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        String noradID = request.getParameter("noradID");
        String epochYear = request.getParameter("epochYear");
        String epochDay = request.getParameter("epochDay");
        String card1 = request.getParameter("card1");
        String card2 = request.getParameter("card2");
        card1 = card1.replace('_', ' ');
        card1 = card1.replace('^', '.');
        card2 = card2.replace('_', ' ');
        card2 = card2.replace('^', '.');

        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(
                DatabaseConnector.getURL(),
                DatabaseConnector.getProperties()
        );
        Statement stmt = null;
        stmt = connection.createStatement();
        String query = String.format("INSERT INTO tle (\"NORAD_ID\", epoch_year, epoch_day, line1, line2) "
                + "VALUES (%d, %d, %f, \'%s\', \'%s\')",
            Integer.parseInt(noradID),
            Integer.parseInt(epochYear),
            Double.parseDouble(epochDay),
            card1,
            card2
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
        return "Loads one TLE into database";
    }// </editor-fold>

}
