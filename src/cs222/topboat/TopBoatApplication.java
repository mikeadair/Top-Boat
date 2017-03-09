package cs222.topboat;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class TopBoatApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        UI.initialize(primaryStage);
    }
}
