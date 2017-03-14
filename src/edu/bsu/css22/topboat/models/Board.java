package edu.bsu.css22.topboat.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.HashMap;


public class Board {
    public static final int HEIGHT = 10;
    public static final int WIDTH = 10;

    private static Board playerBoard = new Board();
    private static Board opponentBoard = new Board();

    private Tile[][] tileMap;
    public SimpleObjectProperty<Tile> selectedTileProperty = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Tile> hoverTileProperty = new SimpleObjectProperty<>();

    private Board() {
        initTileMap();
    }

    private void initTileMap() {
        tileMap = new Tile[HEIGHT][WIDTH];
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                tileMap[y][x] = new Tile(this, x, y);
            }
        }
    }

    public static Board playerBoard() {
        if(playerBoard == null) {
            playerBoard = new Board();
        }
        return playerBoard;
    }

    public static Board opponentBoard() {
        if(opponentBoard == null) {
            opponentBoard = new Board();
        }
        return opponentBoard;
    }

    public Tile get(int x, int y) {
        return tileMap[y][x];
    }

    public boolean isValidPlacementTile(Ship ship) {
        if(tileMap[ship.getY()][ship.getX()].occupied.get()) {
            return false;
        }
        for(Ship.Orientation orientation : Ship.Orientation.values()) {
            ship.orientation = orientation;
            if(validateShipOrientation(ship)) {
                return true;
            }
        }
        return false;
    }

    public boolean validateShipOrientation(Ship ship) {
        for(int i = 1; i < ship.type.length; i++) {
            int newX = ship.getX() + (ship.orientation.xMod * i);
            int newY = ship.getY() + (ship.orientation.yMod * i);

            try {
                if (tileMap[newY][newX].occupied.get()) {
                    return false;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                return false;
            }
        }
        return true;
    }

    public void occupyTilesWithShip(Ship ship, Ship.Orientation oldOrientation) {
        System.out.println("placing tiles for " + ship.type.name());
        if(oldOrientation != null) {
            System.out.println("removing tiles for previous orientation: " + oldOrientation);
            for(int i = 0; i < ship.type.length; i++) {
                int oldX = ship.getX() + (oldOrientation.xMod * i);
                int oldY = ship.getY() + (oldOrientation.yMod * i);

                tileMap[oldY][oldX].imageProperty.set(null);
            }
        }
        for(int i = 0; i < ship.type.length; i++) {
            int newX = ship.getX() + (ship.orientation.xMod * i);
            int newY = ship.getY() + (ship.orientation.yMod * i);

            Tile tile = tileMap[newY][newX];
            tile.shipOrientation = ship.orientation;
            if(i == 0) {
                tile.imageProperty.set(Tile.FRONT_IMAGE);
            } else if(i == ship.type.length-1) {
                tile.imageProperty.set(Tile.BACK_IMAGE);
            } else {
                tile.imageProperty.set(Tile.MIDDLE_IMAGE);
            }
        }
    }


    public static class Tile extends StackPane {
        private static final Background OCEAN_BACKGROUND;
        static {
            CornerRadii radii = new CornerRadii(0);
            Insets insets = new Insets(0);
            BackgroundFill oceanFill = new BackgroundFill(Color.rgb(33, 103, 182), radii, insets);
            OCEAN_BACKGROUND = new Background(oceanFill);
        }
        public static final Image FRONT_IMAGE = new Image(Tile.class.getResourceAsStream("../images/ship-front.png"));
        public static final Image MIDDLE_IMAGE = new Image(Tile.class.getResourceAsStream("../images/ship-middle.png"));
        public static final Image BACK_IMAGE = new Image(Tile.class.getResourceAsStream("../images/ship-back.png"));

        private Board board;
        public int x;
        public int y;
        public SimpleBooleanProperty occupied = new SimpleBooleanProperty(false);
        private SimpleObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
        private ImageView imageView = new ImageView();
        private Ship.Orientation shipOrientation;
        public TileName name;



        private Tile(Board board, int x, int y) {
            setBackground(OCEAN_BACKGROUND);
            this.board = board;
            this.x = x;
            this.y = y;
            this.name = new TileName(x, y);

            addEventHandler(MouseEvent.MOUSE_ENTERED, event -> board.hoverTileProperty.set(this));
            addEventHandler(MouseEvent.MOUSE_EXITED, event -> board.hoverTileProperty.set(null));
            addEventHandler(MouseEvent.MOUSE_CLICKED, event -> board.selectedTileProperty.set(this));

            imageProperty.addListener((observable, oldImage, newImage) -> {
                if(newImage == null) {
                    getChildren().remove(imageView);
                } else {
                    imageView.setImage(newImage);
                    switch (shipOrientation) {
                        case UP:
                            imageView.setRotate(180);
                            imageView.fitHeightProperty().bind(this.heightProperty());
                            break;
                        case DOWN:
                            imageView.setRotate(0);
                            imageView.fitHeightProperty().bind(this.heightProperty());
                            break;
                        case LEFT:
                            imageView.setRotate(90);
                            imageView.fitHeightProperty().bind(this.widthProperty());
                            break;
                        case RIGHT:
                            imageView.setRotate(270);
                            imageView.fitHeightProperty().bind(this.widthProperty());
                            break;
                    }
                    getChildren().add(imageView);
                }
            });
        }
    }

    public static class TileName {
        private static final HashMap<Integer, String> yValues;
        static {
            yValues = new HashMap<>();
            char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
            for (int i = 0; i < HEIGHT; i++) {
                String value = String.valueOf(alphabet[i]);
                if (i > alphabet.length) {
                    value = value + i;
                }
                yValues.put(i, value);
            }
        }

        private String name;

        private TileName(int x, int y) {
            name = (yValues.get(y) + (x + 1));
        }

        @Override
        public String toString() {
            return name;
        }

    }
}
