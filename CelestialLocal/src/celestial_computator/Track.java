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

import java.util.ArrayList;
import java.util.List;

/**
 * Simply list of coordinates
 */
public class Track {
    
    private List<Coordinates> track;
    
    public Track() {
        track = new ArrayList<>();
    }
    
    public void add(Coordinates coord) {
        track.add(coord);
    }
    
    public void setTrack(List<Coordinates> track) {
        this.track = track;
    }
    public List<Coordinates> getTrack() {
        return track;
    }
    
}
