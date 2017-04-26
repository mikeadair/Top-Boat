package edu.bsu.css22.topboat.controllers;

import edu.bsu.css22.topboat.UI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class MultiSelectController implements Initializable{
    @FXML Button backButton;
    @FXML Button hostButton;
    @FXML Button joinButton;
    @FXML ListView playerListView;
    @FXML Text statusText;
    @FXML ProgressIndicator progressIndicator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backButton.setOnAction(new BackButtonListener());
        hostButton.setOnAction(new HostButtonListener());
        joinButton.setOnAction(new JoinButtonListener());
    }

    private class BackButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            UI.changeView(UI.Views.MAIN_MENU);
        }
    }

    private class HostButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            //Setup Host
        }
    }

    private class JoinButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            //Check if playerListView has selected then attempt to join
        }
    }
}
