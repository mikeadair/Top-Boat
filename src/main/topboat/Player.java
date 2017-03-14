package main.topboat;

import main.topboat.models.Ship;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private String name;
    private HashMap<Ship.Type, Ship> ships;

    public Player(String name) {
        this.name = name;
    }

//    private void initializeShips() {
//        ships = new HashMap(Ship.Type.values().length);
//        for(Ship.Type type : Ship.Type.values()) {
//            ships.put(type, new Ship(type));
//        }
//    }

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
