package com.example.rummikubfrontscreen.setup.MCTS;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.rummikubfrontscreen.setup.Board;
import com.example.rummikubfrontscreen.setup.Colour;
import com.example.rummikubfrontscreen.setup.GameApp;
import com.example.rummikubfrontscreen.setup.PossibleMoves;
import com.example.rummikubfrontscreen.setup.Tile;
import com.example.rummikubfrontscreen.setup.Value;

public class MCTS {
    private MCTSGameState gameState;
    private Node root;
    private static final int MAX_ITERATIONS = 1000;
    static double explorationParameter = 1.4;
    private ArrayList<ArrayList<Tile>> board;
    private ArrayList<Tile> deck;
    ArrayList<Tile> guessedOpponentDeck;
    private String time;

    public MCTS(MCTSGameState gameState) {
        this.gameState = gameState;
        this.root = new Node(gameState, null);
    }

    public Node findNextMove() {
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            Node candidateNode = selectCandidateNode(root);
            if (!gameState.isWinner()) {
                expand(candidateNode);
            }

            Node nodeToExplore = candidateNode;
            if (!candidateNode.getChildren().isEmpty()) {
                nodeToExplore = candidateNode.getRandomChildNode();
            }

            int playoutResult = 0;
            backPropagate(nodeToExplore, playoutResult);
        }
        return root.selection();
    }

    private Node selectCandidateNode(Node root) {
        Node node = root;
        while (!node.isLeafNode()) {
            node = findBestNodeUCT(node);
        }
        return node;
    }

    private Node findBestNodeUCT(Node node) {
        double maxScore = -1;
        Node bestNode = null;
        ArrayList<Node> children = node.getChildren();
        for (Node child : children) {
            double ucbValue = child.getUCTScore();
            System.out.println(maxScore);
            if (ucbValue > maxScore) {
                maxScore = ucbValue;
                bestNode = child;
            }
        }
        if (bestNode == null) {
            System.out.println("BestNode is null in findBestNodeUCT");
        }
        return bestNode;
    }

    private ArrayList<Node> MctsAlgorithm(int iterations) {

        for (int i = 0; i < iterations; i++) {
            System.out.println(root);
            Node candidateNode = selectCandidateNode(root);

            if (!gameState.isWinner()) {
                expand(candidateNode);
            }

            Node nodeToExplore = candidateNode;
            if (candidateNode.getChildren().isEmpty()) {
                candidateNode.expandOwnMovesOnly();
                nodeToExplore = candidateNode.getRandomChildNode();
            }
            System.out.println("Nodes tiles that will be randomly played out now");
            System.out.println(nodeToExplore.getAmountOfTiles());

            int playoutResult = simulateRandomPlayout(nodeToExplore);
            backPropagate(nodeToExplore, playoutResult);
        }
        return root.getChildren();
    }

    private void expand(Node node) {

        // GameApp gameApp = new GameApp();
        ArrayList<Object[]> moveStates = PossibleMoves.possibleMoves(node.getGameState().getBoard(),
                node.getGameState().getCurrentHand(),
                0);
        MCTSGameState nodeGameState = node.getGameState();
        for (Object[] moveState : moveStates) {
            MCTSGameState newGameState = nodeGameState.copyAndNextPlayer((Board) moveState[0],
                    (ArrayList<Tile>) moveState[1]);
            Node childNode = new Node(newGameState, node);
            node.addChild(childNode);
        }
        System.out.println("\n\nYou just ran expand check pls if the right player is playing in this node");
    }

    public static void main(String[] args) {

        MCTSmain mcmain = new MCTSmain();
        MCTS mcts = mcmain.getMcts();
        Node node = mcmain.getRoot();

        // System.out.println(simulateRandomPlayout(node));

        System.out.println("This is the root\n" + mcts.root);

        mcts.root.expandOwnMovesOnly();
        System.out.println("The root has " + mcts.root.getChildren().size() + " many children");

        mcts.MctsAlgorithm(5);
        for (Node child : mcts.root.getChildren()) {
            System.out.println(child);
        }
    }

    private static int simulateRandomPlayout(Node node) {

        System.out.println(node);
        MCTSGameState gameState = node.getGameState().clone();
        System.out.println(gameState);

        while (!gameState.isWinner()) {
            System.out.println("\n\nStil doing Random Playout");
            System.out.println(gameState);
            // GameApp gameApp = new GameApp();
            ArrayList<Object[]> moveStates = PossibleMoves.possibleMoves(gameState.getBoard(),
                    gameState.getCurrentHand(), 1);
            System.out.println("We got " + moveStates.size() + " moves");
            if (moveStates.isEmpty()) {
                System.out.println("No more moves left");
                return 0;
            }

            Object[] moveState = moveStates.get((int) (Math.random() * moveStates.size()));
            gameState = gameState.copyAndNextPlayer((Board) moveState[0], (ArrayList<Tile>) moveState[1]);
        }
        System.out.println("\n\nRandom Playout is done");
        System.out.println(gameState);

        int result = gameState.isAIPlayerWinner();

        return result;
    }

    /**
     * 
     * @param node
     * @param playoutResult either 0 or 1 depening on win or loss
     */
    private void backPropagate(Node node, int playoutResult) {
        while (node != null) {
            node.addPlayout(playoutResult);
            node = node.getParent();
        }
    }

    public ArrayList<Tile> getGuessedOpponentDeck(ArrayList<Tile> knownTiles) {
        ArrayList<Double> probabilities = null;// calculateDeckProbabilities(knownTiles);
        ArrayList<Tile> guessedOpponentDeck = new ArrayList<Tile>();
        for (int i = 0; i < probabilities.size(); i++) {
            double probability = probabilities.get(i);
            if (probability > 0.5) {
                Tile tile = new Tile(Colour.values()[i / 13], Value.values()[i % 13]);
                guessedOpponentDeck.add(tile);
            }
        }
        return guessedOpponentDeck;
    }

    public ArrayList<Tile> convert(ArrayList<ArrayList<Tile>> board) {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (ArrayList<Tile> row : board) {
            tiles.addAll(row);
        }
        return tiles;
    }
}
