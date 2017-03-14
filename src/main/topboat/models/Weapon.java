package main.topboat.models;

public class Weapon {
    public int[][] values;
    public String name;

    public Weapon(int[][] values, String name){
        this.values = values;
        this.name = name;
    }

    public enum Type {
        SINGLESHOT(new Weapon(new int[][]{{0,0}}, "Single Shot")),
        CROSSSHOT(new Weapon(new int[][]{{0,0},{0,1},{0,-1},{1,0},{-1,0}}, "Cross Shot"));

        Type(Weapon aoe) { this.affectedTiles = aoe; }

        public Weapon affectedTiles;
    }
}
