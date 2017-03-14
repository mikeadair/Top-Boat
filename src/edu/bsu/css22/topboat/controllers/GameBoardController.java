package edu.bsu.css22.topboat.controllers;

import edu.bsu.css22.topboat.models.Board;
import edu.bsu.css22.topboat.Game;
import edu.bsu.css22.topboat.models.Log;
import edu.bsu.css22.topboat.models.Ship;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class GameBoardController implements Initializable {
    @FXML TabPane tabPane;
    @FXML GridPane playerGrid;
    @FXML GridPane opponentGrid;
    @FXML Button fireButton;
    @FXML Text selectedTileText;

    private Board selectedBoard;
    private HoverTileListener hoverTileListener = new HoverTileListener();

    private static final ShipPlacementListener shipPlacementTileListener = new ShipPlacementListener();

    private static final ChangeListener<Board.Tile> mainTileListener = (observable, oldTile, newTile) -> {

    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTabPane();
        initGameBoards();
        startShipPlacement();
        fireButton.setOnAction(event -> {
            Board.Tile targetTile = Board.opponentBoard().selectedTileProperty.get();
            if (targetTile.occupied.get()){
                //targetTile.getChildren().add();
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
                Board.Tile tile = board.get(x, y);
                grid.add(tile, x, y);
            }
        }
    }

    private void startShipPlacement() {

        Board.playerBoard().selectedTileProperty.addListener(shipPlacementTileListener);
        Board.opponentBoard().selectedTileProperty.addListener(shipPlacementTileListener);
        tabPane.setOnKeyPressed(event -> {
            System.out.println("key pressed");
            if(event.getCode() == KeyCode.ENTER) {

            } else if(event.getCode() == KeyCode.UP) {
                shipPlacementTileListener.orientation.set(Ship.Orientation.UP);
                event.consume();
            } else if(event.getCode() == KeyCode.DOWN) {
                shipPlacementTileListener.orientation.set(Ship.Orientation.DOWN);
                event.consume();
            } else if(event.getCode() == KeyCode.LEFT) {
                shipPlacementTileListener.orientation.set(Ship.Orientation.LEFT);
                event.consume();
            } else if(event.getCode() == KeyCode.RIGHT) {
                shipPlacementTileListener.orientation.set(Ship.Orientation.RIGHT);
                event.consume();
            }
        });
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
        private int currentTypeIndex = 0;
        private Ship.Type currentShipType = Ship.Type.values()[currentTypeIndex];
        private Ship currentShip = new Ship(currentShipType, 0, 0);
        SimpleObjectProperty<Ship.Orientation> orientation = new SimpleObjectProperty<>();
        private Board.Tile selectedTile;

        @Override
        public void changed(ObservableValue<? extends Board.Tile> observable, Board.Tile oldTile, Board.Tile newTile) {
            System.out.println("in ship placement listener");
            currentShip.setX(newTile.x);
            currentShip.setY(newTile.y);
            if(!Board.playerBoard().isValidPlacementTile(currentShip)) {
                System.out.println("not valid placement tile");
                Log.gameLog().addMessage(new Log.Message("That tile is not a valid ship origin.", Log.Message.Type.ERROR));
                currentShip.setX(oldTile.x);
                currentShip.setY(oldTile.y);
                return;
            }

            selectedTile = newTile;
            currentShip.setX(selectedTile.x);
            currentShip.setY(selectedTile.y);

            if(oldTile == newTile){
                Game.player1.addShip(currentShip);
                currentTypeIndex++;
                currentShipType = Ship.Type.values()[currentTypeIndex];
                selectedTile = null;
            }
        }

        public ShipPlacementListener() {
            orientation.addListener((observable, oldOrientation, newOrientation) -> {
                if(selectedTile != null) {
                    currentShip.orientation = newOrientation;

                    if(!Board.playerBoard().validateShipOrientation(currentShip)) {
                        currentShip.orientation = oldOrientation;
                    } else {
                        Board.playerBoard().occupyTilesWithShip(currentShip, oldOrientation);
                    }
                }
            });
        }
    }

}
