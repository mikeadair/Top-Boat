package edu.bsu.css22.topboat.models;

import javafx.scene.image.Image;

public class Ship {
    private static final Image[] SHIP_IMAGES = {
            new Image(Ship.class.getResourceAsStream("../images/ship-front.png")),
            new Image(Ship.class.getResourceAsStream("../images/ship-middle.png")),
            new Image(Ship.class.getResourceAsStream("../images/ship-back.png"))
    };
    private static final Image[] SUB_IMAGES = {
            new Image(Ship.class.getResourceAsStream("../images/sub-front.png")),
            new Image(Ship.class.getResourceAsStream("../images/sub-middle.png")),
            new Image(Ship.class.getResourceAsStream("../images/sub-back.png"))
    };

    private int x;
    private int y;
    public Type type;
    public String name;
    public Orientation orientation;

    public Ship(Type type, int x, int y) {
        this.type = type;
        this.name = type.name();
        this.x = x;
        this.y = y;
        this.orientation = null;
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
            return type.images[2];
        } else {
            return type.images[1];
        }
    }


    public enum Type {
        CARRIER(5, SHIP_IMAGES),
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
