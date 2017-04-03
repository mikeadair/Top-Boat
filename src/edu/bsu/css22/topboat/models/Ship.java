package edu.bsu.css22.topboat.models;

import edu.bsu.css22.topboat.Player;
import javafx.scene.image.Image;

public class Ship {
    private static final Image[] CARRIER_IMAGES = {
            new Image(Ship.class.getResourceAsStream("../images/carrier-1.png")),
            new Image(Ship.class.getResourceAsStream("../images/carrier-2.png")),
            new Image(Ship.class.getResourceAsStream("../images/carrier-3.png")),
            new Image(Ship.class.getResourceAsStream("../images/carrier-4.png")),
            new Image(Ship.class.getResourceAsStream("../images/carrier-5.png"))
    };
    private static final Image[] SHIP_IMAGES = {
            new Image(Ship.class.getResourceAsStream("../images/ship-front.png")),
            new Image(Ship.class.getResourceAsStream("../images/ship-middle.png")),
            new Image(Ship.class.getResourceAsStream("../images/ship-middle.png")),
            new Image(Ship.class.getResourceAsStream("../images/ship-back.png"))
    };
    private static final Image[] SUB_IMAGES = {
            new Image(Ship.class.getResourceAsStream("../images/sub-front.png")),
            new Image(Ship.class.getResourceAsStream("../images/sub-middle.png")),
            new Image(Ship.class.getResourceAsStream("../images/sub-back.png"))
    };

    private Player player;
    private int x;
    private int y;
    public Type type;
    public String name;
    public Orientation orientation;
    private int sectionsRemaining;

    public Ship(Player player, Type type, int x, int y) {
        this.player = player;
        this.type = type;
        this.name = type.name();
        this.x = x;
        this.y = y;
        this.orientation = null;
        this.sectionsRemaining = type.length;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getLength() {
        return type.length;
    }

    public Image getImageForIndex(int index) {
        if(index == 0) {
            return type.images[0];
        } else if(index == type.length-1) {
            return type.images[type.images.length-1];
        } else {
            return type.images[index];
        }
    }

    public boolean hit() {
        System.out.println(name + " hit. current life: " + sectionsRemaining + ". new life: " + (sectionsRemaining-1));
        sectionsRemaining -= 1;
        if(sectionsRemaining == 0) {
            return true;
        }
        return false;
    }

    public enum Type {
        CARRIER(5, CARRIER_IMAGES),
        BATTLESHIP(4, SHIP_IMAGES),
        CRUISER(3, SHIP_IMAGES),
        SUBMARINE(3, SUB_IMAGES),
        DESTROYER(2, SHIP_IMAGES);

        Type(int l, Image[] images) {
            this.length = l;
            this.images = images;
        }

        public int length;

        Image[] images;
    }

    public enum Orientation {
        UP(0, -1),
        LEFT(-1, 0),
        RIGHT(1, 0),
        DOWN(0, 1);

        Orientation(int xM, int yM) {
            xMod = xM;
            yMod = yM;
        }

        public int xMod;
        public int yMod;
    }
}
