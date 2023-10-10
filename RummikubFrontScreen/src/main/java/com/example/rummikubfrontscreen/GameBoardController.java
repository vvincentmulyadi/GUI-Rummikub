package com.example.rummikubfrontscreen;

import com.example.rummikubfrontscreen.setup.Colour;
import com.example.rummikubfrontscreen.setup.GameApp;
import com.example.rummikubfrontscreen.setup.Tile;
import com.example.rummikubfrontscreen.setup.Value;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

    private ArrayList<Button> fxTileButtons = new ArrayList<>();

    private ArrayList<FXTile> fxTiles = new ArrayList<FXTile>();

    private ArrayList<Button> buttonsOnPlayingField = new ArrayList<>();

    private ArrayList<Double> buttonsOnPlayingFieldPosX = new ArrayList<>();
    private ArrayList<Double> buttonsOnPlayingFieldPosY = new ArrayList<>();

    private ArrayList<Button> buttonsInPlayerHand = new ArrayList<>();

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

    public void resetPlayingField() {
        ArrayList<Button> buttonsOnPlayingField =  new ArrayList<>();
        double minX = 1;
        double minY = 1;
        double maxX = 485;
        double maxY = 285;

        ArrayList<Node> buttonsToKeep = new ArrayList<>();
        for (Node node : Pane.getChildren()) {
            if (node instanceof Button){
                double buttonX = node.getLayoutX();
                double buttonY = node.getLayoutY();

                if (buttonX >= minX && buttonX <= maxX && buttonY >= minY && buttonY <= maxY) {
                    buttonsToKeep.add(node);
                    buttonsOnPlayingFieldPosX.add(node.getLayoutX());
                    buttonsOnPlayingFieldPosY.add(node.getLayoutY());
                }
            }
        }

        this.buttonsOnPlayingField = buttonsOnPlayingField;



        for (int i = 0; i < fxTileButtons.size(); i++) {
            Pane.getChildren().remove(fxTileButtons.get(i));
        }

        for (int i = 0; i < buttonsOnPlayingField.size(); i++) {
            Pane.getChildren().remove(this.buttonsOnPlayingField.get(i));
        }


        for (Node node : buttonsToKeep){
            if (node instanceof Button){
                buttonsOnPlayingField.add((Button) node);
            }
        }

    }

    @FXML
    private void changePlayer() {

        double initialX = 20;
        double initialY = 350;

        resetPlayingField();

        /*for (int i = 0; i < fxTileButtons.size(); i++) {
            Pane.getChildren().remove(fxTileButtons.get(i));
        }*/

        gameApp.nextPlayer();
        tiles = gameApp.getCurPlr().getHand();

        for (int i = 0; i < buttonsOnPlayingField.size(); i++) {

            buttonsOnPlayingField.get(i).setLayoutX(buttonsOnPlayingFieldPosX.get(i));
            buttonsOnPlayingField.get(i).setLayoutY(buttonsOnPlayingFieldPosY.get(i));

            buttonsOnPlayingField.get(i).setPrefWidth(33);
            buttonsOnPlayingField.get(i).setPrefHeight(41);

            Pane.getChildren().add(buttonsOnPlayingField.get(i));
        }


        for (int i = 0; i < tiles.size(); i++){
            colour = tiles.get(i).getColour();
            value = tiles.get(i).getValue();
            fxTile = initFXTile(colour, value);
            fxTiles.add(fxTile);


          while (isButtonOccupyingCoordinates(initialX, initialY)) {
                initialX += 45;
                if(initialX == 335){
                    initialX = 20;
                    initialY += 45;
                }
            }

            fxTile.fxTileButton.setLayoutX(initialX);
            fxTile.fxTileButton.setLayoutY(initialY);

            fxTile.fxTileButton.setPrefWidth(33);
            fxTile.fxTileButton.setPrefHeight(41);

            Pane.getChildren().add(fxTile.fxTileButton);
            fxTileButtons.set(i, fxTile.fxTileButton);
        }

        initializeTileAsButton();

    }

    @FXML
    private void start(){
        gameApp = new GameApp();
        gameApp.getGs().getPlayers();
        gameApp.getGs().getTiles();
        tiles = gameApp.getCurPlr().getHand();

        for (int i = 0; i < tiles.size(); i++){
            colour = tiles.get(i).getColour();
            value = tiles.get(i).getValue();
            fxTile = initFXTile(colour, value);
            fxTiles.add(fxTile);

            double initialX = 20;
            double initialY = 350;


            while (isButtonOccupyingCoordinates(initialX, initialY)) {
                if (initialX < 270){
                    initialX += 45;
                } else {
                    initialX = 20;
                    initialY = 395;
                }
            }

            fxTile.fxTileButton.setLayoutX(initialX);
            fxTile.fxTileButton.setLayoutY(initialY);

            fxTile.fxTileButton.setPrefWidth(33);
            fxTile.fxTileButton.setPrefHeight(41);

            Pane.getChildren().add(fxTile.fxTileButton);
            fxTileButtons.add(fxTile.fxTileButton);
        }
        initializeTileAsButton();
    }

    @FXML
    private void drawButton() {
        tile = new Tile();
        tile = gameApp.draw();

        colour = tile.getColour();
        value = tile.getValue();
        fxTile = initFXTile(colour, value);

        double initialX = 20;
        double initialY = 350;

        while (isButtonOccupyingCoordinates(initialX, initialY)) {
            initialX += 45;
            if(initialX == 335){
                initialX = 20;
                initialY += 45;
            }
        }

        fxTile.fxTileButton.setLayoutX(initialX);
        fxTile.fxTileButton.setLayoutY(initialY);

        fxTile.fxTileButton.setPrefWidth(33);
        fxTile.fxTileButton.setPrefHeight(41);

        Pane.getChildren().add(fxTile.fxTileButton);
        fxTileButtons.add(fxTile.fxTileButton);
        initializeTileAsButton();
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
