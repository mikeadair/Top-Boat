package edu.bsu.css22.topboat.Util;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketConnection {
    private Socket socket;

    public SocketConnection() throws IOException {
        String ip = "107.191.44.5";
        int port = 5000;

        boolean status = connect(ip, port);

        if(!status) {
            System.out.println("Could not connect");
        }else{
            echo("hi");
        }
    }

    private boolean connect(String ip, int port) {
        try {
            this.socket = new Socket(ip, port);
            socket.setKeepAlive(true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void echo(String message) throws IOException {
        PrintWriter writer = new PrintWriter(getSocket().getOutputStream(),true);
        writer.write(message);
    }

    private Socket getSocket() {
        return socket;
    }
}