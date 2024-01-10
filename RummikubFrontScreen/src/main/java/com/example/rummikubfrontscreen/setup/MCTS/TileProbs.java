package com.example.rummikubfrontscreen.setup.MCTS;

import com.example.rummikubfrontscreen.setup.Colour;
import com.example.rummikubfrontscreen.setup.Tile;
import com.example.rummikubfrontscreen.setup.Value;

import java.util.ArrayList;
import java.util.Arrays;

public class TileProbs {
    private double[] tileProbsUniform;
    private double[] tileValueProbs;
    private double[] tileColourProbs;
    private double[][] tileProbsPredicted;

    private int numOfTiles = 53;

    public TileProbs() {
        tileProbsUniform = new double[numOfTiles];
        for (int i = 0; i < numOfTiles; i++) {
            tileProbsUniform[i] = 1;
        }
        tileValueProbs = new double[14];
        for (int i = 0; i < 14; i++) {
            tileValueProbs[i] = 1;
        }
        tileColourProbs = new double[4];
        for (int i = 0; i < 4; i++) {
            tileColourProbs[i] = 1;
        }
    }

    public static void main(String[] args) {

        ArrayList<Tile> groupYe = new ArrayList<>();

        groupYe.add(new Tile(Colour.YELLOW, Value.ONE));
        groupYe.add(new Tile(Colour.YELLOW, Value.ONE));
        groupYe.add(new Tile(Colour.YELLOW, Value.THREE));
        groupYe.add(new Tile(Colour.YELLOW, Value.SEVEN));
        groupYe.add(new Tile(Colour.YELLOW, Value.FIVE));
        groupYe.add(new Tile(Colour.YELLOW, Value.SIX));
        groupYe.add(new Tile(Colour.YELLOW, Value.SEVEN));
        groupYe.add(new Tile(Colour.YELLOW, Value.EIGHT));
        groupYe.add(new Tile(Colour.YELLOW, Value.EIGHT));
        groupYe.add(new Tile(Colour.BLACK, Value.EIGHT));
        groupYe.add(new Tile(Colour.RED, Value.EIGHT));
        groupYe.add(new Tile(Colour.RED, Value.NINE));
        groupYe.add(new Tile(Colour.RED, Value.SEVEN));
        groupYe.add(new Tile(Colour.BLUE, Value.EIGHT));
        ArrayList<ArrayList<Tile>> groups = new ArrayList<>();
        groups.add(groupYe);
        MCTSAction mcts = new MCTSAction();
        ArrayList<ArrayList<Tile>> legalMoves = mcts.ownMoveGroup(groupYe);

        System.out.println(Arrays.toString(allTilesPros(groupYe, legalMoves)));

    }

    public static double[] allTilesPros(ArrayList<Tile> hand, ArrayList<ArrayList<Tile>> board) {
        // To be implemented
        double[] array = new double[53];

        for (int i = 0; i < array.length; i++) {
            array[i] = 1;
        }

        for (Tile tile : hand) {
            int tileIndex = tileToIndexConverter(tile);
            array[tileIndex] -= 0.5;
        }

        for (ArrayList<Tile> meld : board) {
            for (Tile tile : meld) {
                int tileIndex = tileToIndexConverter(tile);
                array[tileIndex] -= 0.5;
            }
        }
        return array;
    }

    public double[] getTileProbsUniform() {
        return tileProbsUniform;
    }

    public double[] getTileProbsPredicted() {
        return tileProbsUniform;
    }

    public void adjustUniformProbs(int tileIndex) {
        if (tileProbsUniform[tileIndex] != 0)
            tileProbsUniform[tileIndex] = tileProbsUniform[tileIndex] - 0.5;
    }

    public void adjustTileNumProbs(int tileValue) {

    }

    public static int tileToIndexConverter(Tile tile) {
        int value = tile.getInt();
        Colour colour = tile.getColour();
        switch (value) {
            case 1:
                if (colour == Colour.YELLOW)
                    return 1;
                if (colour == Colour.RED)
                    return 2;
                if (colour == Colour.BLUE)
                    return 3;
                if (colour == Colour.BLACK)
                    return 4;
                break;
            case 2:
                if (colour == Colour.YELLOW)
                    return 5;
                if (colour == Colour.RED)
                    return 6;
                if (colour == Colour.BLUE)
                    return 7;
                if (colour == Colour.BLACK)
                    return 8;
                break;
            case 3:
                if (colour == Colour.YELLOW)
                    return 9;
                if (colour == Colour.RED)
                    return 10;
                if (colour == Colour.BLUE)
                    return 11;
                if (colour == Colour.BLACK)
                    return 12;
                break;
            case 4:
                if (colour == Colour.YELLOW)
                    return 13;
                if (colour == Colour.RED)
                    return 14;
                if (colour == Colour.BLUE)
                    return 15;
                if (colour == Colour.BLACK)
                    return 16;
                break;
            case 5:
                if (colour == Colour.YELLOW)
                    return 17;
                if (colour == Colour.RED)
                    return 18;
                if (colour == Colour.BLUE)
                    return 19;
                if (colour == Colour.BLACK)
                    return 20;
                break;
            case 6:
                if (colour == Colour.YELLOW)
                    return 21;
                if (colour == Colour.RED)
                    return 22;
                if (colour == Colour.BLUE)
                    return 23;
                if (colour == Colour.BLACK)
                    return 24;
                break;
            case 7:
                if (colour == Colour.YELLOW)
                    return 25;
                if (colour == Colour.RED)
                    return 26;
                if (colour == Colour.BLUE)
                    return 27;
                if (colour == Colour.BLACK)
                    return 28;
                break;
            case 8:
                if (colour == Colour.YELLOW)
                    return 29;
                if (colour == Colour.RED)
                    return 30;
                if (colour == Colour.BLUE)
                    return 31;
                if (colour == Colour.BLACK)
                    return 32;
                break;
            case 9:
                if (colour == Colour.YELLOW)
                    return 33;
                if (colour == Colour.RED)
                    return 34;
                if (colour == Colour.BLUE)
                    return 35;
                if (colour == Colour.BLACK)
                    return 36;
                break;
            case 10:
                if (colour == Colour.YELLOW)
                    return 37;
                if (colour == Colour.RED)
                    return 38;
                if (colour == Colour.BLUE)
                    return 39;
                if (colour == Colour.BLACK)
                    return 40;
                break;
            case 11:
                if (colour == Colour.YELLOW)
                    return 41;
                if (colour == Colour.RED)
                    return 42;
                if (colour == Colour.BLUE)
                    return 43;
                if (colour == Colour.BLACK)
                    return 44;
                break;
            case 12:
                if (colour == Colour.YELLOW)
                    return 45;
                if (colour == Colour.RED)
                    return 46;
                if (colour == Colour.BLUE)
                    return 47;
                if (colour == Colour.BLACK)
                    return 48;
                break;
            case 13:
                if (colour == Colour.YELLOW)
                    return 49;
                if (colour == Colour.RED)
                    return 50;
                if (colour == Colour.BLUE)
                    return 51;
                if (colour == Colour.BLACK)
                    return 52;
                break;
            case 0:
                return 53;
        }
        return 0;
    }
}
