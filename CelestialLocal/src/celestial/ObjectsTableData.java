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
package celestial;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/** 
 * Database data representation in TableView
 */
public class ObjectsTableData {
    
    private final SimpleIntegerProperty rNoradID;
    private final SimpleStringProperty rName;
    private final SimpleStringProperty rObjType;
    private final SimpleStringProperty rOrbType;
    private final SimpleStringProperty rCountry;
    
    public ObjectsTableData(Integer sNoradID, String sName, String sObjType, String sOrbType, String sCountry) {
        this.rNoradID = new SimpleIntegerProperty(sNoradID);
        this.rName = new SimpleStringProperty(sName);
        this.rObjType = new SimpleStringProperty(sObjType);
        this.rOrbType = new SimpleStringProperty(sOrbType);
        this.rCountry = new SimpleStringProperty(sCountry);
    }
    
    
    public void setRNoradID(Integer value) {
        rNoradID.set(value);
    }
    public Integer getRNoradID() {
        return rNoradID.get();
    }
    
    public void setRName(String value) {
        rName.set(value);
    }
    public String getRName() {
        return rName.get();
    }
    
    public void setRObjType(String value) {
        rObjType.set(value);
    }
    public String getRObjType() {
        return rObjType.get();
    }
    
    public void setROrbType(String value) {
        rOrbType.set(value);
    }
    public String getROrbType() {
        return rOrbType.get();
    }
    
    public void setRCountry(String value) {
        rCountry.set(value);
    }
    public String getRCountry() {
        return rCountry.get();
    }
    
}
