import edu.bsu.css22.topboat.Player;

import edu.bsu.css22.topboat.models.Board;
import edu.bsu.css22.topboat.models.Ship;

import org.junit.Assert;
import org.junit.Test;


public class ModelTests {
    Board playerBoard = Board.playerBoard();
    Ship carrier = new Ship(new Player(),Ship.Type.CARRIER,0,0);


    @Test
    public void testShipCreation() {
        carrier = new Ship(new Player(), Ship.Type.CARRIER, -1, -1);
        Assert.assertEquals(carrier.name, Ship.Type.CARRIER.name());
    }

    @Test
    public void testAddShipToTIle(){
        Board.Tile tile = playerBoard.getTile(0, 0);
        tile.ship = carrier;
        Assert.assertTrue(tile.isOccupied());
    }

    @Test public void testTileName(){
        Assert.assertEquals(playerBoard.getTile(0,0).name.toString(),"A1");
    }

    @Test public void testShipLength(){
        Assert.assertEquals(carrier.getLength(), 5);
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
