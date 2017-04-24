package edu.bsu.css22.topboat.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketConnection {
    private static final String HOST = "45.32.170.120";

    private SocketLoop socketLoop = new SocketLoop();

    private  Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private SocketCommunicationListener listener;

    public boolean connect(int port) {
        try {
            socket = new Socket(HOST, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch(IOException e) {
            System.err.println("The socket connection could not be established");
            e.printStackTrace();
            return false;
        }
        socketLoop.run();
        return true;
    }

    public void onDataReceived(SocketCommunicationListener listener) {
        this.listener = listener;
    }

    public interface SocketCommunicationListener {
        void onDataReceived(String data);
    }

    private class SocketLoop {
        private Thread thread;
        private Runnable runnable = () -> {
            try {
                String data = in.readLine();
                if(listener != null) {
                    listener.onDataReceived(data);
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        };

        void run() {
            thread = new Thread(runnable);
            thread.start();
        }
    }
}