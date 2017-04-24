import edu.bsu.css22.topboat.Game;
import edu.bsu.css22.topboat.SinglePlayerGame;
import edu.bsu.css22.topboat.UI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import org.junit.Assert;
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
        click("#messages_tab");
        for(int i = 0; i < 2; i++){
            click("#messageField");
            type("Sending test message.");
            click("#sendButton");
        }
        Assert.assertEquals((((ListView) find("#loglist_messages")).getItems().size()),2);
    }

    @Test
    public void testTabs() {
        click("#messages_tab");
        Assert.assertTrue(((ListView) find("#loglist_messages")).isVisible());
    }
}
