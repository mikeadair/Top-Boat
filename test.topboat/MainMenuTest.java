import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.junit.*;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.io.IOException;

public class MainMenuTest extends GuiTest {

    @Override
    protected Parent getRootNode() {
        stage.centerOnScreen();
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("views/main_menu.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parent;
    }

    @Test
    public void verifyMultiOptionsAppear() {
        click("#multiplayerSelect");
        Assert.assertTrue((find("#localSelect")).isVisible());
        Assert.assertTrue((find("#connectSelect")).isVisible());
    }

    @Test
    public void verifySubmitAppears() {
        click("#singlePlayerSelect");
        Assert.assertTrue((find("#submitUsername").isVisible()));
    }

    @Test
    public void testButtons() {
        click("#singlePlayerSelect");
        click("#usernameInput");
        type("Michael");
        Assert.assertTrue((find("#singlePlayerSelect")).isDisabled());
    }
}
