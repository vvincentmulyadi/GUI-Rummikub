package com.example.rummikubfrontscreen.setup.MCTS;

import com.example.rummikubfrontscreen.setup.Board;
import com.example.rummikubfrontscreen.setup.GameApp;
import com.example.rummikubfrontscreen.setup.GameSetup;
import com.example.rummikubfrontscreen.setup.GameTracker;
import com.example.rummikubfrontscreen.setup.LR;
import com.example.rummikubfrontscreen.setup.PossibleMoves;
import com.example.rummikubfrontscreen.setup.Tile;
import com.example.rummikubfrontscreen.setup.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class annSim {
    GameApp ga;
    GameSetup gs;
    LR model;
    int drawn = 0;
    MCTS mcts;

    public annSim() {
        model = new LR();
        MCTSmain mcmain = new MCTSmain(2);
        ga = mcmain.gameApp;
        gs = ga.getGs();
        mcts = mcmain.getMcts();
        mcts.MctsPlayThrough(20); 

        ArrayList<Node> inputs = mcts.inputs;
        ArrayList<Node> targets = mcts.targets;
        model.train(inputs, targets);
    }

    public void simulateGame() throws IOException {
        int numOfRounds = 0;
        drawn = 0;
        MCTSGameState games = new MCTSGameState(ga.getCurPlr(), ga.getGs().getBoard(), ga.getPlayers());
        while (!ga.isWinner()) {
            // System.out.println("new round");
            // ga.getGs().getBoard().addDrawPile(ga.getGs().getTiles());
            Board b = ga.getGs().getBoard();
            if (ga.getGs().getBoard().getDrawPile().isEmpty()) {
                System.out.println("no winner!");
                GameTracker.setWinner(-1);
                GameTracker.setNumOfMoves(numOfRounds);
                GameTracker.writeToFile();
                return;
            }

            ArrayList<Tile> h = ga.getCurPlr().getHand();
            System.out.println("player playing: " + ga.getCurPlr().getId() + ", hand: " + h);
            System.out.println("board before move: " + b.toString());

            ArrayList<Object[]> moves = PossibleMoves.possibleMoves(b, h, 0);
            if (ga.getCurPlr().getId() == 0) {
                // makeMove(moves, b);
                // if(drawn == -1){
                //     break;
                // }
                games = new MCTSGameState(ga.getCurPlr(), ga.getGs().getBoard(), ga.getPlayers());
                makeMoveMcts(games);
            } else {
                makeMoveLR(moves, h.size(), b);
                numOfRounds += 1;
                if(drawn == -1){
                    break;
                }
            }
            moves.clear();
            System.out.println("\n");
        }
        
        if(drawn == -1){
            GameTracker.setWinner(-1);
        }else if (ga.getCurPlr().getId() == 0) {
            GameTracker.setWinner(0);
        } else {
            GameTracker.setWinner(1);
        }
        GameTracker.setNumOfMoves(numOfRounds);
        GameTracker.writeToFile();
        System.out.println("The winner is: " + ga.getCurPlr().toString());
        System.out.println("Rounds: " + numOfRounds);
    }

    public boolean firstMove(ArrayList<Tile> move) {
        int sum = 0;
        for (Tile tile : move) {
            sum += tile.getInt();
        }
        if (sum >= 30) {
            return true;
        }
        return false;
    }

    public void makeMoveMcts(MCTSGameState gs){
        Node node = new Node(gs, null);
        int nodeInt = mcts.MctsAlgorithm(node, 50);
        Node node1 = node.getChildren().get(nodeInt);
        ga.getGs().getBoard().setCurrentGameBoard(node1.getGameState().getBoard().getCurrentGameBoard(),node1.getGameState().getBoard().getDrawPile());
        ga.getCurPlr().setHand(node1.getGameState().getPlayers().get(0).getHand());
        ga.nextPlayer();
    }

    public void makeMoveLR(ArrayList<Object[]> moves, int h, Board b) {
        Object[] predictedOutput = null;
        if (!moves.isEmpty()) {
            predictedOutput = model.predict(moves, h, b);
            if (predictedOutput != null) {
                ga.getGs().getBoard().setCurrentGameBoard(((Board) predictedOutput[0]).getCurrentGameBoard(),
                        ((Board) predictedOutput[0]).getDrawPile());
                ga.getCurPlr().setHand((ArrayList<Tile>) predictedOutput[1]);
                System.out.println("move chosen: ");
                Utils.print(predictedOutput);
            } else {
                System.out.println(ga.getCurPlr().getId());
                Tile tile = ga.draw();
                System.out.println("drawn: " + tile);
                if (tile == null) {
                    System.out.println("No winner!");
                    drawn = -1;
                }
            }
        } else {
            System.out.println(ga.getCurPlr().getId());
            Tile tile = ga.draw();
            System.out.println("drawn: " + tile);
            if (tile == null) {
                System.out.println("No winner!");
                drawn = -1;
            }
        }
        ga.nextPlayer();
    }

    public void makeMove(ArrayList<Object[]> moves, Board b) {
        ArrayList<ArrayList<Tile>> newBoard = new ArrayList<>();
        ArrayList<Tile> newHand = new ArrayList<>();
        ArrayList<Tile> newDrawPile = new ArrayList<>();
        if (!moves.isEmpty()) {
                int m = Integer.MAX_VALUE;
                int i = -1;
                for(Object[] state : moves) {
                    ArrayList<Tile> n = (ArrayList<Tile>) state[1];
                    Board ne = ((Board) state[0]);
                    int l = n.size();
                    if (l < m) {
                        m = l;
                        newBoard = new ArrayList<>(ne.getCurrentGameBoard());
                        newHand = new ArrayList<>(n);
                        newDrawPile = new ArrayList<>(ne.getDrawPile());
                    }
                }
            if (!newBoard.isEmpty() && newBoard != b.getCurrentGameBoard()) {
                System.out.println("move chosen: " + newBoard);
                ga.getGs().getBoard().setCurrentGameBoard(newBoard, newDrawPile);
                System.out.println(ga.getCurPlr().getId());
                ga.getCurPlr().setHand(newHand);
            } else {
                Tile tile = ga.draw();
                System.out.println("drawn: " + tile);
                if (tile == null) {
                    System.out.println("No winner!");
                    drawn = -1;
                }
            }
        } else {
            Tile tile = ga.draw();
            System.out.println("drawn: " + tile);
            if (tile == null) {
                System.out.println("No winner!");
                drawn = -1;
            }
        }
        ga.nextPlayer();
    }

    // public void makeMoveRand(ArrayList<Object[]> moves) {
    // Random rand = new Random();
    // int rn = rand.nextInt(moves.size());
    // Object[] move = moves.get(rn);
    // Board b = new Board(((Board) move[0]).getCurrentGameBoard());
    // ArrayList<Tile> h = new ArrayList<>((ArrayList<Tile>) move[1]);
    // System.out.println(ga.getCurPlr().getId());
    // ga.getGs().getBoard().setCurrentGameBoard(b.getCurrentGameBoard());
    // ga.getCurPlr().setHand(h);
    // }

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 20; i++) {
            annSim s = new annSim();
            s.simulateGame();
        }
    }
}
