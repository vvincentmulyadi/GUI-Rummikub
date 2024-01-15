package com.example.rummikubfrontscreen.setup;

import java.util.ArrayList;
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
            for (int i = 0; i < remainingTiles.size(); i++) {
                nextremainingTiles.remove(0);
                if ((remainingTiles.get(i).getColour() == cLine.get(cLine.size() - 1).getColour() &&
                        remainingTiles.get(i).getInt() == cLine.get(cLine.size() - 1).getInt() + 1) ||
                        remainingTiles.get(i).getValue() == Value.JOKER ||
                        (remainingTiles.get(i).getColour() == cLine.get(cLine.size() - 1).getColour()
                                && cLine.size() > 1 &&
                                cLine.get(cLine.size() - 1).getValue() == Value.JOKER &&
                                remainingTiles.get(i).getInt() == cLine.get(cLine.size() - 2).getInt() + 2)) {
                    cLine.add(remainingTiles.get(i));
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
            for (int i = 0; i < remainingTiles.size(); i++) {
                boolean c = true;
                nextremainingTiles.remove(0);
                for (Tile tile : cLine) {
                    if (tile.getColour() == remainingTiles.get(i).getColour() &&
                            tile.getValue() == remainingTiles.get(i).getValue()) {
                        c = false;
                    }
                }
                if ((remainingTiles.get(i).getColour() != cLine.get(cLine.size() - 1).getColour() &&
                        remainingTiles.get(i).getValue() == cLine.get(cLine.size() - 1).getValue()) ||
                        remainingTiles.get(i).getValue() == Value.JOKER || (cLine.size() > 2 &&
                                cLine.get(cLine.size() - 1).getValue() == Value.JOKER &&
                                remainingTiles.get(i).getValue() == cLine.get(cLine.size() - 2).getValue())) {
                    if (c) {
                        cLine.add(remainingTiles.get(i));
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
    public static void makeBoardState(ArrayList<ArrayList<Tile>> cBoard, ArrayList<ArrayList<Tile>> remainingSeq,
            ArrayList<ArrayList<ArrayList<Tile>>> gBoard, Board b) {
        if (cBoard.isEmpty()) {
            cBoard.add(remainingSeq.get(0));
            ArrayList<ArrayList<Tile>> nextRemainingSeq = new ArrayList<>(remainingSeq);
            nextRemainingSeq.remove(0);
            makeBoardState(cBoard, nextRemainingSeq, gBoard, b);
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
                }
                makeBoardState(cBoard, nextRemainingSeq, gBoard, b);
                cBoard.remove(cBoard.size() - 1);
            }
        }
    }

    // making valid board states, checking if we don't take anything back to our
    // hand
    public static boolean makeValidBoardState(ArrayList<ArrayList<Tile>> gBoard, Board board) {
        ArrayList<ArrayList<Tile>> bList = new ArrayList<>(board.getCurrentGameBoard());
        ArrayList<Tile> boardList = new ArrayList<>();
        for (ArrayList<Tile> sequence : bList) {
            boardList.addAll(sequence);
        }
        //System.out.println("boardList: " + boardList);
        ArrayList<Tile> tilesOnBoard = new ArrayList<>();
        for (ArrayList<Tile> seq : gBoard) {
            tilesOnBoard.addAll(seq);
        }
        // System.out.println("tiles from move: "+tilesOnBoard);
        if (tilesOnBoard.containsAll(boardList) && tilesOnBoard.size() >= boardList.size()) {
            // System.out.println("tiles from move: "+tilesOnBoard);
            return true;
        }
        return false;
    }

    // getting possible moves in a structure of an arrayList of Object array that
    // contains board and hand
    public static ArrayList<Object[]> possibleMoves(Board b, ArrayList<Tile> hand) {
        ArrayList<Object[]> states = new ArrayList<>();

        ArrayList<Tile> drawPile = b.getDrawPile();
        if (drawPile != null && !drawPile.isEmpty()) {
            Object[] drawMove = MCTSAction.drawTileFromBoard(b, hand);
            if(drawMove != null){
                states.add(drawMove);
            }
        }

        ArrayList<Tile> combined = new ArrayList<>();
        ArrayList<ArrayList<Tile>> bList = b.getCurrentGameBoard();
        ArrayList<Tile> boa = new ArrayList<>();
        for (ArrayList<Tile> sequence : bList) {
            combined.addAll(sequence);
            boa.addAll(sequence);
        }
        combined.addAll(hand);
        ArrayList<ArrayList<Tile>> gLinesG = new ArrayList<>();
        ArrayList<ArrayList<Tile>> gLinesR = new ArrayList<>();
        ArrayList<ArrayList<Tile>> gLines = new ArrayList<>();

        // System.out.println(combined);
        addToLineNextTile(1, new ArrayList<>(), combined, gLinesR);
        // System.out.println(combined);
        addToLineNextTile(0, new ArrayList<>(), combined, gLinesG);
        gLines.addAll(gLinesG);
        gLines.addAll(gLinesR);
        // System.out.println("glines "+ gLines);
        if (gLines.isEmpty()) {
            System.out.println("glines is empty");
            return null;
        }
        ArrayList<ArrayList<ArrayList<Tile>>> gBoard = new ArrayList<>();
        makeBoardState(new ArrayList<>(), gLines, gBoard, b);
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
        return states;
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

        System.out.println(toString(possibleMoves(b, hand)));
        
    }
}
