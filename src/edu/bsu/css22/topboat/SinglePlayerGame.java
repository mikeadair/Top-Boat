package edu.bsu.css22.topboat;

public class SinglePlayerGame extends Game {

    @Override
    public void init() {
        player1 = new Player("Player 1");
        player2 = new ComputerPlayer();
    }

    @Override
    public void start() {
        boolean shouldPlay = true;
        Player currentPlayer = player1;
        while(true) {
            currentPlayer.takeTurn();
        }
    }

    @Override
    public void finish() {

    }
}
