import edu.bsu.css22.topboat.Game;
import edu.bsu.css22.topboat.SinglePlayerGame;
import edu.bsu.css22.topboat.UI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.io.IOException;

public class LogTest extends GuiTest {
    @Override
    protected Parent getRootNode() {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("views/log.fxml"));
            UI.initialize(stage);
            stage.centerOnScreen();
            new SinglePlayerGame();
            Game.player1.setName("Kaleb");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parent;
    }

    @Test
    public void testSendMessage() {
        click("#messageField");
        type("this is a message");
        click("#sendButton");
        click("#messages_tab");
    }
}
