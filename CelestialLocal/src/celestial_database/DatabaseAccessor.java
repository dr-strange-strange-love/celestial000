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
package celestial_database;

import celestial.FXMLLocalController;
import celestial.ObjectsTableData;
import celestial_exception.TLEUnavailableException;
import celestial_object.EarthOrbitalObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import sgp4v.SatElset;
import sgp4v.SatElsetException;

public class DatabaseAccessor {

    public static void loadOneTLE(EarthOrbitalObject eoo) {
        try {
            /* Getting data local */
            int noradID = eoo.getNoradID();
            int epochYear = eoo.getEpochYear();
            double epochDay = eoo.getEpochDay();
            String card1 = eoo.getTLE().getCard1();
            String card2 = eoo.getTLE().getCard2();
            /* Getting data proper */
            String epochDayStr = Double.toString(epochDay);
            card1 = card1.replace(' ', '_');
            card1 = card1.replace('.', '^');
            card2 = card2.replace(' ', '_');
            card2 = card2.replace('.', '^');

            /* Getting URL with params */
            String url_str = "http://localhost:8080/CelestialGlobal/LoadOneTLE?";
            String charset = java.nio.charset.StandardCharsets.UTF_8.name();
            String param1 = Integer.toString(noradID);
            String param2 = Integer.toString(epochYear);
            String param3 = Double.toString(epochDay);
            String param4 = card1;
            String param5 = card2;
            String query_srt = String.format("noradID=%s&epochYear=%s&epochDay=%s&card1=%s&card2=%s", 
                    URLEncoder.encode(param1, charset), 
                    URLEncoder.encode(param2, charset), 
                    URLEncoder.encode(param3, charset), 
                    URLEncoder.encode(param4, charset), 
                    URLEncoder.encode(param5, charset)
            );
            URL url = new URL(url_str + query_srt);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            out.write("");
            out.flush();
            out.close();

            String response;
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((response = in.readLine()) != null ) {
                System.out.println(response);
            }
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(DatabaseAccessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void loadOneSSR(EarthOrbitalObject eoo) {
        try {
            /* Getting data local */
            int noradID = eoo.getNoradID();
            String name = eoo.getName();
            String objType = eoo.getObjType();
            String orbType = eoo.getOrbType();
            String country = eoo.getCountry();
            /* Getting data proper */
            name = name.replace(' ', '_');

            /* Getting URL with params */
            String url_str = "http://localhost:8080/CelestialGlobal/LoadOneSSR?";
            String charset = java.nio.charset.StandardCharsets.UTF_8.name();
            String param1 = Integer.toString(noradID);
            String param2 = name;
            String param3 = objType;
            String param4 = orbType;
            String param5 = country;
            String query_srt = String.format("noradID=%s&name=%s&objType=%s&orbType=%s&country=%s", 
                    URLEncoder.encode(param1, charset), 
                    URLEncoder.encode(param2, charset), 
                    URLEncoder.encode(param3, charset), 
                    URLEncoder.encode(param4, charset), 
                    URLEncoder.encode(param5, charset)
            );
            URL url = new URL(url_str + query_srt);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            out.write("");
            out.flush();
            out.close();

            String response;
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((response = in.readLine()) != null ) {
                System.out.println(response);
            }
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(DatabaseAccessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void loadManyTLE() {
        ArrayList<EarthOrbitalObject> eoos = (ArrayList<EarthOrbitalObject>) FXMLLocalController.getEOOS();
        eoos.stream().forEach((eoo) -> {
            DatabaseAccessor.loadOneTLE(eoo);
        });
    }
    
    public static void loadManySSR() {
        ArrayList<EarthOrbitalObject> eoos = (ArrayList<EarthOrbitalObject>) FXMLLocalController.getEOOS();
        eoos.stream().forEach((eoo) -> {
            DatabaseAccessor.loadOneSSR(eoo);
        });
    }
    
    public static void loadAll() {
        DatabaseAccessor.loadManyTLE();
        DatabaseAccessor.loadManySSR();
    }
    
    public static void getAll(TableView<ObjectsTableData> dbTable, ObservableList<ObjectsTableData> tbl_data,
            String filterNorad, String filterName) {
        try {
            /* Getting data local */
            String filterNoradStr = filterNorad;
            String filterNameStr = filterName;
            /* Getting data proper */
            filterNameStr = filterNameStr.replace(' ', '_');

            /* Getting URL with params */
            String url_str = "http://localhost:8080/CelestialGlobal/GetAll?";
            String charset = java.nio.charset.StandardCharsets.UTF_8.name();
            String param1 = filterNoradStr;
            String param2 = filterNameStr;
            String query_srt = String.format("filterNorad=%s&filterName=%s", 
                    URLEncoder.encode(param1, charset), 
                    URLEncoder.encode(param2, charset)
            );
            URL url = new URL(url_str + query_srt);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            out.write("");
            out.flush();
            out.close();

            String response;
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((response = in.readLine()) != null ) {
                int norad_id = Integer.parseInt(response);
                String name = in.readLine();
                String obj_type = in.readLine();
                String orb_type = in.readLine();
                String country = in.readLine();
                tbl_data.add(new ObjectsTableData(norad_id, name, obj_type, orb_type, country));
            }
            in.close();
            dbTable.setItems(tbl_data);
        } catch (IOException ex) {
            Logger.getLogger(DatabaseAccessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static EarthOrbitalObject getMostSuitableTLE(int noradID, double fixed_epoch)
            throws TLEUnavailableException, SatElsetException {
        EarthOrbitalObject eoo = null;
        
        try {
            /* Getting data local */
            int noradIDInt = noradID;
            double fixedEpoch = fixed_epoch;

            /* Getting URL with params */
            String url_str = "http://localhost:8080/CelestialGlobal/GetMostSuitableTLE?";
            
            String param1 = Integer.toString(noradIDInt);
            String param2 = Double.toString(fixedEpoch);
            String charset = java.nio.charset.StandardCharsets.UTF_8.name();
            String query_srt = String.format("noradID=%s&fixedEpoch=%s", 
                    URLEncoder.encode(param1, charset), 
                    URLEncoder.encode(param2, charset)
            );
            URL url = new URL(url_str + query_srt);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            out.write("");
            out.flush();
            out.close();
            
            int norad_id;
            String epoch_year_str;
            String epoch_day_str;
            int epoch_year;
            double epoch_day;
            String card1;
            String card2;
            String response;
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((response = in.readLine()) != null ) {
                norad_id = Integer.parseInt(response);
                epoch_year_str = in.readLine();
                epoch_day_str = in.readLine();
                epoch_year = Integer.parseInt(epoch_year_str);
                epoch_day = Double.parseDouble(epoch_day_str);
                card1 = in.readLine();
                card2 = in.readLine();
                eoo = new EarthOrbitalObject(norad_id, epoch_year, epoch_day, new SatElset(card1, card2));
            }
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(DatabaseAccessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return eoo;
    }
    
}
