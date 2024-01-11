package com.example.rummikubfrontscreen.setup.MCTS;

import com.example.rummikubfrontscreen.GameBoardController;
import com.example.rummikubfrontscreen.setup.*;
import java.util.*;

public class MCTSGameState {
    private Board board;
    private Player player;
    private ArrayList<Player> listofplayers;
    private Player aiPlayer;
    private int visitCount;
    private boolean[] playersEndTurn = new boolean[4];
    private Player winnerIndex;
    // UCT change
    private int winScore;
    private Random randomizer;

    public MCTSGameState(Player player, Board board, ArrayList<Player> playerList) {
        this.board = board;
        this.player = player;
        this.listofplayers = playerList;
        this.visitCount = 0;
        this.winScore = 0;
        this.randomizer = new Random();
        this.aiPlayer = playerList.get(0);
    }

    public MCTSGameState copyAndNextPlayer(Board newBoard, ArrayList<Tile> newHand) {
        int indexOfCurrentPlayer = listofplayers.indexOf(player);
        ArrayList<Player> newPlayerList = (ArrayList<Player>) listofplayers.clone();
        Player clonedPlayer = player.clone(newHand);
        newPlayerList.set(indexOfCurrentPlayer, clonedPlayer);

        MCTSGameState gs = new MCTSGameState(clonedPlayer, newBoard, newPlayerList);
        gs.changeCurrentPlayersHand(newHand);

        gs.nextPlayer();
        return gs;
    }

    public void changeCurrentPlayersHand(ArrayList<Tile> newHand) {
        player.setHand(newHand);
    }

    private void nextPlayer() {
        int indexOfNextPlayer = (this.getPlayers().indexOf(player) + 1) % this.getPlayers().size();
        player = this.getPlayers().get(indexOfNextPlayer);
    }

    public int getVisits() {
        return visitCount;
    }

    public int getWinScore() {
        return winScore;
    }

    public void incrementVisitCount() {
        visitCount++;
    }

    /*
     * TODO Subtract AI from all tiles
     */
    public ArrayList<Tile> getCurrentHand(Player currentPlayer) {
        ArrayList<Tile> aiHand = aiPlayer.getHand();
        ArrayList<Tile> currentHand = null;
        // return player hand right now because we would run it with dmcts where the
        // hands are known
        if (currentPlayer == currentPlayer) {
            currentHand = aiHand;
        } else {
            GameSetup gs = new GameSetup();
            currentHand = gs.generateHand();
        }

        return currentPlayer.getHand();
    }

    public ArrayList<Object[]> getOwnMoveStates() {

        ArrayList<Object[]> ownMoveStates = MCTSAction.ownMoveGroup(getCurrentHand(player), board);
        return ownMoveStates;
    }

    public ArrayList<ArrayList<Tile>> getLegalMoves(ArrayList<Tile> currentHand, Player currentPlayer) {
        GameSetup gameSetup = new GameSetup();
        Board curBoard = gameSetup.getBoard();
        ArrayList<ArrayList<Tile>> legalMoves = new ArrayList<ArrayList<Tile>>();

        /*
         * TODO figure out how to get the size of the board and then if size is zero
         * then use the checkers to
         * add move to list of legal moves if value of runs and rovs are above 30 move
         * to next tile
         * if not move to next player
         * then check if 1 tile form set at the board
         * if not move to next tile on hand
         * then check if 3 tiles form a set on the board
         * check until every tile in players hand has been iterated over and then end
         * turn for AI
         * create another AIplayer class
         * 
         */

        return legalMoves;
    }

    public Board getBoard() {
        return this.board;
    }

    public boolean isWinner() {
        return false;
    }

    public Player getCurPlayer() {
        return this.player;
    }

    public ArrayList<Player> getPlayers() {
        return this.listofplayers;
    }

    @Override
    public MCTSGameState clone() {
        MCTSGameState gs = new MCTSGameState(this.player, this.board.clone(),
                (ArrayList<Player>) this.listofplayers.clone());
        return gs;
    }

    @Override
    public String toString() {
        return "Board: \n" + board.toString() + " Hand: " + aiPlayer.getHand().toString();
    }
}