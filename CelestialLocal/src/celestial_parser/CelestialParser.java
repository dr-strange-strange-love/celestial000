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
package celestial_parser;

import celestial.FXMLLocalController;
import celestial_object.EarthOrbitalObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import sgp4v.SatElset;
import sgp4v.SatElsetException;

public class CelestialParser {
    
    private File file;
    
    public CelestialParser(File file) {
        this.file = file;
    }
    
    /**
     * Checks file type (2-line or 3-line) and reads it
     */
    public void readTLEFile() {
        FXMLLocalController.clearEOOS();
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            
            if (Character.isLetter(line.charAt(0))) {
                /** If 3-line */
                this.read3LineTLEFile();
            } else if (line.charAt(0) == '1') {
                /** If 2-line */
                this.read2LineTLEFile();
            } else {
                throw new IOException("Invalid TLE data.");
            }
        } catch (IOException ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Something went wrong...");
            alert.setContentText("TLE data or file is invalid/corrupted!\nChoose different file.");
            alert.showAndWait();
            FXMLLocalController.clearEOOS();
        }
    }
    
    /**
     * Reads 2-line TLE file to EOOS Vector in FXMLLocalController (clears old EOOS Vector beforehand)
     */
    public void read2LineTLEFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            for(String line1; (line1 = br.readLine()) != null; ) {
                String line2 = br.readLine();

                try {
                    SatElset tle = new SatElset(line1, line2);
                    EarthOrbitalObject eoo = new EarthOrbitalObject(
                        tle.getSatID(),
                        tle.getEpochYr(),
                        tle.getEpochDay(),
                        tle);
                    FXMLLocalController.addEOO(eoo);
                } catch (SatElsetException ex) {
                    System.out.println("Skipping corrupted TLE...");
                }
            }
        } catch (IOException ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Something went wrong...");
            alert.setContentText("TLE data or file is invalid/corrupted!\nChoose different file.");
            alert.showAndWait();
            FXMLLocalController.clearEOOS();
        }
    }
    
    /**
     * Reads 3-line TLE file to EOOS Vector in FXMLLocalController (clears old EOOS Vector beforehand)
     */
    public void read3LineTLEFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            for(String line; (line = br.readLine()) != null; ) {
                String line1 = br.readLine();
                String line2 = br.readLine();
                
                try {
                    SatElset tle = new SatElset(line1, line2);
                    EarthOrbitalObject eoo = new EarthOrbitalObject(
                            line,
                            tle.getSatID(),
                            tle.getEpochYr(),
                            tle.getEpochDay(),
                            tle);
                    FXMLLocalController.addEOO(eoo);
                } catch (SatElsetException ex) {
                    System.out.println("Skipping corrupted TLE...");
                }
            }
        } catch (IOException ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Something went wrong...");
            alert.setContentText("TLE data or file is invalid/corrupted!\nChoose different file.");
            alert.showAndWait();
            FXMLLocalController.clearEOOS();
        }
    }
}
