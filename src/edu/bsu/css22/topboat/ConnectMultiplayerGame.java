package edu.bsu.css22.topboat;

import edu.bsu.css22.topboat.Util.SocketConnection;
import edu.bsu.css22.topboat.models.Board;
import edu.bsu.css22.topboat.models.Log;
import javafx.application.Platform;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;

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

        player2 = new ConnectedPlayer();

        player1.setBoard(Board.playerBoard());
        player2.setBoard(Board.opponentBoard());
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
        chatServer.emitMessage("game", message);
    }

    @Override
    void handlePlayerMessage(Log.Message message) {
        chatServer.emitMessage("chat", message);
    }

    private class GameServer {
        private static final int GAME_PORT = 5000;
        private Thread thread;
        private ServerSocket serverSocket;
        private SocketConnection gameSocket;
        private boolean isHost;

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
                gameSocket.disconnect();
                if (isHost) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {}
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
            gameSocket = new SocketConnection(serverSocket.accept());
            Platform.runLater(() -> Game.startGame(ConnectMultiplayerGame.this));
        }

        private void connectToHost(String host) {
            gameSocket = new SocketConnection();
            gameSocket.connect(host, GAME_PORT);
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
                chatSocket.disconnect();
                if (isHost) {
                    serverSocket.close();
                }
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

    private class ChatSocketListener implements SocketConnection.DataReceivedListener {

        @Override
        public void onDataReceived(String data) {
            JSONObject messageObject = new JSONObject(data);
            String messageType = messageObject.getString("type");
            String messageContents = messageObject.getString("contents");
            if(messageType.equals("game")) {
                Log.chatLog().addMessage(new Log.Message(messageContents, Log.Message.Type.INFO));
            } else if(messageType.equals("chat")) {
                Log.chatLog().addMessage(new Log.Message(messageContents, Log.Message.Type.OPPONENT_MESSAGE));
            }
        }
    }
}
