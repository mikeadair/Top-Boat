package edu.bsu.css22.topboat;


import edu.bsu.css22.topboat.controllers.GameBoardController;
import edu.bsu.css22.topboat.controllers.ViewController;
import edu.bsu.css22.topboat.models.Log;
import edu.bsu.css22.topboat.models.Stats;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class Game {
    State Ended = new State(() -> Thread.currentThread().interrupt());
    State Running = new State(() -> {
        ((ViewController)UI.currentController()).gameBoardController().startGameFunctionality();
        Log.gameLog().addMessage(new Log.Message("Battle Time!", Log.Message.Type.INFO));
        Log.gameLog().addMessage(new Log.Message("#1: Navigate to the enemy map", Log.Message.Type.INFO));
        Log.gameLog().addMessage(new Log.Message("#2: Select a weapon from the right side", Log.Message.Type.INFO));
        Log.gameLog().addMessage(new Log.Message("#3: Select a cell to fire at", Log.Message.Type.INFO));
        Log.gameLog().addMessage(new Log.Message("#4: Press 'Fire Weapon'", Log.Message.Type.INFO));
        this.stats.setPlayers(player1, player2);
        currentPlayer = player1;
        waitingPlayer = player2;
        while(!Thread.currentThread().isInterrupted()) {
            currentPlayer.takeTurn();
            if (waitingPlayer.allShipsSunk()) {
                this.stats.setResult(player1.allShipsSunk(),player2.allShipsSunk());
                GameBoardController.loadStats();
                changeState(Ended);
                return;
            }
            transitionPlayers();
        }
    });
    State Initializing = new State(() -> {
        handleShipPlacement();
        listenForStateChange();
    });

    private Thread gameThread;
    private GameLoop gameLoop;

    public static Player player1 = new Player();
    public static Player player2 = new Player();

    public static Stats stats = new Stats();

    static BlockingQueue<State> stateChangeQueue = new ArrayBlockingQueue<>(1);
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
        ((ViewController)UI.currentController()).gameBoardController().startShipPlacement();
        Log.gameLog().addMessage(new Log.Message("Ship Placement Time!", Log.Message.Type.INFO));
        Log.gameLog().addMessage(new Log.Message("#1: Select a cell to place a ship", Log.Message.Type.INFO));
        Log.gameLog().addMessage(new Log.Message("#2: Use arrow keys to rotate", Log.Message.Type.INFO));
        Log.gameLog().addMessage(new Log.Message("#3: Press 'enter' to confirm placement", Log.Message.Type.INFO));
        Log.gameLog().addMessage(new Log.Message("#4: Repeat until all your ships are placed", Log.Message.Type.INFO));
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
    }

    public static void startGame(Game game) {
        UI.changeView(UI.Views.MAIN_GAME);
        game.gameThread = new Thread(game.gameLoop, "GameThread");
        game.gameThread.start();
    }

    private void listenForStateChange() {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                State newState = stateChangeQueue.take();
                newState.operation.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void changeState(State newState) {
        stateChangeQueue.offer(newState);
    }

    private class GameLoop implements Runnable {
        @Override
        public void run() {
            init();
            Initializing.operation.run();
            finish();
        }
    }

    static class State {
        State(Runnable op) {
            this.operation = op;
        }
        Runnable operation;
    }
}
