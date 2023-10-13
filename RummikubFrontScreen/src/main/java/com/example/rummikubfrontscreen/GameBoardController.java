package com.example.rummikubfrontscreen;

import com.example.rummikubfrontscreen.setup.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Arrays;
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

    private Paint paint;

    private ArrayList<Button> fxTileButtons = new ArrayList<>();

    private ArrayList<FXTile> fxTiles = new ArrayList<FXTile>();

    private ArrayList<Button> buttonsOnPlayingField = new ArrayList<>();

    private ArrayList<Double> buttonsOnPlayingFieldPosX = new ArrayList<>();
    private ArrayList<Double> buttonsOnPlayingFieldPosY = new ArrayList<>();
    ArrayList<Tile> tilesInField = new ArrayList<>();


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

        // System.out.println("Mouse clicked at coordinates: X=" + x + ", Y=" + y);
    }

    @FXML
    private void handleButtonClick(ActionEvent event){
        fxTile.fxTileButton = (Button) event.getSource();
    }

    public void resetPlayingField() {
        ArrayList<Tile> tiles = gameApp.getGs().getTiles();
        System.out.println(tiles.size());
        System.out.println("tiles list: " + tiles);
        double minX = 1;
        double minY = 1;
        double maxX = 485;
        double maxY = 285;

        ArrayList<Node> buttonsToKeep = new ArrayList<>();
        buttonsOnPlayingFieldPosX.clear();
        buttonsOnPlayingFieldPosY.clear();
        Colour colour;
        Paint paint;
        Value value;
        for (Node node : Pane.getChildren()) {
            if (node instanceof Button){
                double buttonX = node.getLayoutX();
                double buttonY = node.getLayoutY();
                if (buttonX >= minX && buttonX <= maxX && buttonY >= minY && buttonY <= maxY) {
                    buttonsToKeep.add(node);
                    buttonsOnPlayingFieldPosX.add(node.getLayoutX());
                    buttonsOnPlayingFieldPosY.add(node.getLayoutY());
                    paint = ((Button) node).getTextFill();
                    value = Value.getValueBySymbol(((Button) node).getText());
                    colour = paintToColour(paint);
                    for (int i = 0; i < tiles.size(); i++){
                        if (tiles.get(i).getColour().equals(colour) && tiles.get(i).getValue() == value) {
                            tiles.get(i).setX(node.getLayoutX());
                            tiles.get(i).setY(node.getLayoutY());
                            tilesInField.add(tiles.get(i));
                            if(gameApp.getCurPlr().getHand().contains(tiles.get(i))) {
                                gameApp.getCurPlr().removeTile(tiles.get(i));
                            }
                        }
                    }
                }
            }
        }



        if (!wholeProcessChecker(tilesInField)) {
            System.out.println("Playerfield is Unnnnvalid");
        } else {
            System.out.println("The whole field appears to be valid");
        }



        for (Button fxTileButton : fxTileButtons) {
            Pane.getChildren().remove(fxTileButton);
        }

        for (Button button : buttonsOnPlayingField) {
            Pane.getChildren().remove(button);
        }

        buttonsOnPlayingField = new ArrayList<>();
        for (int i = 0; i < buttonsToKeep.size(); i ++){
            if (buttonsToKeep.get(i) instanceof Button){
                buttonsOnPlayingField.add((Button) buttonsToKeep.get(i));
            }
        }
        for(int i = 0; i<buttonsOnPlayingField.size();i++){

        }
    }


    @FXML
    private void changePlayer() {

        System.out.println("Last fxTile: "+fxTile.fxTileButton);

        resetPlayingField();

        /*for (int i = 0; i < fxTileButtons.size(); i++) {
            Pane.getChildren().remove(fxTileButtons.get(i));
        }*/

        gameApp.nextPlayer();
        tiles = gameApp.getCurPlr().getHand();
        System.out.println();

        for (int i = 0; i < buttonsOnPlayingField.size(); i++) {

            buttonsOnPlayingField.get(i).setLayoutX(buttonsOnPlayingFieldPosX.get(i));
            buttonsOnPlayingField.get(i).setLayoutY(buttonsOnPlayingFieldPosY.get(i));


            buttonsOnPlayingField.get(i).setPrefWidth(33);
            buttonsOnPlayingField.get(i).setPrefHeight(41);

            System.out.println(Pane.getChildren());
            Pane.getChildren().add(buttonsOnPlayingField.get(i));
        }


        for (int i = 0; i < tiles.size(); i++){
            fxTile = initFXTile(tiles.get(i));
            fxTiles.add(fxTile);
            putButtons();
        }
        initializeTileAsButton();
        //fxTile.fxTileButton = null;
    }

    @FXML
    private void start(){
        gameApp = new GameApp();
        tiles = gameApp.getCurPlr().getHand();

        for (int i = 0; i < tiles.size(); i++){
            fxTile = initFXTile(tiles.get(i));
            fxTiles.add(fxTile);
            putButtons();
        }

        initializeTileAsButton();
        fxTile.fxTileButton = null;
        System.out.println(fxTile.fxTileButton == null);
    }

    private boolean wholeProcessChecker (ArrayList<Tile> unstructuredTiles) {
        // Verifying game field
        TilePositionScanner tScanner = new TilePositionScanner();
        ArrayList<ArrayList<Tile>> structeredTiles = tScanner.scanner(unstructuredTiles);

        System.out.println("The series are :"+structeredTiles);
        return Board.boardVerifier(structeredTiles);
    }

    @FXML
    private void drawButton() {
        tile = new Tile();
        tile = gameApp.draw();
        color = tile.getColour();
        value = tile.getValue();
        fxTile = initFXTile(tile);
        putButtons();
        initializeTileAsButton();
        //fxTile.fxTileButton = null;
    }

    private boolean isButtonOccupyingCoordinates(double x, double y) {
        for (javafx.scene.Node node : Pane.getChildren()) {
            if (node instanceof Button button) {
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

    public FXTile initFXTile(Tile tile){
        FXTile fxTile = new FXTile(tile);
        fxTile.setFXTile(convertColourToPaint(tile.getColour()), tile.getValue());
        return fxTile;
    }


    private Paint convertColourToPaint(Colour colour){
        return switch (colour) {
            case RED -> Color.CRIMSON;
            case BLUE -> Color.DARKCYAN;
            case BLACK -> Color.BLACK;
            case YELLOW -> Color.ORANGE;
        };
    }
    private Colour paintToColour(Paint paint){
        if (paint == Color.CRIMSON){
            return Colour.RED;
        }
        if (paint == Color.DARKCYAN){
            return Colour.BLUE;
        }
        if (paint == Color.ORANGE){
            return Colour.YELLOW;
        }
        if (paint == Color.BLACK){
            return Colour.BLACK;
        }
        else return null;
    }
}
