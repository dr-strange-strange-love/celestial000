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
package celestial_object;

import sgp4v.SatElset;

/**
 *
 * @author Amadeus
 */
public class EarthOrbitalObject {
    
    private String    name;
    private final int       noradID;
    /** epoch year [0, 99] */
    private final int       epochYear;
    /** epoch day [0.0, 366.0] */
    private final double    epochDay;
    private final SatElset  tle;
    private String    objType;
    private String    orbType;
    private String    country;
    
    /**
     * Initializes EarthOrbitalObject with minimal characteristics
     * 
     * @param noradID
     * @param epochYear
     * @param epochDay
     * @param tle 
     */
    public EarthOrbitalObject(int noradID, int epochYear, double epochDay, SatElset tle) {
        this.noradID = noradID;
        this.epochYear = epochYear;
        this.epochDay = epochDay;
        this.tle = tle;
        
        this.name = " ";
        this.objType = " ";
        this.orbType = " ";
        this.country = " ";
    }
    
    /**
     * Initializes EarthOrbitalObject with minimal characteristics
     * 
     * @param name
     * @param noradID
     * @param epochYear
     * @param epochDay
     * @param tle 
     */
    public EarthOrbitalObject(String name, int noradID, int epochYear, double epochDay, SatElset tle) {
        this.name = name;
        this.noradID = noradID;
        this.epochYear = epochYear;
        this.epochDay = epochDay;
        this.tle = tle;
        
        this.objType = " ";
        this.orbType = " ";
        this.country = " ";
    }
    
    
    public void setObjType(String objType) {
        this.objType = objType;
    }
    
    public void setOrbType(String orbType) {
        this.orbType = orbType;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    

    public String getName() {
        return name;
    }
    
    public int getNoradID() {
        return noradID;
    }
    
    public int getEpochYear() {
        return epochYear;
    }
    
    public double getEpochDay() {
        return epochDay;
    }
    
    public SatElset getTLE() {
        return tle;
    }
    
    public String getObjType() {
        return objType;
    }
    
    public String getOrbType() {
        return orbType;
    }
    
    public String getCountry() {
        return country;
    }
    
}
