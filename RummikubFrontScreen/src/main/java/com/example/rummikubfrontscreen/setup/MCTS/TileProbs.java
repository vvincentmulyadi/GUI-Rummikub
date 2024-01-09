package com.example.rummikubfrontscreen.setup.MCTS;


import com.example.rummikubfrontscreen.setup.Tile;

import java.util.ArrayList;

public class TileProbs {
    private double [] tileProbsUniform;
    private double [] tileProbsPredicted;

    //Types of tiles 54 types, 106 total
    private int numOfTiles = 54;

    public TileProbs(){
        tileProbsUniform = new double[numOfTiles];
        for (int i = 0; i < numOfTiles; i++){
            tileProbsUniform[i] = 1;
        }
    }

    public void adjustUniformProbs(int tileIndex){
        if (tileProbsUniform[tileIndex] != 0)
            tileProbsUniform[tileIndex] = tileProbsUniform[tileIndex] - 0.5;
    }

    public void calcPredictionParams(){

    }

    public int tileToIndexConverter(){
        //To be implemented
        return 0;
    }
}
