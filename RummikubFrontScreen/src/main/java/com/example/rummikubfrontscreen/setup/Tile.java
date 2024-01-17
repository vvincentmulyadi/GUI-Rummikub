package com.example.rummikubfrontscreen.setup;

import java.util.*;

/**
 * The `Tile` class represents a tile in a game, with properties such as colour, value, position, and ID.
 */
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


    // The constructor is initializing a `Tile` object with the given `colour` and `value` parameters. 
    // It also assigns a unique `id` to the tile by incrementing the `counter` variable, and by the 'value' and 'color'.
    public Tile(Colour colour, Value value) {
        this.colour = colour;
        this.value = value;
        id = counter++;
        if (value == Value.JOKER) {
            idToColorSort = value.ordinal() + colour.ordinal();
        } else
            idToColorSort = colour.ordinal() * 13 + value.ordinal();

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

    public int getIdToColorSort() {
        return idToColorSort;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(colour, value, x, y, id, idToColorSort);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Tile tile = (Tile) obj;
        return id == tile.id &&
                Double.compare(tile.x, x) == 0 &&
                Double.compare(tile.y, y) == 0 &&
                idToColorSort == tile.idToColorSort &&
                colour == tile.colour &&
                value == tile.value;
    }

}

/**
 * The TileComparator class is a Java class that implements the Comparator interface and compares two
 * Tile objects based on their idToColorSort property.
 */
class TileComparator implements Comparator<Tile> {

    public int compare(Tile s1, Tile s2) {
        if (s1.getIdToColorSort() == s2.getIdToColorSort())
            return 0;
        else if (s1.getIdToColorSort() > s2.getIdToColorSort())
            return 1;
        else
            return -1;
    }
}
