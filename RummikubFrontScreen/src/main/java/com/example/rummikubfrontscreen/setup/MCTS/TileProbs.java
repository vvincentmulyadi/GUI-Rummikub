package com.example.rummikubfrontscreen.setup.MCTS;


import com.example.rummikubfrontscreen.setup.Colour;
import com.example.rummikubfrontscreen.setup.Tile;

import java.util.ArrayList;

public class TileProbs {
    private double [] tileProbsUniform;
    private double [] tileValueProbs;
    private double [] tileColourProbs;
    private double [][] tileProbsPredicted;

    private int numOfTiles = 53;

    public TileProbs(){
        tileProbsUniform = new double[numOfTiles];
        for (int i = 0; i < numOfTiles; i++){
            tileProbsUniform[i] = 1;
        }
        tileValueProbs = new double[14];
        for (int i = 0; i < 14; i++){
            tileValueProbs[i] = 1;
        }
        tileColourProbs = new double[4];
        for (int i = 0; i < 4; i++){
            tileColourProbs[i] = 1;
        }

    }

    public double[] getTileProbsUniform(){
        return tileProbsUniform;
    }

    public double[] getTileProbsPredicted(){
        return tileProbsUniform;
    }

    public void adjustUniformProbs(int tileIndex){
        if (tileProbsUniform[tileIndex] != 0)
            tileProbsUniform[tileIndex] = tileProbsUniform[tileIndex] - 0.5;
    }

    public void adjustTileNumProbs(int tileValue){

    }



    public int tileToIndexConverter(Tile tile){
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
