package edu.bsu.css22.topboat.controllers;

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
    public List<Weapon> arsenal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadWeapons();
        ObservableList<Weapon> observableList = FXCollections.observableList(arsenal);
        weaponListView.setItems(observableList);

        weaponListView.setCellFactory(new Callback<ListView<Weapon>, ListCell<Weapon>>(){

            @Override
            public ListCell<Weapon> call(ListView<Weapon> p) {

                ListCell<Weapon> cell = new ListCell<Weapon>(){

                    @Override
                    protected void updateItem(Weapon weapon, boolean bool) {
                        super.updateItem(weapon, bool);
                        if(weapon != null) {
                            if(weapon.type.quantity == -1){
                                setText(weapon.type.name + ": Infinite");
                            }else{
                                setText(weapon.type.name + ": " + weapon.type.quantity);
                            }
                        }
                    }

                };
                return cell;
            }
        });
    }

    public void loadWeapons(){
        int i = 0;
        arsenal = new ArrayList<>();
        for(Weapon.Type type : Weapon.Type.values()){
            arsenal.add(i, new Weapon(type));
        }
    }
}
