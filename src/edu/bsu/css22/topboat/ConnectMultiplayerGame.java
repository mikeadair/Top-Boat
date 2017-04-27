package edu.bsu.css22.topboat;

import edu.bsu.css22.topboat.Util.SocketConnection;
import edu.bsu.css22.topboat.models.Board;
import javafx.application.Platform;

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
    void start() {
    }

    @Override
    void finish() {

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

        private void hostServer() {
            thread = new Thread(() -> {
                try {
                    serverSocket = new ServerSocket(CHAT_PORT);
                    chatSocket = new SocketConnection(serverSocket.accept());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }

        private void connectToHost(String host) {
            chatSocket = new SocketConnection();
            chatSocket.connect(host, CHAT_PORT);
        }
    }
}
