package com.example.rummikubfrontscreen;

import com.example.rummikubfrontscreen.setup.Colour;
import com.example.rummikubfrontscreen.setup.Tile;
import com.example.rummikubfrontscreen.setup.Value;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

public class FXTile extends Tile {

    public String fxTileID;


    private Tile tile;
    Button fxTileButton = new Button();

    public FXTile (){

    }

    public FXTile(Tile tile){
        super(tile.getColour(), tile.getValue());
        this.tile = tile;
    }

    public void setFXTileID(String fxTileID){
        this.fxTileID = fxTileID;
    }

    public void setFXTile(Paint color, Value value){
        fxTileButton.setText(value.getValueSymbol());
        fxTileButton.setTextFill(color);
    }

    public Tile getTile() {
        return this.tile;
    }










}
