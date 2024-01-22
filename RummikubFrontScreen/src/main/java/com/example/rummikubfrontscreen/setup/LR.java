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
            x[i] = extractFeatures(move, inputs.get(i).getGameState().getCurrentHand().size());
            y[i] = calculateScore(targets.get(i));
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
        System.out.println(slope);
        System.out.println(intercept);
    }

    private double mean(double[] array) {
        double sum = 0;
        for (double value : array) {
            sum += value;
        }
        return sum / array.length;
    }

    // Extracted features from the current state, we chose the board size and players hand and divided them.
    private double extractFeatures(Object[] instance, int h) {
        double bSize = ((Board) instance[0]).getCurrentGameBoard().size();
        double hand = ((ArrayList<Tile>) instance[1]).size();
        double diff = h - hand;
        
        return bSize/hand + diff;
    }

    // Calculated score for the state, so visits of the node and whether it was on the winning path
    private double calculateScore(Node target) {
        double visits = target.getVisitCount();
        double winScore = target.getWinCount();
        double x = visits + winScore;
        return x;

    }

    public Object[] predict(ArrayList<Object[]> input, int h) {
        // Use the trained model to predict the output
        Object[] bestPrediction = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (Object[] instance : input) {
            if(instance == input.get(0)){
                continue;
            }
            double predictedScore = slope * extractFeatures(instance, h) + intercept;
            System.out.println(predictedScore);

            if (predictedScore > bestScore || ((ArrayList<Tile>) instance[1]).size() == 0) {
                bestScore = predictedScore;
                bestPrediction = instance;
                if( ((ArrayList<Tile>) instance[1]).size() == 0){
                    bestPrediction = instance;
                    return bestPrediction;
                }
            }
        }

        return bestPrediction;
    }

    public static void main(String[] args) {
        // Example usage
        LR model = new LR();
        MCTSmain mcmain = new MCTSmain();
        MCTS mcts = mcmain.getMcts();
        mcts.MctsPlayThrough(20);

        ArrayList<Node> inputs = mcts.inputs;
        ArrayList<Node> targets = mcts.targets;

        // Train the model
        model.train(inputs, targets);

        ArrayList<ArrayList<Tile>> board = new ArrayList<>();
        ArrayList<Tile> seq2 = new ArrayList<>();
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.RED, Value.ONE));
        seq2.add(new Tile(Colour.BLUE, Value.ONE));
        seq2.add(new Tile(Colour.BLACK, Value.ONE));
        board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.RED, Value.THREE));
        seq2.add(new Tile(Colour.BLUE, Value.THREE));
        seq2.add(new Tile(Colour.BLACK, Value.THREE));
        board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.RED, Value.FOUR));
        seq2.add(new Tile(Colour.BLACK, Value.FOUR));
        seq2.add(new Tile(Colour.YELLOW, Value.FOUR));
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
        seq2.add(new Tile(Colour.BLACK, Value.SIX));
        seq2.add(new Tile(Colour.BLUE, Value.SIX));
        seq2.add(new Tile(Colour.RED, Value.SIX));
        board.add(seq2);


        ArrayList<Tile> drawPile = new ArrayList<>();
        drawPile.add(new Tile(Colour.BLACK, Value.FIVE));
        drawPile.add(new Tile(Colour.YELLOW, Value.FIVE));
        drawPile.add(new Tile(Colour.RED, Value.FIVE));

        Board b = new Board(board, drawPile);

        ArrayList<Tile> hand = new ArrayList<>();
        hand.add(new Tile(Colour.RED, Value.FIVE));
        hand.add(new Tile(Colour.BLUE, Value.THREE));
        hand.add(new Tile(Colour.YELLOW, Value.JOKER));
        hand.add(new Tile(Colour.BLUE, Value.FOUR));
        hand.add(new Tile(Colour.BLACK, Value.FIVE));
        hand.add(new Tile(Colour.YELLOW, Value.TWO));
        hand.add(new Tile(Colour.YELLOW, Value.FOUR));
        hand.add(new Tile(Colour.BLUE, Value.SIX));

        // Use the trained model to make predictions
        ArrayList<Object[]> input = PossibleMoves.possibleMoves(b, hand, 0);
        Utils.print(input.get(4));
        System.out.println(input.size());
        Object[] predictedOutput = model.predict(input, hand.size());

        System.out.println("Predicted Output: ");
        Utils.print(predictedOutput);
    }
}
