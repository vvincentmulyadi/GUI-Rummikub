package com.example.rummikubfrontscreen.setup.MCTS;

import java.util.ArrayList;

import com.example.rummikubfrontscreen.setup.Board;
import com.example.rummikubfrontscreen.setup.Player;
import com.example.rummikubfrontscreen.setup.Utils;
import com.example.rummikubfrontscreen.setup.Tile;

/**
 * The Node class represents a node in a Monte Carlo Tree Search algorithm, used for game playing.
 */
public class Node {
    private int voting = 0;
    private MCTSGameState gameState;
    private Node parent;
    private ArrayList<Node> children;

    double uctValue;// UCT Score
    // Total Number of playouts or simulations
    private int visitCount;
    // Number of childnodes that resulted in a win
    private int winCount;
    private int staleMateCount;
    // If the gamestate has a winner the game is over
    private boolean absoluteLeaf = false;
    // If the node was played through once
    private boolean playedThrough = false;

    private double explorationParameter = 1.4;

    public void addVote() {
        this.voting++;
    }

    public int getVotes() {
        return this.voting;
    }

    public int getWinner() {
        return gameState.isAIPlayerWinner();
    }

    public Node(MCTSGameState gameState, Node parent) {
        if (gameState.hasWinner() == 1) {
            this.absoluteLeaf = true;
        }
        this.gameState = gameState;
        this.children = new ArrayList<>();
        this.uctValue = 0;
        this.visitCount = 1;
        this.parent = parent;
    }

    public void setPlayout() {
        this.playedThrough = true;
    }

    public boolean hadRandomPlayout() {
        return this.playedThrough;
    }

    public boolean hasChildWinner() {
        for (Node child : children) {
            if (child.isWinner()) {
                return true;
            }
        }
        return false;
    }

    public Node getChildWinnerNode() {
        for (Node child : children) {
            if (child.isWinner()) {
                // System.out.println("We are in getchioldwinnnrereoirjldsjf");
                // System.out.println(child);
                return child;
            }
        }
        System.out.println("You shouldnt have tried this method if there is no winner");
        return null;
    }

    public Node newRoot() {
        Node newRoot = new Node(this.gameState, null);
        return newRoot;
    }

    public boolean isWinner() {
        return gameState.hasWinner() == 1;
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
        calcUCTValue();
        return this.uctValue;
    }

    /**
     * 
     * @param node
     * @param playoutResult either -1, 0 or 1 depening on win or loss
     */
    public void addPlayout(int playoutResult) {
        this.winCount += playoutResult;
        if (playoutResult == 0) {
            this.staleMateCount++;
        }
        this.visitCount++;

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

    public int getWinCount() {
        return winCount;
    }

    public Node getRandomChildNode() {
        int randomIndex = (int) (Math.random() * this.children.size());
        // System.out.println("Children size " + this.children.size());
        if (this.children.size() > 1) {
            // System.out.println("\n\n Here are Children!!!" + getChildren().toString() +
            // "\n\n\n");
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

    /**
     * This method is for debugging purposses because in the mcts algorithm there
     * appear new tiles
     */
    public int getAmountOfTiles() {
        int amountOfTiles = 0;
        amountOfTiles += gameState.getBoard().getDrawPile().size();
        amountOfTiles += gameState.getBoard().getCurrentGameBoard().size() * 3;
        for (Player player : gameState.getPlayers()) {
            amountOfTiles += player.getHand().size();
        }
        return amountOfTiles;
    }

    public int getVisitCount() {
        return this.visitCount;
    }

    public boolean isLeafNode() {
        return children.isEmpty();
    }

    /**
     * sets the uct value for all playouts
     */
    public void calcUCTValue() {
        if (visitCount == 0) {
            this.uctValue = Double.MIN_VALUE;
            return;
        }
        double winRate = (double) winCount / (double) visitCount;
        double underRoot = Math.log(parent.getVisitCount()) / this.getVisitCount();
        underRoot = Math.sqrt(underRoot);
        this.uctValue = winRate + this.explorationParameter * underRoot;
    }

    @Override
    public Node clone() {
        Node clone = new Node(this.gameState.clone(), this.getParent());
        return clone;
    }

    @Override
    public String toString() {
        return gameState + " \nwith uctValue: " + uctValue + " and visitCount: " + visitCount + " and winCount: "
                + winCount + " and staleMateCount: " + staleMateCount + "\n";
    }

}
