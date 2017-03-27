package edu.bsu.css22.topboat;

import edu.bsu.css22.topboat.Util.ShipPlacementHandler;
import edu.bsu.css22.topboat.models.Board;
import edu.bsu.css22.topboat.models.Ship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class ComputerPlayer extends Player {
    private static final String[] names = new String[] {
            "Sheila", "Burger Bob", "Steve the Scuba", "Capt. Oscar Meyer", "BombBot",
            "Chief Squanto", "Pvt. Johnson", "Peter Dragon", "Data", "The Borg", "Gryphon Peter"
    };
    private static final Random random = new Random();

    public ComputerPlayer() {
        super();
        setName(names[random.nextInt(names.length)]);
    }

    @Override
    public void takeTurn() {

    }

    public void placeShips() {
        ShipPlacementHandler placementHandler = new ShipPlacementHandler(this);
        new Thread(() -> {
            while(!placementHandler.allShipsPlaced()) {
                Board.Tile tryTile = getRandomTile();
                if (placementHandler.isValidPlacementOrigin(tryTile)) {
                    Ship.Orientation randomOrientation = getRandomOrientationFromList(placementHandler.getValidOrientations());
                    placementHandler.confirmShipPlacement(tryTile, randomOrientation);
                }
            }
            long waitAmount = (random.nextInt(10) + 5) * 1000;
            try {
                Thread.sleep(waitAmount);
            } catch (InterruptedException e) {}
            setReady(true);
        }, "ComputerShipPlacementThread").start();
    }

    private Board.Tile getRandomTile() {
        int randomX = random.nextInt(Board.WIDTH);
        int randomY = random.nextInt(Board.HEIGHT);

        return getBoard().getTile(randomX, randomY);
    }

    private Ship.Orientation getRandomOrientationFromList(ArrayList<Ship.Orientation> validOrientations) {
        int randomIndex = random.nextInt(validOrientations.size());
        return validOrientations.get(randomIndex);
    }

}
