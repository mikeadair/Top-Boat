package edu.bsu.css22.topboat.models;

import javafx.scene.image.Image;

import java.util.Random;

public class Drop {
    private int x;
    private int y;
    private boolean opened = false;

    private static final Image DROP_IMAGE[] = {
            new Image(Drop.class.getResourceAsStream("../images/box-red.png")),
            new Image(Drop.class.getResourceAsStream("../images/box-blue.png"))
    };

    private static final Random random = new Random();

    public Drop(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Image getImage(){
        return DROP_IMAGE[random.nextInt(DROP_IMAGE.length)];
    }

    public Weapon.Type openDrop(){
        opened = true;
        if(random.nextBoolean()){
            //Give Weapon
            return Weapon.Type.values()[random.nextInt(Weapon.Type.values().length)];
        }else{
            //Do Bad Stuff
            return null;
        }
    }
}
