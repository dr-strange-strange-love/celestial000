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

import celestial_object.EarthOrbitalObject;
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
import sgp4v.SatElset;
import sgp4v.SatElsetException;

@WebServlet(name = "GetMostSuitableTLE", urlPatterns = {"/GetMostSuitableTLE"})
public class GetMostSuitableTLE extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException, SatElsetException {        
        String noradIDStr = request.getParameter("noradID");
        String fixedEpochStr = request.getParameter("fixedEpoch");
        fixedEpochStr = fixedEpochStr.replace('_', '.');
        int noradIDInt = Integer.parseInt(noradIDStr);
        double fixedEpochDouble = Double.parseDouble(fixedEpochStr);

        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(
                DatabaseConnector.getURL(),
                DatabaseConnector.getProperties()
        );
        Statement stmt = null;
        stmt = connection.createStatement();
        String query = String.format("select * from tle where \"NORAD_ID\" = %d "
                + "order by epoch_year, epoch_day asc",
                noradIDInt
        );
        EarthOrbitalObject eoo = null;
        double diff = 1000; // years
        String output = "";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            Integer norad_id = rs.getInt("NORAD_ID");
            Integer epoch_year = rs.getInt("epoch_year");
            Double epoch_day = rs.getDouble("epoch_day");
            String card1 = rs.getString("line1");
            String card2 = rs.getString("line2");
            double leap = 0;
            if ((epoch_year % 4) == 0) leap = 1;
            double fixedDate = epoch_year + (1/(365+leap))*epoch_day;
            if (Math.abs(fixedDate - fixedEpochDouble) < diff) {
                eoo = new EarthOrbitalObject(norad_id, epoch_year, epoch_day, new SatElset(card1, card2));
                diff = Math.abs(fixedDate - fixedEpochDouble);
            }
        }
        rs.close();
        stmt.close();
        connection.close();
        
        if (eoo != null) {
            output = output + eoo.getNoradID() + "\r";
            output = output + eoo.getEpochYear() + "\r";
            output = output + eoo.getEpochDay() + "\r";
            output = output + eoo.getTLE().getCard1() + "\r";
            output = output + eoo.getTLE().getCard2() + "\r";
            response.setContentLength(output.length());
            response.getOutputStream().write(output.getBytes());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } else {
            throw new SatElsetException();
        }
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
        } catch (SatElsetException ex) {
            String output = "No such norad in database";
            response.setContentLength(output.length());
            response.getOutputStream().write(output.getBytes());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
    }

    @Override
    public String getServletInfo() {
        return "Fetches TLE which best resembles characteristics provided";
    }// </editor-fold>

}
