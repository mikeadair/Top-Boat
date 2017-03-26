package edu.bsu.css22.topboat;

import edu.bsu.css22.topboat.models.Board;
import edu.bsu.css22.topboat.models.Ship;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class ComputerPlayer extends Player {
    private static final String[] names = new String[] {
            "Sheila", "Burger Bob", "Steve the Scuba", "Capt. Oscar Meyer", "BombBot",
            "Chief Squanto", "Pvt. Johnson", "Peter Dragon", "Data", "The Borg"
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
        new Thread(() -> {
            int currentTypeIndex = 0;
            Ship currentShip = new Ship(Ship.Type.values()[currentTypeIndex], -1, -1);
            while(true) {
                Board.Tile tryTile = getRandomTile();
                if(tryTile.occupied) {
                    continue;
                }

//                ArrayList<Ship.Orientation> validOrientations

                currentTypeIndex++;
                currentShip = new Ship(Ship.Type.values()[currentTypeIndex], -1, -1);
            }


        }).start();
    }

    private Board.Tile getRandomTile() {
        int randomX = random.nextInt(Board.WIDTH);
        int randomY = random.nextInt(Board.HEIGHT);

        return getBoard().get(randomX, randomY);
    }

    private void tryOrientationsForTile(Board.Tile tile) {
        ArrayList<Ship.Orientation> orientations = new ArrayList<>(Arrays.asList(Ship.Orientation.values()));
        Collections.shuffle(orientations);
        for(Ship.Orientation orientation : orientations) {

        }

    }

    private Ship.Orientation getRandomOrientation() {
        int randomIndex = random.nextInt(Ship.Orientation.values().length);
        return Ship.Orientation.values()[randomIndex];
    }

}
