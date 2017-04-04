import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.junit.Assert;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.io.IOException;

public class GameBoardTest extends GuiTest {
    @Override
    protected Parent getRootNode() {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("views/gameboard.fxml"));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parent;
    }

    @Test
    public void testPlayerTabSelection() {
        TabPane tabPane = find("#tabPane");
        click("#playerTab");

        Tab playerTab = tabPane.getTabs().get(0);
        Tab selectedTab = tabPane.getSelectionModel().selectedItemProperty().get();
        Assert.assertEquals(playerTab, selectedTab);
    }

    @Test
    public void testOpponentTabSelection() {
        TabPane tabPane = find("#tabPane");
        click("#opponentTab");

        Tab opponentTab = tabPane.getTabs().get(1);
        Tab selectedTab = tabPane.getSelectionModel().selectedItemProperty().get();
        Assert.assertEquals(opponentTab, selectedTab);
    }
}
