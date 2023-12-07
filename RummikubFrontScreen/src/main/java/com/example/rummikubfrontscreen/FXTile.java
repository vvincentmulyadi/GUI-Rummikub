package com.example.rummikubfrontscreen;

import com.example.rummikubfrontscreen.setup.Colour;
import com.example.rummikubfrontscreen.setup.Tile;
import com.example.rummikubfrontscreen.setup.Value;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class FXTile extends Tile {

    public String fxTileID;


    private Tile tile;
    Button fxTileButton = new Button();

    public FXTile (){

    }

    public FXTile(Tile tile){
        this.tile = tile;
        setFXTile(convertColourToPaint(tile.getColour()), tile.getValue());
    }

    private Paint convertColourToPaint(Colour colour){
        return switch (colour) {
            case RED -> Color.CRIMSON;
            case BLUE -> Color.DARKCYAN;
            case BLACK -> Color.BLACK;
            case YELLOW -> Color.ORANGE;
        };
    }


    public void setFXTile(Paint color, Value value){
        fxTileButton.setText(value.getValueSymbol());
        fxTileButton.setTextFill(color);
        fxTileButton.setId(Integer.toString(tile.getId()));
    }


    public Tile getTile() {
        return this.tile;
    }

    public String toString(){
        return getTile().getValue().getValue() + " " + getTile().getColour().toString();
    }










}
