package edu.bsu.css22.topboat;

public class SinglePlayerGame extends Game {

    public SinglePlayerGame() {
        player1 = new Player();
        player2 = new ComputerPlayer();
    }

    @Override
    public void init() {

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
