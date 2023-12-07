package com.example.rummikubfrontscreen;



import com.example.rummikubfrontscreen.setup.Colour;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class initFXTile {

    /*public void initFXTile(Colour colour, Value value){
        FXTile fxTile = new FXTile(colour, value);
        fxTile.setFXTile(convertColourToPaint(colour), value);
    }*/

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
