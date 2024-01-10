package com.example.rummikubfrontscreen.setup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameApp {

    GameSetup gs;
    ArrayList<Player> plrs;
    private Player curPlr;
    public ArrayList<Tile> tiles;
    private ArrayList<ArrayList<Tile>> gLines = new ArrayList<>();


    public ArrayList<ArrayList<ArrayList<Tile>>> result;

    public GameApp() {
        gs = new GameSetup();
        plrs = gs.getPlayers();
        curPlr = plrs.get(0);
        tiles = gs.getTiles();
    }

    public boolean isWinner(){
        return curPlr.getHand().isEmpty();
    }

    public void nextPlayer () {
        int i = plrs.indexOf(curPlr);
        if(i==plrs.size()-1){
            curPlr = plrs.get(0);
        }else{
            curPlr = plrs.get(i+1);
        }
    }

    public void previousPlayer(){
        int i = plrs.indexOf(curPlr);
        if(i==0){
            i = 4;
        }else{
            i = i-1;
        }
        curPlr = plrs.get(i);
    }

    public Tile draw(){
        Random rand = new Random();
        int i = rand.nextInt(tiles.size()-1);
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

//    public ArrayList<ArrayList<ArrayList<Tile>>> getAllCombinations(ArrayList<Tile> tiles){
//        result = new ArrayList<>();
//        ArrayList<Tile> currentCombination = new ArrayList<>();
//
//        getAllCombinationsHelper(tiles, 0, new ArrayList<>(), result);
//        return result;
//    }

    private void addToLineNextTile(int lineType, ArrayList<Tile> cLine, ArrayList<Tile> remainingTiles, ArrayList<ArrayList<Tile>> gLines){
        // lineType == 0 - group, need to sortByColour
        // lineType == 1 - run, need to sortByNum
        // cLine - currently build line
        // gLines - valid single lines
        // we build lines with first element or proceed it with joker/jokers
        if(lineType == 0){
            Player.sortByColour(remainingTiles);
        }else{
            Player.sortByNum(remainingTiles, 0, remainingTiles.size()-1);
        }
        if (cLine.isEmpty()){
            cLine.add(remainingTiles.get(0));
            ArrayList<Tile> nextremainingTiles = new ArrayList<>(remainingTiles);
            nextremainingTiles.remove(0);
            addToLineNextTile(lineType, cLine, nextremainingTiles, gLines);
            addToLineNextTile(lineType, cLine, nextremainingTiles,gLines);
            cLine.remove(cLine.size()-1);
        } else
        if (lineType==1){
            //it can be joker o a number with same color +1
            for (int i =0; i<remainingTiles.size(); i++){
                if ((remainingTiles.get(i).getColour()==cLine.get(cLine.size()-1).getColour() &&
                        remainingTiles.get(i).getInt()==cLine.get(cLine.size()-1).getInt()+1) ||
                        remainingTiles.get(i).getValue()==Value.JOKER ||
                        (remainingTiles.get(i).getColour()==cLine.get(cLine.size()-1).getColour() &&!cLine.isEmpty() &&
                        cLine.get(cLine.size()-1).getValue()==Value.JOKER &&
                        remainingTiles.get(i).getInt()==cLine.get(cLine.size()-2).getInt()+2)){
                    cLine.add(remainingTiles.get(i));
                    if (cLine.size()>=3){
                        gLines.add(new ArrayList<>(cLine));

                    }
                    ArrayList<Tile> nextremainingTiles = new ArrayList<>(remainingTiles);
                    nextremainingTiles.remove(i);
                    addToLineNextTile(1, cLine, nextremainingTiles, gLines);
                    cLine.remove(cLine.size()-1);
                }
            }
        }else{
            //it can be a joker or the same number with a different colour
            for (int i=0; i<remainingTiles.size(); i++){
                boolean c=true;
                for(Tile tile : cLine){
                    if(tile.getColour()==remainingTiles.get(i).getColour() &&
                            tile.getValue()==remainingTiles.get(i).getValue()){
                        c=false;
                    }
                }
                if ((remainingTiles.get(i).getColour()!=cLine.get(cLine.size()-1).getColour() &&
                        remainingTiles.get(i).getValue()==cLine.get(cLine.size()-1).getValue()) ||
                        remainingTiles.get(i).getValue()==Value.JOKER || (!cLine.isEmpty() &&
                        cLine.get(cLine.size()-1).getValue()==Value.JOKER &&
                        remainingTiles.get(i).getValue()==cLine.get(cLine.size()-2).getValue())){
                    if (c) {
                        cLine.add(remainingTiles.get(i));
                        if (cLine.size() >= 3) {
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


//    public void getAllCombinationsHelper (ArrayList<Tile> remainingTiles, int startIndex, ArrayList<ArrayList<Tile>> currentCombination, ArrayList<ArrayList<ArrayList<Tile>>> result) {
//
//        if (startIndex == remainingTiles.size()) {
//            result.add(new ArrayList<>(currentCombination));
//            return;
//        }
//
//        ArrayList<Tile> singleElementGroup = new ArrayList<>();
//        singleElementGroup.add(remainingTiles.get(startIndex));
//        currentCombination.add(singleElementGroup);
//        getAllCombinationsHelper(remainingTiles, startIndex + 1, currentCombination, result);
//        currentCombination.remove(currentCombination.size() - 1);
//
//        for (int i = 0; i < currentCombination.size(); i++) {
//            ArrayList<Tile> existingGroup = new ArrayList<>(currentCombination.get(i));
//            existingGroup.add(remainingTiles.get(startIndex));
//
//            ArrayList<ArrayList<Tile>> newGrouping = new ArrayList<>(currentCombination);
//            newGrouping.set(i, existingGroup);
//
//            getAllCombinationsHelper(remainingTiles, startIndex + 1, newGrouping, result);
//        }
//
//    }

//    public boolean isValidCombination(ArrayList<ArrayList<Tile>> combination){
//        for(int i = 0; i <combination.size(); i++){
//            Player.sortByNum(combination.get(i), 0, combination.get(i).size()-1);
//        }
//        return Board.boardVerifier(combination);
//    }
//
//    public ArrayList<ArrayList<ArrayList<Tile>>> finalAllPossibleCombinations(ArrayList<Tile> tiles){
//        ArrayList<ArrayList<ArrayList<Tile>>> combinations = getAllCombinations(tiles);
//        ArrayList<ArrayList<ArrayList<Tile>>> finalResult = new ArrayList<>();
//        for(ArrayList<ArrayList<Tile>> combination : combinations){
//            for(ArrayList<Tile> innerCombination : combination){
//                ArrayList<ArrayList<ArrayList<Tile>>> innerCombinations = getAllCombinations(innerCombination);
//                for(ArrayList<ArrayList<Tile>> moreInnerCombination : innerCombinations){
//                    if(isValidCombination(moreInnerCombination) && !finalResult.contains(moreInnerCombination)){
//                        finalResult.add(moreInnerCombination);
//                    }
//                }
//            }
//        }
//        return finalResult;
//    }

//    public boolean isValidBoardState(ArrayList<Tile> board, ArrayList<ArrayList<Tile>> combination){
//        for(Tile tile : board){
//            int exists = 0;
//            for(ArrayList<Tile> sequence : combination){
//                if(sequence.contains(tile)){
//                    exists += 1;
//                }
//            }
//            if(exists == 0){
//                return false;
//            }
//        }
//        return true;
//    }

//    public ArrayList<ArrayList<ArrayList<Tile>>> validBoardStates(ArrayList<Tile> board, ArrayList<ArrayList<ArrayList<Tile>>> combinations){
//        ArrayList<ArrayList<ArrayList<Tile>>> validStates = new ArrayList<>();
//        for(ArrayList<ArrayList<Tile>> sequence : combinations){
//            if(isValidBoardState(board, sequence)){
//                validStates.add(sequence);
//            }
//        }
//        return validStates;
//    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        GameApp GA = new GameApp();

        ArrayList<Tile> board = new ArrayList<>();

        board.add(new Tile(Colour.YELLOW, Value.TWO));
        board.add(new Tile(Colour.YELLOW, Value.ONE));
        board.add(new Tile(Colour.YELLOW, Value.FOUR));
        board.add(new Tile(Colour.BLACK, Value.FOUR));
        board.add(new Tile(Colour.RED, Value.FOUR));
        board.add(new Tile(Colour.YELLOW, Value.THREE));
        board.add(new Tile(Colour.YELLOW, Value.JOKER));

        ArrayList<Tile> hand = new ArrayList<>();

        hand.add(new Tile(Colour.BLACK, Value.FIVE));
        hand.add(new Tile(Colour.BLACK, Value.SIX));
        hand.add(new Tile(Colour.RED, Value.TEN));
        hand.add(new Tile(Colour.RED, Value.NINE));
        hand.add(new Tile(Colour.RED, Value.FOUR));

        ArrayList<Tile> combined = new ArrayList<>();
        combined.addAll(board);
        combined.addAll(hand);

        GA.addToLineNextTile(0, new ArrayList<>(), combined, GA.gLines);
        System.out.println(GA.gLines);

        GA.gLines.clear();
        GA.addToLineNextTile(1, new ArrayList<>(), combined, GA.gLines);
        System.out.println(GA.gLines);


//        ArrayList<ArrayList<ArrayList<Tile>>> combinations = GA.finalAllPossibleCombinations(combined, board);
//        combinations = GA.validBoardStates(board, combinations);
//
//        for(ArrayList<ArrayList<Tile>> combination : combinations){
//            System.out.println(combination);
//        }
//
//        long endTime   = System.nanoTime();
//        long totalTime = endTime - startTime;
//        double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;
//        System.out.println(elapsedTimeInSecond);




    }
}
