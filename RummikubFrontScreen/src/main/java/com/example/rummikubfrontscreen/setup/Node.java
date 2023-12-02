package com.example.rummikubfrontscreen.setup;

import java.util.ArrayList;

public class Node {
    private MCTSGameState gameState;
    private Node parent;
    private ArrayList<Node> children;
    double score;
    public int visitCount;

    public Node(MCTSGameState gameState, Node parent) {
        this.gameState = gameState;
        this.children = new ArrayList<>();
        this.score = 0;
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public MCTSGameState getGameState() {
        return this.gameState;
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    public Node getRandomChildNode() {
        int randomIndex = (int) (Math.random() * this.children.size());
        return this.children.get(randomIndex);
    }

    public Node selection() {
        Node selected = null;
        double bestValue = Double.NEGATIVE_INFINITY;
        if (isLeafNode()) {
            return this;
        }
        for (Node child : children) {
            double uctValue = calcUCTValue(child);
            if (uctValue > bestValue) {
                selected = child;
                bestValue = uctValue;
            }
        }
        return selected.selection();
    }

    public void incrementVisitCount() {
        this.gameState.incrementVisitCount();
    }

    public boolean isLeafNode() {
        return children.isEmpty();
    }

    private double calcUCTValue(Node child) {
        if (child.visitCount == 0) {
            return Double.MAX_VALUE;
        }
        double exploitationTerm = child.score / child.visitCount;
        double explorationTerm = Math.sqrt(Math.log(child.getParent().visitCount) / child.visitCount);
        return exploitationTerm + MCTS.explorationParameter * explorationTerm;
    }
}
