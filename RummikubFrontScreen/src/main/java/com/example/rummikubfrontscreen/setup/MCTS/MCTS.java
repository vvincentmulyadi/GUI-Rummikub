package com.example.rummikubfrontscreen.setup.MCTS;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.rummikubfrontscreen.setup.Tile;

public class MCTS {
    private MCTSGameState gameState;
    private Node root;
    private static final int MAX_ITERATIONS = 1000;
    static double explorationParameter = 1.4;
    private ArrayList<ArrayList<Tile>> board;
    private ArrayList<Tile> deck;
    ArrayList<Tile> guessedOpponentDeck;
    private String time;

    public MCTS(MCTSGameState gameState, ArrayList<ArrayList<Tile>> board, ArrayList<Tile> deck,int numberofTiles) {
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
        ArrayList<MCTSGameState> legalMoves = null;// gameState.getLegalMoves(); // Implement this method
        for (MCTSGameState move : legalMoves) {
            Node childNode = new Node(move, node);knownTiles
            node.addChild(childNode);
        }
    }

    private int simulateRandomPlayout(Node node)     {
        // ArrayList<Tile> currentHand = this.gameState.getCurrentHand();
        // Implement the simulation logic based on your game rules
        // Move randMove=new Move(this.gameState.getBoard(),currentHand);// Replace with
        // the actual result    
        int result = 0;

        return result;
    }

    private void backPropagate(Node node, int playoutResult) {
        while (node != null) {
            node.visitCount++;
            node.uctValue += playoutResult;
            node = node.getParent();
        }
    }
    public ArrayList<Double> calculateDeckProbabilities(ArrayList<Tile> knownTiles)
    {    
        int unknownTiles = 106;
        ArrayList<Double> probabilities = new ArrayList<Double>();
       

    }
    private ArrayList<Tile> getDeck()
    {
    ArrayList<Tile>allTiles = new ArrayList<Tile>();
        
    }

}
