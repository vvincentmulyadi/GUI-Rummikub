package com.example.rummikubfrontscreen.setup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameApp {

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

    public GameApp() {
        gs = new GameSetup();
        plrs = gs.getPlayers();
        curPlr = plrs.get(0);
        tiles = gs.getTiles();
    }

    public boolean isWinner() {
        return curPlr.getHand().isEmpty();
    }

    public void nextPlayer() {
        int i = plrs.indexOf(curPlr);
        if (i == plrs.size() - 1) {
            curPlr = plrs.get(0);
        } else {
            curPlr = plrs.get(i + 1);
        }
    }

    public void previousPlayer() {
        int i = plrs.indexOf(curPlr);
        if (i == 0) {
            i = 4;
        } else {
            i = i - 1;
        }
        curPlr = plrs.get(i);
    }

    public Tile draw() {
        Random rand = new Random();
        int i = rand.nextInt(tiles.size() - 1);
        Tile tile = tiles.get(i);
        curPlr.addTile(tile);
        tiles.remove(i);
        return tile;
    }

    public Player getCurPlr() {
        return curPlr;
    }

    public GameSetup getGs() {
        return gs;
    }

    private void addToLineNextTile(int lineType, ArrayList<Tile> cLine, ArrayList<Tile> remainingTiles, ArrayList<ArrayList<Tile>> gLines) {
        // lineType == 0 - group, need to sortByColour
        // lineType == 1 - run, need to sortByNum
        // cLine - currently build line
        // gLines - valid single lines
        // we build lines with first element or proceed it with joker/jokers
        if (lineType == 0) {
            Player.sortByColor(remainingTiles);
        } else {
            Player.sortByNum(remainingTiles, 0, remainingTiles.size() - 1);
        }
        if (cLine.isEmpty()) {
            cLine.add(remainingTiles.get(0));
            ArrayList<Tile> nextremainingTiles = new ArrayList<>(remainingTiles);
            nextremainingTiles.remove(0);
            addToLineNextTile(lineType, cLine, nextremainingTiles, gLines);
            //addToLineNextTile(lineType, cLine, nextremainingTiles, gLines);
            cLine.remove(cLine.size() - 1);
        } else if (lineType == 1) {
            // it can be joker o a number with same color +1
            for (int i = 0; i < remainingTiles.size(); i++) {
                if ((remainingTiles.get(i).getColour() == cLine.get(cLine.size() - 1).getColour() &&
                        remainingTiles.get(i).getInt() == cLine.get(cLine.size() - 1).getInt() + 1) ||
                        remainingTiles.get(i).getValue() == Value.JOKER ||
                        (remainingTiles.get(i).getColour() == cLine.get(cLine.size() - 1).getColour()
                                && cLine.size()>1 &&
                                cLine.get(cLine.size() - 1).getValue() == Value.JOKER &&
                                remainingTiles.get(i).getInt() == cLine.get(cLine.size() - 2).getInt() + 2)) {
                    cLine.add(remainingTiles.get(i));
                    if (cLine.size() >= 3) {
                        gLines.add(new ArrayList<>(cLine));
                    }
                    ArrayList<Tile> nextremainingTiles = new ArrayList<>(remainingTiles);
                    nextremainingTiles.remove(i);
                    addToLineNextTile(1, cLine, nextremainingTiles, gLines);
                    cLine.remove(cLine.size() - 1);
                }
            }
        } else {
            // it can be a joker or the same number with a different colour
            for (int i = 0; i < remainingTiles.size(); i++) {
                boolean c = true;
                for (Tile tile : cLine) {
                    if (tile.getColour() == remainingTiles.get(i).getColour() &&
                            tile.getValue() == remainingTiles.get(i).getValue()) {
                        c = false;
                    }
                }
                if ((remainingTiles.get(i).getColour() != cLine.get(cLine.size() - 1).getColour() &&
                        remainingTiles.get(i).getValue() == cLine.get(cLine.size() - 1).getValue()) ||
                        remainingTiles.get(i).getValue() == Value.JOKER || (!cLine.isEmpty() &&
                                cLine.get(cLine.size() - 1).getValue() == Value.JOKER &&
                                remainingTiles.get(i).getValue() == cLine.get(cLine.size() - 2).getValue())) {
                    if (c) {
                        cLine.add(remainingTiles.get(i));
                        if (cLine.size() >= 3 && cLine.size() < 5) {
                            gLines.add(new ArrayList<>(cLine));
                        }
                        ArrayList<Tile> nextremainingTiles = new ArrayList<>(remainingTiles);
                        nextremainingTiles.remove(i);
                        addToLineNextTile(0, cLine, nextremainingTiles, gLines);
                        cLine.remove(cLine.size() - 1);
                    }
                }
            }
        }
    }

    // combining the made runs and groups into all possible board states they can create
    public void makeBoardState(ArrayList<ArrayList<Tile>> cBoard,ArrayList<ArrayList<Tile>> remainingSeq, ArrayList<ArrayList<ArrayList<Tile>>> gBoard){
        if(cBoard.isEmpty()){
            cBoard.add(remainingSeq.get(0));
            ArrayList<ArrayList<Tile>> nextRemainingSeq = new ArrayList<>(remainingSeq);
            nextRemainingSeq.remove(0);
            makeBoardState(cBoard, nextRemainingSeq, gBoard);
            cBoard.remove(cBoard.size()-1);
        }
        for(int i=0; i<remainingSeq.size(); i++){
            boolean c = true;
            for(Tile tile : remainingSeq.get(i)){
                if(!cBoard.isEmpty()) {
                    for (ArrayList<Tile> seq : cBoard) {
                        if (seq.contains(tile)) {
                            c = false;
                        }
                    }
                }
            }
            if(c){
                cBoard.add(remainingSeq.get(i));
                gBoard.add(new ArrayList<>(cBoard));
                ArrayList<ArrayList<Tile>> nextRemainingSeq = new ArrayList<>(remainingSeq);
                nextRemainingSeq.remove(i);
                makeBoardState(cBoard, nextRemainingSeq, gBoard);
                cBoard.remove(cBoard.size() - 1);
            }
        }
    }

    // making valid board states, checking if we don't take anything back to our hand
    public void makeValidBoardState(ArrayList<ArrayList<ArrayList<Tile>>> gBoard, Board board){
        ArrayList<ArrayList<Tile>> bList = board.getCurrentGameBoard();
        ArrayList<Tile> boardList = new ArrayList<>();
        for(ArrayList<Tile> sequence : bList){
            boardList.addAll(sequence);
        }
        for(ArrayList<ArrayList<Tile>> state : gBoard){
            ArrayList<Tile> tilesOnBoard = new ArrayList<>();
            for(ArrayList<Tile> seq : state){
                tilesOnBoard.addAll(seq);
            }
            if(tilesOnBoard.containsAll(boardList)){
                vBoard.add(state);
            }
        }
    }

    // getting possible moves in a structure of an arrayList of Object array that contains board and hand
    public void possibleMoves(Board board, ArrayList<Tile> hand){
        ArrayList<Tile> combined = new ArrayList<>();
        ArrayList<ArrayList<Tile>> bList = board.getCurrentGameBoard();
        ArrayList<Tile> b = new ArrayList<>();
        for(ArrayList<Tile> sequence : bList){
            combined.addAll(sequence);
        }
        combined.addAll(hand);
        addToLineNextTile(0, new ArrayList<>(), combined, gLines);
        addToLineNextTile(1, new ArrayList<>(), combined, gLines);
        makeBoardState(new ArrayList<>(), gLines, gBoard);
        makeValidBoardState(gBoard, board);
        for(int i=0;i<vBoard.size();i++){
            ArrayList<Tile> hl = getHandFromBoard(vBoard.get(i), new ArrayList<>(hand));
            Object[] arr = new Object[2];
            arr[0] = new Board(vBoard.get(i));
            arr[1] = hl;
            states.add(arr);
        }
    }

    public ArrayList<Tile> getHandFromBoard(ArrayList<ArrayList<Tile>> board, ArrayList<Tile> h){
        ArrayList<Tile> b = new ArrayList<>();
        for(ArrayList<Tile> seq : board){
            b.addAll(seq);
        }
        for (Tile tile : b) {
            h.remove(tile);
        }
        return h;
    }

    @Override
    public String toString(){
       String s = "";
        for(int i =0; i<states.size();i++){
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
        GameApp GA = new GameApp();

        ArrayList<ArrayList<Tile>> board = new ArrayList<>();
        ArrayList<Tile> seq = new ArrayList<>();

        seq.add(new Tile(Colour.YELLOW, Value.TWO));
        seq.add(new Tile(Colour.YELLOW, Value.ONE));
        seq.add(new Tile(Colour.YELLOW, Value.THREE));
        board.add(seq);
        ArrayList<Tile> seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.YELLOW, Value.FOUR));
        seq2.add(new Tile(Colour.BLACK, Value.FOUR));
        seq2.add(new Tile(Colour.RED, Value.FOUR));
        seq2.add(new Tile(Colour.YELLOW, Value.JOKER));
        board.add(seq2);

        Board b = new Board(board);

        ArrayList<Tile> hand = new ArrayList<>();

        hand.add(new Tile(Colour.BLACK, Value.FIVE));
        hand.add(new Tile(Colour.BLACK, Value.SIX));
        hand.add(new Tile(Colour.RED, Value.TEN));
        hand.add(new Tile(Colour.RED, Value.NINE));
        hand.add(new Tile(Colour.BLUE, Value.FOUR));

        GA.possibleMoves(b, hand);
        System.out.println(GA);


//        ArrayList<Tile> combined = new ArrayList<>();
//        ArrayList<ArrayList<Tile>> bList = b.getCurrentGameBoard();
//        for(ArrayList<Tile> sequence : bList){
//            combined.addAll(sequence);
//        }
//        combined.addAll(hand);
//        System.out.println(combined);
//
//        GA.addToLineNextTile(0, new ArrayList<>(), combined, GA.gLines);
//        //System.out.println(GA.gLines);
//
//        GA.addToLineNextTile(1, new ArrayList<>(), combined, GA.gLines);
//        System.out.println(GA.gLines);
//
//        GA.makeBoardState(new ArrayList<>(), GA.gLines, GA.gBoard);
//        for(int i = 0; i<GA.gBoard.size(); i++) {
//            System.out.println(GA.gBoard.get(i));
//        }

//        GA.makeValidBoardState(GA.gBoard, b);
//        for(int i = 0; i<GA.vBoard.size(); i++) {
//            System.out.println(GA.vBoard.get(i));
//        }

        // ArrayList<ArrayList<ArrayList<Tile>>> combinations =
        // GA.finalAllPossibleCombinations(combined, board);
        // combinations = GA.validBoardStates(board, combinations);
        //
        // for(ArrayList<ArrayList<Tile>> combination : combinations){
        // System.out.println(combination);
        // }
        //
        // long endTime = System.nanoTime();
        // long totalTime = endTime - startTime;
        // double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;
        // System.out.println(elapsedTimeInSecond);

        // public void getAllCombinationsHelper (ArrayList<Tile> remainingTiles, int
        // startIndex, ArrayList<ArrayList<Tile>> currentCombination,
        // ArrayList<ArrayList<ArrayList<Tile>>> result) {
        //
        // if (startIndex == remainingTiles.size()) {
        // result.add(new ArrayList<>(currentCombination));
        // return;
        // }
        //
        // ArrayList<Tile> singleElementGroup = new ArrayList<>();
        // singleElementGroup.add(remainingTiles.get(startIndex));
        // currentCombination.add(singleElementGroup);
        // getAllCombinationsHelper(remainingTiles, startIndex + 1, currentCombination,
        // result);
        // currentCombination.remove(currentCombination.size() - 1);
        //
        // for (int i = 0; i < currentCombination.size(); i++) {
        // ArrayList<Tile> existingGroup = new ArrayList<>(currentCombination.get(i));
        // existingGroup.add(remainingTiles.get(startIndex));
        //
        // ArrayList<ArrayList<Tile>> newGrouping = new ArrayList<>(currentCombination);
        // newGrouping.set(i, existingGroup);
        //
        // getAllCombinationsHelper(remainingTiles, startIndex + 1, newGrouping,
        // result);
        // }
        //
        // }

        // public boolean isValidCombination(ArrayList<ArrayList<Tile>> combination){
        // for(int i = 0; i <combination.size(); i++){
        // Player.sortByNum(combination.get(i), 0, combination.get(i).size()-1);
        // }
        // return Board.boardVerifier(combination);
        // }
        //
        // public ArrayList<ArrayList<ArrayList<Tile>>>
        // finalAllPossibleCombinations(ArrayList<Tile> tiles){
        // ArrayList<ArrayList<ArrayList<Tile>>> combinations =
        // getAllCombinations(tiles);
        // ArrayList<ArrayList<ArrayList<Tile>>> finalResult = new ArrayList<>();
        // for(ArrayList<ArrayList<Tile>> combination : combinations){
        // for(ArrayList<Tile> innerCombination : combination){
        // ArrayList<ArrayList<ArrayList<Tile>>> innerCombinations =
        // getAllCombinations(innerCombination);
        // for(ArrayList<ArrayList<Tile>> moreInnerCombination : innerCombinations){
        // if(isValidCombination(moreInnerCombination) &&
        // !finalResult.contains(moreInnerCombination)){
        // finalResult.add(moreInnerCombination);
        // }
        // }
        // }
        // }
        // return finalResult;
        // }

        // public boolean isValidBoardState(ArrayList<Tile> board,
        // ArrayList<ArrayList<Tile>> combination){
        // for(Tile tile : board){
        // int exists = 0;
        // for(ArrayList<Tile> sequence : combination){
        // if(sequence.contains(tile)){
        // exists += 1;
        // }
        // }
        // if(exists == 0){
        // return false;
        // }
        // }
        // return true;
        // }

        // public ArrayList<ArrayList<ArrayList<Tile>>> validBoardStates(ArrayList<Tile>
        // board, ArrayList<ArrayList<ArrayList<Tile>>> combinations){
        // ArrayList<ArrayList<ArrayList<Tile>>> validStates = new ArrayList<>();
        // for(ArrayList<ArrayList<Tile>> sequence : combinations){
        // if(isValidBoardState(board, sequence)){
        // validStates.add(sequence);
        // }
        // }
        // return validStates;
        // }

        // public ArrayList<ArrayList<ArrayList<Tile>>>
        // getAllCombinations(ArrayList<Tile> tiles){
        // result = new ArrayList<>();
        // ArrayList<Tile> currentCombination = new ArrayList<>();
        //
        // getAllCombinationsHelper(tiles, 0, new ArrayList<>(), result);
        // return result;
        // }

    }
}
