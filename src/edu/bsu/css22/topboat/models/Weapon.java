package edu.bsu.css22.topboat.models;

public class Weapon {
    public Type type;

    public Weapon(Type type){
        this.type = type;
    }

    public enum Type {
        SINGLESHOT(new int[][]{{0,0}},"Single Shot",-1),
        CROSS_SHOT(new int[][]{{0,0},{0,1},{0,-1},{1,0},{-1,0}},"Cross Shot",0),
        H_LINE_SHOT(new int[][]{{0,0},{0,1},{0,2},{0,-1},{0,-2}}, "H Line Shot",0),
        V_LINE_SHOT(new int[][]{{0,0},{1,0},{1,0},{-1,0},{-2,0}}, "V Line Shot",0);

        Type(int[][] affectedTiles, String name, int quantity){
            this.affectedTiles = affectedTiles;
            this.name = name;
            this.quantity = quantity;
        }

        public int[][] affectedTiles;
        public String name;
        public int quantity;
    }
}
