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
                expand(candidateNode, gameState);
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

    public void deepFirstSearch() {

        Node cur = root;
        while (cur.expandOwnMovesOnly()) {
            cur = cur.getRandomChildNode();
        }
    }

    private Node selectCandidateNode(Node root) {
        Node node = root;
        while (!node.isLeafNode()) {
            node = findBestNodeUCT(node);
        }
        return node;
    }

    private Node findBestNodeUCT(Node node) {
        double maxScore = Double.MIN_VALUE;
        Node bestNode = null;
        ArrayList<Node> children = node.getChildren();
        for (Node child : children) {
            // ımplentatıon todo
            double ucbValue = child.getUCTScore();// calcUCTValue(child, explorationParameter);
            if (ucbValue > maxScore) {
                maxScore = ucbValue;
                bestNode = child;
            }
        }
        return bestNode;
    }

    private void expand(Node node, MCTSGameState gameState) {

        // GameApp gameApp = new GameApp();
        ArrayList<Object[]> moveStates = PossibleMoves.possibleMoves(gameState.getBoard(), gameState.getCurrentHand(),
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
        System.out.println(simulateRandomPlayout(node));
        System.out.println("The best move is: " + node.getGameState().getBoard().toString());
    }

    private static int simulateRandomPlayout(Node node) {

        MCTSGameState gameState = node.getGameState();
        while (!gameState.isWinner()) {
            System.out.println("\n\nStil doing Random Playout");
            System.out.println(gameState);
            // GameApp gameApp = new GameApp();
            ArrayList<Object[]> moveStates = PossibleMoves.possibleMoves(gameState.getBoard(),
                    gameState.getCurrentHand(), 1);
            System.out.println("We got " + moveStates.size() + " moves");
            Object[] moveState = moveStates.get((int) (Math.random() * moveStates.size()));
            gameState = gameState.copyAndNextPlayer((Board) moveState[0], (ArrayList<Tile>) moveState[1]);
        }

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
