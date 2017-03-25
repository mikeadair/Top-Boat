package edu.bsu.css22.topboat;


import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public abstract class Game {
    private Thread gameThread;
    private GameLoop gameLoop;

    public static Player player1;
    public static Player player2;

    static SimpleObjectProperty<State> currentState = new SimpleObjectProperty<>();
    static Player currentPlayer;
    static Player waitingPlayer;

    public Game() {
        gameLoop = new GameLoop();
    }

    abstract void init();
    abstract void start();
    abstract void finish();

    static void transitionPlayers() {
        Player temp = currentPlayer;
        currentPlayer = waitingPlayer;
        waitingPlayer = temp;
        /*TODO: this will be overridden by LocalMultiplayerGame to show a transition
        screen in between players taking turns
         */
    }

    static void handleShipPlacement() {
        /*TODO: this will be overridden by LocalMultiplayerGame to show a transition
        screen in between players placing ships
        */

    }

    public static void startGame(Game game) {
        UI.changeView(UI.Views.MAIN_GAME);
        game.gameThread = new Thread(game.gameLoop, "GameThread");
        game.gameThread.start();
    }

    private class GameLoop implements Runnable {
        @Override
        public void run() {
            setupStateChangeListener();
            init();
            currentState.set(State.Initializing);
            finish();
        }
    }

    private void setupStateChangeListener() {
        currentState.addListener((observable, oldState, newState) -> {
            newState.operation.run();
        });
    }

    enum State {
        Ended(() -> Thread.currentThread().interrupt()),
        Running(() -> {
            while(!Thread.currentThread().isInterrupted()) {
                currentPlayer.takeTurn();
                if (waitingPlayer.allShipsSunk()) {
                    currentState.set(State.Ended);
                    return;
                }
                transitionPlayers();
            }
        }),
        Initializing(() -> {
            player1.attachReadyListener((observable, oldReady, newReady) -> {
                System.out.println("player 1 ready");
            });

            player2.attachReadyListener((observable, oldReady, newReady) -> {

            });
        });

        State(Runnable op) {
            this.operation = op;
        }
        Runnable operation;
    }
}
