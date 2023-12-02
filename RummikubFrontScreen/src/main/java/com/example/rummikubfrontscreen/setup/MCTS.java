package com.example.rummikubfrontscreen.setup;

import java.util.ArrayList;
import java.util.Random;

public class MCTS {
    private MCTSGameState gameState;
    private Node root;
    private static final int MAX_ITERATIONS = 1000;
    static double explorationParameter = 1.4;

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

            int playoutResult = simulateRandomPlayout(nodeToExplore);
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
            double ucbValue = calcUCTValue(child, explorationParameter);
            if (ucbValue > maxScore) {
                maxScore = ucbValue;
                bestNode = child;
            }
        }
        return bestNode;
    }

    private void expand(Node node, MCTSGameState gameState) {
        ArrayList<MCTSGameState> legalMoves = gameState.getLegalMoves(); // Implement this method
        for (MCTSGameState move : legalMoves) {
            Node childNode = new Node(move, node);
            node.addChild(childNode);
        }
    }

    private double calcUCTValue(Node child, double explorationParameter) {
        if (child.visitCount == 0) {
            return Double.MAX_VALUE;
        }
        double exploitationTerm = child.score / child.visitCount;
        double explorationTerm = Math.sqrt(Math.log(child.getParent().visitCount) / child.visitCount);
        return exploitationTerm + explorationParameter * explorationTerm;
    }

    private int simulateRandomPlayout(Node node) {
        // Implement the simulation logic based on your game rules
        return 0; // Replace with the actual result
    }

    private void backPropagate(Node node, int playoutResult) {
        while (node != null) {
            node.visitCount++;
            node.score += playoutResult;
            node = node.getParent();
        }
    }
}
