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
package celestial_computator;

import celestial_exception.TLEUnavailableException;
import celestial_object.EarthOrbitalObject;
import celestial_database.DatabaseAccessor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import sgp4v.SatElsetException;
import sgp4v.Sgp4Data;
import sgp4v.Sgp4Unit;

/**
 * From noradID and dayFix all characteristics are calculated here
 */
public class EarthOrbitalComputator {

    private int     noradID;
    private EarthOrbitalObject      eoo;
    private int     currentYear;
    private double  currentDay;
    private double  dayFix;
    private int     fixedYear;  // Toggler-adjusted year
    private double  fixedDay;   // Toggler-adjusted day
    private double  fixedEpoch; // Toggler-adjusted epoch
    private int     startYr;
    private double  startDay;
    private int     stopYr;
    private double  stopDay;
    private double  step;
    private double  lastLon;
    private double  lastLat;
    private double  x = 0;
    private double  y;
    private double  z;
    private double  vx;
    private double  vy;
    private double  vz;
    private double  v;
    private double  alt;
    
    public EarthOrbitalComputator(int noradID) {
        this.noradID = noradID;
    }
    
    /**
     * Calculates time-related characteristics
     * 
     * @param dayFix double adjustment in days from system time
     */
    public void eocInit(double dayFix) {
        this.dayFix = dayFix;
        
        double leap = 0;        // Keep it double
        double leapPrev = 0;    // Keep it double
        double cLeap = 0;       // Keep it double

        currentYear = this.getEpochYear();
        currentDay = this.getEpochDay();

        
        int cYear = currentYear;
        if ((cYear % 4) == 0) cLeap = 1;
        fixedDay = currentDay + dayFix;
        while (fixedDay > (365 + cLeap)) {
            cYear += 1;
            fixedDay -= (365 + cLeap);
            if ((cYear % 4) == 0) cLeap = 1;
            else cLeap = 0;
        }
        while (fixedDay < 0) {
            cYear -= 1;
            fixedDay += (365 + cLeap);
            if ((cYear % 4) == 0) cLeap = 1;
            else cLeap = 0;
        }
        fixedYear = cYear;

        if ((fixedYear % 4) == 0) leap = 1;
        if (((fixedYear-1) % 4) == 0) leapPrev = 1;
        fixedEpoch = fixedYear + (1/(365+leap))*fixedDay;
        this.getMostSuitableTLE();

        startDay = fixedDay - (1 / eoo.getTLE().getMeanMotion());
        if (startDay < 0) {
            startDay += (365 + leapPrev);
            startYr = fixedYear - 1;
        }
        startYr = fixedYear;

        stopDay = fixedDay + 2*(1 / eoo.getTLE().getMeanMotion());
        if (stopDay > 365 + leap) {
            stopDay -= (365 + leap);
            stopYr = fixedYear + 1;
        }
        stopYr = fixedYear;
        step = 4.00;    // minutes
    }

    /**
     * @return system epoch_year as int
     */
    public int getEpochYear() {
        DateFormat df;
        Date dateobj;
        int epoch_year;
                
        df = new SimpleDateFormat("yy");
        dateobj = new Date();
        epoch_year = Integer.parseInt(df.format(dateobj));
        
        return epoch_year;
    }
    
    /**
     * @return system epoch_day as double
     */
    public double getEpochDay() {
        DateFormat df;
        Date dateobj;
        int day;
        double hour;
        double minute;
        double second;
        double epoch_day;
                
        df = new SimpleDateFormat("D");
        dateobj = new Date();
        day = Integer.parseInt(df.format(dateobj));
        
        df = new SimpleDateFormat("H");
        dateobj = new Date();
        hour = ((double) 1/24) * Double.parseDouble(df.format(dateobj));
        
        df = new SimpleDateFormat("m");
        dateobj = new Date();
        minute = ((double) 1/24) * ((double) 1/60) * Double.parseDouble(df.format(dateobj));
        
        df = new SimpleDateFormat("s");
        dateobj = new Date();
        second = ((double) 1/24) * ((double) 1/3600) * Double.parseDouble(df.format(dateobj));
        
        epoch_day = day + hour + minute + second;

        return epoch_day;
    }
    
    public void getMostSuitableTLE() {
        try {
            eoo = DatabaseAccessor.getMostSuitableTLE(noradID, fixedEpoch);
        } catch (TLEUnavailableException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Something went terribly wrong...");
                alert.setContentText("TLE for this object is unavailable!");
                alert.showAndWait();
        } catch (SatElsetException ex) {
            Logger.getLogger(EarthOrbitalComputator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Coordinates calcCoordinates() throws SatElsetException {
        double radiusearthkm = 6378.135;
        double vkmpersec = radiusearthkm * 0.0743669161331734132 / 60.0;
        double shift = fixedDay - Math.floor(fixedDay);  // Runner, FIX
        
        Sgp4Unit sgp4 = new Sgp4Unit();
        Vector results = sgp4.runSgp4(eoo.getTLE().getCard1(), eoo.getTLE().getCard2(),
                fixedYear, fixedDay, fixedYear, fixedDay, 1);
        
        Sgp4Data data = (Sgp4Data) results.elementAt(0);
        Coordinates coord = this.FromVector3(data.getX(), data.getY(), data.getZ(), shift);
        lastLon = coord.getLon();
        lastLat = coord.getLat();
        x  = data.getX() * radiusearthkm;
        y  = data.getY() * radiusearthkm;
        z  = data.getZ() * radiusearthkm;
        vx = data.getXdot() * vkmpersec;
        vy = data.getYdot() * vkmpersec;
        vz = data.getZdot() * vkmpersec;
        v  = Math.sqrt(vx*vx + vy*vy + vz*vz);
        alt = Math.sqrt(x*x + y*y + z*z) - radiusearthkm;

        return coord;
    }
    
    public Track calcTrack() throws SatElsetException {
        /* Fractional parts */
        double start = startDay - Math.floor(startDay);
        double stop = stopDay - Math.floor(stopDay);
        double shift = start;   // Runner, FIX [start + 0.25]

        Track track = new Track();
        Sgp4Unit sgp4 = new Sgp4Unit();
        Vector results = sgp4.runSgp4(eoo.getTLE().getCard1(), eoo.getTLE().getCard2(),
                startYr, startDay, stopYr, stopDay, step);

        int sz = results.size();
        double azStep = (stopDay - startDay) / (sz);
        Sgp4Data data = null;
        Coordinates coord = null;
        for (int i = 0; i < results.size(); i++) {
            data = (Sgp4Data) results.elementAt(i);
            coord = this.FromVector3(data.getX(), data.getY(), data.getZ(), shift);
            
            shift += azStep;
            if (shift > 1) shift -= 1;
            if (shift < 0) shift += 1;
            
            track.add(coord);
        }
        
        return track;
    }
    
    public Coordinates FromVector3(double x, double y, double z, double shift)
    {
        double lon = (double) Math.atan2(y, x);
        double lat = (double) Math.atan2(z, Math.sqrt(x*x + y*y));
        
        lon = (Math.PI + lon + (2*Math.PI*shift));
        if (lon > 2*Math.PI) lon -= 2*Math.PI;
        lon -= Math.PI;

        Coordinates coord = new Coordinates(lon, lat);
        return coord;
    }
    
    public int getNoradID() {
        return noradID;
    }
    public EarthOrbitalObject getEOO() {
        return eoo;
    }
    
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }
    public double getV() {
        return v;
    }
    public double getVX() {
        return vx;
    }
    public double getVY() {
        return vy;
    }
    public double getVZ() {
        return vz;
    }
    public double getAlt() {
        return alt;
    }

}
