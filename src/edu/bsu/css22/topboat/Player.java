package edu.bsu.css22.topboat;

import edu.bsu.css22.topboat.models.Log;
import edu.bsu.css22.topboat.models.Ship;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private String name;
    private HashMap<Ship.Type, Ship> ships;
    private SimpleBooleanProperty ready = new SimpleBooleanProperty(false);

    public Player() {
        this.ships = new HashMap<>(Ship.Type.values().length);
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
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

    public void setReady(boolean isReady) {
        if(isReady) {
            Log.gameLog().addMessage(new Log.Message(name + " is ready!", Log.Message.Type.SUCCESS));
        } else {
            Log.gameLog().addMessage(new Log.Message(name + " decided they're not ready after all", Log.Message.Type.SUCCESS));

        }
        ready.set(isReady);
    }

    public void attachReadyListener(ChangeListener<Boolean> newListener) {
        ready.addListener(newListener);
    }
}
