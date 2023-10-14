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



public class GameBoardController {

    @FXML
    private Pane Pane;

    public GameApp gameApp;

    public Tile tile;

    private ArrayList<Tile> tiles;

    private FXTile fxTile;


    private ArrayList<Button> fxTileButtons = new ArrayList<>();

    private ArrayList<FXTile> fxTiles = new ArrayList<FXTile>();

    private ArrayList<Button> buttonsOnPlayingField = new ArrayList<>();

    private ArrayList<Double> buttonsOnPlayingFieldPosX = new ArrayList<>();
    private ArrayList<Double> buttonsOnPlayingFieldPosY = new ArrayList<>();

    boolean gameStarted = false;






    @FXML
    private void initializeTileAsButton(){
        for (Button button: fxTileButtons){
            button.setOnAction(this::handleButtonClick);
        }
    }


    /**
     *
     * Creates new Button() from fxTileButton. factory-like
     */
    @FXML
    private void handlePaneClick(MouseEvent event) {
        if (fxTile.fxTileButton != null){
            fxTile.fxTileButton.setLayoutX(event.getSceneX());
            fxTile.fxTileButton.setLayoutY(event.getSceneY());
        }
    }

    @FXML
    private void handleButtonClick(ActionEvent event){
        fxTile.fxTileButton = (Button) event.getSource();
    }

    public void resetPlayingField() {
        ArrayList<Tile> tiles = gameApp.getGs().getAllTiles();
        System.out.println(tiles.size());
        System.out.println("tiles list: " + tiles);
        double minX = 1;
        double minY = 1;
        double maxX = 485;
        double maxY = 285;

        ArrayList<Node> buttonsToKeep = new ArrayList<>();
        ArrayList<Tile> tilesInField = new ArrayList<>();
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
                    System.out.println("this1");
                    for (int i = 0; i < tiles.size(); i++){
                        if (tiles.get(i).getColour().equals(colour) && tiles.get(i).getValue() == value) {
                    System.out.println("this2");
                            tilesInField.add(tiles.get(i));
                            tiles.get(i).setX(node.getLayoutX());
                            tiles.get(i).setY(node.getLayoutY());
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
    }


    @FXML
    private void changePlayer() {
        if (!gameStarted) return;

        resetPlayingField();

        gameApp.nextPlayer();
        tiles = gameApp.getCurPlr().getHand();
        System.out.println();

        for (int i = 0; i < buttonsOnPlayingField.size(); i++) {
            buttonsOnPlayingField.get(i).setLayoutX(buttonsOnPlayingFieldPosX.get(i));
            buttonsOnPlayingField.get(i).setLayoutY(buttonsOnPlayingFieldPosY.get(i));
            buttonsOnPlayingField.get(i).setPrefWidth(33);
            buttonsOnPlayingField.get(i).setPrefHeight(41);
            Pane.getChildren().add(buttonsOnPlayingField.get(i));
        }

        for (Tile value : tiles) {
            fxTile = initFXTile(value);
            fxTiles.add(fxTile);
            putButtons();
        }
        initializeTileAsButton();
    }

    @FXML
    private void start(){
        if (gameStarted) return;

        gameApp = new GameApp();
        tiles = gameApp.getCurPlr().getHand();

        for (Tile value : tiles) {
            fxTile = initFXTile(value);
            fxTiles.add(fxTile);
            putButtons();
        }

        initializeTileAsButton();
        gameStarted = true;
    }

    private boolean wholeProcessChecker (ArrayList<Tile> unstructuredTiles) {
        System.out.println("Unstructered: "+unstructuredTiles);
        // Verifying game field
        TilePositionScanner tScanner = new TilePositionScanner();
        ArrayList<ArrayList<Tile>> structuredTiles = tScanner.scanner(unstructuredTiles);

        System.out.println("The series are :"+structuredTiles);
        return Board.boardVerifier(structuredTiles);
    }

    @FXML
    private void drawButton() {
        if (!gameStarted) return;
        tile = gameApp.draw();
        fxTile = initFXTile(tile);
        putButtons();
        initializeTileAsButton();

        changePlayer();
    }

    private void putButtons(){

            double initialX = 20;
            double initialY = 350;

            while (isButtonOccupyingCoordinates(initialX, initialY)) {
                if (initialX < 270) {
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

    public FXTile initFXTile(Tile tile){
        FXTile fxTile = new FXTile(tile);
        return fxTile;
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
