package edu.bsu.css22.topboat.models;

public class FireEvent {
    private Weapon weapon;
    private Board.Tile target;

    public FireEvent(Board.Tile target, Weapon weapon) {
        this.target = target;
        this.weapon = weapon;
    }

    public Board.Tile getTarget() {
        return target;
    }

    public Weapon getWeapon() {
        if(weapon == null){
            weapon = new Weapon(Weapon.Type.SINGLESHOT);
        }
        return weapon;
    }
}
