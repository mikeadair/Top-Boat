package edu.bsu.css22.topboat.Util;

import edu.bsu.css22.topboat.Player;
import edu.bsu.css22.topboat.models.Board;
import edu.bsu.css22.topboat.models.Ship;
import edu.bsu.css22.topboat.models.Ship.Type;
import edu.bsu.css22.topboat.models.Ship.Orientation;

import java.util.ArrayList;

public class ShipPlacementHandler {
    private Player player;
    private int currentTypeIndex = 0;
    private Ship currentShip;
    private Board.Tile currentShipOrigin;
    private ArrayList<Orientation> validOrientations = new ArrayList<>();
    private boolean allShipsPlaced = false;

    public ShipPlacementHandler(Player player) {
        this.player = player;
        this.currentShip = new Ship(player, Type.values()[currentTypeIndex], -1, -1);
    }

    public ArrayList<Orientation> getValidOrientations() {
        return validOrientations;
    }

    public Ship getCurrentShip() {
        return currentShip;
    }

    public boolean isValidPlacementOrigin(Board.Tile tile) {
        getValidOrientations(tile);
        return validOrientations.size() != 0;
    }

    private boolean tileContainsDifferentShip(Board.Tile tile) {
        if(tile.isOccupied()) {
            return tile.ship != currentShip;
        } else {
            return false;
        }
    }

    private void getValidOrientations(Board.Tile tile) {
        validOrientations.clear();
        if(tileContainsDifferentShip(tile)) return;

        for(Orientation orientation : Orientation.values()) {
            boolean orientationIsValid = true;
            for(int i = 1; i < currentShip.getLength(); i++) {
                int x = tile.x + (orientation.xMod * i);
                int y = tile.y + (orientation.yMod * i);

                Board.Tile tryTile = player.getBoard().getTile(x, y);
                if(tryTile == null) {
                    orientationIsValid = false;
                    break;
                }

                if (tileContainsDifferentShip(tryTile)) {
                    orientationIsValid = false;
                    break;
                }
            }
            if(orientationIsValid) {
                validOrientations.add(orientation);
            }
        }
    }

    public void newShipPlacement(Board.Tile tile, Orientation orientation) {
        resetCurrentPlacement();
        currentShip.setX(tile.x);
        currentShip.setY(tile.y);
        currentShipOrigin = tile;
        currentShip.orientation = orientation;
        for(int i = 0; i < currentShip.getLength(); i++) {
            int x = tile.x + (orientation.xMod * i);
            int y = tile.y + (orientation.yMod * i);

            Board.Tile newTile = player.getBoard().getTile(x, y);
            currentShip.orientation = orientation;
            newTile.ship = currentShip;
            onNewShipPlacementInTile(newTile, currentShip, i);
        }
    }

    public void onNewShipPlacementInTile(Board.Tile tile, Ship ship, int index){}

    public void resetCurrentPlacement() {
        if(currentShipOrigin == null) return;

        for(int i = 0; i < currentShip.getLength(); i++) {
            int x = currentShipOrigin.x + (currentShip.orientation.xMod * i);
            int y = currentShipOrigin.y + (currentShip.orientation.yMod * i);

            Board.Tile resetTile = player.getBoard().getTile(x, y);
            resetTile.ship = null;
            onResetShipPlacementInTile(resetTile);
        }
    }

    public void onResetShipPlacementInTile(Board.Tile tile){}

    public void confirmShipPlacement(Board.Tile finalTile, Orientation finalOrientation) {
        newShipPlacement(finalTile, finalOrientation);
        player.addShip(currentShip);

        currentTypeIndex++;
        if(currentTypeIndex < Type.values().length) {
            validOrientations.clear();
            currentShip = new Ship(player, Type.values()[currentTypeIndex], -1, -1);
            currentShipOrigin = null;
        } else {
            allShipsPlaced = true;
        }
    }

    public boolean allShipsPlaced() {
        return allShipsPlaced;
    }
}
