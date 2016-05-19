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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "GetAll", urlPatterns = {"/GetAll"})
public class GetAll extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        String filterNorad = request.getParameter("filterNorad");
        String filterName = request.getParameter("filterName");
        filterName = filterName.replace('_', ' ');

        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(
                DatabaseConnector.getURL(),
                DatabaseConnector.getProperties()
        );
        Statement stmt = null;
        stmt = connection.createStatement();
        String query = "select \"NORAD_ID\", name, obj_type, orb_type, country from ssr " +
                    "where cast(\"NORAD_ID\" as text) like '%" + filterNorad + "%' " +
                    "and name ilike '%" + filterName + "%'";
        String output = "";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            Integer norad_id = rs.getInt("NORAD_ID");
            String name = rs.getString("name");
            String obj_type = rs.getString("obj_type");
            String orb_type = rs.getString("orb_type");
            String country = rs.getString("country");
            name = name.trim();
            obj_type = obj_type.trim();
            orb_type = orb_type.trim();
            country = country.trim();
            output = output + norad_id + "\r";
            output = output + name + "\r";
            output = output + obj_type + "\r";
            output = output + orb_type + "\r";
            output = output + country + "\r";
        }
        rs.close();
        stmt.close();
        connection.close();

        response.setContentLength(output.length());
        response.getOutputStream().write(output.getBytes());
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Nothing here..
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
        return "Fetches one object from database";
    }// </editor-fold>

}
