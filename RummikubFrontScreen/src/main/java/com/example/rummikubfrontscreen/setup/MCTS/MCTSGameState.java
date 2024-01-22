package com.example.rummikubfrontscreen.setup.MCTS;

import com.example.rummikubfrontscreen.setup.*;
import java.util.*;

/**
 * The `MCTSGameState` class represents the state of a game in the Monte Carlo Tree Search algorithm,
 * including the board, players, visit count, and win score.
 */
public class MCTSGameState {
    private Board board;
    private Player player;
    private ArrayList<Player> listofplayers;
    private Player aiPlayer;
    private int visitCount;
    // UCT change
    private int winScore;

    public MCTSGameState(Player player, Board board, ArrayList<Player> playerList) {
        this.board = board;
        this.player = player;
        this.listofplayers = playerList;
        this.visitCount = 0;
        this.winScore = 0;
        this.aiPlayer = playerList.get(0);
    }

    public int hasWinner() {
        for (Player player : listofplayers) {
            if (player.getHand().isEmpty()) {
                return 1;
            }
        }
        return 0;
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

    public ArrayList<Tile> getCurrentHand(Player currentPlayer) {
        ArrayList<Tile> aiHand = aiPlayer.getHand();
        ArrayList<Tile> currentHand = null;
        if (currentPlayer == currentPlayer) {
            currentHand = aiHand;
        } else {
            GameSetup gs = new GameSetup();
            currentHand = gs.generateHand();
        }

        return currentPlayer.getHand();
    }

    public ArrayList<Tile> getCurrentHand() {
        return player.getHand();
    }

    public ArrayList<Tile> getAlltileOnField() {
        ArrayList<ArrayList<Tile>> boardTiles = board.getCurrentGameBoard();
        ArrayList<Tile> allT = new ArrayList<>();
        for (ArrayList<Tile> meld : boardTiles) {
            for (Tile tile : meld) {
                allT.add(tile);
            }
        }
        return allT;
    }

    public ArrayList<Object[]> getOwnMoveStates() {

        ArrayList<Object[]> ownMoveStates;// = MCTSAction.ownMoveGroup(board, getCurrentHand(player));


        ownMoveStates = PossibleMoves.possibleMoves(board, getCurrentHand(player), 0);

        return ownMoveStates;
    }

    public Board getBoard() {
        return this.board;
    }

    public boolean isWinner() {
        for (Player player : listofplayers) {
            if (player.getHand().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public Player getWinner() {
        for (Player player : listofplayers) {
            if (player.getHand().isEmpty()) {
                return player;
            }
        }
        return null;
    }

    public int isAIPlayerWinner() {

        if (aiPlayer.getHand().isEmpty()) {
            return 1;
        }
        for (Player player : listofplayers) {
            if (player.getHand().isEmpty()) {
                return 0;
            }
        }
        return -1;
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
        String str = "";
        str = "\nPlayer " + player.getId() + " is playing\n";
        for (Player player : listofplayers) {
            str += player.toString() + "\n";
        }
        return str + "\nBoard looks like: \n" + board.toString();
    }
}
