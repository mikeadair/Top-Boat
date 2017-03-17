package edu.bsu.css22.topboat.models;

public class Ship {
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


    public enum Type {
        CARRIER(5, "ship"),
        BATTLESHIP(4, "ship"),
        CRUISER(3, "ship"),
        SUBMARINE(3, "sub"),
        DESTROYER(2, "ship");

        Type(int l, String imageType) {
            this.length = l;
            this.imageType = imageType;
        }

        public int length;
        public String imageType;
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

        int xMod;
        int yMod;
    }
}
