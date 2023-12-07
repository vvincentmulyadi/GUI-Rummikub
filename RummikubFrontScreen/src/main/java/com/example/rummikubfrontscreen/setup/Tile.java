package com.example.rummikubfrontscreen.setup;

public class Tile {

    public Tile() {}

    private Colour colour;
    private Value value;
    private double x;
    private double y;

    public int getId() {
        return id;
    }

    private int id;
    private static int counter = 0;


    public Tile(Colour colour, Value value){
        this.colour = colour;
        this.value = value;
        id = counter++;
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
