package edu.bsu.css22.topboat.controllers;

import edu.bsu.css22.topboat.Game;
import edu.bsu.css22.topboat.models.Log;
import edu.bsu.css22.topboat.models.Log.Message;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

import java.applet.Applet;
import java.applet.AudioClip;
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
    public static final AudioClip game_message = Applet.newAudioClip(LogController.class.getResource("/edu/bsu/css22/topboat/sounds/game_message.wav"));
    public static final AudioClip player_message = Applet.newAudioClip(LogController.class.getResource("/edu/bsu/css22/topboat/sounds/player_message.wav"));

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
        Game.emitPlayerMessage(new Log.Message(Game.player1.getName() + ": " + messageField.getText(), Message.Type.PLAYER_MESSAGE));
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
                game_message.play();
                notifyTab(game_tab);
            }
            if (log == loglist_messages && currentTab.getValue() != messages_tab) {
                player_message.play();
                notifyTab(messages_tab);
            }
        });
    }
}
