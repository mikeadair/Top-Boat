package edu.bsu.css22.topboat;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UI {
    private static Stage primaryStage;

    public static void initialize(Stage pStage) throws IOException {
        primaryStage = pStage;
        primaryStage.setTitle("Top Boat");
        changeView(Views.MAIN_MENU);
        primaryStage.show();

    }

    public static void changeView(UI.Views view) {
        try {
            Parent root = FXMLLoader.load(UI.class.getResource(view.resourcePath));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            System.err.println("Unable to change views: caused by");
            e.printStackTrace();
        }
    }

    public static enum Views {
        MAIN_MENU("views/main_menu.fxml"),
        GAME_SELECTION(null),
        MAIN_GAME("views/main.fxml");

        Views(String path) {
            this.resourcePath = path;
        }

        String resourcePath;
    }
}
