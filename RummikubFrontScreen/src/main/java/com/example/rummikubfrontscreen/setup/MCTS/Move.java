package com.example.rummikubfrontscreen.setup.MCTS;
import com.example.rummikubfrontscreen.setup.*;

import javafx.animation.PauseTransition;

import java.util.*;
public class Move {
    //everything simplified to the bare parameters to avoid tight decoupling here
    private ArrayList<ArrayList<Tile>>initialBoard;
    private ArrayList<Tile> initialHand;
    private ArrayList<Tile>outputHand;
    private ArrayList<ArrayList<Tile>>outputBoard;
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
this.outputBoard=new ArrayList<>();
this.outputHand=new ArrayList<Tile>();
this.possibleCombos=possibleMoves(convert(board),this.initialHand);


}
public ArrayList<ArrayList<Tile>> getRandomMoves() 
    {   
        
        return this.randomMove;
    }

/*public void makeRandomMove(ArrayList<ArrayList<Tile>>currentBoard, ArrayList<Tile>currentHand, ArrayList<Tile> deck,int setIndex){
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
        ArrayList<ArrayList<Tile>>copyBoard=copy(currentBoard);
        copyBoard.add(this.possibleCombos.get(i));
        ArrayList<Tile> currentAvailableTiles=new ArrayList<Tile>(deck);
        remove(deck, currentAvailableTiles);
        if(Board.boardVerifier(copyBoard))
        {
            outputBoard.addAll(copyBoard);
        }
        return makeRandomMove(copyBoard, currentAvailableTiles,i);
    }

} */
public ArrayList<ArrayList<Tile>> copy(ArrayList<ArrayList<Tile>>original){
    ArrayList<ArrayList<Tile>> copyArrayList=new ArrayList<>();
    for(ArrayList<Tile>inArrayList:original){
        ArrayList<Tile>tempCopy=new ArrayList<>(inArrayList);
        copyArrayList.add(tempCopy);
    }
    return copyArrayList;
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
    
    for(Tile tile:initialHand)
    {
        checkedTiles.add(tile);
    }
    for(Tile tile:convertedBoard)
    {
        checkedTiles.add(tile);
    }
    ArrayList<ArrayList<Tile>> possibleSets=new ArrayList<>();
    for(ArrayList<Tile> series:possibleSets)
    {
        if(isSetPresent(checkedTiles, series))
        {
            possibleSets.add(series); //
        }
    }
    return possibleSets;
}
private static boolean isSetPresent(ArrayList<Tile> hand, ArrayList<Tile> set)
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





}