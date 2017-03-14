package edu.bsu.css22.topboat.models;

public class Ship {
    private int x;
    private int y;
    public Type type;
    public Orientation orientation;

    public Ship(Type type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
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
        CARRIER(5),
        BATTLESHIP(4),
        CRUISER(3),
        SUBMARINE(3),
        DESTROYER(2);

        Type(int l) {
            this.length = l;
        }

        public int length;
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
