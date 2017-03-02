package cs222.topboat.controllers;

import cs222.topboat.models.Board;
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
    private SelectedTileListener selectedTileListener = new SelectedTileListener();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTabPane();
        initGameBoards();
    }

    private void initTabPane() {
        Tab playerGridTab = tabPane.getTabs().get(0);

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
           if (newTab == playerGridTab) {
               selectedBoard = Board.playerBoard();
               fireButton.setVisible(false);
           } else {
               selectedBoard = Board.opponentBoard();
               fireButton.setVisible(true);
           }
            rebindHoverListener();
        });
    }

    private void rebindHoverListener() {
        selectedBoard.hoverTileProperty.addListener(selectedTileListener);
    }

    private void initGameBoards() {
        initBoard(playerGrid, Board.playerBoard());
        initBoard(opponentGrid, Board.opponentBoard());
    }

    private void initBoard(GridPane board, Board source) {
        for(int y = 0; y < Board.HEIGHT; y++) {
            for(int x = 0; y < Board.WIDTH; x++) {
                board.add(source.get(x, y), x, y);
            }
        }
    }

    private class SelectedTileListener implements ChangeListener<Board.Tile> {
        @Override
        public void changed(ObservableValue observable, Board.Tile oldValue, Board.Tile newValue) {
            if (newValue == null) {
                Board.Tile selectedTile = selectedBoard.selectedTileProperty.get();
                selectedTileText.setText(selectedTile.name.toString());
            } else {
                selectedTileText.setText(newValue.name.toString());
            }
        }
    }
}
