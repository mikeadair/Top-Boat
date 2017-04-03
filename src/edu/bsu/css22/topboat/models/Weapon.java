package edu.bsu.css22.topboat.models;

public class Weapon {
    public static final int INFINITE_AMMO = -1;
    private Type type;
    private int shotsRemaining;

    public Weapon(Type type){
        this.type = type;
        this.shotsRemaining = type.initialQuantity;
    }

    public String getName() {
        return type.name;
    }

    public int getShotsRemaining() {
        return shotsRemaining;
    }

    public int[][] getAffectedTiles() {
        return type.affectedTiles;
    }

    public void fireWeapon() {
        if(this.shotsRemaining != INFINITE_AMMO){
            this.shotsRemaining--;
        }
    }

    public enum Type {
        SINGLESHOT(new int[][]{{0,0}}, "Single Shot", INFINITE_AMMO),
        CROSS_SHOT(new int[][]{{0,0},{0,1},{0,-1},{1,0},{-1,0}}, "Cross Shot", 1),
        H_LINE_SHOT(new int[][]{{0,0},{0,1},{0,2},{0,-1},{0,-2}}, "H Line Shot", 1),
        V_LINE_SHOT(new int[][]{{0,0},{1,0},{2,0},{-1,0},{-2,0}}, "V Line Shot", 1),
        NUKE(new int[][]{{-2,-2},{-2,-1},{-2,0},{-2,1},{-2,2},{-1,-2},{-1,-1},{-1,0},{-1,1},{-1,2},{0,-2},{0,-1},{0,0},{0,1},{0,2},{1,-2},{1,-1},{1,0},{1,1},{1,2},{2,-2},{2,-1},{2,0},{2,1},{2,2}}, "Nuke", 1);

        Type(int[][] affectedTiles, String name, int quantity){
            this.affectedTiles = affectedTiles;
            this.name = name;
            this.initialQuantity = quantity;
        }

        private int[][] affectedTiles;
        private String name;
        private int initialQuantity;
    }
}
