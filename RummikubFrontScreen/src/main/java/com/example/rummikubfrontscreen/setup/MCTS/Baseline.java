package com.example.rummikubfrontscreen.setup.MCTS;

import java.util.ArrayList;

import com.example.rummikubfrontscreen.setup.Board;
import com.example.rummikubfrontscreen.setup.Tile;

public class Baseline {

    private ArrayList<ArrayList<Tile>> board;
    private ArrayList<Tile> hand;

    public Baseline(ArrayList<ArrayList<Tile>> board, ArrayList<Tile> hand) {
        this.board = board;
        this.hand = hand;
    }

    public ArrayList<ArrayList<Tile>> getBestMove() {
        Move moves = new Move(this.board, this.hand);
        ArrayList<ArrayList<ArrayList<Tile>>> actionspace = moves.getResultingBoards();
        // ... rest of the method implementation ...
    }
}