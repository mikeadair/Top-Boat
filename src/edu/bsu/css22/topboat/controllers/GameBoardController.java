package edu.bsu.css22.topboat.controllers;

import edu.bsu.css22.topboat.Game;
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

    private final ShipPlacementListener shipPlacementTileListener = new ShipPlacementListener();

    private static final ChangeListener<Board.Tile> mainTileListener = (observable, oldTile, newTile) -> {

    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTabPane();
        initGameBoards();
        startShipPlacement();
        fireButton.setOnAction(event -> {
            Board.Tile targetTile = Board.opponentBoard().selectedTileProperty.get();
            if (targetTile.occupied){
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
                Board.Tile tile = board.get(x, y);
                grid.add(tile, x, y);
            }
        }
    }

    public void startShipPlacement() {

        Board.playerBoard().selectedTileProperty.addListener(shipPlacementTileListener);
        tabPane.setOnKeyPressed(event -> {
            switch(event.getCode()) {
                case UP:
                    shipPlacementTileListener.orientation.set(Ship.Orientation.UP);
                    event.consume();
                    break;
                case DOWN:
                    shipPlacementTileListener.orientation.set(Ship.Orientation.DOWN);
                    event.consume();
                    break;
                case LEFT:
                    shipPlacementTileListener.orientation.set(Ship.Orientation.LEFT);
                    event.consume();
                    break;
                case RIGHT:
                    shipPlacementTileListener.orientation.set(Ship.Orientation.RIGHT);
                    event.consume();
                    break;
                case ENTER:
                    shipPlacementTileListener.confirmPlacement();
                    event.consume();
                    break;
            }
        });
    }

    void endShipPlacement() {
        Board.playerBoard().selectedTileProperty.removeListener(shipPlacementTileListener);
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

    private class ShipPlacementListener implements ChangeListener<Board.Tile> {
        private int currentTypeIndex = 0;
        private Ship.Type currentShipType = Ship.Type.values()[currentTypeIndex];
        private Ship currentShip = new Ship(currentShipType, 0, 0);
        SimpleObjectProperty<Ship.Orientation> orientation = new SimpleObjectProperty<>();
        private Board.Tile selectedTile;

        @Override
        public void changed(ObservableValue<? extends Board.Tile> observable, Board.Tile oldTile, Board.Tile newTile) {
            Ship.Orientation newOrientation = Board.playerBoard().validatePosition(newTile.x,newTile.y,currentShip.type.length,currentShipType);
            if(newOrientation != null){ System.out.println(newOrientation.name()); }
            if(selectedTile != null && newTile != selectedTile){
                Board.playerBoard().backToOcean(currentShip);
            }
            if(newOrientation == null){
                Log.gameLog().addMessage(new Log.Message("That tile is not a valid placement option!", Log.Message.Type.ERROR));
                currentShip.orientation = null;
                selectedTile = null;
                return;
            }
            if(oldTile != null && oldTile != newTile && currentShip.orientation != null) {
                System.out.println("removing ship");
                Board.playerBoard().backToOcean(currentShip);
            }
            currentShip.setX(newTile.x);
            currentShip.setY(newTile.y);
            selectedTile = newTile;
            currentShip.orientation = newOrientation;
            orientation.set(null);
            orientation.set(newOrientation);
        }

        public void confirmPlacement() {
            if(currentShip.orientation != null){
                Log.gameLog().addMessage(new Log.Message("Placed " + currentShip.type.name() + " at " + selectedTile.name + ": Facing " + currentShip.orientation.name(), Log.Message.Type.SUCCESS));
                Game.player1.addShip(currentShip);
                currentTypeIndex++;
                try {
                    currentShipType = Ship.Type.values()[currentTypeIndex];
                } catch(ArrayIndexOutOfBoundsException e) {
                    Log.gameLog().addMessage(new Log.Message("All your ships have been placed!", Log.Message.Type.SUCCESS));
                    Game.player1.setReady(true);
                    endShipPlacement();
                }
                currentShip = new Ship(currentShipType,0,0);
                selectedTile = null;
            }else{
                Log.gameLog().addMessage(new Log.Message("You must select an orientation before confirming!", Log.Message.Type.ERROR));
            }
        }

        public ShipPlacementListener() {
            orientation.addListener((Observable orientation) -> {
                if(((SimpleObjectProperty)orientation).getValue() == null){
                    return;
                }
                if(selectedTile != null) {
                    if(orientation == null){
                        return;
                    }
                    if(Board.playerBoard().worksWithOrientation(currentShip, (Ship.Orientation) ((SimpleObjectProperty)orientation).getValue())){
                        if(currentShip.orientation != null){
                            Board.playerBoard().backToOcean(currentShip);
                        }
                        currentShip.orientation = (Ship.Orientation) ((SimpleObjectProperty)orientation).getValue();
                        Board.playerBoard().occupyTilesWithShip(currentShip);
                    }else{
                        Log.gameLog().addMessage(new Log.Message("Ship cannot be oriented that direction", Log.Message.Type.ERROR));
                    }
                }
            });
        }
    }

}
