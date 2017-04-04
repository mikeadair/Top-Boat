import edu.bsu.css22.topboat.Game;
import edu.bsu.css22.topboat.SinglePlayerGame;
import edu.bsu.css22.topboat.UI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import junit.framework.Assert;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.io.IOException;

public class ArsenalTest extends GuiTest {

    @Override
    protected Parent getRootNode() {
        Parent parent = null;
        try {
            new SinglePlayerGame();
            Game.player1.setName("Kaleb");
            parent = FXMLLoader.load(getClass().getResource("views/arsenal.fxml"));
            UI.initialize(stage);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parent;
    }
    
    @Test
    public void testArsenalSelection() {
        ListView arsenalList = find("#weaponListView");
        click(arsenalList);
        Object firstChild = arsenalList.getItems().get(0);
        Object selectedItem = arsenalList.getSelectionModel().getSelectedItem();
        Assert.assertEquals(firstChild, selectedItem);
    }

    @Test
    public void testArsenal2Selection() {
        ListView arsenalList = find("#weaponListView");
        click(arsenalList);
        push(KeyCode.DOWN);
        Object firstChild = arsenalList.getItems().get(1);
        Object selectedItem = arsenalList.getSelectionModel().getSelectedItem();
        Assert.assertEquals(firstChild, selectedItem);
    }

    @Test
    public void testArsenal3Selection() {
        ListView arsenalList = find("#weaponListView");
        click(arsenalList);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        Object firstChild = arsenalList.getItems().get(2);
        Object selectedItem = arsenalList.getSelectionModel().getSelectedItem();
        Assert.assertEquals(firstChild, selectedItem);
    }

    @Test
    public void testArsenal4Selection() {
        ListView arsenalList = find("#weaponListView");
        click(arsenalList);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        Object firstChild = arsenalList.getItems().get(3);
        Object selectedItem = arsenalList.getSelectionModel().getSelectedItem();
        Assert.assertEquals(firstChild, selectedItem);
    }

    @Test
    public void testArsenal5Selection() {
        ListView arsenalList = find("#weaponListView");
        click(arsenalList);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        Object firstChild = arsenalList.getItems().get(4);
        Object selectedItem = arsenalList.getSelectionModel().getSelectedItem();
        Assert.assertEquals(firstChild, selectedItem);
    }
}
