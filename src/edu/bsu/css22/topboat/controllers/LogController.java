package edu.bsu.css22.topboat.controllers;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import edu.bsu.css22.topboat.models.Log;
import edu.bsu.css22.topboat.models.Log.Message;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class LogController implements Initializable{

    @FXML ListView loglist_game;
    @FXML ListView loglist_messages;
    @FXML Button sendButton;
    @FXML TextField messageField;
    @FXML TabPane logs;
    @FXML Tab game_tab;
    @FXML Tab messages_tab;

    Log gameLog;
    Log chatLog;
    public static final AudioClip game_message = Applet.newAudioClip(LogController.class.getResource("../sounds/game_message.wav"));
    public static final AudioClip player_message = Applet.newAudioClip(LogController.class.getResource("../sounds/player_message.wav"));

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeLogs();
    }

    private void initializeLogs() {
        gameLog = Log.gameLog();
        chatLog = Log.chatLog();

        gameLog.addMessageReceivedListener(newMessage -> showMessageInLog(loglist_game, newMessage));
        chatLog.addMessageReceivedListener(newMessage -> showMessageInLog(loglist_messages, newMessage));
        sendButton.setOnAction(event -> sendPlayerMessage());
        logs.getSelectionModel().selectedItemProperty().addListener((observable,oldSelected,newSelected)->{
            removeTabNotify(newSelected);
        });
        messageField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                sendPlayerMessage();
            }
        });
    }

    private void sendPlayerMessage(){
        chatLog.sendMessage(messageField.getText());
        messageField.setText("");
    }

    private void removeTabNotify(Tab tab){
        tab.setStyle(null);
    }

    private void notifyTab(Tab tab){
        tab.setStyle("-fx-background-color: #71DE9B;");
    }

    private void showMessageInLog(ListView log, Message newMessage) {
        Text messageView = new Text(newMessage.getContents());
        messageView.setFill(newMessage.getColor());
        Platform.runLater(() -> {
            log.getItems().add(messageView);
            log.scrollTo(log.getItems().toArray().length);
            ReadOnlyObjectProperty<Tab> currentTab = logs.getSelectionModel().selectedItemProperty();
            if (log == loglist_game && currentTab.getValue() != game_tab) {
                System.out.println("GAME LOG");
                game_message.play();
                notifyTab(game_tab);
            }
            if (log == loglist_messages && currentTab.getValue() != messages_tab) {
                System.out.println("MESSAGES LOG");
                player_message.play();
                notifyTab(messages_tab);
            }
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
