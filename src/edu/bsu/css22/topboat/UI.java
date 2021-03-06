package edu.bsu.css22.topboat;

import edu.bsu.css22.topboat.Util.SocketConnection;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UI {
    private static Stage primaryStage;
    private static Initializable currentController;

    public static void initialize(Stage pStage) throws IOException {
        primaryStage = pStage;
        primaryStage.setTitle("Top Boat");
        changeView(Views.MAIN_MENU);
        primaryStage.show();
    }

    public static void changeView(UI.Views view) {
        try {
            FXMLLoader loader = new FXMLLoader(UI.class.getResource(view.resourcePath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            currentController = loader.getController();
        } catch (IOException e) {
            System.err.println("Unable to change views: caused by");
            e.printStackTrace();
        }
    }

    public static Initializable currentController() {
        return currentController;
    }

    public static enum Views {
        MAIN_MENU("views/main_menu.fxml"),
        MAIN_GAME("views/main.fxml"),
        MULTI_SELECTION("views/multi_select.fxml");

        Views(String path) {
            this.resourcePath = path;
        }

        String resourcePath;
    }
}
