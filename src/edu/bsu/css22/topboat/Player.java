package edu.bsu.css22.topboat;

import edu.bsu.css22.topboat.models.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;

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

    public void setReady(boolean isReady, boolean shouldEmitMessage) {
        ready.set(isReady);

        if(!shouldEmitMessage) return;

        if(isReady) {
            Game.emitGameMessage(new Log.Message(name + " is ready!", Log.Message.Type.SUCCESS));
        } else {
            Game.emitGameMessage(new Log.Message(name + " decided they're not ready after all", Log.Message.Type.SUCCESS));
        }
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

    public HashMap<Ship.Type, Ship> getShips() {
        return ships;
    }

    public void addShip(Ship ship) {
        ships.put(ship.type, ship);
    }

    public void removeShip(Ship ship) {
        ships.remove(ship.type);
    }

    public void takeTurn() {
        System.out.println(name + " is taking their turn");
        try {
            FireEvent fireEvent = fireEvents.take();
            ArrayList<Board.Tile> hits = new ArrayList<>();
            ArrayList<Board.Tile> misses = new ArrayList<>();
            for(int[] affectedTile : fireEvent.getWeapon().getAffectedTiles()) {
                int x = affectedTile[1] + fireEvent.getTarget().x;
                int y = affectedTile[0] + fireEvent.getTarget().y;

                Board.Tile target = fireEvent.getTarget().getBoard().getTile(x, y);
                if (target == null || target.hasBeenHit()) {
                    continue;
                } else {
                    if(target.hit()) {
                        hits.add(target);
                    } else {
                        misses.add(target);
                    }
                }
            }
            if(hits.size() > 0) {
                String hitMessage = "You hit at " + buildStringFromTiles(hits);
                Log.gameLog().addMessage(new Log.Message(hitMessage, Log.Message.Type.SUCCESS));
            }
            if(misses.size() > 0) {
                String missMessage = "You missed at " + buildStringFromTiles(misses);
                Log.gameLog().addMessage(new Log.Message(missMessage, Log.Message.Type.ERROR));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String buildStringFromTiles(ArrayList<Board.Tile> tiles) {
        StringBuilder builder = new StringBuilder();
        if(tiles.size() == 1){
            return tiles.get(0).name.toString() + ".";
        }
        for(int i = 0; i < tiles.size(); i++) {
            if(i != tiles.size() - 1 && i != 0) {
                builder.append(", ");
            }
            if(i == tiles.size() - 1) {
                builder.append(", and ");
            }
            Board.Tile tile = tiles.get(i);
            builder.append(tile.name.toString());
        }
        return builder.toString() + ".";
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
