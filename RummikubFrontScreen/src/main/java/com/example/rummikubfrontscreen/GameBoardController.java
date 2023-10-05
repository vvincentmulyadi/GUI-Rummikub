package com.example.rummikubfrontscreen;

import com.example.rummikubfrontscreen.setup.Colour;
import com.example.rummikubfrontscreen.setup.GameApp;
import com.example.rummikubfrontscreen.setup.Tile;
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
    private Pane Pane;

    public GameApp gameApp;

    public Tile tile;

    private ArrayList<Tile> tiles;

    private FXTile fxTile;

    private Colour colour;

    private Value value;
    double initialX = 20;
    double initialY = 350;

    private ArrayList<Button> fxTileButtons = new ArrayList<>();

    private ArrayList<FXTile> fxTiles = new ArrayList<FXTile>();

    @FXML
    private void initializeTileAsButton(){
        for (Button button: fxTileButtons){
            button.setOnAction(this::handleButtonClick);
        }
    }

    @FXML
    private void handlePaneClick(MouseEvent event) {
        double x = event.getSceneX();
        double y = event.getSceneY();

        if (fxTile.fxTileButton != null){
            fxTile.fxTileButton.setLayoutX(event.getSceneX());
            fxTile.fxTileButton.setLayoutY(event.getSceneY());
        }

        System.out.println("Mouse clicked at coordinates: X=" + x + ", Y=" + y);
    }

    @FXML
    private void handleButtonClick(ActionEvent event){
        fxTile.fxTileButton = (Button) event.getSource();
        System.out.println(fxTile.fxTileButton.getText());
    }

    @FXML
    private Pane Pane;

    @FXML
    private void drawButton() {
        // Create a new button
        Button newButton = new Button("New Button");

        colour = tile.getColour();
        value = tile.getValue();
        fxTile = initFXTile(colour, value);

        double initialX = 20;
        double initialY = 350;

        while (isButtonOccupyingCoordinates(initialX, initialY)) {
            if (initialX < 270){
                initialX += 45;
            } else if (initialY >= 405){
                initialX = 20;
                initialY = 450;
            } else{
                initialX = 20;
                initialY += 45;
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
                return Color.CRIMSON;
            case BLUE:
                return Color.DARKCYAN;
            case BLACK:
                return Color.BLACK;
            case YELLOW:
                return Color.ORANGE;
        }
        return Color.WHITE;
    }

    public void initGameBoard(){
        gameApp = new GameApp();
        gameApp.getGs().getPlayers();
        gameApp.getGs().getPlayers();
        gameApp.getGs().getTiles();

    }


}
