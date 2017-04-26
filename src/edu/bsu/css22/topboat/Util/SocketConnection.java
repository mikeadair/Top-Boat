package edu.bsu.css22.topboat.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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

    private HashMap<Object, Object> params = new HashMap<>();

    public SocketConnection() {}

    public SocketConnection(Socket socket) {
        this.socket = socket;
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
        socketLoop.run();
        return true;
    }

    public void disconnect() {
        try {
            socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void write(Object data) {
        out.println(data.toString());
    }

    public void writeParams() {
        StringBuilder paramBuilder = new StringBuilder("{");
        for (Map.Entry<Object, Object> param : params.entrySet()) {
            String key = param.getKey().toString();
            String value = param.getValue().toString();
            paramBuilder.append("\"" + key + "\" : \"" + value + "\",");
        }
        paramBuilder.replace(paramBuilder.lastIndexOf(","), paramBuilder.lastIndexOf(",") + 1, "");
        paramBuilder.append("}");
        write(paramBuilder.toString());
    }

    public SocketConnection addParam(Object key, Object value) {
        params.put(key, value);
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
                    System.out.println("data received");
                    if (dataListener != null) {
                        dataListener.onDataReceived(data);
                    }
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        };

        void run() {
            thread = new Thread(runnable);
            thread.start();
        }

        void end() {
            thread.interrupt();
        }
    }
}