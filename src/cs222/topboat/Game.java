package cs222.topboat;

public abstract class Game {
    private Thread gameThread;
    private GameLoop gameLoop;

    static Player player1;
    static Player player2;

    static State currentState;
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
        game.gameThread = new Thread(game.gameLoop, "GameThread");
        game.gameThread.start();
    }

    private class GameLoop implements Runnable {
        @Override
        public void run() {
            init();
            while (!Thread.currentThread().isInterrupted()) {
                currentState.operation.run();
            }
            finish();
        }
    }

    enum State {
        Ended(() -> Thread.currentThread().interrupt()),
        Running(() -> {
            while(!Thread.currentThread().isInterrupted()) {
                currentPlayer.takeTurn();
                if (waitingPlayer.allShipsSunk()) {
                    currentState = State.Ended;
                    return;
                }
                transitionPlayers();
            }
        }),
        Initializing(() -> {
            /*TODO: code for placing ships will happen here. Local Multiplayer will handle this differently
            because we'll want to show a transition screen between player1 and player2
             placing their ships. The same thing will happen with taking turns*/
        });

        State(Runnable op) {
            this.operation = op;
        }
        Runnable operation;
    }
}
