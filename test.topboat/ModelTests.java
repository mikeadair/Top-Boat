import edu.bsu.css22.topboat.Player;
import edu.bsu.css22.topboat.models.Board;
import edu.bsu.css22.topboat.models.Ship;
import org.testng.Assert;
import org.testng.annotations.Test;


public class ModelTests {
    Board player = Board.playerBoard();
    Ship carrier = new Ship(new Player(),Ship.Type.CARRIER,0,0);

    @Test public void testTileOccupied(){
        Assert.assertFalse(player.getTile(0,4).isOccupied());
    }

    @Test public void testTileName(){
        Assert.assertEquals(player.getTile(0,0).name.toString(),"A1");
    }

    @Test public void testShipLength(){
        Assert.assertEquals(carrier.type.length,5);
    }

    @Test public void testShipXCoords(){
        carrier.setX(5);
        Assert.assertEquals(carrier.getX(),5);
    }

    @Test public void testShipYCoords(){
        carrier.setY(2);
        Assert.assertNotSame(carrier.getX(),2);
    }
}
