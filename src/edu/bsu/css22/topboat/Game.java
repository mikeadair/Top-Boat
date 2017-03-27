package edu.bsu.css22.topboat;


import edu.bsu.css22.topboat.controllers.ViewController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public abstract class Game {
    State Ended = new State(() -> Thread.currentThread().interrupt());
    State Running = new State(() -> {
        ((ViewController)UI.currentController()).gameBoardController().startGameFunctionality();
        while(!Thread.currentThread().isInterrupted()) {
            currentPlayer.takeTurn();
            if (waitingPlayer.allShipsSunk()) {
                currentState.set(Ended);
                return;
            }
            transitionPlayers();
        }
    });
    State Initializing = new State(() -> {
        handleShipPlacement();
    });

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

    void handleShipPlacement() {
        player1.attachReadyListener((observable, oldReady, newReady) -> {
            if(newReady && player2.isReady()) {
                currentState.set(Running);
            }
        });

        player2.attachReadyListener((observable, oldReady, newReady) -> {
            if(newReady && player1.isReady()) {
                currentState.set(Running);
            }
        });
        ((ViewController)UI.currentController()).gameBoardController().startShipPlacement();
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
            currentState.set(Initializing);
            finish();
        }
    }

    private void setupStateChangeListener() {
        currentState.addListener((observable, oldState, newState) -> {
            newState.operation.run();
        });
    }

    static class State {
        State(Runnable op) {
            this.operation = op;
        }
        Runnable operation;
    }
}
