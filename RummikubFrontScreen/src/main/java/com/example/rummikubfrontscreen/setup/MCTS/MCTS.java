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
    public Node root;
    private static final int MAX_ITERATIONS = 1000;
    static double explorationParameter = 1.4;
    private ArrayList<ArrayList<Tile>> board;
    private ArrayList<Tile> deck;
    ArrayList<Tile> guessedOpponentDeck;
    private String time;
    public static ArrayList<Node> inputs = new ArrayList<>();
    public static ArrayList<Node> targets = new ArrayList<>();

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

    public Node MctsAlgorithm(int iterations) {

        if (root.isLeafNode())
            expand(root);

        if (root.getChildren().size() == 1) {
            return root.getChildren().get(0);
        }
        if (root.hasChildWinner()) {
            //System.out.println("We got a winner!!!");
            return root.getChildWinnerNode();
        }

        for (int i = 0; i < iterations; i++) {
            Node candidateNode = selectCandidateNode(root);

            Node nodeToExplore = candidateNode;

            if (!candidateNode.hadRandomPlayout() && !candidateNode.getGameState().isWinner()) {
                expand(candidateNode);
                if (!candidateNode.getChildren().isEmpty()) {
                    nodeToExplore = candidateNode.getRandomChildNode();
                }
            } else {
                nodeToExplore = candidateNode;
            }

            //System.out.println("Random playout starts again");
            int playoutResult = simulateRandomPlayout(nodeToExplore);
            backPropagate(nodeToExplore, playoutResult);
        }
        Node bestNode = bestNode();
        return bestNode;
    }

    private Node bestNode() {
        int maxVisits = -1;
        Node bestNode = null;
        for (Node child : root.getChildren()) {
            if (maxVisits < child.getVisitCount()) {
                maxVisits = child.getVisitCount();
                bestNode = child;
            }
        }
        return bestNode;
    }

    private void expand(Node node) {

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
    }

    public void MctsPlayThrough(int rounds) {

        for (int i = 0; i < rounds; i++) {

            Node action = MctsAlgorithm(20);
            //System.out.println("\n\n\n\n\nThose Are the Children that could have been played:");
            for (Node child : root.getChildren()) {
                System.out.println(child);
            }
            //System.out.println("This is the action we are going to take: " + action);
            inputs.add(action.getParent());
            targets.add(action);
            root = action.newRoot();
            if (root.isWinner()) {
                //System.out.println("We got a winner after " + i + " rounds");
                break;
            }
            System.out.println("were at iteration: " + i);

        }
        System.out.println(root);
    }

    public static void main(String[] args) {

        MCTSmain mcmain = new MCTSmain();
        MCTS mcts = mcmain.getMcts();
        Node node = mcmain.getRoot();

        // System.out.println(simulateRandomPlayout(node));

        // mcts.root.expandOwnMovesOnly();

        // Node nextMoce = mcts.MctsAlgorithm(50);
        // mcts.root = nextMoce;

        mcts.MctsPlayThrough(20);

        //System.out.println("This is the root\n" + mcts.root);
        //System.out.println("The root has " + mcts.root.getChildren().size() + " many children");
        
        System.out.println(inputs);
        System.out.println(targets);

        // for (Node child : mcts.root.getChildren()) {
        //     System.out.println(child);
        // }
    }

    private static int simulateRandomPlayout(Node node) {
        node.setPlayout();

        MCTSGameState gameState = node.getGameState().clone();
        // System.out.println(node);
        // System.out.println(gameState);

        while (!gameState.isWinner()) {
            // System.out.println("\n\nStil doing Random Playout");
            // System.out.println(gameState);

            ArrayList<Object[]> moveStates = PossibleMoves.possibleMoves(gameState.getBoard(),
                    gameState.getCurrentHand(), 0);

            // System.out.println("We got " + moveStates.size() + " moves");
            if (moveStates.isEmpty()) {
                //System.out.println("No more moves left");
                return 0;
            }

            Object[] moveState = moveStates.get((int) (Math.random() * moveStates.size()));
            gameState = gameState.copyAndNextPlayer((Board) moveState[0], (ArrayList<Tile>) moveState[1]);
        }
        System.out.println(gameState);

        int result = gameState.isAIPlayerWinner();
        //System.out.println("\n\nRandom Playout is done");
        //System.out.println("The OutPut of playout is " + result);

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
    //simple method for calculating deck probabilities
    public ArrayList<Double> calculateDeckProbabilities(ArrayList<Tile> knownTiles){
        ArrayList<Double> probabilities = new ArrayList<Double>();
        ArrayList<Tile> tilesOnBoard = convert(this.board);
        ArrayList<Tile> tilesOnHand = this.deck;
        knownTiles.addAll(tilesOnBoard);
        knownTiles.addAll(tilesOnHand);
        int unknownTiles = 106 - knownTiles.size();
        TileProbs tileProbs = new TileProbs();
    
        for(Tile tile : knownTiles) {
            int tileIndex = TileProbs.tileToIndexConverter(tile);
            tileProbs.adjustUniformProbs(tileIndex);
        }
    
        for(int i = 0; i < tileProbs.getTileProbsUniform().length; i++) {
            probabilities.add(tileProbs.getTileProbsUniform()[i] / unknownTiles);
        }
    
        return probabilities;
    }

    public ArrayList<Tile> convert(ArrayList<ArrayList<Tile>> board) {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (ArrayList<Tile> row : board) {
            tiles.addAll(row);
        }
        return tiles;
    }
}
