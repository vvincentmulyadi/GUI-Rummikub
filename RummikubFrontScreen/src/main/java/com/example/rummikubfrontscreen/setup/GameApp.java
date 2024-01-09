package com.example.rummikubfrontscreen.setup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameApp {

    GameSetup gs;
    ArrayList<Player> plrs;
    private Player curPlr;
    public ArrayList<Tile> tiles;

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

    public ArrayList<ArrayList<ArrayList<Tile>>> getAllCombinations(ArrayList<Tile> tiles){
        result = new ArrayList<>();
        ArrayList<Tile> currentCombination = new ArrayList<>();

        getAllCombinationsHelper(tiles, 0, new ArrayList<>(), result);
        return result;
    }

    public void getAllCombinationsHelper (ArrayList<Tile> remainingTiles, int startIndex, ArrayList<ArrayList<Tile>> currentCombination, ArrayList<ArrayList<ArrayList<Tile>>> result) {

        if (startIndex == remainingTiles.size()) {
            result.add(new ArrayList<>(currentCombination));
            return;
        }

        ArrayList<Tile> singleElementGroup = new ArrayList<>();
        singleElementGroup.add(remainingTiles.get(startIndex));
        currentCombination.add(singleElementGroup);
        getAllCombinationsHelper(remainingTiles, startIndex + 1, currentCombination, result);
        currentCombination.remove(currentCombination.size() - 1);

        for (int i = 0; i < currentCombination.size(); i++) {
            ArrayList<Tile> existingGroup = new ArrayList<>(currentCombination.get(i));
            existingGroup.add(remainingTiles.get(startIndex));

            ArrayList<ArrayList<Tile>> newGrouping = new ArrayList<>(currentCombination);
            newGrouping.set(i, existingGroup);

            getAllCombinationsHelper(remainingTiles, startIndex + 1, newGrouping, result);
        }

    }

    public boolean isValidCombination(ArrayList<ArrayList<Tile>> combination){
        for(int i = 0; i <combination.size(); i++){
            Player.sortByNum(combination.get(i), 0, combination.get(i).size()-1);
        }
        return Board.boardVerifier(combination);
    }

    public ArrayList<ArrayList<ArrayList<Tile>>> finalAllPossibleCombinations(ArrayList<Tile> tiles){
        ArrayList<ArrayList<ArrayList<Tile>>> combinations = getAllCombinations(tiles);
        ArrayList<ArrayList<ArrayList<Tile>>> finalResult = new ArrayList<>();
        for(ArrayList<ArrayList<Tile>> combination : combinations){
            for(ArrayList<Tile> innerCombination : combination){
                ArrayList<ArrayList<ArrayList<Tile>>> innerCombinations = getAllCombinations(innerCombination);
                for(ArrayList<ArrayList<Tile>> moreInnerCombination : innerCombinations){
                    if(isValidCombination(moreInnerCombination) && !finalResult.contains(moreInnerCombination)){
                        finalResult.add(moreInnerCombination);
                    }
                }
            }
        }
        return finalResult;
    }

    public boolean isValidBoardState(ArrayList<Tile> board, ArrayList<ArrayList<Tile>> combination){
        for(Tile tile : board){
            int exists = 0;
            for(ArrayList<Tile> sequence : combination){
                if(sequence.contains(tile)){
                    exists += 1;
                }
            }
            if(exists == 0){
                return false;
            }
        }
        return true;
    }

    public ArrayList<ArrayList<ArrayList<Tile>>> validBoardStates(ArrayList<Tile> board, ArrayList<ArrayList<ArrayList<Tile>>> combinations){
        ArrayList<ArrayList<ArrayList<Tile>>> validStates = new ArrayList<>();
        for(ArrayList<ArrayList<Tile>> sequence : combinations){
            if(isValidBoardState(board, sequence)){
                validStates.add(sequence);
            }
        }
        return validStates;
    }

    public static void main(String[] args) {
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

        ArrayList<Tile> combined = new ArrayList<>();
        combined.addAll(board);
        combined.addAll(hand);

        ArrayList<ArrayList<ArrayList<Tile>>> combinations = GA.finalAllPossibleCombinations(combined);
        combinations = GA.validBoardStates(board, combinations);

        for(ArrayList<ArrayList<Tile>> combination : combinations){
            System.out.println(combination);
        }


    }
}
