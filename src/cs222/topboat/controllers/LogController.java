package cs222.topboat.controllers;

import com.sun.org.apache.xml.internal.security.Init;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by michaeladair on 2/21/17.
 */
public class LogController implements Initializable{

    @FXML
    ScrollPane sp_game;
    @FXML
    ScrollPane sp_messages;
    @FXML
    ListView loglist_game;
    @FXML
    ListView loglist_messages;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configure();
    }

    private void configure(){
        //No Horizontal Scroll on ScrollPane
        sp_game.setFitToWidth(true);
        sp_game.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp_messages.setFitToWidth(true);
        sp_messages.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }
}
