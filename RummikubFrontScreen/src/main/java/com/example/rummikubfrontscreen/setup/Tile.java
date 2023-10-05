package com.example.rummikubfrontscreen.setup;

import javafx.scene.control.Button;

public class Tile {

    public Tile() {}

    private Colour colour;
    private Value value;
    private int x;
    private int y;


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
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
