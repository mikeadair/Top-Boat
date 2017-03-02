package cs222.topboat.models;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.HashMap;


public class Board {
    public static final int HEIGHT = 10;
    public static final int WIDTH = 10;

    private static final Board playerBoard = new Board();
    private static final Board opponentBoard = new Board();

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
        return playerBoard;
    }

    public static Board opponentBoard() {
        return opponentBoard;
    }

    public Tile get(int x, int y) {
        return tileMap[y][x];
    }

    public static class Tile extends StackPane {
        private static final Background BACKGROUND;
        private static final BackgroundFill BACKGROUND_FILL;
        static {
            CornerRadii cornerRadii = new CornerRadii(0);
            Insets insets = new Insets(0, 0, 0, 0);
            BACKGROUND_FILL = new BackgroundFill(Color.BLUE, cornerRadii, insets);
            BACKGROUND = new Background(BACKGROUND_FILL);
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
            addEventHandler(MouseEvent.MOUSE_CLICKED, event -> board.selectedTileProperty.set(this));
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
            name = (yValues.get(y) + x);
        }

        @Override
        public String toString() {
            return name;
        }

    }
}
