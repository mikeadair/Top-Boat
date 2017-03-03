package cs222.topboat.models;

import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
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
    public Tile selectedTile;
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

    public static class Tile extends StackPane {
        private final Background BACKGROUND;
        private final BackgroundImage BACKGROUND_IMAGE;
        Image image = new Image(getClass().getResourceAsStream("../images/ocean.png"));
        {
            BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
            BACKGROUND_IMAGE = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
            BACKGROUND = new Background(BACKGROUND_IMAGE);
        }

        private Board board;
        private int x;
        private int y;
        public TileName name;

        private Tile(Board board, int x, int y) {
            setBackground(BACKGROUND);
            this.board = board;
            this.x = x;
            this.y = y;
            this.name = new TileName(x, y);

            addEventHandler(MouseEvent.MOUSE_ENTERED, event -> board.hoverTileProperty.set(this));
            addEventHandler(MouseEvent.MOUSE_EXITED, event -> board.hoverTileProperty.set(null));
            addEventHandler(MouseEvent.MOUSE_CLICKED, event -> board.selectedTile = this);
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
