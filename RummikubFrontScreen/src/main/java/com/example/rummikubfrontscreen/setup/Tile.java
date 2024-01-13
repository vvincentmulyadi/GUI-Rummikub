package com.example.rummikubfrontscreen.setup;
import java.util.*;
public class Tile {

    public Tile() {
    }

    private Colour colour;
    private Value value;
    private double x;
    private double y;

    public int getId() {
        return id;
    }

    private int id;
    private static int counter = 0;

    private int idToColorSort;

    public Tile(Colour colour, Value value) {
        this.colour = colour;
        this.value = value;
        id = counter++;
        if (value==Value.JOKER){
            idToColorSort=value.ordinal()+colour.ordinal();
        }else
            idToColorSort=colour.ordinal()*13+value.ordinal();

    }

    @Override
    public Tile clone() {
        Tile tile = new Tile(colour, value);
        tile.setX(x);
        tile.setY(y);
        return tile;
    }

    public Colour getColour() {
        return colour;
    }

    public Value getValue() {
        return value;
    }

    public int getInt() {
        return value.getValue();
    }

    public int getIdToColorSort(){return idToColorSort; }

    public String toString() {
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
class TileComparator implements Comparator<Tile> {

    // override the compare() method
    public int compare(Tile s1, Tile s2)
    {
        if (s1.getIdToColorSort() == s2.getIdToColorSort())
            return 0;
        else if (s1.getIdToColorSort() > s2.getIdToColorSort())
            return 1;
        else
            return -1;
    }
}