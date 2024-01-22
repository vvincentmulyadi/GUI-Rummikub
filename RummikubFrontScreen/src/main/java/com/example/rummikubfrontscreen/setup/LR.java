package com.example.rummikubfrontscreen.setup;

import java.util.ArrayList;

import com.example.rummikubfrontscreen.setup.MCTS.*;

public class LR {

    private double slope;
    private double intercept;

    public LR() {
        this.slope = 0;
        this.intercept = 0;
    }

    public void train(ArrayList<Node> inputs, ArrayList<Node> targets) {

        double[] x = new double[inputs.size()];
        double[] y = new double[targets.size()];

        for (int i = 0; i < inputs.size(); i++) {
            Object[] move = new Object[2];
            move[0] = inputs.get(i).getGameState().getBoard();
            move[1] = inputs.get(i).getGameState().getCurrentHand();
            x[i] = extractFeatures(move);
            y[i] = calculateScore(targets.get(i));
            System.out.println(x[i]);
            System.out.println(y[i]);
        }

        linearRegression(x, y);
    }

    private void linearRegression(double[] x, double[] y) {
        int n = x.length;

        // Calculate mean of x and y
        double xMean = mean(x);
        double yMean = mean(y);

        // Calculate slope (m) and intercept (b)
        double numerator = 0;
        double denominator = 0;
        for (int i = 0; i < n; i++) {
            numerator += (x[i] - xMean) * (y[i] - yMean);
            denominator += Math.pow((x[i] - xMean), 2);
        }

        slope = numerator / denominator;
        intercept = yMean - slope * xMean;
    }

    private double mean(double[] array) {
        double sum = 0;
        for (double value : array) {
            sum += value;
        }
        return sum / array.length;
    }

    // Extracted features from the current state, we chose the board size and players hand and divided them.
    private double extractFeatures(Object[] instance) {
        double bSize = ((Board) instance[0]).getCurrentGameBoard().size();
        double hand = ((ArrayList<Tile>) instance[1]).size();
        double totalHand = 0;
        for(int i = 0;i<hand;i++){
            totalHand += ((ArrayList<Tile>) instance[1]).get(i).getValue().getValue();
        }
        return bSize/hand + totalHand;
    }

    // Calculated score for the state, so visits of the node and whether it was on the winning path
    private double calculateScore(Node target) {
        double visits = target.getVisitCount();
        double winScore = target.getWinCount();
        double x = visits + winScore;
        return x;

    }

    public Object[] predict(ArrayList<Object[]> input) {
        // Use the trained model to predict the output
        Object[] bestPrediction = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (Object[] instance : input) {
            double predictedScore = slope * extractFeatures(instance) + intercept;

            if (predictedScore > bestScore) {
                bestScore = predictedScore;
                bestPrediction = instance;
            }
        }

        return bestPrediction;
    }

    public static void main(String[] args) {
        // Example usage
        LR model = new LR();
        MCTSmain mcmain = new MCTSmain();
        MCTS mcts = mcmain.getMcts();
        Node nextMoce = mcts.MctsAlgorithm(50);
        mcts.root = nextMoce;
        mcts.MctsPlayThrough(20);

        ArrayList<Node> features = mcts.inputs;
        ArrayList<Node> targets = mcts.targets;
        System.out.println(features);
        System.out.println(targets);

        // Train the model
        model.train(features, targets);

        ArrayList<ArrayList<Tile>> board = new ArrayList<>();
        ArrayList<Tile> seq2 = new ArrayList<>();
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.RED, Value.ONE));
        seq2.add(new Tile(Colour.BLUE, Value.ONE));
        seq2.add(new Tile(Colour.BLACK, Value.ONE));
        board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.RED, Value.ONE));
        seq2.add(new Tile(Colour.BLUE, Value.ONE));
        seq2.add(new Tile(Colour.YELLOW, Value.ONE));
        board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.RED, Value.NINE));
        seq2.add(new Tile(Colour.BLUE, Value.NINE));
        seq2.add(new Tile(Colour.BLACK, Value.NINE));
        board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.RED, Value.NINE));
        seq2.add(new Tile(Colour.BLACK, Value.NINE));
        seq2.add(new Tile(Colour.YELLOW, Value.NINE));
        board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.RED, Value.TEN));
        seq2.add(new Tile(Colour.BLACK, Value.TEN));
        seq2.add(new Tile(Colour.YELLOW, Value.TEN));
        board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.RED, Value.TWELVE));
        seq2.add(new Tile(Colour.BLUE, Value.TWELVE));
        seq2.add(new Tile(Colour.YELLOW, Value.TWELVE));
        board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.BLUE, Value.SIX));
        seq2.add(new Tile(Colour.BLACK, Value.SIX));
        seq2.add(new Tile(Colour.YELLOW, Value.SIX));
        board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.RED, Value.TWO));
        seq2.add(new Tile(Colour.RED, Value.THREE));
        seq2.add(new Tile(Colour.RED, Value.FOUR));
        board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.BLACK, Value.ELEVEN));
        seq2.add(new Tile(Colour.BLACK, Value.TWELVE));
        seq2.add(new Tile(Colour.BLACK, Value.THIRTEEN));
        board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.YELLOW, Value.SIX));
        seq2.add(new Tile(Colour.YELLOW, Value.SEVEN));
        seq2.add(new Tile(Colour.YELLOW, Value.EIGHT));
        board.add(seq2);

        Board b = new Board(board, new ArrayList<>());

        ArrayList<Tile> hand = new ArrayList<>();
        hand.add(new Tile(Colour.RED, Value.FIVE));
        hand.add(new Tile(Colour.BLUE, Value.THREE));
        hand.add(new Tile(Colour.YELLOW, Value.JOKER));
        hand.add(new Tile(Colour.BLUE, Value.FOUR));
        hand.add(new Tile(Colour.BLUE, Value.EIGHT));
        hand.add(new Tile(Colour.BLUE, Value.TEN));
        hand.add(new Tile(Colour.BLUE, Value.THIRTEEN));
        hand.add(new Tile(Colour.BLACK, Value.FIVE));
        hand.add(new Tile(Colour.YELLOW, Value.TWO));
        hand.add(new Tile(Colour.YELLOW, Value.TWO));
        hand.add(new Tile(Colour.YELLOW, Value.FOUR));
        hand.add(new Tile(Colour.BLUE, Value.SIX));
        hand.add(new Tile(Colour.BLUE, Value.NINE));

        // Use the trained model to make predictions
        ArrayList<Object[]> inputFeatures = PossibleMoves.possibleMoves(b, hand, 0);
        Object[] predictedOutput = model.predict(inputFeatures);

        System.out.println("Predicted Output: " + predictedOutput);
    }
}
