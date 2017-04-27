package edu.bsu.css22.topboat.controllers;

import edu.bsu.css22.topboat.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    @FXML TextField usernameInput;

    private Game game;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        singlePlayerSelect.setOnAction(new SinglePlayerSelectListener());
        multiplayerSelect.setOnAction(new MultiplayerSelectListener());
        usernameInput.textProperty().addListener(new TextFieldListener());
    }


    private class SinglePlayerSelectListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            game = new SinglePlayerGame();
            Game.startGame(game);
        }
    }

    private class MultiplayerSelectListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            UI.changeView(UI.Views.MULTI_SELECTION);
        }
    }

    private class TextFieldListener implements ChangeListener<String> {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            Game.player1.setName(newValue);
            if(newValue == null || newValue.equals("")) {
                singlePlayerSelect.setDisable(true);
                multiplayerSelect.setDisable(true);
            } else {
                singlePlayerSelect.setDisable(false);
                multiplayerSelect.setDisable(false);
            }
        }
    }
}
