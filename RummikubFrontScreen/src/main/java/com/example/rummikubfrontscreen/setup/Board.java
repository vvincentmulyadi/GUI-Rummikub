package com.example.rummikubfrontscreen.setup;

import java.util.Random;
import java.util.ArrayList;

/**
 * The Board class represents a game board with a 2D grid of tiles and provides
 * methods for
 * manipulating and verifying the board state.
 */
public class Board {

    // 2D arraylist grid representing the runs and rows
    private ArrayList<ArrayList<Tile>> currentGameBoard;
    private ArrayList<Tile> drawPile;

    public ArrayList<ArrayList<Tile>> getCurrentGameBoard() {
        return currentGameBoard;
    }

    // Constructor for the board
    public Board(ArrayList<ArrayList<Tile>> listOfTiles) {
        System.out.println("Warning You are using the deprecated version of the board constructor");
        System.out.println(
                "If you are using this board for any type of ai I would recommend using the \nconstructor that takes in the drawPile");
        System.out.println(
                "The Proble is that most possibleMoves finder also include the move of drawing a tile but for that the board needs a copy of the tiles which you are not providing here");

        currentGameBoard = listOfTiles;
    }

    // Constructor for the board
    public Board(ArrayList<ArrayList<Tile>> listOfTiles, ArrayList<Tile> drawPile) {
        this.drawPile = drawPile;
        currentGameBoard = listOfTiles;
    }

    public Board() {
    };

    public ArrayList<Tile> getDrawPile() {
        return drawPile;
    }

    public void addDrawPile(ArrayList<Tile> drawPile) {
        this.drawPile = drawPile;
    }

    public void setCurrentGameBoard(ArrayList<ArrayList<Tile>> currentGameBoard, ArrayList<Tile> drawPilus) {
        this.currentGameBoard = currentGameBoard;
        this.drawPile = drawPilus;
    }

    public Tile drawTile() {

        Random rand = new Random();
        if (drawPile.size() == 1)
            return drawPile.remove(0);

        if (drawPile == null) {
            System.out.println("!!! The draw pile is null in the board class !!!");
        }
        if (drawPile.size() == 0) {
            System.out.println("!!! The draw pile is empty !!!");
            return null;
        }
        int i = rand.nextInt(drawPile.size() - 1);
        Tile tile = drawPile.get(i);
        drawPile.remove(i);
        return tile;
    }

    public boolean addSeries(ArrayList<Tile> series) {
        ArrayList<ArrayList<Tile>> seriesInGame = new ArrayList<>();
        seriesInGame.add(series);
        if (Board.boardVerifier(seriesInGame)) {
            currentGameBoard.add(series);
            return true;
        }
        System.out.println("The AI's move was invalid");
        return false;
    }

    /**
     * The function `boardVerifier` checks if a given board configuration is valid
     * for a game by
     * verifying if the series of tiles in the game consist of valid groups or runs.
     * 
     * @param seriesInGame An ArrayList of ArrayLists of Tile objects. Each inner
     *                     ArrayList represents
     *                     a series of tiles in the game.
     * @return The method is returning a boolean value.
     */
    public static boolean boardVerifier(ArrayList<ArrayList<Tile>> seriesInGame) {
        for (int i = 0; i < seriesInGame.size(); i++) {
            if (seriesInGame.get(i).size() < 3) {
                return false;
            }

            Tile tile1 = null, tile2 = null;
            // index of a first tl which is not a joker
            int indexTile1 = 0;
            // loop assigning values of first two tiles not being a joker
            for (int j = 0; j < seriesInGame.get(i).size(); j++) {
                if (seriesInGame.get(i).get(j).getValue().equals(Value.JOKER))
                    continue;
                if (tile1 == null) {
                    tile1 = seriesInGame.get(i).get(j);
                    indexTile1 = j;
                } else {
                    tile2 = seriesInGame.get(i).get(j);
                    break;
                }
            }
            // case where there were two joker and one tl
            if (tile2 == null)
                continue;

            // checking whether we are checking a group or a run
            if (tile1.getValue() == tile2.getValue()) {
                // Check group
                if (!checkGroup(seriesInGame.get(i), tile1))
                    return false;
            } else {
                // Check Run
                if (!checkRun(seriesInGame.get(i), tile1, indexTile1))
                    return false;
            }
            tile1 = null;
            tile2 = null;
        }
        return true;
    }

    /**
     * The function checks if a given group of tiles is valid, meaning all tiles
     * have the same value
     * and each color is used only once.
     * 
     * @param group An ArrayList of Tile objects representing a group of tiles.
     * @param tile1 The `tile1` parameter is an instance of the `Tile` class, which
     *              represents a single
     *              tile in a game.
     * @return The method is returning a boolean value.
     */
    public static boolean checkGroup(ArrayList<Tile> group, Tile tile1) {
        Value v = tile1.getValue();
        ArrayList<Colour> coloursUsed = new ArrayList<>();
        if (group.size() > 4)
            return false;
        for (int i = 0; i < group.size(); i++) {
            if (group.get(i).getValue() == Value.JOKER)
                continue;
            if (group.get(i).getValue() != v)
                return false;
            if (coloursUsed.contains(group.get(i).getColour()))
                return false;
            else
                coloursUsed.add(group.get(i).getColour());
        }
        return true;
    }

    /**
     * The function checks if a given ArrayList of tiles forms a valid run.
     * 
     * @param run          An ArrayList of Tile objects representing a run of tiles
     *                     in a Rummikub game.
     * @param tile1        The parameter "tile1" is an instance of the Tile class,
     *                     which represents a single
     *                     tile in a game.
     * @param indexOfTile1 The `indexOfTile1` parameter represents the index of
     *                     `tile1` in the `run`
     *                     ArrayList.
     * @return The method is returning a boolean value.
     */
    public static boolean checkRun(ArrayList<Tile> run, Tile tile1, int indexOfTile1) {
        Colour c = tile1.getColour();
        for (int i = 0; i < run.size(); i++) {
            if (run.get(i).getValue() == Value.JOKER)
                continue;
            if (run.get(i).getColour() != c)
                return false;
            if (i - indexOfTile1 + tile1.getValue().getValue() != run.get(i).getValue().getValue())
                return false;
        }
        return true;
    }

    @Override
    public Board clone() {
        ArrayList<ArrayList<Tile>> newBoard = new ArrayList<>();
        for (ArrayList<Tile> series : currentGameBoard) {
            ArrayList<Tile> newSeries = new ArrayList<>();
            for (Tile tile : series) {
                // System.out.println(tile);
                if (tile == null)
                    continue;
                newSeries.add((Tile) tile.clone());
            }
            newBoard.add(newSeries);
        }
        // This clone doesnt deep clone the drawpile since if it most often stays the
        // same
        return new Board(newBoard, (ArrayList<Tile>) drawPile.clone());
    }

    @Override
    public String toString() {
        String str = "";
        str += "DrawPile: " + drawPile.size() + "\n";
        for (int i = 0; i < currentGameBoard.size(); i++) {
            // if (currentGameBoard.get(i).size() == 0)
            // continue;
            str += i + "  ";
            str += currentGameBoard.get(i).toString() + "  ";
            str += "\n";
        }
        return str;
    }
}
