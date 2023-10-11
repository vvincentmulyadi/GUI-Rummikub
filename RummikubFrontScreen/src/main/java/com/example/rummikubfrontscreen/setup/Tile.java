package com.example.rummikubfrontscreen.setup;

import javafx.scene.control.Button;

public class Tile {

    public Tile() {}

    private Colour colour;
    private Value value;
    private double x;
    private double y;


    public Tile(Colour colour, Value value){
        this.colour = colour;
        this.value = value;
    }

    public Tile(Colour colour, Value value, int x, int y){
        this.colour = colour;
        this.value = value;
        this.x = x;
        this.y = y;
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
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}
