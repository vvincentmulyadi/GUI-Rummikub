package com.example.rummikubfrontscreen.setup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AllPossibleMoves {
    GameSetup gs;

    ArrayList<Player> plrs;
    private Player curPlr;
    public ArrayList<Tile> tiles;
    // valid lines
    private ArrayList<ArrayList<Tile>> gLines = new ArrayList<>();
    // possible board states
    private ArrayList<ArrayList<ArrayList<Tile>>> gBoard = new ArrayList<>();
    // valid board states (not taking back to hand)
    private ArrayList<ArrayList<ArrayList<Tile>>> vBoard = new ArrayList<>();

    private ArrayList<Object[]> states = new ArrayList<>();

    // combining the made runs and groups into all possible board states they can
    // create
    public void makeBoardState(ArrayList<ArrayList<Tile>> cBoard, ArrayList<ArrayList<Tile>> remainingSeq,
            ArrayList<ArrayList<ArrayList<Tile>>> gBoard) {
        if (cBoard.isEmpty()) {
            cBoard.add(remainingSeq.get(0));
        }
    }

    // making valid board states, checking if we don't take anything back to our
    // hand
    public void makeValidBoardState(ArrayList<ArrayList<ArrayList<Tile>>> gBoard, Board board) {
        ArrayList<ArrayList<Tile>> bList = board.getCurrentGameBoard();
        ArrayList<Tile> boardList = new ArrayList<>();
        for (ArrayList<Tile> sequence : bList) {
            boardList.addAll(sequence);
        }
        for (ArrayList<ArrayList<Tile>> state : gBoard) {
            ArrayList<Tile> tilesOnBoard = new ArrayList<>();
            for (ArrayList<Tile> seq : state) {
                tilesOnBoard.addAll(seq);
            }
            if (tilesOnBoard.containsAll(boardList)) {
                vBoard.add(state);
            }
        }
    }

    // getting possible moves in a structure of an arrayList of Object array that
    // contains board and hand
    public void possibleMoves(Board board, ArrayList<Tile> hand) {
        ArrayList<Tile> combined = new ArrayList<>();
        ArrayList<ArrayList<Tile>> bList = board.getCurrentGameBoard();
        ArrayList<Tile> b = new ArrayList<>();
        for (ArrayList<Tile> sequence : bList) {
            combined.addAll(sequence);
        }
        combined.addAll(hand);
        // addToLineNextTile(0, new ArrayList<>(), combined, gLines);
        // addToLineNextTile(1, new ArrayList<>(), combined, gLines);
        makeBoardState(new ArrayList<>(), gLines, gBoard);
        makeValidBoardState(gBoard, board);
        for (int i = 0; i < vBoard.size(); i++) {
            ArrayList<Tile> hl = getHandFromBoard(vBoard.get(i), new ArrayList<>(hand));
            Object[] arr = new Object[2];
            arr[0] = new Board(vBoard.get(i));
            arr[1] = hl;
            states.add(arr);
        }
    }

    public ArrayList<Tile> getHandFromBoard(ArrayList<ArrayList<Tile>> board, ArrayList<Tile> h) {
        ArrayList<Tile> b = new ArrayList<>();
        for (ArrayList<Tile> seq : board) {
            b.addAll(seq);
        }
        for (Tile tile : b) {
            h.remove(tile);
        }
        return h;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < states.size(); i++) {
            s += "board:\n";
            s += states.get(i)[0].toString();
            s += "hand:\n";
            s += states.get(i)[1].toString();
            s += "\n";
            s += "\n";
        }
        return s;
    }

}
