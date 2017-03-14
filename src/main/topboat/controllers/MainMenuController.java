package main.topboat.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import main.topboat.*;

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
        boolean gameStartHandled = false;
        if(usernameInput.getText().equals("") || usernameInput.getText() == null) {
            submitUsername.setVisible(true);
            singlePlayerSelect.setDisable(true);
            multiplayerSelect.setDisable(true);
            localSelect.setDisable(true);
            connectSelect.setDisable(true);

            gameStartHandled = true;

            Game.startGame(game);
        }
        return gameStartHandled;
    }




    private class SinglePlayerSelectListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            game = new SinglePlayerGame();
            boolean gameStarted = handleNoUsername();
            if(!gameStarted) {
                Game.startGame(game);
            }
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
            game = new ConnectMultiplayerGame();
            handleNoUsername();
        }
    }

    private class UsernameButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            if (handleNoUsername() == false){
                UI.changeView(UI.Views.MAIN_GAME);
            }
        }
    }

}
