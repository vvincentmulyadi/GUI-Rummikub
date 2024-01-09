package com.example.rummikubfrontscreen.setup.MCTS;
import com.example.rummikubfrontscreen.GameBoardController;
import com.example.rummikubfrontscreen.setup.*;
import java.util.*;

public class MCTSGameState{
    private ArrayList<ArrayList<Tile>> board;
    private ArrayList<Tile> deck;
    private Player player;
    private List<Player> listofplayers;
    private Player aiPlayer;
    private int visitCount;
    private boolean[] playersEndTurn=new boolean[4];
    private Player winnerIndex;
    //UCT change
    private int winScore;
    private Random randomizer;

    
    public MCTSGameState(Player player, ArrayList<ArrayList<Tile>> board,ArrayList<Tile> deck,List<Player> playerList) {
        this.board = board;
        this.deck = deck;   
        this.player = player;
        this.listofplayers=playerList;
        this.visitCount = 0;
        this.winScore=0;
        this.randomizer = new Random();
        this.aiPlayer=playerList.get(0);
    }
   
   public int getVisits() {
    return visitCount;
   }
   
   public int getWinScore()
   {
      return winScore;
   }

   public void incrementVisitCount() {
    visitCount++;
   }
  

   /*
    * TODO Subtract AI from all tiles
    */
   public ArrayList<Tile> getCurrentHand(Player currentPlayer){
    ArrayList<Tile> aıHand = aiPlayer.getHand();
     ArrayList<Tile>  currentHand = null; 
    if(currentPlayer == aiPlayer)
    {
      currentHand=aıHand;   
    }
    else{
        GameSetup gs = new GameSetup();
         currentHand= gs.generateHand();
    }

    return currentHand;
   }
public ArrayList<ArrayList<Tile>> getLegalMoves(ArrayList<Tile>currentHand,Player currentPlayer) {
   GameSetup gameSetup = new GameSetup();
    Board curBoard= gameSetup.getBoard();
    ArrayList<ArrayList<Tile>> legalMoves = new ArrayList<ArrayList<Tile>>();

    /*
     * TODO figure out how to get the size of the board and then if size is zero then use the checkers to 
     * add move to list of legal moves if value of runs and rovs are above 30 move to next tile
     * if not move to next player
     * then check if 1 tile form set at the board
     * if not move to next tile on hand
     * then check if 3 tiles form a set on the board
     * check until every tile in players hand has been iterated over and then end turn for AI
     * create another AIplayer class
     * 
     */
    
    


    
    
    return legalMoves;
}
public ArrayList<ArrayList<Tile>> getBoard()
{
    return this.board;
}
public ArrayList<Tile> getDeck()
{
    return this.deck;
}


public boolean isWinner() {
    return false;
}
}




