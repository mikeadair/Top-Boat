package edu.bsu.css22.topboat.controllers;

import edu.bsu.css22.topboat.Game;
import edu.bsu.css22.topboat.models.Weapon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ArsenalController implements Initializable {
    @FXML ListView<Weapon> weaponListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Weapon> observableList = FXCollections.observableList(Game.player1.getArsenal());
        weaponListView.setItems(observableList);

        weaponListView.setCellFactory(new Callback<ListView<Weapon>, ListCell<Weapon>>(){

            @Override
            public ListCell<Weapon> call(ListView<Weapon> p) {

                ListCell<Weapon> cell = new ListCell<Weapon>(){

                    @Override
                    protected void updateItem(Weapon weapon, boolean bool) {
                        super.updateItem(weapon, bool);
                        if(weapon != null) {
                            if(weapon.getShotsRemaining() == Weapon.INFINITE_AMMO) {
                                setText(weapon.getName() + ": Infinite");
                            } else {
                                setText(weapon.getName() + ": " + weapon.getShotsRemaining());
                            }
                        }
                    }
                };
                return cell;
            }
        });
    }

    public void resetWeaponSelection() {
        System.out.println("resetting weapon selection");
        weaponListView.getSelectionModel().select(0);
    }

    public Weapon getSelectedWeapon() {
        return weaponListView.getSelectionModel().getSelectedItem();
    }
}
