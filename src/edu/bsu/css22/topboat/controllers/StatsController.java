package edu.bsu.css22.topboat.controllers;

import edu.bsu.css22.topboat.Game;
import edu.bsu.css22.topboat.models.Stats;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class StatsController implements Initializable {

    @FXML
    GridPane playerGridPane;
    @FXML
    GridPane opponentGridPane;

    private Stats stats;

    private Label hitsTitle;
    private Label missesTitle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stats = Game.stats;
        formatGrid(playerGridPane);
        formatGrid(opponentGridPane);
        formatTitles();
        loadStats(playerGridPane, 0);
        loadStats(opponentGridPane, 1);
        System.out.println("Stats Controller Loaded");
    }

    private void formatTitles() {
        Font font = new Font("Comic Sans MS", 25);
        hitsTitle = new Label();
        hitsTitle.setFont(font);
        hitsTitle.setTextFill(Color.BLACK);
        hitsTitle.setText("Hits: ");

        missesTitle = new Label();
        missesTitle.setFont(font);
        missesTitle.setTextFill(Color.BLACK);
        missesTitle.setText("Misses: ");
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
        Label name = new Label();
        name.setText(stats.getPlayer(player).getName().toString());
        name.setFont(new Font("Comic Sans MS", 25));
        if (!stats.getResult(player)) {
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
            grid.add(hitsTitle, 0, 1);
            grid.add(hits, 1, 1);
        });

        Label misses = new Label();
        misses.setText(String.valueOf(stats.getMisses(player)));
        misses.setFont(new Font("Comic Sans MS", 20));
        misses.setTextFill(Color.BLACK);

        Platform.runLater(() -> {
            grid.add(missesTitle, 0, 2);
            grid.add(misses, 1, 2);
        });
    }
}
