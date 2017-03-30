package edu.bsu.css22.topboat;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.LogManager;

public class TopBoatApplication extends Application {

    public static void main(String[] args) {
        /* Supresses Warning from LogLists */
        LogManager.getLogManager().reset();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        UI.initialize(primaryStage);
    }
}
