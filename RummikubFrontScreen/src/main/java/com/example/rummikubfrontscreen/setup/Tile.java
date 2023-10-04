package com.example.rummikubfrontscreen.setup;

import javafx.scene.control.Button;

public class Tile {

    public Tile() {}

    private Colour colour;
    private Value value;

    public Tile(Colour colour, Value value){
        this.colour = colour;
        this.value = value;
    }

    public Colour getColour(){
        return colour;
    }

    public Value getValue(){
        return value;
    }

    public String toString(){
        return getValue().getValue() + " " + getColour().toString();
    }

}
