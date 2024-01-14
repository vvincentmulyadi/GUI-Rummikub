package com.example.rummikubfrontscreen.setup.ANN;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;



public class NeuralNetwork {
    private ActivationFunction activationFunction = new ActivationFunction.ReLinear(1.0);
    private Random rand = new Random();

    private int inputNodes;
    private int hiddenLayers;
    private int hiddenNodes;
    private int outputNodes;

    private SimpleMatrix[] weights;
    private SimpleMatrix[] biases;

    // learning rate should not go higher than 0.5 because the higher the value of the learning
    // rate the more unstable the learning rate becomes.
    private double learningRate;

    //Neural network constructor with only 1 hidden layer
    public NeuralNetwork(int inputNodes, int hiddenNodes, int outputNodes){
        this.inputNodes = inputNodes;
        this.hiddenLayers = 1;
        this.hiddenNodes = hiddenNodes;
        this.outputNodes = outputNodes;
    }

    public NeuralNetwork(int inputNodes, int hiddenLayers, int hiddenNodes, int outputNodes){
        this.inputNodes = inputNodes;
        this.hiddenLayers = hiddenLayers;
        this.hiddenNodes = hiddenNodes;
        this.outputNodes = outputNodes;

        initializeDefaultValues();
        initializeWeights();
        initializeBiases();
    }

    public NeuralNetwork(NeuralNetwork n){
        this.inputNodes = n.inputNodes;
        this.hiddenLayers = n.hiddenLayers;
        this.hiddenNodes = n.hiddenNodes;
        this.outputNodes = n.outputNodes;

        this.weights = new SimpleMatrix[hiddenLayers+1];
        this.biases = new SimpleMatrix[hiddenLayers+1];

        for(int i = 0; i < n.biases.length; i++) {
            this.biases[i] = n.biases[i];
        }

        for(int i = 0; i < n.weights.length; i++) {
            this.weights[i] = n.weights[i];
        }

        this.learningRate = n.learningRate;
        this.activationFunction = n.activationFunction;
    }

    private void initializeDefaultValues() {
        this.learningRate = 0.1;

        this.activationFunction = new ActivationFunction.ReLinear(learningRate);
    }

    private void initializeWeights() {
        weights = new SimpleMatrix[hiddenLayers + 1];

        for(int i = 0; i< weights.length; i++) {
            if(i == 0) {
                //intput layer to 1st hidden layer
                weights[i] = SimpleMatrix.random64(hiddenNodes, inputNodes, -1, 1, rand);
            } else if( i == weights.length -1) {
                // last hidden layer to output layer
                weights[i] = SimpleMatrix.random64(outputNodes, hiddenNodes, -1, 1, rand);
            } else {
                // hidden layer to hidden layer
                weights[i] = SimpleMatrix.random64(hiddenNodes, hiddenNodes, -1, 1, rand);
            }
        }
    }

    private void initializeWeights() {
        weights = new SimpleMatrix[hiddenLayers + 1];
        double heInitRange = Math.sqrt(2.0 / inputNodes);
    
        for (int i = 0; i < weights.length; i++) {
            int inSize = i == 0 ? inputNodes : hiddenNodes;
            int outSize = i == weights.length - 1 ? outputNodes : hiddenNodes;
            weights[i] = SimpleMatrix.random64(outSize, inSize, -heInitRange, heInitRange, rand);
        }
    }

    private void initializeBiases() {
        biases = new SimpleMatrix[hiddenLayers + 1];

        for(int i = 0; i< biases.length; i++) {
            if (i == biases.length - 1) {
                biases[i] = SimpleMatrix.random64(outputNodes, 1, -1, 1, rand);
            } else {
                biases[i] = SimpleMatrix.random64(hiddenNodes, 1, -1, 1, rand);
            }
        }
    }

    public void train(double[] inputArray, double[] targetArray) {
       if(targetArray.length != outputNodes) {
            throw new WrongDimensions(targetArray.length, outputNodes, "Output");
        } else {
            //modifying the input to only contain the cards we are interested in, not all possibilities
            double[] newInput = modifyInput(inputArray);
            ActivationFunction activationFunction = new ActivationFunction.ReLinear(1.0);

            SimpleMatrix input = MatrixUtilities.arrayToMatrix(newInput);
            SimpleMatrix target = MatrixUtilities.arrayToMatrix(targetArray);

            SimpleMatrix layers[] = new SimpleMatrix[hiddenLayers + 2];
            layers[0] = input;
            for(int j=1; j<hiddenLayers + 2; j++) {
                layers[j] = calculateLayer(weights[j-1], biases[j-1], input, activationFunction);
                input = layers[j];
            }
            for(int n= hiddenLayers + 1; n>0; n--) {
                SimpleMatrix errors = target.minus(layers[n]);
                SimpleMatrix gradients = calculateGradient(layers[n], errors, activationFunction);
                SimpleMatrix deltas = calculateDeltas(gradients, layers[n-1]);
                biases[n-1] = biases[n-1].plus(gradients);
                weights[n-1] = weights[n-1].plus(deltas);
                SimpleMatrix previousError = weights[n-1].transpose().mult(errors);
              //  System.out.println("previousError: " + previousError);
                target = previousError.plus(layers[n-1]);
            }
            for(int i =0; i<targetArray.length;i++)
                 System.out.println("For i "+ i +": "+ targetArray[i]);
           // System.out.println();
           // System.out.println("target: " + target);

        }
    }

    public NeuralNetwork copy() {
        return new NeuralNetwork(this);
    }

    public NeuralNetwork merge(NeuralNetwork n) {
        return this.merge(n, 0.5);
    }

    public NeuralNetwork merge(NeuralNetwork n, double prob) {
        if(!Arrays.equals(this.getDimensions(), n.getDimensions())) {
            throw new WrongDimensions(this.getDimensions(), n.getDimensions());
        } else {
            NeuralNetwork result = this.copy();

            for(int i=0; i<result.weights.length; i++) {
                result.weights[i] = MatrixUtilities.mergeMatrices(this.weights[i], n.weights[i], prob);
            }

            for(int i=0; i<result.biases.length; i++) {
                result.biases[i] = MatrixUtilities.mergeMatrices((this.biases[i]), n.biases[i], prob);
            }

            return result;
        }
    }

    private double[] modifyInput(double[] inputArray)
    {
        double[] newInput = new double[52];
        int j=0;
        int temp = 0;
        // 40 cards in your hand (place: 0),
        // 40 cards in the opponent's hand (place: 1),
        // 40 cards in the deck (place: 2),
        // 40 cards already played (place: 3),
        // 40 cards for a briscola (place: 4),
        // 40 for the card on the table (place: 5)
        //--------------------------------------------
        //go through the initial input array
        for(int i =0; i< 80;i++){
            //if there is a card we add it to the new input array
            if(inputArray[i]==1){
                newInput[j++]=i;
            }
        }
        for(int i=120; i<160;i++)
        {
            if(inputArray[i]==1){ // so i = from * 40 + spacing + movingCard.getRanking() - 11
                newInput[j++]=i;
            }
        }
        //finding the briscola suit
        for (int k = 160; k <= 200; k++) {
            if (inputArray[k] == 1) {
                temp = k;
                break;
            }
        }
        /*
         case SWORDS->spacing=0;
         case CLUBS->spacing=1;
         case CUPS->spacing=2;
         case COINS->spacing=3;
         */
        switch((temp-160) /10){
            case 0: inputArray[48] = 1;
            case 1: inputArray[49] = 1;
            case 2: inputArray[50] = 1;
            case 3: inputArray[51] = 1;
        }
        return newInput;
    }

    private SimpleMatrix calculateLayer(SimpleMatrix weights, SimpleMatrix bias, SimpleMatrix input, ActivationFunction activationFunction) {
        SimpleMatrix result = weights.mult(input);
       // result = result.plus(bias);
        return applyActivationFunction(result, false, activationFunction);
    }

    private SimpleMatrix calculateGradient(SimpleMatrix layer, SimpleMatrix error, ActivationFunction activationFunction) {
        SimpleMatrix gradient = applyActivationFunction(layer, true, activationFunction);
        gradient = gradient.elementMult(error);
        return gradient.scale(learningRate);
    }

    private SimpleMatrix calculateDeltas(SimpleMatrix gradient, SimpleMatrix layer){
        return gradient.mult(layer.transpose());
    }

    private SimpleMatrix applyActivationFunction(SimpleMatrix input, boolean derivative, ActivationFunction activationFunction) {
        SimpleMatrix output = new SimpleMatrix(input.numRows(), input.numCols());
        double[] result = new double[input.numRows()];
        if(derivative) {
            for (int i = 0; i < input.numRows(); i++) {
                double value = input.get(i, 0);
                result[i] = activationFunction.Derivative(value);
                //output.set(i, 0, result);
            }
//            System.out.println("derivative result: "+ Arrays.toString(result));
            output = MatrixUtilities.arrayToMatrix(result);
        } else {
            for( int i = 0; i< input.numRows(); i++) {
                double value = input.get(i,0);
                result[i] = activationFunction.calc(value);
                //output.set(i,0,result);
            }
            output = MatrixUtilities.arrayToMatrix(result);
//            System.out.println("activation result: "+ Arrays.toString(result));
        }
        return output;
    }

    public void writeToFile(String fileName) throws IOException {
        ReadAndWriteToFile.writeToFile(this, fileName);
    }

    public static SimpleMatrix[] readFromFile(String fileName) throws IOException {
        return ReadAndWriteToFile.ReadToFile(fileName);
    }



    public int getOutput(){
        SimpleMatrix matrix = weights[hiddenLayers];
        double node =0;
        int ans = 0;
            for (int i = 0; i < matrix.numCols(); i++) {
                for (int j = 0; j < matrix.numRows(); j++) {
                    if(matrix.get(j,i)> node) {
                        node = matrix.get(j,i);
                        if(i==0)
                            ans = 0;
                        else if(i==1)
                            ans=1;
                    }
                }
            }
            //System.out.println(ans);
            return (int)node;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public int getInputNodes() {
        return inputNodes;
    }

    public int getHiddenLayers() {
        return hiddenLayers;
    }

    public int getHiddenNodes() {
        return hiddenNodes;
    }

    public int getOutputNodes() {
        return outputNodes;
    }

    public SimpleMatrix[] getWeights() {
        return weights;
    } //a Simple Matrix

    public void setWeights(SimpleMatrix[] weights){
        this.weights = weights;
    }

    public SimpleMatrix[] getBiases() {
        return biases;
    }

    public void setBiases(SimpleMatrix[] biases) {
        this.biases = biases;
    }

    public int[] getDimensions() {
        return new int[]{inputNodes, hiddenLayers, hiddenNodes, outputNodes};
    }
}

class ReLinear{
    private double a=1.0;
    public ReLinear(double _a){
        this.a=_a;
    }
    public double calc(double x){
        return Math.max(0.0, x);
    }
    public double Derivative(double input) {
        if (input > 0)
            return 1;
        return 0;
    }
}