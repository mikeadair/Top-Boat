package edu.bsu.css22.topboat;

import edu.bsu.css22.topboat.models.Ship;

import java.util.HashMap;
import java.util.Map;

public class Player {
    public String name;
    private HashMap<Ship.Type, Ship> ships;

    public Player(String name) {
        this.ships = new HashMap<>(Ship.Type.values().length);
        this.name = name;
    }

    public void addShip(Ship ship) {
        ships.put(ship.type, ship);
    }

    public void takeTurn() {

    }

    public boolean allShipsSunk() {
        for (Map.Entry<Ship.Type, Ship> ship : ships.entrySet()) {
            if(ship.getValue() != null) {
                return false;
            }
        }
        return true;
    }
}
