package com.example.rummikubfrontscreen;
import com.example.rummikubfrontscreen.FXTile;
import com.example.rummikubfrontscreen.TilePositionScanner;
import com.example.rummikubfrontscreen.setup.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashMap;


public class GameBoardController {

    @FXML
    private Pane Pane;

    public GameApp gameApp;

    public Tile tile;

    private ArrayList<Tile> tiles;

    private FXTile fxTile = new FXTile();


    private ArrayList<Button> fxTileButtons = new ArrayList<>();

    private ArrayList<FXTile> fxTiles = new ArrayList<FXTile>();

    private ArrayList<Button> buttonsOnPlayingField = new ArrayList<>();

    private ArrayList<Double> buttonsOnPlayingFieldPosX = new ArrayList<>();
    private ArrayList<Double> buttonsOnPlayingFieldPosY = new ArrayList<>();
    private ArrayList<Double> prevButtonsOnPlayingFieldPosX;
    private ArrayList<Double> prevButtonsOnPlayingFieldPosY;
    private ArrayList<Node> buttonsToKeep = new ArrayList<>();
    private ArrayList<Node> prevButtonsToKeep;
    private ArrayList<Tile> tilesInField = new ArrayList<>();
    private ArrayList<Tile> prevTilesInField;
    private ArrayList<Tile> allTilesInField = new ArrayList<>();
    private ArrayList<Tile> currentHand = new ArrayList<>();
    HashMap<String, Tile> all_tiles = new HashMap<>();
    private RandomAgent agent;


    boolean gameStarted = false;
    boolean drawn = false;
    boolean endTurnBlocked = true;

    double minX = 1;
    double minY = 1;
    double maxX = 485;
    double maxY = 285;
    boolean winner = false;

    /**
     * Initializes the Tile object as a Button
     */
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

    /**
     * Handles clicking event of clicking on the tile
     **/
    @FXML
    private void handleButtonClick(ActionEvent event){
        fxTile.fxTileButton = (Button) event.getSource();
    }

    /**
     * Resets the individual buttons on the playing field
     */
    public void resetButtonsOnField(){

        for (Node node : Pane.getChildren()) {
            if (node instanceof Button){
                double buttonX = node.getLayoutX();
                double buttonY = node.getLayoutY();

                if (buttonX >= minX && buttonX <= maxX && buttonY >= minY && buttonY <= maxY) {
                    String id = node.getId();
                    buttonsToKeep.add(node);
                    buttonsOnPlayingFieldPosX.add(node.getLayoutX());
                    buttonsOnPlayingFieldPosY.add(node.getLayoutY());

                    Tile curTile = all_tiles.get(id);

                    // Should only be added when approved
                    tilesInField.add(curTile);
                    if (!allTilesInField.contains(curTile)){
                        allTilesInField.add(curTile);
                    }

                    gameApp.getGs().setTilesInPlay(tilesInField);

                    curTile.setX(buttonX);
                    curTile.setY(buttonY);

                    if(gameApp.getCurPlr().getHand().contains(curTile) ){
                        gameApp.getCurPlr().removeTile(curTile);
                    }
                }
            }
        }
        winner = gameApp.isWinner();
        //endTurnBlocked = !wholeProcessChecker(tilesInField);
        if (winner){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("YOU WON!");
            alert.showAndWait();
        }

    }

    // getting the previous state of the game
    public void previousState(){
        buttonsToKeep = prevButtonsToKeep;
        buttonsOnPlayingFieldPosX = prevButtonsOnPlayingFieldPosX;
        buttonsOnPlayingFieldPosY = prevButtonsOnPlayingFieldPosY;
        tilesInField = prevTilesInField;
        gameApp.getCurPlr().setHand(currentHand);
    }


    /**
     * Resets the playing field as a whole
     */
    public void resetPlayingField() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("YOU WON!");

        ArrayList<Tile> tiles = gameApp.getGs().getAllTiles();

        // get the previous state of the game
        prevButtonsToKeep = new ArrayList<>(buttonsToKeep);
        prevButtonsOnPlayingFieldPosX = new ArrayList<>(buttonsOnPlayingFieldPosX);
        prevButtonsOnPlayingFieldPosY = new ArrayList<>(buttonsOnPlayingFieldPosY);
        prevTilesInField = new ArrayList<>(gameApp.getGs().getTilesInPlay());
        currentHand = new ArrayList<>(gameApp.getCurPlr().getHand());

        // clear the lists for the new scanning of the field
        buttonsToKeep.clear();
        tilesInField.clear();
        buttonsOnPlayingFieldPosX.clear();
        buttonsOnPlayingFieldPosY.clear();

        // reset the field and buttons
        resetButtonsOnField();

        // if the field is wrong revert to previous state
        if (!wholeProcessChecker(tilesInField)) {
            System.out.println("oopsie");
            previousState();
            endTurnBlocked = true;
            drawn = false;

        } else {
            System.out.println("The whole field appears to be valid");
            endTurnBlocked = false;
            // the player did not draw but the move was valid
            drawn = true;
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
        Board board = Board(buttonsOnPlayingField);
        gameApp.getGs.setBoard(board);
    }

    /**
     * Initializes the tiles on the hand of the next player
     */

    @FXML
    private void changePlayer() {
        if (!gameStarted) return;

        // agent is the last player
        if(gameApp.getCurPlr().getId()==gameApp.getGs().getPlayers().size()-1){
            agent.takeRandomAction();
            if(agent.getChosenMoves().isEmpty()){
                System.out.println("\n");
                System.out.println("Agent has drawn tile");
                System.out.println("\n");
                drawn = false;
                drawButton();
            }else{
                System.out.println("\n");
                System.out.println("The agent chose:" + agent.getChosenMoves());
                System.out.println("Please make the move for the agent");
                System.out.println("\n");
            }
        }

        // if the move was not valid revert to previous state
        if(!drawn){
            if (endTurnBlocked) {
                endTurnBlocked = false;
                return;
            }
            drawn = true;
            return;
        }

        resetPlayingField();


        // if the player made a valid move, move to the next player
        if(!endTurnBlocked){
            gameApp.nextPlayer();
        } else if(drawn){
            gameApp.nextPlayer();
        }

        tiles = gameApp.getCurPlr().getHand();
        System.out.println("curr hand"+tiles);

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
            putButtonHand();
        }
        initializeTileAsButton();

        // Setting up next round
        drawn = false;

    }

    /**
     * Starts the entire game and initializes the tiles on the hand of first player
     */
    @FXML
    private void start(){
        if (gameStarted) return;

        gameApp = new GameApp();
        agent = gameApp.getGs().getAgent();
        tiles = gameApp.getCurPlr().getHand();

        for (Tile tile : tiles) {
            fxTile = initFXTile(tile);
            fxTiles.add(fxTile);
            putButton();
        }

        initializeTileAsButton();
        gameStarted = true;

        // allbuttons

        for (Tile tile : gameApp.getGs().getAllTiles()) {
            all_tiles.put(Integer.toString(tile.getId()), tile);

        }

    }

    /**
     * Checks whether the matchings on the game board are valid
     */
    private boolean wholeProcessChecker (ArrayList<Tile> unstructuredTiles) {
        System.out.println("Unstructered: "+unstructuredTiles);
        // Verifying game field
        TilePositionScanner tScanner = new TilePositionScanner();
        ArrayList<ArrayList<Tile>> structuredTiles = tScanner.scanner(unstructuredTiles);
        System.out.println("The series are :"+structuredTiles);
        return Board.boardVerifier(structuredTiles);
    }

    /**
     * Allows a player to draw a new tile randomly
     */
    @FXML
    private void drawButton() {

        if(drawn){
            return;
        }

        System.out.println("drawn");
        tile = gameApp.draw();
        fxTile = initFXTile(tile);
        System.out.println(fxTile);

        putButtonHand();
        initializeTileAsButton();

        drawn = true;
    }
    private void putButtonHand(){
        double initialX = 20;
        double initialY = 380;

        while (isButtonOccupyingCoordinates(initialX, initialY)) {
            initialX += 45;
            if(initialX == 335){
                initialX = 20;
                initialY += 45;
            }
        }

        fxTile.fxTileButton.setLayoutX(initialX);
        fxTile.fxTileButton.setLayoutY(initialY);

        fxTile.fxTileButton.setPrefHeight(41);
        fxTile.fxTileButton.setPrefWidth(33);

        fxTile.fxTileButton.setFont(javafx.scene.text.Font.font("Segoe UI Black", 10));
        fxTile.fxTileButton.setStyle("-fx-background-color: #EDE8C9; -fx-border-color: #B8B09F; -fx-border-width: 1; -fx-background-radius: 5; -fx-border-radius: 5;");
        Pane.getChildren().add(fxTile.fxTileButton);
        fxTileButtons.add(fxTile.fxTileButton);
    }
    /**
     * Handles placing the button on the game board
     */
    private void putButton(){

        double initialX = 20;
        double initialY = 380;

        while (isButtonOccupyingCoordinates(initialX, initialY)) {
            if (initialX < 270) {
                initialX += 45;
            } else {
                initialX = 20;
                initialY = 425;
            }
        }

        fxTile.fxTileButton.setLayoutX(initialX);
        fxTile.fxTileButton.setLayoutY(initialY);

        fxTile.fxTileButton.setPrefHeight(41);
        fxTile.fxTileButton.setPrefWidth(33);

        fxTile.fxTileButton.setFont(javafx.scene.text.Font.font("Segoe UI Black", 10));
        fxTile.fxTileButton.setStyle("-fx-background-color: #EDE8C9; -fx-border-color: #B8B09F; -fx-border-width: 1; -fx-background-radius: 5; -fx-border-radius: 5;");
        Pane.getChildren().add(fxTile.fxTileButton);
        fxTileButtons.add(fxTile.fxTileButton);
    }

    /**
     * Checks whether a button is in a certain coordinates
     */

    private boolean isButtonOccupyingCoordinates(double x, double y) {
        for (Node node : Pane.getChildren()) {
            if (node instanceof Button button) {
                if (button.getLayoutX() == x && button.getLayoutY() == y) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Initializes the FXTile
     */
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
