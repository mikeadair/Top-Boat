package edu.bsu.css22.topboat;

import edu.bsu.css22.topboat.models.Board;

public class SinglePlayerGame extends Game {

    public SinglePlayerGame() {
        player1 = new Player();
        player1.setBoard(Board.playerBoard());

        player2 = new ComputerPlayer();
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
        ((ComputerPlayer)player2).placeShips();
    }

    @Override
    public void finish() {

    }
}
