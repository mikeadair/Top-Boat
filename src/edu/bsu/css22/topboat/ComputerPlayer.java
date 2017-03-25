package edu.bsu.css22.topboat;

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

}
