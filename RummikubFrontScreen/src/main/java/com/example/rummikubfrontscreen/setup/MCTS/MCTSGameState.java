package com.example.rummikubfrontscreen.setup.MCTS;

import com.example.rummikubfrontscreen.GameBoardController;
import com.example.rummikubfrontscreen.setup.GameSetup;
import com.example.rummikubfrontscreen.setup.*;
import java.util.*;

public class MCTSGameState {
    private ArrayList<ArrayList<Tile>> board;
    private Player player;
    private List<Player> listofplayers;
    private Player aiPlayer;
    private int visitCount;
    private GameApp gameApp;

    private Player winnerIndex;
    // UCT change
    private int winScore;

    public MCTSGameState(Player player, ArrayList<ArrayList<Tile>> board, MCTSGameState state) {
        this.board = board;
        this.player = state.getPlayer();
        this.visitCount = state.getVisits();
        this.winScore = -1;
    }

    public int getVisits() {
        return visitCount;
    }

    public Player getPlayer() {
        Player currentPlayer = gameApp.getCurPlr();
        return currentPlayer;
    }

    public int getWinScore() {
        if (winScore == -1) {
            winScore = calculateWinScore();
        }

        return winScore;
    }

    private int calculateWinScore() {
        return 0;

    }

    public void incrementVisitCount() {
        visitCount++;
    }

    private int updateGameState(ArrayList<ArrayList<Tile>> newBoard, List<Player> playerList) {
        /*
         * just an integer update if players turn is finished and board is valid
         * if player won
         */return 0;

    }

    /*
     * TODO Subtract AI from all tiles
     */
    public ArrayList<Tile> getCurrentHand(Player currentPlayer) {
        ArrayList<Tile> aıHand = aiPlayer.getHand();
        ArrayList<Tile> currentHand = null;
        if (currentPlayer == aiPlayer) {
            currentHand = aıHand;
        } else {
            GameSetup gs = new GameSetup();
            currentHand = gs.generateHand();
        }

        return currentHand;
    }

    public ArrayList<ArrayList<Tile>> getBoard() {
        return this.board;
    }

    public ArrayList<ArrayList<Tile>> simpleMove(ArrayList<Tile> currentHand, Player currentPlayer, Board board) {
        ArrayList<ArrayList<Tile>> lines = board.getCurrentGameBoard();
        // lines series in board
        for (ArrayList<Tile> line : lines) {
            for (Tile tile : currentHand) {
                ArrayList<Tile> newLine = new ArrayList<Tile>(line);
                newLine.add(tile);
                // index of a first tl which is not a joker
                int indexTile1 = 0;
                Tile tile1 = null;
                for (int j = 0; j < newLine.size(); j++) {
                    if (line.get(j).getValue().equals(Value.JOKER))
                        continue;
                    if (line == null) {
                        tile1 = line.get(j);
                        indexTile1 = j;
                    }
                }
                if (board.checkGroup(newLine, tile)) {
                    lines.indexOf(line);
                    lines.set(lines.indexOf(line), newLine);
                    return lines;
                }
                if (board.checkRun(newLine, tile, indexTile1)) {
                    ArrayList<Tile> newHand = new ArrayList<Tile>(currentHand);
                    newHand.remove(tile);
                    lines.indexOf(line);
                    lines.set(lines.indexOf(line), newLine);
                    outerloop: while (true) {
                        for (Tile newTile : newHand) {
                            newLine.add(newTile);
                            for (int j = 0; j < newLine.size(); j++) {
                                if (line.get(j).getValue().equals(Value.JOKER))
                                    continue;
                                if (line == null) {
                                    tile1 = line.get(j);
                                    indexTile1 = j;
                                }
                            }
                            if (!board.checkRun(newLine, tile1, indexTile1)) {
                                break outerloop;
                            }
                            lines.indexOf(line);
                            lines.set(lines.indexOf(line), newLine);
                        }
                        newHand = new ArrayList<Tile>(currentHand);
                        newHand.remove(tile);
                        return lines;
                    }

                }

            }
        }
        return lines;
    }

    public boolean isWinner() {
        return false;
    }
}
