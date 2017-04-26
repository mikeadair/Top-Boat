package edu.bsu.css22.topboat.controllers;

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

    }
}
