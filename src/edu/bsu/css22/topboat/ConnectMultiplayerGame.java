package edu.bsu.css22.topboat;

import edu.bsu.css22.topboat.Util.SocketConnection;
import edu.bsu.css22.topboat.controllers.GameBoardController;
import edu.bsu.css22.topboat.controllers.ViewController;
import edu.bsu.css22.topboat.models.Board;
import edu.bsu.css22.topboat.models.Log;
import edu.bsu.css22.topboat.models.Ship;
import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ConnectMultiplayerGame extends Game {
    private String host;
    private boolean isHost;
    private GameServer gameServer;
    private ChatServer chatServer;

    public ConnectMultiplayerGame(boolean isHost) {
        super();
        this.isHost = isHost;
        gameServer = new GameServer(isHost);
        chatServer = new ChatServer(isHost);

        LocalPlayer localPlayer = new LocalPlayer(gameServer);
        localPlayer.setName(player1.getName());

        player1 = localPlayer;
        player2 = new ConnectedPlayer(gameServer);

        player1.setBoard(Board.playerBoard());
        player2.setBoard(Board.opponentBoard());

        Running = new State(() -> {
            try {
                ((ViewController)UI.currentController()).gameBoardController().startGameFunctionality();
                TimeUnit.MILLISECONDS.sleep(500);
                Log.gameLog().addMessage(new Log.Message("Battle Time!", Log.Message.Type.INFO));
                Log.gameLog().addMessage(new Log.Message("#1: Navigate to the enemy map", Log.Message.Type.INFO));
                Log.gameLog().addMessage(new Log.Message("#2: Select a weapon from the right side", Log.Message.Type.INFO));
                Log.gameLog().addMessage(new Log.Message("#3: Select a cell to fire at", Log.Message.Type.INFO));
                Log.gameLog().addMessage(new Log.Message("#4: Press 'Fire Weapon'", Log.Message.Type.INFO));
                stats.setPlayers(player1, player2);
                if(isHost) {
                    currentPlayer = player1;
                    waitingPlayer = player2;
                } else {
                    currentPlayer = player2;
                    waitingPlayer = player1;
                }
                while(!Thread.currentThread().isInterrupted()) {
                    currentPlayer.takeTurn();
                    if (waitingPlayer.allShipsSunk()) {
                        stats.setResult(player1.allShipsSunk(), player2.allShipsSunk());
                        GameBoardController.loadStats();
                        changeState(Ended);
                        return;
                    }
                    transitionPlayers();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void startServers() {
        gameServer.start(host);
        chatServer.start(host);
    }

    public void stopServers() {
        gameServer.end();
        chatServer.end();
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    void init() {}

    @Override
    void start() {}

    @Override
    void finish() {}

    @Override
    void handleGameMessage(Log.Message message) {
        Log.gameLog().addMessage(message);
        chatServer.emitMessage("game", message);
    }

    @Override
    void handlePlayerMessage(Log.Message message) {
        if(!message.getContents().equals("")) {
            Log.chatLog().addMessage(message);
            chatServer.emitMessage("chat", message);
        }
    }

    @Override
    void handleShipPlacement() {
        super.handleShipPlacement();
        player1.attachReadyListener((observable, oldReady, newReady) -> {
            if(newReady) {
                sendReady();
            }
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

    private void sendReady() {
        JSONObject dataObject = new JSONObject();
        JSONObject responseObject = new JSONObject();
        JSONArray shipsArray = new JSONArray();
        for(Map.Entry<Ship.Type, Ship> shipEntry : player1.getShips().entrySet()) {
            JSONObject shipObject = new JSONObject();
            Ship ship = shipEntry.getValue();
            shipObject.put("type", ship.type.toString().toUpperCase());
            shipObject.put("orientation", ship.orientation.toString().toUpperCase());
            shipObject.put("x", ship.getX());
            shipObject.put("y", ship.getY());
            shipsArray.put(shipObject);
        }
        dataObject.put("type", "playerReady");
        responseObject.put("ships", shipsArray);
        responseObject.put("name", Game.player1.getName());
        responseObject.put("ready", true);
        dataObject.put("response", responseObject);
        gameServer.sendData(dataObject.toString());
    }

    public class GameServer {
        private static final int GAME_PORT = 5000;
        private Thread thread;
        private ServerSocket serverSocket;
        private SocketConnection gameSocket;
        private boolean isHost;
        private SocketConnection.DataReceivedListener socketListener = new GameSocketListener();

        public GameServer(boolean isHost) {
            this.isHost = isHost;
        }

        public void start(String host) {
            if(isHost) {
                hostServer();
            } else {
                connectToHost(host);
            }
        }

        public void end() {
            try {
                if (isHost) {
                    serverSocket.close();
                }
                gameSocket.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {}
        }

        public String nextData() {
            try {
                return gameSocket.nextData();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void sendData(String data) {
            gameSocket.write(data);
        }

        private void hostServer() {
            thread = new Thread(() -> {
                try {
                    serverSocket = new ServerSocket(GAME_PORT);
                    waitForConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }

        private void waitForConnection() throws IOException {
            try {
                gameSocket = new SocketConnection(serverSocket.accept());
                gameSocket.onDataReceived(socketListener);
            } catch(SocketException e) {}
            Platform.runLater(() -> {
                if(!serverSocket.isClosed())
                    Game.startGame(ConnectMultiplayerGame.this);
            });
        }

        private void connectToHost(String host) {
            gameSocket = new SocketConnection();
            gameSocket.connect(host, GAME_PORT);
            gameSocket.onDataReceived(socketListener);
        }
    }

    private class ChatServer {
        private static final int CHAT_PORT = 5001;
        private Thread thread;
        private ServerSocket serverSocket;
        private SocketConnection chatSocket;
        private boolean isHost;
        private SocketConnection.DataReceivedListener socketListener = new ChatSocketListener();

        public ChatServer(boolean isHost) {
            this.isHost = isHost;
        }

        public void start(String host) {
            if(isHost) {
                hostServer();
            } else {
                connectToHost(host);
            }
        }

        public void end() {
            try {
                if (isHost) {
                    serverSocket.close();
                }
                chatSocket.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {}
        }

        public void emitMessage(String type, Log.Message message) {
            JSONObject messageObject = new JSONObject();
            messageObject.put("type", type);
            messageObject.put("contents", message.getContents());
            chatSocket.write(messageObject.toString());
        }

        private void hostServer() {
            thread = new Thread(() -> {
                try {
                    serverSocket = new ServerSocket(CHAT_PORT);
                    chatSocket = new SocketConnection(serverSocket.accept());
                    chatSocket.onDataReceived(socketListener);
                } catch(SocketException e) {
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }

        private void connectToHost(String host) {
            chatSocket = new SocketConnection();
            chatSocket.connect(host, CHAT_PORT);
            chatSocket.onDataReceived(socketListener);
        }
    }

    private void opponenentReady(JSONObject response) {
        boolean isReady = response.getBoolean("ready");
        String playerName = response.getString("name");
        Game.player2.setName(playerName);
        if(isReady) {
            JSONArray shipsArray = response.getJSONArray("ships");
            for(Object shipObject : shipsArray) {
                JSONObject ship = (JSONObject) shipObject;
                String shipType = ship.getString("type");
                String orientation = ship.getString("orientation");
                int x = ship.getInt("x");
                int y = ship.getInt("y");
                addNewShipToOpponent(shipType, orientation, x, y);
            }
        }
        Game.player2.setReady(isReady, false);
    }

    private void addNewShipToOpponent(String t, String o, int x, int y) {
        Ship.Type type = Ship.Type.valueOf(t);
        Ship.Orientation orientation = Ship.Orientation.valueOf(o);
        Ship ship = new Ship(Game.player2, type, x, y);
        ship.orientation = orientation;
        Game.player2.addShip(ship);
        for(int i = 0; i < ship.getLength(); i++) {
            int tileX = x + (orientation.xMod * i);
            int tileY = y + (orientation.yMod * i);
            Game.player2.getBoard().getTile(tileX, tileY).ship = ship;
        }
    }


    private class GameSocketListener implements SocketConnection.DataReceivedListener {

        @Override
        public void onDataReceived(String data) {
            System.out.println("game data received: " + data.toString());
            JSONObject dataObject = new JSONObject(data);
            String type = dataObject.getString("type");
            JSONObject responseObject = dataObject.getJSONObject("response");
            switch(type) {
                case "playerReady":
                    opponenentReady(responseObject);
                    break;
            }
        }
    }

    private class ChatSocketListener implements SocketConnection.DataReceivedListener {

        @Override
        public void onDataReceived(String data) {
            JSONObject messageObject = new JSONObject(data);
            String messageType = messageObject.getString("type");
            String messageContents = messageObject.getString("contents");
            if(messageType.equals("game")) {
                Log.gameLog().addMessage(new Log.Message(messageContents, Log.Message.Type.INFO));
            } else if(messageType.equals("chat")) {
                Log.chatLog().addMessage(new Log.Message(messageContents, Log.Message.Type.OPPONENT_MESSAGE));
            }
        }
    }
}
