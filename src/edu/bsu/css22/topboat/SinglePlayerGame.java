package edu.bsu.css22.topboat;

import edu.bsu.css22.topboat.models.*;

public class SinglePlayerGame extends Game {

    public SinglePlayerGame() {
        player1.setBoard(Board.playerBoard());

        player2 = new edu.bsu.css22.topboat.models.ComputerPlayer();
        player2.setBoard(Board.opponentBoard());
    }

    @Override
    public void init() {

    }

    @Override
    public void start() {
    }

    @Override
     void handleShipPlacement() {
        super.handleShipPlacement();
        player1.attachReadyListener((observable, oldReady, newReady) -> {
            if(newReady && player2.isReady()) {
                changeState(Running);
            }
        });

        player2.attachReadyListener((observable, oldReady, newReady) -> {
            if(newReady && player1.isReady()) {
                changeState(Running);
            }
        });
        ((edu.bsu.css22.topboat.models.ComputerPlayer)player2).placeShips();
    }

    @Override
    public void finish() {

    }
}
