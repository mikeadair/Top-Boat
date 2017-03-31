package edu.bsu.css22.topboat.controllers;

import edu.bsu.css22.topboat.Game;
import edu.bsu.css22.topboat.models.Stats;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class StatsController implements Initializable {

    @FXML Tab statsTab;
    @FXML GridPane playerGridPane;
    @FXML GridPane opponentGridPane;

    private Stats stats;

    private final Image WON_BG_IMAGE = new Image(this.getClass().getResourceAsStream("../images/firework.gif"));

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stats = Game.stats;
        formatGrid(playerGridPane);
        formatGrid(opponentGridPane);
        loadStats(playerGridPane, 0);
        loadStats(opponentGridPane, 1);
        GameBoardController.boardTabPane.getSelectionModel().select(statsTab);
        System.out.println("Stats Controller Loaded");
    }

    private Label[] getLabels() {
        Font font = new Font("Comic Sans MS", 25);
        Label hitsTitle = new Label();
        hitsTitle.setFont(font);
        hitsTitle.setTextFill(Color.BLACK);
        hitsTitle.setText("Hits: ");

        Label missesTitle = new Label();
        missesTitle.setFont(font);
        missesTitle.setTextFill(Color.BLACK);
        missesTitle.setText("Misses: ");

        Label[] labels = new Label[2];
        labels[0] = hitsTitle;
        labels[1] = missesTitle;

        return labels;
    }

    private void formatGrid(GridPane grid) {
        grid.setPadding(new Insets(10, 10, 10, 10));
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(50);
        col.setHalignment(HPos.CENTER);
        Platform.runLater(() -> {
            grid.getColumnConstraints().addAll(col, col);
        });
    }

    private void loadStats(GridPane grid, int player) {
        Label[] labels = getLabels();

        Label name = new Label();
        name.setText(stats.getPlayer(player).getName().toString());
        name.setFont(new Font("Comic Sans MS", 25));
        if (!stats.getResult(player)) {
            BackgroundImage img = new BackgroundImage(WON_BG_IMAGE, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true));
            Background bg = new Background(null, img, null, null);
            grid.setBackground(bg);
            name.setText(name.getText() + " WON!");
            name.setTextFill(Color.GREEN);
        } else {
            name.setText(name.getText() + " LOST!");
            name.setTextFill(Color.RED);
        }

        Platform.runLater(() -> {
            grid.add(name, 0, 0, 2, 1);
        });

        Label hits = new Label();
        hits.setText(String.valueOf(stats.getHits(player)));
        hits.setFont(new Font("Comic Sans MS", 20));
        hits.setTextFill(Color.BLACK);

        Platform.runLater(() -> {
            grid.add(labels[0], 0, 1);
            grid.add(hits, 1, 1);
        });

        Label misses = new Label();
        misses.setText(String.valueOf(stats.getMisses(player)));
        misses.setFont(new Font("Comic Sans MS", 20));
        misses.setTextFill(Color.BLACK);

        Platform.runLater(() -> {
            grid.add(labels[1], 0, 2);
            grid.add(misses, 1, 2);
        });
    }
}
