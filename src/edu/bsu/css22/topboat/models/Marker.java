package edu.bsu.css22.topboat.models;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;

public class Marker extends Rectangle {
    FadeTransition animation;
    ArrayList<Marker> siblings = new ArrayList<>();

    public Marker() {
        super(30, 30, Color.WHITE);
        animation = new FadeTransition(Duration.millis(500), this);
        animation.setFromValue(1.0);
        animation.setToValue(0);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.setAutoReverse(true);
    }

    public void playAnimation() {
        animation.play();
    }

    public void cancelAnimation() {
        animation.stop();
    }

    public void addToTile(Board.Tile tile) {

    }

    public void removeFromTile() {
        
    }
}
