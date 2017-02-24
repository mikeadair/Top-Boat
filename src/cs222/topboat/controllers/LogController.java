package cs222.topboat.controllers;

import cs222.topboat.models.Log;
import cs222.topboat.models.Log.Message;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class LogController implements Initializable{

    @FXML ListView loglist_game;
    @FXML ListView loglist_messages;

    Log gameLog;
    Log chatLog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeLogs();
        new Thread(new RandomMessageThread(), "RandomMessageThread").start();
    }

    private void initializeLogs() {
        gameLog = Log.gameLog();
        chatLog = Log.chatLog();

        gameLog.addMessageReceivedListener(newMessage -> {
            Text message = new Text(newMessage.getContents());
            message.setFill(newMessage.getColor());
            loglist_game.getItems().add(message);
        });

        chatLog.addMessageReceivedListener(newMessage -> {
            Text message = new Text(newMessage.getContents());
            message.setFill(newMessage.getColor());
            loglist_messages.getItems().add(message);
        });
    }

    private class RandomMessageThread implements Runnable {
        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    gameLog.addMessage(new Message("Server Message", Message.Type.SUCCESS));
                    Thread.sleep(3000);
                    chatLog.addMessage(new Message("Hello again, friend.", Message.Type.OPPONENT_MESSAGE));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
