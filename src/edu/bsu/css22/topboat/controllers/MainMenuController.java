package edu.bsu.css22.topboat.controllers;

import edu.bsu.css22.topboat.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
    @FXML Button singlePlayerSelect;
    @FXML Button multiplayerSelect;
    @FXML Button localSelect;
    @FXML Button connectSelect;
    @FXML TextField usernameInput;
    @FXML Button submitUsername;

    private Game game;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        singlePlayerSelect.setOnAction(new SinglePlayerSelectListener());
        multiplayerSelect.setOnAction(new MultiplayerSelectListener());
        localSelect.setOnAction(new LocalSelectListener());
        connectSelect.setOnAction(new ConnectSelectListener());
        submitUsername.setOnAction(new UsernameButtonListener());
    }

    private boolean handleNoUsername() {
        if(usernameInput.getText().equals("") || usernameInput.getText() == null) {
            submitUsername.setVisible(true);
            singlePlayerSelect.setDisable(true);
            multiplayerSelect.setDisable(true);
            localSelect.setDisable(true);
            connectSelect.setDisable(true);
            return true;
        } else {
            Game.player1.setName(usernameInput.getText());
            return false;
        }
    }

    private class SinglePlayerSelectListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            game = new SinglePlayerGame();
            if(handleNoUsername()) return;

            Game.startGame(game);
        }
    }

    private class MultiplayerSelectListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            localSelect.setVisible(!localSelect.isVisible());
            connectSelect.setVisible(!connectSelect.isVisible());
        }
    }

    private class LocalSelectListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            game = new LocalMultiplayerGame();
            handleNoUsername();
        }
    }

    private class ConnectSelectListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            UI.changeView(UI.Views.MULTI_SELECTION);
        }
    }

    private class UsernameButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            if(usernameInput.getText().equals("")) return;

            Game.player1.setName(usernameInput.getText());
            Game.startGame(game);
        }
    }

}
