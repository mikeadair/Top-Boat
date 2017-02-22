package cs222.topboat;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by michaeladair on 2/21/17.
 */
public class ViewController implements Initializable  {

        @FXML
        GridPane battle_grid;
        @FXML
        ScrollPane scrollpane;
        @FXML
        HBox root;
        @FXML
        ListView loglist;
        @Override
        public void initialize(URL location, ResourceBundle resources) {
            editUI();
        }

        private void editUI(){
            //No Horizontal Scroll on ScrollPane
            scrollpane.setFitToWidth(true);
            scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        }

}
