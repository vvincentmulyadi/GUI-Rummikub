package com.example.rummikubfrontscreen.setup.MCTS;

import com.example.rummikubfrontscreen.setup.Colour;
import com.example.rummikubfrontscreen.setup.GameSetup;
import com.example.rummikubfrontscreen.setup.Tile;
import com.example.rummikubfrontscreen.setup.Value;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The `TileProbs` class represents a collection of methods and data structures for calculating and
 * manipulating probabilities related to a set of tiles in a game.
 */
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

    public static ArrayList<Tile> combiner(MCTSGameState gameState) {
        double d = 2.5;
        GameSetup gs = new GameSetup(d);
        ArrayList<Tile> allTiles = gs.getAllTiles();
        ArrayList<Tile> hand = gameState.getPlayers().get(0).getHand();
        ArrayList<Tile> board = gameState.getAlltileOnField();
        for (Tile tile : hand) {
            allTiles.remove(tile);
        }
        for (Tile tile : board) {
            allTiles.remove(tile);
        }
        return allTiles;
    }

    public static ArrayList<Tile> handSampler(ArrayList<Tile> possibleTiles, MCTSGameState gs) {
        int handSize = gs.getPlayers().get(1).getHand().size();
        ArrayList<Tile> newHand = new ArrayList<>();
        for (int i = 0; i < handSize; i++) {
            int randomIndex = (int) (Math.random() * possibleTiles.size());
            newHand.add(possibleTiles.get(randomIndex));
            possibleTiles.remove(randomIndex);
        }
        return newHand;
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

    public static Tile indexToTileConverter(int i) {
        int col = 1 + i % 4;
        int val = i / 4;
        if (i == 0) {
            return new Tile(Colour.BLACK, Value.JOKER);
        }
        switch (col) {
            case 1:
                return new Tile(Colour.BLACK, Value.values()[val - 1]);
            case 2:
                return new Tile(Colour.YELLOW, Value.values()[val]);
            case 3:
                return new Tile(Colour.RED, Value.values()[val]);
            case 4:
                return new Tile(Colour.BLUE, Value.values()[val]);
        }
        return null;
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
