package edu.bsu.css22.topboat.Util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class SocketConnection {
    public static final String DEFAULT_HOST = "107.191.44.5";

    private SocketLoop socketLoop = new SocketLoop();

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private SocketConnectedListener connectedListener;
    private DataReceivedListener dataListener;

    private JSONObject params = new JSONObject();

    public SocketConnection() {}

    public SocketConnection(Socket socket) {
        try {
            this.socket = socket;
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch(IOException e) {
            System.err.println("The socket connection could not be established");
            e.printStackTrace();
        }
    }

    public boolean connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch(IOException e) {
            System.err.println("The socket connection could not be established");
            e.printStackTrace();
            return false;
        }
        if(connectedListener != null) {
            connectedListener.onSocketConnected();
        }
        socketLoop.run();
        return true;
    }

    public void disconnect() {
        try {
            socketLoop.stop();
            socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void write(Object data) {
        out.println(data.toString());
    }

    public void writeParams() {
        write(params.toString());
    }

    public SocketConnection addParam(Object key, Object value) {
        params.put(key.toString(), value);
        return this;
    }

    public void onSocketConnected(SocketConnectedListener listener) {
        this.connectedListener = listener;
        if(socket.isConnected()) {
            listener.onSocketConnected();
        }
    }

    public void onDataReceived(DataReceivedListener listener) {
        this.dataListener = listener;
    }

    public interface SocketConnectedListener {
        void onSocketConnected();
    }

    public interface DataReceivedListener {
        void onDataReceived(String data);
    }

    private class SocketLoop {
        private Thread thread;
        private Runnable runnable = () -> {
            try {
                while(!Thread.currentThread().isInterrupted()) {
                    String data = in.readLine();
                    if (dataListener != null) {
                        dataListener.onDataReceived(data);
                    }
                }
            } catch(SocketException e) {
                if(!Thread.currentThread().isInterrupted()) {
                    System.err.println("The socket was closed unexpectedly");
                    e.printStackTrace();
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        };

        void run() {
            thread = new Thread(runnable);
            thread.start();
        }

        void stop() {
            thread.interrupt();
        }
    }
}