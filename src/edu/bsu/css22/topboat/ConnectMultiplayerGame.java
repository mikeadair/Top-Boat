package edu.bsu.css22.topboat;

import edu.bsu.css22.topboat.Util.SocketConnection;

public class ConnectMultiplayerGame extends Game {
    SocketConnection gameSocket;
    SocketConnection chatSocket;

    @Override
    void init() {
        gameSocket = new SocketConnection();
        chatSocket = new SocketConnection();

        boolean gameSocketConnected = gameSocket.connect(1000);
        boolean chatSocketConnected = chatSocket.connect(1001);

        gameSocket.onDataReceived(data -> {
            System.out.println("game socket received " + data);
        });

        chatSocket.onDataReceived(data -> {
            System.out.println("chat socket received " + data);
        });
    }

    @Override
    void start() {

    }

    @Override
    void finish() {

    }
}
