package com.example.rummikubfrontscreen.setup.MCTS;
import com.example.rummikubfrontscreen.GameBoardController;
import com.example.rummikubfrontscreen.setup.GameSetup;
import com.example.rummikubfrontscreen.setup.*;
import java.util.*;

public class MCTSGameState extends GameApp{
    private GameApp board;
    private Player player;
    private List<Player> listofplayers;
    private Player aiPlayer;
    private int visitCount;
    private GameApp gameApp;
    private Player winnerIndex;
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
   public int updateState(GameApp newBoard, Player player)
   {
    if(gameApp.isWinner())
    {
        this.winnerIndex=player;
        this.board=newBoard;
        return 1;

    }
    else if()
   }
   public int getVisits() {
    return visitCount;
   }
   public Player getPlayer()
   {
    Player currentPlayer=gameApp.getCurPlr();
    return currentPlayer;
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
public ArrayList<ArrayList<Tile>> simpleMove(ArrayList<Tile> currentHand,Player currentPlayer,Board board)
{
    ArrayList<ArrayList<Tile>> lines = board.getCurrentGameBoard();
    //lines series in board
    for(ArrayList<Tile> line:lines)
    {
     for(Tile tile:currentHand)
     {
            ArrayList<Tile>newLine= new ArrayList<Tile>(line);
            newLine.add(tile);
             //index of a first tl which is not a joker
             int indexTile1 = 0;
             Tile tile1 = null;
        for(int j = 0; j < newLine.size();j++){
            if(line.get(j).getValue().equals(Value.JOKER)) continue;
            if (line == null) {
                tile1 = line.get(j);
                indexTile1 = j;
            }
        }
        if(board.checkGroup(newLine,tile))
        {
         lines.indexOf(line);
         lines.set(lines.indexOf(line),newLine);
         return lines;
        }
        if(board.checkRun(newLine,tile,indexTile1))
        { 
            ArrayList<Tile> newHand=new ArrayList<Tile>(currentHand);
            newHand.remove(tile);
            lines.indexOf(line);
            lines.set(lines.indexOf(line),newLine);
            outerloop:
            while(true)
              {
                for(Tile newTile:newHand)
                {
                newLine.add(newTile);
                for(int j = 0; j < newLine.size();j++){
                if(line.get(j).getValue().equals(Value.JOKER)) continue;
                if (line == null) {
                    tile1 = line.get(j);
                    indexTile1 = j;
                }
                }
                if(!board.checkRun(newLine,tile1,indexTile1))
                {
                    break outerloop;
                }
                lines.indexOf(line);
                lines.set(lines.indexOf(line),newLine);
              }
            newHand=new ArrayList<Tile>(currentHand);
                newHand.remove(tile);
            return lines;
                }
            
            }

        }
     }
     return lines;
    }

}

