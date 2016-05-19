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
 * ObjectsToRender in TableView
 */
public class ObjectsToRenderTableData {
    
    private final SimpleIntegerProperty rNoradID;
    private final SimpleIntegerProperty rEpochYear;
    private final SimpleStringProperty  rEpochDay;
    
    public ObjectsToRenderTableData(Integer sNoradID, Integer sEpochYear, String sEpochDay) {
        this.rNoradID = new SimpleIntegerProperty(sNoradID);
        this.rEpochYear = new SimpleIntegerProperty(sEpochYear);
        this.rEpochDay = new SimpleStringProperty(sEpochDay);
    }
    
    
    public void setRNoradID(Integer value) {
        rNoradID.set(value);
    }
    public Integer getRNoradID() {
        return rNoradID.get();
    }
    
    public void setREpochYear(Integer value) {
        rEpochYear.set(value);
    }
    public Integer getREpochYear() {
        return rEpochYear.get();
    }
    
    public void setREpochDay(String value) {
        rEpochDay.set(value);
    }
    public String getREpochDay() {
        return rEpochDay.get();
    }
    
}
