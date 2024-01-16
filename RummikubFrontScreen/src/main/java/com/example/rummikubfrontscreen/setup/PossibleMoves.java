package com.example.rummikubfrontscreen.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.rummikubfrontscreen.setup.MCTS.MCTSAction;

public class PossibleMoves {
    
    private static void addToLineNextTile(int lineType, ArrayList<Tile> cLine, ArrayList<Tile> remainingTiles,
            ArrayList<ArrayList<Tile>> gLines) {
        // lineType == 0 - group, need to sortByColour
        // lineType == 1 - run, need to sortByNum
        // cLine - currently build line
        // gLines - valid single lines
        // we build lines with first element or proceed it with joker/jokers
        if (cLine.isEmpty()) {
            Player.sortByColor(remainingTiles);
            cLine.add(remainingTiles.get(0));
            ArrayList<Tile> nextremainingTiles = new ArrayList<>(remainingTiles);
            nextremainingTiles.remove(0);
            addToLineNextTile(lineType, cLine, nextremainingTiles, gLines);
            if (!nextremainingTiles.isEmpty()) {
                addToLineNextTile(lineType, new ArrayList<>(), nextremainingTiles, gLines);
            }
            cLine.remove(cLine.size() - 1);
        } else if (lineType == 1) {
            // it can be joker o a number with same color +1
            ArrayList<Tile> nextremainingTiles = new ArrayList<>(remainingTiles);
            for (Tile remainingTile : remainingTiles) {         
                nextremainingTiles.remove(0);

                if ((remainingTile.getColour() == cLine.get(cLine.size() - 1).getColour() &&
                        remainingTile.getInt() == cLine.get(cLine.size() - 1).getInt() + 1) ||
                        remainingTile.getValue() == Value.JOKER ||
                        (remainingTile.getColour() == cLine.get(cLine.size() - 1).getColour()
                                && cLine.size() > 1 &&
                                cLine.get(cLine.size() - 1).getValue() == Value.JOKER &&
                                remainingTile.getInt() == cLine.get(cLine.size() - 2).getInt() + 2)) {
                    cLine.add(remainingTile);
                    if (cLine.size() >= 3) {
                        gLines.add(new ArrayList<>(cLine));
                    }

                    addToLineNextTile(1, cLine, nextremainingTiles, gLines);
                    cLine.remove(cLine.size() - 1);
                }
            }
        } else {
            // it can be a joker or the same number with a different colour
            ArrayList<Tile> nextremainingTiles = new ArrayList<>(remainingTiles);
            for (Tile remainingTile : remainingTiles) {    

                nextremainingTiles.remove(0);
                
                boolean c = true;
                for (Tile tile : cLine) {
                    if (tile.getColour() == remainingTile.getColour() &&
                            tile.getValue() == remainingTile.getValue()) {
                        c = false;
                        break;
                    }
                }
                if ((remainingTile.getColour() != cLine.get(cLine.size() - 1).getColour() &&
                        remainingTile.getValue() == cLine.get(cLine.size() - 1).getValue()) ||
                        remainingTile.getValue() == Value.JOKER || (cLine.size() > 2 &&
                                cLine.get(cLine.size() - 1).getValue() == Value.JOKER &&
                                remainingTile.getValue() == cLine.get(cLine.size() - 2).getValue())) {
                    if (c) {
                        cLine.add(remainingTile);
                        if (cLine.size() >= 3 && cLine.size() < 5) {
                            gLines.add(new ArrayList<>(cLine));
                        }
                        addToLineNextTile(0, cLine, nextremainingTiles, gLines);
                        cLine.remove(cLine.size() - 1);
                    }
                }
            }
        }
    }

    // combining the made runs and groups into all possible board states they can
    // create
    public static void makeBoardState(ArrayList<ArrayList<Tile>> cBoard, ArrayList<ArrayList<Tile>> remainingSeq, ArrayList<ArrayList<ArrayList<Tile>>> gBoard, Board b) {
        
        if(cBoard.isEmpty()){
            cBoard.add(remainingSeq.get(0));
            ArrayList<ArrayList<Tile>> nextRemainingSeq = new ArrayList<>(remainingSeq);
            nextRemainingSeq.remove(0);
            // ArrayList<ArrayList<Tile>> nextRemainingSeq = getValidLines(remainingSeq.get(0), remainingSeq, hashLines);
            //makeBoardState(cBoard, nextRemainingSeq, gBoard, b);
            if (makeValidBoardState(cBoard, b)) {
                gBoard.add(new ArrayList<>(cBoard));
            }
            cBoard.remove(cBoard.size() - 1);
        }
        ArrayList<ArrayList<Tile>> nextRemainingSeq = new ArrayList<>(remainingSeq);
        for (int i = 0; i < remainingSeq.size(); i++) {
            boolean c = true;
            nextRemainingSeq.remove(0);
            for (Tile tile : remainingSeq.get(i)) {
                if (!cBoard.isEmpty() && containsTile(cBoard, tile)) {
                        c = false;
                        break;
                    }
                }
            
            if (c) {
                cBoard.add(remainingSeq.get(i));
                if(makeValidBoardState(cBoard, b)) {
                    gBoard.add(new ArrayList<>(cBoard));
                }
                //nextRemainingSeq = getValidLines(remainingSeq.get(i), remainingSeq, hashLines);
                makeBoardState(cBoard, nextRemainingSeq, gBoard, b);
                cBoard.remove(cBoard.size() - 1);
            }
        }
    }

    private static boolean containsTile(ArrayList<ArrayList<Tile>> cBoard, Tile targetTile) {
        for (ArrayList<Tile> seq : cBoard) {
            if (seq.contains(targetTile)) {
                return true;
            }
        }
        return false;
    }


    // making couple board states for random playout
    public static Object[] makeCoupleBoardStates(ArrayList<ArrayList<Tile>> cBoard, ArrayList<ArrayList<Tile>> remainingSeq, ArrayList<ArrayList<ArrayList<Tile>>> gBoard, Board b, ArrayList<Tile> hand) {
        
        if(cBoard.isEmpty()){
            cBoard.add(remainingSeq.get(0));
            ArrayList<ArrayList<Tile>> nextRemainingSeq = new ArrayList<>(remainingSeq);
            nextRemainingSeq.remove(0);
            cBoard.remove(cBoard.size() - 1);
        }
        ArrayList<ArrayList<Tile>> nextRemainingSeq = new ArrayList<>(remainingSeq);
        for (int i = 0; i < remainingSeq.size(); i++) {
            boolean c = true;
            nextRemainingSeq.remove(0);
            for (Tile tile : remainingSeq.get(i)) {
                if (!cBoard.isEmpty()) {
                    for (ArrayList<Tile> seq : cBoard) {
                        if (seq.contains(tile)) {
                            c = false;
                        }
                    }
                }
            }
            if (c) {
                cBoard.add(remainingSeq.get(i));
                if(makeValidBoardState(cBoard, b)) {
                    gBoard.add(new ArrayList<>(cBoard));
                    if(gBoard.size()==1){
                        Object[] oneBoard = new Object[2];
                        oneBoard[0] = new Board(gBoard.get(0));
                        oneBoard[1] = getHandFromBoard(gBoard.get(0), hand);
                        return oneBoard;
                        
                    }
                }
                if(gBoard.isEmpty()){
                    makeCoupleBoardStates(cBoard, nextRemainingSeq, gBoard, b, hand);
                }
                cBoard.remove(cBoard.size() - 1);
            }
        }
        return null;
    }

    public static ArrayList<ArrayList<Tile>> getValidLines(ArrayList<Tile> usedSeq, ArrayList<ArrayList<Tile>> remainingSeq, HashMap<Tile, ArrayList<ArrayList<Tile>>> hashLines){
        ArrayList<ArrayList<Tile>> nextRemainingSeq = new ArrayList<>(remainingSeq);
        ArrayList<ArrayList<Tile>> sequencesToRemove = new ArrayList<>();
        nextRemainingSeq.remove(0);
        for(Tile tile : usedSeq){
            ArrayList<ArrayList<Tile>> tilesSeq = hashLines.get(tile);
            for(ArrayList<Tile> seq : tilesSeq){
                sequencesToRemove.add(seq); 
            }
        }
        //System.out.println(sequencesToRemove);
        nextRemainingSeq.removeAll(sequencesToRemove);
        return nextRemainingSeq;
    }


    // making valid board states, checking if we don't take anything back to our
    // hand
    public static boolean makeValidBoardState(ArrayList<ArrayList<Tile>> gBoard, Board board) {
        ArrayList<ArrayList<Tile>> bList = new ArrayList<>(board.getCurrentGameBoard());
        ArrayList<Tile> boardList = new ArrayList<>();
        for (ArrayList<Tile> sequence : bList) {
            boardList.addAll(sequence);
        }
        ArrayList<Tile> tilesOnBoard = new ArrayList<>();
        for (ArrayList<Tile> seq : gBoard) {
            tilesOnBoard.addAll(seq);
        }
        if (tilesOnBoard.containsAll(boardList) && tilesOnBoard.size() >= boardList.size()) {
            return true;
        }
        return false;
    }

    // getting possible moves in a structure of an arrayList of Object array that
    // contains board and hand, integer makeBoardType == 0 - all board states, other - couple of them
    public static ArrayList<Object[]> possibleMoves(Board b, ArrayList<Tile> hand, int makeBoardType) {
        ArrayList<Object[]> states = new ArrayList<>();

        ArrayList<Tile> drawPile = b.getDrawPile();
        if (drawPile != null && !drawPile.isEmpty()) {
            Object[] drawMove = MCTSAction.drawTileFromBoard(b, hand);
            if(drawMove != null){
                states.add(drawMove);
            }
        }
        
        ArrayList<ArrayList<Tile>> bList = b.getCurrentGameBoard();
        ArrayList<Tile> boa = new ArrayList<>();
        for (ArrayList<Tile> sequence : bList) {
            boa.addAll(sequence);
        }
        
        ArrayList<ArrayList<Tile>> gLines = getLines(b, hand);
        ArrayList<ArrayList<ArrayList<Tile>>> gBoard = new ArrayList<>();
        if(makeBoardType == 0){
            makeBoardState(new ArrayList<>(), gLines, gBoard, b);
        }else{
            makeCoupleBoardStates(new ArrayList<>(), gLines, gBoard, b, hand);
        }

        for (int i = 0; i < gBoard.size(); i++) {
            ArrayList<Tile> bo = new ArrayList<>();
            ArrayList<Tile> hl = getHandFromBoard(gBoard.get(i), hand);
            for (ArrayList<Tile> seq : gBoard.get(i)) {
                bo.addAll(seq);
            }
            if (!(bo.size() == boa.size())) {
                Object[] arr = new Object[2];
                arr[0] = new Board((ArrayList<ArrayList<Tile>>) gBoard.get(i).stream().map(ArrayList::new)
                        .collect(Collectors.toList()), b.getDrawPile());
                arr[1] = new ArrayList<>(hl);
                states.add(arr);
            }
        }
        System.out.println(states.size());
        return states;
    }

    public static ArrayList<ArrayList<Tile>> getLines(Board b, ArrayList<Tile> hand){
        ArrayList<Tile> combined = new ArrayList<>();
        ArrayList<ArrayList<Tile>> bList = b.getCurrentGameBoard();
        for (ArrayList<Tile> sequence : bList) {
            combined.addAll(sequence);
        }
        combined.addAll(hand);

        ArrayList<ArrayList<Tile>> gLinesG = new ArrayList<>();
        ArrayList<ArrayList<Tile>> gLinesR = new ArrayList<>();
        ArrayList<ArrayList<Tile>> gLines = new ArrayList<>();

        addToLineNextTile(1, new ArrayList<>(), combined, gLinesR);
        addToLineNextTile(0, new ArrayList<>(), combined, gLinesG);
        gLines.addAll(gLinesG);
        gLines.addAll(gLinesR);
        if (gLines.isEmpty()) {
            System.out.println("glines is empty");
            return null;
        }

        // HashMap<Tile, ArrayList<ArrayList<Tile>>> hashTiles = new HashMap<>();
        // for (Tile keyTile : combined) {
        //     hashTiles.put(keyTile, new ArrayList<>());
        // }
        // for(ArrayList<Tile> seq : gLines){
        //     for(Tile tile : seq){
        //         hashTiles.putIfAbsent(tile, new ArrayList<>());
        //         if(!hashTiles.get(tile).contains(seq)){
        //             hashTiles.get(tile).add(seq);
        //         }
        //     }
        // }


        return gLines;
    }

    public static ArrayList<Tile> getHandFromBoard(ArrayList<ArrayList<Tile>> board, ArrayList<Tile> hand) {
        ArrayList<Tile> h = new ArrayList<>(hand);
        ArrayList<Tile> b = new ArrayList<>();
        for (ArrayList<Tile> seq : board) {
            b.addAll(seq);
        }
        for (Tile tile : b) {
            h.remove(tile);
        }
        return h;
    }


    public static String toString(ArrayList<Object[]> states) {
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

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        ArrayList<ArrayList<Tile>> board = new ArrayList<>();
        ArrayList<Tile> seq = new ArrayList<>();

        seq.add(new Tile(Colour.YELLOW, Value.TWO));
        seq.add(new Tile(Colour.YELLOW, Value.ONE));
        seq.add(new Tile(Colour.YELLOW, Value.THREE));
        board.add(seq);
        ArrayList<Tile> seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.BLUE, Value.TWO));
        seq2.add(new Tile(Colour.BLACK, Value.TWO));
        seq2.add(new Tile(Colour.RED, Value.TWO));
        board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.YELLOW, Value.ONE));
        seq2.add(new Tile(Colour.YELLOW, Value.TWO));
        seq2.add(new Tile(Colour.YELLOW, Value.THREE));
        seq2.add(new Tile(Colour.YELLOW, Value.FOUR));
        seq2.add(new Tile(Colour.YELLOW, Value.FIVE));
        seq2.add(new Tile(Colour.YELLOW, Value.SIX));
        board.add(seq2);

        Board b = new Board(board, new ArrayList<>());

        ArrayList<Tile> hand = new ArrayList<>();

        hand.add(new Tile(Colour.BLACK, Value.FOUR));
        hand.add(new Tile(Colour.RED, Value.ONE));
        hand.add(new Tile(Colour.YELLOW, Value.SEVEN));
        hand.add(new Tile(Colour.BLUE, Value.TWELVE));
        hand.add(new Tile(Colour.BLUE, Value.JOKER));
        hand.add(new Tile(Colour.RED, Value.THREE));
        hand.add(new Tile(Colour.RED, Value.FOUR));
        hand.add(new Tile(Colour.BLUE, Value.THREE));

        System.out.println(toString(possibleMoves(b, hand, 0)));
        long estimatedTime = System.nanoTime() - startTime;
        System.out.println(estimatedTime);
        
    }
}