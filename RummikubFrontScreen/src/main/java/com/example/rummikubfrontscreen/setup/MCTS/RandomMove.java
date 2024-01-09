package com.example.rummikubfrontscreen.setup.MCTS;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.rummikubfrontscreen.setup.Board;
import com.example.rummikubfrontscreen.setup.Colour;
import com.example.rummikubfrontscreen.setup.Player;
import com.example.rummikubfrontscreen.setup.Tile;

public class RandomMove {
     Player currentPlayer;
    private ArrayList<ArrayList<Tile>>initialBoard;
    private Board currentBoard;
    private ArrayList<Tile> initialHand;
    private ArrayList<Tile> currentHand;
    private ArrayList<Tile>outputHand;
    private ArrayList<ArrayList<Tile>>outputBoard;
    private ArrayList<ArrayList<Tile>>possibleCombos;
    private ArrayList<ArrayList<Tile>>allSets;
    private ArrayList<Tile>deck;
    private ArrayList<ArrayList<Tile>> randomMove;
 
    HashMap<Colour, Integer> colorMap;
}
