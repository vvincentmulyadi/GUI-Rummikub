package com.example.rummikubfrontscreen.setup.MCTS;

import java.util.ArrayList;

public class Node {
    private MCTSGameState gameState;
    private Node parent;
    private ArrayList<Node> children;
    private ArrayList<Double> playoutScores;
    double uctValue;//UCT Score
    public int visitCount;
    private boolean isTerminal;
    private double explorationParameter=1.4;

    public Node(MCTSGameState gameState, Node parent) {
        this.gameState = gameState;
        this.children = new ArrayList<>();
        this.uctValue = 0;
        this.visitCount = 1;
        this.parent = parent;
        this.isTerminal = false;
    }

    public Node getParent() {
        return parent;
    }
    public double getUCTScore(){
      return this.uctValue;
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
        this.incrementVisitCount();
        Node selected = null;
        double bestValue = 0;
        if (isLeafNode()) {
            return this;
        }
        for (Node child : children) {
            if (child.getUCTScore() > bestValue) {
                selected = child;
               
            }
        }
        return selected.selection();
    }

    public void incrementVisitCount() {
        this.gameState.incrementVisitCount();
    }
    public int getVisitCount()
    {
        return this.visitCount;
    }

    public boolean isLeafNode() {
        return children.isEmpty();
    }
   /*sets the uct value for all playouts */
    public void calcUCTValue() {
        if (visitCount == 0) {
            this.uctValue=Double.MIN_VALUE;
            return;
        }
        double totalUctValue = 0;
        for (double UctValue: playoutScores){
            totalUctValue += UctValue;
        }
        this.uctValue = (totalUctValue/playoutScores.size());
        this.uctValue = this.explorationParameter*Math.log(parent.getVisitCount())/this.getVisitCount();
    }
    /*public void simulateRandomPlayout(){
    Move randomMove=new Move(this.MCTSGameState.)
    } */
}
