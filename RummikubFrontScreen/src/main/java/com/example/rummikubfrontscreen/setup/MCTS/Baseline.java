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

    // best move can be simply the biggest amount of tiles dispersed
    public ArrayList<Tile> getBestMove() {
        Move moveGenerator = new Move(this.board, hand);
        ArrayList<ArrayList<Tile>> moves = moveGenerator.getAllSets();
        int maxSize = 0;
        int currentSize = 0; // the total size of the current arraylist that we are checking
        int setIndex = 0; // the index we are currently checking
        int bestMove = 0; // the index of the best move in the moves
        ArrayList<Tile> biggestMoveSet = new ArrayList<Tile>();
        int bestMovePoints = 0; // the points of the best move

        for (int i = 0; i < moves.size(); i++) {
            ArrayList<Tile> currentSet = moves.get(i);
            currentSize = currentSet.size();
            int currentMovePoints = 0; // the points of the current move

            for (Tile tile : currentSet) {

                // currentMovePoints += tile.getValue(); // add the value of each tile to the
                // current move points
            }

            if (currentSize > maxSize || (currentSize == maxSize && currentMovePoints > bestMovePoints)) {
                maxSize = currentSize;
                bestMovePoints = currentMovePoints;
                bestMove = i;
            }
        }

        biggestMoveSet = moves.get(bestMove);

        return biggestMoveSet;
    }

    // try and see if the best move is valid in a copy of the board and create a
    // node with uct value
    public ArrayList<ArrayList<Tile>> applyBestMove() {
        ArrayList<ArrayList<Tile>> copyBoard = new ArrayList<ArrayList<Tile>>();
        for (ArrayList<Tile> set : board) {
            ArrayList<Tile> copySet = new ArrayList<Tile>();
            for (Tile tile : set) {
                copySet.add(tile);
            }
            copyBoard.add(copySet);
        }
        ArrayList<Tile> copyHand = new ArrayList<Tile>();
        for (Tile tile : hand) {
            copyHand.add(tile);
        }
        ArrayList<Tile> bestMove = getBestMove();
        Move moveGenerator = new Move(copyBoard, copyHand);
        // moveGenerator.applyMove(bestMove);
        return copyBoard;
    }
}
