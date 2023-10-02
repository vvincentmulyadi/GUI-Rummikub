package com.example.rummikubfrontscreen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;


public class GameBoardController {
    @FXML
    private Pane pane;
    @FXML
    private Button selectedButton;

    private FXTile tile;
    /*@FXML
    private void initialize(){
        node.setOnMouseClicked(this::coButtonClick);
    }*/
    /*@FXML
    private void tilesButtonClick(ActionEvent e) {
        Button clickedButton = (Button)e.getSource();
        String clickedButton = click
        if (buttonValue.isEmpty()) {
            clickedButton.setText(this.label.getText());
            this.label.setText("");
        } else {
            this.label.setText(buttonValue);
            clickedButton.setText("");
        }

    }*/

    public void setTileToBeClicked(FXTile tile){
        this.tile = tile;
    }

    @FXML
    private void selectTile(ActionEvent e){
        tile = new FXTile();
        tile.setFXTileID("button1");
    }

    @FXML
    private void handlePaneClick(MouseEvent event) {
        double x = event.getSceneX();
        double y = event.getSceneY();

        if (selectedButton != null){
            selectedButton.setLayoutX(event.getSceneX());
            selectedButton.setLayoutY(event.getSceneY());
        }

        System.out.println("Mouse clicked at coordinates: X=" + x + ", Y=" + y);
    }

    @FXML
    private void handleButtonClick(ActionEvent event){
        selectedButton = (Button) event.getSource();
        System.out.println(selectedButton);
    }
}
