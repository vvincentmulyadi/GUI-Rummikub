package com.example.rummikubfrontscreen;

import com.example.rummikubfrontscreen.setup.Colour;
import com.example.rummikubfrontscreen.setup.Value;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Random;


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

    @FXML
    private Pane Pane;

    @FXML
    private void drawButton() {
        // Create a new button
        Button newButton = new Button("New Button");

        Colour colour = getRandomColour();
        Value value = getRandomValue();
        fxTile = initFXTile(colour, value);

        double initialX = 20;
        double initialY = 350;

        while (isButtonOccupyingCoordinates(initialX, initialY)) {
            if (initialX < 270){
                initialX += 45;
            } else {
                initialX = 20;
                initialY = 405;
            }
        }

        // Set its size
        newButton.setPrefWidth(33); // Set the width
        newButton.setPrefHeight(41); // Set the height

        // Add the new button to the root pane
        Pane.getChildren().add(newButton);
    }

    private boolean isButtonOccupyingCoordinates(double x, double y) {
        for (javafx.scene.Node node : Pane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                if (button.getLayoutX() == x && button.getLayoutY() == y) {
                    return true;
                }
            }
        }
        return false;
    }

   public static Colour getRandomColour(){
        Colour[] colours = Colour.values();
        Random rand = new Random();
        int i = rand.nextInt(colours.length);
        return colours[i];
    }

    public static Value getRandomValue(){
        Value[] values = Value.values();
        Random rand = new Random();
        int i = rand.nextInt(values.length);
        return values[i];
    }

    public FXTile initFXTile(Colour colour, Value value){
        FXTile fxTile = new FXTile();
        fxTile.setFXTile(convertColourToPaint(colour), value);
        return fxTile;
    }

    private Paint convertColourToPaint(Colour colour){
        switch (colour) {
            case RED:
                return Color.RED;
            case BLUE:
                return Color.BLUE;
            case BLACK:
                return Color.BLACK;
            case YELLOW:
                return Color.YELLOW;
        }
        return Color.WHITE;
    }


}
