package cs222.topboat.controllers;

import cs222.topboat.models.Log;
import cs222.topboat.models.Log.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
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

        gameLog.addMessageReceivedListener(newMessage -> showMessageInLog(loglist_game, newMessage));
        chatLog.addMessageReceivedListener(newMessage -> showMessageInLog(loglist_messages, newMessage));
    }

    private void showMessageInLog(ListView log, Message newMessage) {
        Text messageView = new Text(newMessage.getContents());
        messageView.setFill(newMessage.getColor());

        if(Platform.isFxApplicationThread()) {
            log.getItems().add(messageView);
        } else {
            Platform.runLater(() -> log.getItems().add(messageView));
        }
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
