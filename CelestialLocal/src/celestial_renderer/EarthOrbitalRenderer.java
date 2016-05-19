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
package celestial_renderer;

import celestial_computator.Coordinates;
import celestial_computator.EarthOrbitalComputator;
import celestial_computator.Track;
import celestial_exception.NoradAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import sgp4v.SatElsetException;

public class EarthOrbitalRenderer {
    
    private Canvas canvas;
    private GraphicsContext gc;
    private Image img;
    private List<EarthOrbitalComputator> eocl;
    
    public EarthOrbitalRenderer(Canvas canvas, Image img) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.img = img;
        this.eocl = new ArrayList<>();
    }
    
    public void addObject(int noradID, double dayFix) throws NoradAlreadyExistsException {
        if (this.isNorad(noradID) > 0)
            throw new NoradAlreadyExistsException();
        eocl.add(new EarthOrbitalComputator(noradID));
        eocl.get(eocl.size()-1).eocInit(dayFix);
    }
    
    public void delObject (int noradID) {
        for (int num = 0; num < eocl.size(); num++) {
            if (eocl.get(num).getNoradID() == noradID) {
                eocl.remove(num);
            }
        }
    }
    
    public EarthOrbitalComputator render(int bold, double dayFix) {
        EarthOrbitalComputator eoc = null;
        
        gc.drawImage(img, 0, 0);
        for (int num = 0; num < eocl.size(); num++) {
            eocl.get(num).eocInit(dayFix);
            
            try {
                if (num == bold) {
                    gc.setFill(Color.RED);
                    gc.setStroke(Color.RED);
                    gc.setLineWidth(2);
                    this.renderTrack(num);
                    this.renderObject(num);
                    gc.setLineWidth(1);
                    gc.setFill(Color.WHITE);
                    gc.setStroke(Color.WHITE);
                    eoc = eocl.get(num);
                } else {
                    this.renderTrack(num);
                    this.renderObject(num);
                }
            } catch (SatElsetException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Something went wrong...");
                alert.setContentText("Object and it's track can't be rendered!");
                alert.showAndWait();
            }
        }
        
        return eoc;
    }
    
    public void renderObject(int num) throws SatElsetException {
        Coordinates coord = eocl.get(num).calcCoordinates();
        coord = this.radToCanvas(coord);
        
        gc.fillOval(coord.getLon()-5, coord.getLat()-5, 10, 10);
    }
    
    public void renderTrack(int num) throws SatElsetException {
        Track track = eocl.get(num).calcTrack();

        ArrayList results = (ArrayList) track.getTrack();
        Coordinates coord = (Coordinates) results.get(0);
        coord = this.radToCanvas(coord);
        double prevLon = coord.getLon();
        double prevLat = coord.getLat();
        for (int i = 1; i < results.size(); i++) {
            coord = (Coordinates) results.get(i);
            coord = this.radToCanvas(coord);
            
            double lon = coord.getLon();
            double lat = coord.getLat();

            if (lon > prevLon) {
                gc.strokeLine(prevLon, prevLat, lon, lat);
            } else {
                gc.strokeLine(prevLon, prevLat, lon + canvas.getWidth(), lat);
                gc.strokeLine(prevLon - canvas.getWidth(), prevLat, lon, lat);
            }
 
            prevLon = lon;
            prevLat = lat;
        }
    }
    
    public Coordinates radToCanvas(Coordinates coord) {
        double lon = coord.getLon();
        double lat = coord.getLat();
        
        lon *= (canvas.getWidth()) / (2*Math.PI);
        lon += (canvas.getWidth()) / (2);
        
        lat *= (canvas.getWidth()) / (2*Math.PI);
        lat += (canvas.getHeight()) / (2);
        lat = canvas.getHeight() - lat;
        
        return new Coordinates(lon, lat);
    }
    
    public List<EarthOrbitalComputator> getEocl() {
        return eocl;
    }
    
    /**
     * Checks if noradID provided is already in list
     * 
     * @param noradID
     * @return 
     */
    public int isNorad(int noradID) {
        for (int i = 0; i < eocl.size(); i++) {
            int norad = eocl.get(i).getEOO().getNoradID();
            if (norad == noradID) return 1; // Or return norad
        }
        
        return 0;
    }
}
