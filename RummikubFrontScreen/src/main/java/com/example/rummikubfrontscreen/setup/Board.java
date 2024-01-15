package com.example.rummikubfrontscreen.setup;

import java.util.Random;
import java.util.ArrayList;

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

    public void addDrawPile(ArrayList<Tile> drawPile){
        this.drawPile = drawPile;
    }

    public void setCurrentGameBoard(ArrayList<ArrayList<Tile>> currentGameBoard) {
        this.currentGameBoard = currentGameBoard;
    }

    public Tile drawTile() {

        Random rand = new Random();
        if (drawPile.size() == 1)
            return drawPile.remove(0);

        if (drawPile == null) {
            System.out.println("!!! The draw pile is null in the board class !!!");
        }
        System.out.println("Drawpile size: " + drawPile.size());
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

    public static boolean boardVerifier(ArrayList<ArrayList<Tile>> seriesInGame) {
        // convert(currentGameBoard);
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
        return new Board(newBoard, drawPile);
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < currentGameBoard.size(); i++) {
            // if (currentGameBoard.get(i).size() == 0)
            // continue;
            str += i + "  ";
            str += currentGameBoard.get(i).toString() + "  ";
            str += "\n";
        }
        return str;
    }

    public static void main(String[] args) {
        ArrayList<Tile> group = new ArrayList<>();

        group.add(new Tile(Colour.BLUE, Value.SEVEN));
        group.add(new Tile(Colour.RED, Value.SEVEN));
        group.add(new Tile(Colour.BLACK, Value.SEVEN));
        group.add(new Tile(Colour.YELLOW, Value.JOKER));

        ArrayList<Tile> run = new ArrayList<>();
        run.add(new Tile(Colour.BLACK, Value.JOKER));
        run.add(new Tile(Colour.BLACK, Value.JOKER));
        run.add(new Tile(Colour.BLACK, Value.TWO));
        run.add(new Tile(Colour.BLACK, Value.THREE));
        run.add(new Tile(Colour.BLACK, Value.FOUR));

        Board board = new Board();
        ArrayList<ArrayList<Tile>> seriesInGame = new ArrayList<>();
        seriesInGame.add(group);
        seriesInGame.add(run);

        System.out.println(Board.boardVerifier(seriesInGame));
    }

}
