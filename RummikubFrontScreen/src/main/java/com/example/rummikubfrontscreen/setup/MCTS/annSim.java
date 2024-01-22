package com.example.rummikubfrontscreen.setup.MCTS;

import com.example.rummikubfrontscreen.setup.Board;
import com.example.rummikubfrontscreen.setup.GameApp;
import com.example.rummikubfrontscreen.setup.GameSetup;
import com.example.rummikubfrontscreen.setup.GameTracker;
import com.example.rummikubfrontscreen.setup.LinearRegressionModel;
import com.example.rummikubfrontscreen.setup.PossibleMoves;
import com.example.rummikubfrontscreen.setup.Tile;
import com.example.rummikubfrontscreen.setup.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * The `annSim` class is a simulation of a game that uses both the MCTS (Monte Carlo Tree Search)
 * algorithm and a Linear Regression (LR) model to make moves.
 */
public class annSim {
    GameApp ga;
    GameSetup gs;
    LinearRegressionModel model;

    int drawn = 0;
    MCTS mcts;

    public annSim() {
        model = new LinearRegressionModel();
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

    /**
     * The function "makeMoveMcts" uses the Monte Carlo Tree Search algorithm to make a move in a game,
     * updating the game state and player's hand accordingly.
     * 
     * @param gs The parameter "gs" is of type MCTSGameState. It represents the current state of the
     * game in the Monte Carlo Tree Search algorithm.
     */
    public void makeMoveMcts(MCTSGameState gs){
        Node node = new Node(gs, null);
        int nodeInt = mcts.MctsAlgorithm(node, 50);
        Node node1 = node.getChildren().get(nodeInt);
        ga.getGs().getBoard().setCurrentGameBoard(node1.getGameState().getBoard().getCurrentGameBoard(),node1.getGameState().getBoard().getDrawPile());
        ga.getCurPlr().setHand(node1.getGameState().getPlayers().get(0).getHand());
        ga.nextPlayer();
    }

    /**
     * The function `makeMoveLR` makes a move in a game by predicting the next move using a machine
     * learning model, updating the game board and player's hand accordingly, and printing the chosen
     * move.
     * 
     * @param moves An ArrayList of Object arrays representing the possible moves in the game.
     * @param h The parameter "h" is not explicitly defined in the code snippet provided. It is likely
     * a variable representing the current player's hand.
     * @param b The parameter "b" is an object of the class "Board".
     */
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

    /**
     * The function `makeMove` selects the best move from a list of moves and updates the game board
     * and player's hand accordingly, or draws a tile if no moves are available.
     * 
     * @param moves An ArrayList of Object arrays. Each Object array contains two elements: a Board
     * object and an ArrayList of Tile objects.
     * @param b The parameter `b` is an instance of the `Board` class.
     */
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

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 20; i++) {
            annSim s = new annSim();
            s.simulateGame();
        }
    }
}
