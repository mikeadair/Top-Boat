package edu.bsu.css22.topboat;

import edu.bsu.css22.topboat.models.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Player {
    private String name;
    private HashMap<Ship.Type, Ship> ships;
    private Board board;
    private SimpleBooleanProperty ready = new SimpleBooleanProperty(false);
    private ArrayList<Weapon> arsenal = new ArrayList<>();
    private BlockingQueue<FireEvent> fireEvents = new ArrayBlockingQueue<>(1);

    public Player() {
        this.ships = new HashMap<>(Ship.Type.values().length);
        for(Weapon.Type weaponType : Weapon.Type.values()) {
            arsenal.add(new Weapon(weaponType));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public boolean isReady() {
        return ready.get();
    }

    public void setReady(boolean isReady) {
        if(isReady) {
            Log.gameLog().addMessage(new Log.Message(name + " is ready!", Log.Message.Type.SUCCESS));
        } else {
            Log.gameLog().addMessage(new Log.Message(name + " decided they're not ready after all", Log.Message.Type.SUCCESS));

        }
        ready.set(isReady);
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<Weapon> getArsenal() {
        return arsenal;
    }

    public void addShip(Ship ship) {
        ships.put(ship.type, ship);
    }

    public void takeTurn() {
        try {
            FireEvent fireEvent = fireEvents.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean allShipsSunk() {
        for (Map.Entry<Ship.Type, Ship> ship : ships.entrySet()) {
            if(ship.getValue() != null) {
                return false;
            }
        }
        return true;
    }

    public void attachReadyListener(ChangeListener<Boolean> newListener) {
        ready.addListener(newListener);
    }

    public void fire(FireEvent fireEvent) {
        fireEvents.offer(fireEvent);
    }
}
