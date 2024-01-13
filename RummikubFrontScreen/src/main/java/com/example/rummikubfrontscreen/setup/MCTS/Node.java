package com.example.rummikubfrontscreen.setup.MCTS;

import java.util.ArrayList;

import com.example.rummikubfrontscreen.setup.Board;
import com.example.rummikubfrontscreen.setup.Player;
import com.example.rummikubfrontscreen.setup.Utils;
import com.example.rummikubfrontscreen.setup.Tile;

public class Node {
    private MCTSGameState gameState;
    private Node parent;
    private ArrayList<Node> children;
    private ArrayList<Double> playoutScores;
    double uctValue;// UCT Score
    public int visitCount;
    private boolean isTerminal;
    private double explorationParameter = 1.4;

    public Node(MCTSGameState gameState, Node parent) {
        this.gameState = gameState;
        this.children = new ArrayList<>();
        this.uctValue = 0;
        this.visitCount = 1;
        this.parent = parent;
        this.isTerminal = false;
    }

    /**
     * Makes new nodes by looking at the possible moves from the current state
     * we are missing that the drawing of the tile but we would only need to
     * implement it in the first part of
     * the ownMove group.
     */
    public boolean expandOwnMovesOnly() {
        // Get MoveStates (Board, Hand)

        ArrayList<Object[]> ownMoveStates = gameState.getOwnMoveStates();
        if (ownMoveStates == null) {
            return false;
        }
        if (ownMoveStates.isEmpty()) {
            System.out.println("No more moves");
            return false;
        }

        for (Object[] state : ownMoveStates) {
            // Separate the board and the hand
            Board board = (Board) state[0];
            ArrayList<Tile> newHand = (ArrayList<Tile>) state[1];

            // Create a new game state with the new board and hand
            // and let the next player be the current player
            MCTSGameState newGameState = gameState.copyAndNextPlayer(board, newHand);

            Node child = new Node(newGameState, this);
            this.addChild(child);
        }
        return true;
    }

    static int i = 0;

    public Node depthFirstSearch(Node node) {
        i++;
        if (node.getGameState().getCurPlayer().getHand().isEmpty()) {
            System.out.println("NO FUCKING WAY WE GOT A WINNER");
            System.out.println(node.getGameState());
            children.get(-1);
        }
        if (node == null) {
            return null;
        }

        // System.out.println("Visiting node: " + node.getGameState().toString());

        if (!node.expandOwnMovesOnly()) {
            return node;
        }
        // Recursive case: Visit each child node in depth-first order
        for (Node child : node.getChildren()) {

            Node result = depthFirstSearch(child);
            // System.out.println("i: " + i);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public Node getParent() {
        return parent;
    }

    public double getUCTScore() {
        return this.uctValue;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public Node getRandomChildren() {
        int randomIndex = (int) (Math.random() * this.children.size());
        return this.children.get(randomIndex);
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
        System.out.println("Children size " + this.children.size());
        if (this.children.size() > 1) {
            System.out.println("\n\n Here are Children!!!" + getChildren().toString() + "\n\n\n");
            randomIndex = 1;
        }
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

    public int getVisitCount() {
        return this.visitCount;
    }

    public boolean isLeafNode() {
        return children.isEmpty();
    }

    /* sets the uct value for all playouts */
    public void calcUCTValue() {
        if (visitCount == 0) {
            this.uctValue = Double.MIN_VALUE;
            return;
        }
        double totalUctValue = 0;
        for (double UctValue : playoutScores) {
            totalUctValue += UctValue;
        }
        this.uctValue = (totalUctValue / playoutScores.size());
        this.uctValue = this.explorationParameter * Math.log(parent.getVisitCount()) / this.getVisitCount();
    }

    @Override
    public String toString() {
        return "Node [gameState=" + gameState + "]";
    }

}
