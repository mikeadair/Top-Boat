package edu.bsu.css22.topboat.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewController implements Initializable  {

        @FXML HBox root;
        @FXML LogController logController;
        @FXML GameBoardController gameboardController;
        @FXML ArsenalController arsenalController;

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }

        public LogController logController() {
                return logController;
        }

        public GameBoardController gameBoardController() {
                return gameboardController;
        }
        public ArsenalController arsenalController() {
                return arsenalController;
        }
}
