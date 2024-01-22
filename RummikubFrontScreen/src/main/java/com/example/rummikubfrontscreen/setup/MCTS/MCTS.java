package com.example.rummikubfrontscreen.setup.MCTS;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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

    public MCTS() {
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

    private static int mostFrequent(int arr[]) {
        // Sort the array
        Arrays.sort(arr);

        // find the max frequency using linear traversal
        int max_count = 1, res = arr[0];
        int curr_count = 1;

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] == arr[i - 1])
                curr_count++;
            else
                curr_count = 1;

            if (curr_count > max_count) {
                max_count = curr_count;
                res = arr[i - 1];
            }
        }
        return res;
    }

    public Node MctsVotingAlgorithm(Node rootInput, int iterations, int votings) {
        int[] votes = new int[votings];
        if (rootInput.isLeafNode())
            expand(rootInput);
        for (int i = 0; i < votings; i++) {
            Node testNode = (Node) rootInput.clone();
            int vote = MctsAlgorithm(testNode, iterations);
            votes[i] = vote;

        }

        // for (int i = 0; i < rootInput.getChildren().size(); i++) {
        // System.out.println("Vote " + i);
        // System.out.println(rootInput.getChildren().get(i));
        // }
        int finalVote = mostFrequent(votes);
        System.out.println(Arrays.toString(votes));
        System.out.println("Final Vote is " + finalVote);
        return rootInput.getChildren().get(finalVote);
    }

    public int MctsAlgorithm(Node rootInput, int iterations) {

        if (rootInput.isLeafNode())
            expand(rootInput);

        ArrayList<Node> children = rootInput.getChildren();

        if (rootInput.getChildren().size() == 1) {

            // return rootInput.getChildren().get(0);
            return 0;
        }
        if (rootInput.hasChildWinner()) {
            System.out.println("We got a winner!!!");
            Node winner = rootInput.getChildWinnerNode();
            for (int i = 0; i < children.size(); i++) {
                if (children.get(i) == winner)
                    return i;
            }
            // return rootInput.;
        }

        for (int i = 0; i < iterations; i++) {
            Node candidateNode = selectCandidateNode(rootInput);

            Node nodeToExplore = candidateNode;

            if (!candidateNode.hadRandomPlayout() && !candidateNode.getGameState().isWinner()) {
                expand(candidateNode);
                if (!candidateNode.getChildren().isEmpty()) {
                    nodeToExplore = candidateNode.getRandomChildNode();
                }
            } else {
                nodeToExplore = candidateNode;
            }

            int playoutResult = simulateRandomPlayout(nodeToExplore);
            backPropagate(nodeToExplore, playoutResult);
        }
        Node bestNode = bestNode(rootInput);
        for (Node child : rootInput.getChildren()) {
            // System.out.println(child);
        }
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i) == bestNode)
                return i;
        }
        return -1;
    }

    public int MctsAlgorithm(int iterations) {
        return MctsAlgorithm(root, iterations);
    }

    private Node bestNode(Node rootInput) {
        int maxVisits = -1;
        Node bestNode = null;
        for (Node child : rootInput.getChildren()) {
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

    private void MctsPlayThrough(int rounds) {

        for (int i = 0; i < rounds; i++) {

            Node action = MctsAlgorithm(20);
            System.out.println("\n\n\n\n\nThose Are the Children that could have been played:");
            for (Node child : root.getChildren()) {
                System.out.println(child);
            }
            System.out.println("This is the action we are going to take: " + action);
            root = action.newRoot();
            if (root.isWinner()) {
                System.out.println("We got a winner after " + i + " rounds");
                break;
            }

        }
        System.out.println(root);
    }

    public static void main(String[] args) {

        MCTSmain mcmain = new MCTSmain(2);
        MCTS mcts = mcmain.getMcts();
        Node node = mcmain.getRoot();

        System.out.println("So we have: " + node.getAmountOfTiles());

        // System.out.println(simulateRandomPlayout(node));

        // mcts.root.expandOwnMovesOnly();

        Node nextMoce = mcts.MctsAlgorithm(50);
        mcts.root = nextMoce;

        mcts.MctsPlayThrough(20);

        System.out.println("This is the root\n" + mcts.root);
        System.out.println("The root has " + mcts.root.getChildren().size() + " many children");

        for (Node child : mcts.root.getChildren()) {
            System.out.println(child);
        }
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
                System.out.println("No more moves left");
                return 0;
            }

            Object[] moveState = moveStates.get((int) (Math.random() * moveStates.size()));
            gameState = gameState.copyAndNextPlayer((Board) moveState[0], (ArrayList<Tile>) moveState[1]);
        }
        // System.out.println(gameState);

        int result = gameState.isAIPlayerWinner();
        // System.out.println("\n\nRandom Playout is done");
        // System.out.println("The OutPut of playout is " + result);

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

    // simple method for calculating deck probabilities
    public ArrayList<Double> calculateDeckProbabilities(ArrayList<Tile> knownTiles) {
        ArrayList<Double> probabilities = new ArrayList<Double>();
        ArrayList<Tile> tilesOnBoard = convert(this.board);
        ArrayList<Tile> tilesOnHand = this.deck;
        knownTiles.addAll(tilesOnBoard);
        knownTiles.addAll(tilesOnHand);
        int unknownTiles = 106 - knownTiles.size();
        TileProbs tileProbs = new TileProbs();

        for (Tile tile : knownTiles) {
            int tileIndex = TileProbs.tileToIndexConverter(tile);
            tileProbs.adjustUniformProbs(tileIndex);
        }

        for (int i = 0; i < tileProbs.getTileProbsUniform().length; i++) {
            probabilities.add(tileProbs.getTileProbsUniform()[i] / unknownTiles);
        }

        return probabilities;
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
