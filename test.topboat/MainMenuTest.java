import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import junit.framework.Assert;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.io.IOException;

public class MainMenuTest extends GuiTest {

    @Override
    protected Parent getRootNode() {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("views/main_menu.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parent;
    }

    @Test
    public void testButtons() {
        click("#multiplayerSelect");
        Assert.assertTrue((find("#localSelect")).isVisible());
        Assert.assertTrue((find("#connectSelect")).isVisible());
        click("#singlePlayerSelect");
        click("#usernameInput");
        type("Michael");
        Assert.assertTrue((find("#submitUsername")).isVisible());
    }
}
