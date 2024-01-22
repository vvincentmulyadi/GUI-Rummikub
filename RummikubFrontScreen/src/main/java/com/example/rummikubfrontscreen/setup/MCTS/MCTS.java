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

/**
 * The MCTS class implements the Monte Carlo Tree Search algorithm for a game and provides methods for
 * finding the next move and simulating game playthroughs.
 */
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
        ArrayList<Tile> unknownTiles = TileProbs.combiner(rootInput.getGameState());

        for (int i = 0; i < votings; i++) {
            Node testNode = (Node) rootInput.clone();
            MCTSGameState gs = testNode.getGameState();
            // Hiding the hand of the second player
            ArrayList<Tile> realHand = gs.getPlayers().get(1).getHand();
            realHand = TileProbs.handSampler(unknownTiles, gs);

            int vote = MctsAlgorithm(testNode, iterations);
            System.out.println("In MctsVotingAlgorithm we got vote " + vote);
            System.out.println("Vote: " + testNode.getChildren().get(vote));

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
        double[] propabilityDistirbution = new double[children.size()];
        int bestInt = 0;
        for (int i = 0; i < children.size(); i++) {
            System.out.println("The visit count of " + i + " is: " + children.get(i).getVisitCount());

            propabilityDistirbution[i] = children.get(i).getVisitCount() / ((double) iterations + 2);
            System.out.println(propabilityDistirbution[i]);
            if (children.get(i) == bestNode)
                bestInt = i;
        }
        System.out.println("The propabilityDistirbution is: " + Arrays.toString(propabilityDistirbution));
        return bestInt;
    }

    public int MctsAlgorithm(int iterations) {
        return MctsAlgorithm(root, iterations);
    }

    private Node bestNode(Node rootInput) {
        int maxVisits = -1;
        Node bestNode = null;
        for (int i = 0; i < rootInput.getChildren().size(); i++) {
            System.out.println(maxVisits + " " + root.getChildren().get(i).getVisitCount());
            if (maxVisits < root.getChildren().get(i).getVisitCount()) {
                maxVisits = root.getChildren().get(i).getVisitCount();
                bestNode = root.getChildren().get(i);
            }
            System.out.println(i);
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

            int rootaction = MctsAlgorithm(20);
            Node action = root.getChildren().get(rootaction);
            System.out.println("\n\n\n\n\nThose Are the Children that could have been played:");
            for (int j = 0; j < root.getChildren().size(); j++) {
                System.out.println("This is: " + j);
                // System.out.println(root.getChildren().get(j));
            }
            System.out.println("This is the action we are going to take: " + rootaction);

            root = action.newRoot();
            if (root.isWinner()) {
                // System.out.println("We got a winner after " + i + " rounds");
                break;
            }
            System.out.println("were at iteration: " + i);

        }
    }

    public static void main(String[] args) {

        MCTSmain mcmain = new MCTSmain(2);
        MCTS mcts = mcmain.getMcts();
        Node node = mcmain.getRoot();

        System.out.println("So we have: " + node.getAmountOfTiles());

        mcts.MctsPlayThrough(20);

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
                // System.out.println("No more moves left");
                return 0;
            }

            Object[] moveState = moveStates.get((int) (Math.random() * moveStates.size()));
            gameState = gameState.copyAndNextPlayer((Board) moveState[0], (ArrayList<Tile>) moveState[1]);
        }
        // System.out.println(gameState);

        int result = gameState.isAIPlayerWinner();
        return result;
    }

    // System.out.println("\n\nRandom Playout is done");
    // System.out.println("The OutPut of playout is " + result);
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
