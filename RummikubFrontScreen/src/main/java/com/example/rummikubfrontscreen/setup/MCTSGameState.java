package com.example.rummikubfrontscreen.setup;
import com.example.rummikubfrontscreen.GameBoardController;
import com.example.rummikubfrontscreen.setup.*;
import java.util.*;

public class MCTSGameState extends GameApp {
    private GameApp board;
    private BoardOverhead hand;
    private BoardOverhead bohe;
    private Player player;
    private Player aiPlayer;
    private int visitCount;
    //UCT change
    private int winScore;

    
    public MCTSGameState() {
        board = new GameApp();
        
    }
    public MCTSGameState(MCTSGameState state)
    {
        this.board = new GameApp();
        this.player = state.getPlayer();
        this.visitCount = state.getVisits();
        this.winScore=state.getWinScore();
    }
    
   public int getVisits() {
    return visitCount;
   }
   public Player getPlayer()
   {
    Player currentPlayer=getCurPlr();
    return currentPlayer;
   }
   public int getWinScore()
   {
      return winScore;
   }
   public BoardOverhead getGameState()
   {
    return board.getBohe();
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
{   GameSetup gameSetup = new GameSetup();
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

}

}
