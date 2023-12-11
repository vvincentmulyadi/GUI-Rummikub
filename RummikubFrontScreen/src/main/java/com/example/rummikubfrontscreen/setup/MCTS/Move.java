package com.example.rummikubfrontscreen.setup.MCTS;
import com.example.rummikubfrontscreen.setup.*;

import javafx.animation.PauseTransition;

import java.util.*;
public class Move {
    //everything simplified to the bare parameters to avoid tight decoupling here
    private ArrayList<ArrayList<Tile>>initialBoard;
    private ArrayList<Tile> initialHand;
    private ArrayList<Tile>outputHand;
    private ArrayList<ArrayList<Tile>>outputboard;
    private ArrayList<ArrayList<Tile>>possibleCombos;
    private ArrayList<ArrayList<Tile>>allPossibleCombos;
    private ArrayList<Tile>deck;
    private ArrayList<ArrayList<Tile>> randomMove;
    private boolean endTurn;
    private Random randomizer;
public Move(ArrayList<ArrayList<Tile>>board, ArrayList<Tile>hand){
endTurn=false;
randomizer=new Random();
this.initialBoard=board;
this.initialHand=hand;
this.outputboard=new ArrayList<ArrayList<Tile>>();
this.outputHand=new ArrayList<Tile>();
this.possibleCombos=possibleMoves(convert(board),this.initialHand);


}
public ArrayList<ArrayList<Tile>> getRandomMoves() 
    {   
        
        return this.randomMove;
    }

public void makeRandomMove(ArrayList<ArrayList<Tile>>currentBoard, ArrayList<Tile>currentHand, ArrayList<Tile> deck,int setIndex){
    if(setIndex==this.possibleCombos.size()&&Board.boardVerifier(currentBoard))
    {
        endTurn=true;
        return;
    }
    else if(setIndex==this.possibleCombos.size())
    {
        return;
    }
    for(int i=setIndex; i<this.possibleCombos.size(); i++)
    {
        if(endTurn||isSetPresent(deck, possibleCombos.get(i)))
        {
            continue;
        }
    }

}
public ArrayList<Tile> convert(ArrayList<ArrayList<Tile>> currentBoard)
{
    ArrayList<Tile> result=new ArrayList<>();
    for(ArrayList<Tile> line:currentBoard)
    {
        for(Tile tile:line)
        {
            result.add(tile);
        }
    }
    return result;
}
private ArrayList<ArrayList<Tile>> possibleMoves(ArrayList<Tile> convertedBoard,ArrayList<Tile>initialHand)
{
    ArrayList<Tile>checkedTiles=new ArrayList<>();
    ArrayList<ArrayList<Tile>> possibleSets=new ArrayList<>();
    for(Tile tile:initialHand)
    {
        checkedTiles.add(tile);
    }
    for(Tile tile:convertedBoard)
    {
        checkedTiles.add(tile);
    }
    for(ArrayList<Tile> series:checkedTiles)
    {
        
    }
    
}
public boolean isSetPresent(ArrayList<Tile> hand, ArrayList<Tile> set)
{
    if(hand.isEmpty())
    {
        return false;
    }
    Set<Tile>checkHand=new HashSet<>(hand);
    Set<Tile>checkSet=new HashSet<>(set);
    return checkHand.containsAll(checkSet);

}
private static void remove(List<Tile> tileList, ArrayList<Tile> elementsToRemove){
    for(Tile tile:elementsToRemove){
        tileList.remove(tile);
    }
}
private ArrayList<ArrayList<Tile>> copy(ArrayList<ArrayList<Tile>>  original) {
    ArrayList<ArrayList<Tile>>  boardCopy = new ArrayList<>();
    for (ArrayList<ArrayList<Tile>>  row : original) {
        ArrayList<ArrayList<Tile>> rowCopy = new ArrayList<>();
        for (Tile tile : row) {
            rowCopy.add(tile.deepCopy()); // Assuming Tile has a deepCopy() method
        }
        boardCopy.add(rowCopy);
    }
    return new Board(boardCopy);
}   

}