package edu.bsu.css22.topboat.controllers;

import edu.bsu.css22.topboat.Game;
import edu.bsu.css22.topboat.Util.ShipPlacementHandler;
import edu.bsu.css22.topboat.models.Board;
import edu.bsu.css22.topboat.models.Log;
import edu.bsu.css22.topboat.models.Ship;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameBoardController implements Initializable {
    @FXML TabPane tabPane;
    @FXML Tab playerTab;
    @FXML Tab opponentTab;
    @FXML GridPane playerGrid;
    @FXML GridPane opponentGrid;
    @FXML Button fireButton;
    @FXML Text selectedTileText;

    private Board selectedBoard;
    private HoverTileListener hoverTileListener = new HoverTileListener();

    private static final ShipPlacementListener SHIP_PLACEMENT_LISTENER = new ShipPlacementListener();

    private static final ChangeListener<Board.Tile> MAIN_TILE_LISTENER = (observable, oldTile, newTile) -> {

    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTabPane();
        initGameBoards();
        fireButton.setOnAction(event -> {
            Board.Tile targetTile = Board.opponentBoard().selectedTileProperty.get();
            if (targetTile.isOccupied()){
                //TODO: fire weapon functionality
            }

        });
    }

    private void initTabPane() {
        Tab playerGridTab = tabPane.getTabs().get(0);
        selectedBoard = Board.playerBoard();
        fireButton.setVisible(false);
        Board.playerBoard().hoverTileProperty.addListener(hoverTileListener);

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
           if (newTab == playerGridTab) {
               selectedBoard = Board.playerBoard();
               fireButton.setVisible(false);
               selectedTileText.setText("");
               Board.playerBoard().hoverTileProperty.addListener(hoverTileListener);
           } else {
               selectedBoard = Board.opponentBoard();
               fireButton.setVisible(true);
               selectedTileText.setText("");
               Board.opponentBoard().hoverTileProperty.addListener(hoverTileListener);
           }
        });
    }

    private void initGameBoards() {
        initBoard(playerGrid, Board.playerBoard());
        initBoard(opponentGrid, Board.opponentBoard());
    }

    private void initBoard(GridPane grid, Board board) {
        for(int y = 0; y < Board.HEIGHT; y++) {
            for(int x = 0; x < Board.WIDTH; x++) {
                Board.Tile tile = board.getTile(x, y);
                grid.add(tile, x, y);
            }
        }
    }

    public void startShipPlacement() {
        Board.playerBoard().selectedTileProperty.addListener(SHIP_PLACEMENT_LISTENER);
        opponentTab.setDisable(true);
        tabPane.setOnKeyPressed(event -> {
            switch(event.getCode()) {
                case UP:
                    SHIP_PLACEMENT_LISTENER.orientation.set(Ship.Orientation.UP);
                    event.consume();
                    break;
                case DOWN:
                    SHIP_PLACEMENT_LISTENER.orientation.set(Ship.Orientation.DOWN);
                    event.consume();
                    break;
                case LEFT:
                    SHIP_PLACEMENT_LISTENER.orientation.set(Ship.Orientation.LEFT);
                    event.consume();
                    break;
                case RIGHT:
                    SHIP_PLACEMENT_LISTENER.orientation.set(Ship.Orientation.RIGHT);
                    event.consume();
                    break;
                case ENTER:
                    SHIP_PLACEMENT_LISTENER.confirmPlacement();
                    event.consume();
                    break;
            }
        });
    }

    private static void endShipPlacement() {
        Board.playerBoard().selectedTileProperty.removeListener(SHIP_PLACEMENT_LISTENER);
    }

    public void startGameFunctionality() {
        Board.playerBoard().selectedTileProperty.addListener(MAIN_TILE_LISTENER);
        opponentTab.setDisable(false);
    }

    private class HoverTileListener implements ChangeListener<Board.Tile> {
        @Override
        public void changed(ObservableValue observable, Board.Tile oldValue, Board.Tile newValue) {
            if (newValue == null) {
                try {
                    selectedTileText.setText(selectedBoard.selectedTileProperty.get().name.toString());
                } catch (NullPointerException e) {
                    selectedTileText.setText("");
                }
            } else {
                selectedTileText.setText(newValue.name.toString());
            }
        }
    }

    private static class ShipPlacementListener implements ChangeListener<Board.Tile> {
        SimpleObjectProperty<Ship.Orientation> orientation = new SimpleObjectProperty<>();
        ShipPlacementHandler shipPlacementHandler = new ShipPlacementHandler(Game.player1) {
            @Override
            public void onNewShipPlacementInTile(Board.Tile newTile, Ship currentShip, int index) {
                newTile.setImage(currentShip.getImageForIndex(index));
            }

            @Override
            public void onResetShipPlacementInTile(Board.Tile resetTile) {
                resetTile.setImage(null);
            }
        };
        private Board.Tile selectedTile;

        @Override
        public void changed(ObservableValue<? extends Board.Tile> observable, Board.Tile oldTile, Board.Tile newTile) {
            if(shipPlacementHandler.isValidPlacementOrigin(newTile)) {
                selectedTile = newTile;
                Ship.Orientation validOrientation = shipPlacementHandler.getValidOrientations().get(0);
                orientation.set(null);
                orientation.set(validOrientation);
            } else {
                Log.gameLog().addMessage(new Log.Message("That is not a valid placement option!", Log.Message.Type.ERROR));
            }
        }

        public ShipPlacementListener() {
            orientation.addListener((observable, oldOrientation, newOrientation) -> {
                if(newOrientation == null) {
                    return;
                } else if(shipPlacementHandler.getValidOrientations().contains(newOrientation)) {
                    shipPlacementHandler.newShipPlacement(selectedTile, newOrientation);
                } else {
                    orientation.set(oldOrientation);
                    Log.gameLog().addMessage(new Log.Message("That is not a valid orientation!", Log.Message.Type.ERROR));
                }
            });
        }

        public void confirmPlacement() {
            if(selectedTile == null) return;

            Log.gameLog().addMessage(new Log.Message(shipPlacementHandler.getCurrentShip().name + " was placed at " + selectedTile.name, Log.Message.Type.SUCCESS));
            shipPlacementHandler.confirmShipPlacement(selectedTile, orientation.get());
            selectedTile = null;
            if(shipPlacementHandler.allShipsPlaced()) {
                Log.gameLog().addMessage(new Log.Message("All ships placed.", Log.Message.Type.SUCCESS));
                endShipPlacement();
            }
        }
    }

}
