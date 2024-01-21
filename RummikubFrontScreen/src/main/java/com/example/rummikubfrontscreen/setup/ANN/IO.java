package com.example.rummikubfrontscreen.setup.ANN;

//CHANGE THESE 2 LINES
import briscola.game_logic.RunAgent;
import org.ejml.simple.SimpleMatrix;

import java.io.*;
import java.util.*;

public class IO {

   /*
   Writes the new weights of the ANN after training in a file to be able to store and read it for the next game
   @param n - the neural network
   @param fileName - the file path
    */
   public static void writeToFile(NeuralNetwork n, String fileName) throws IOException {
       String name = fileName;
       if (fileName == null) {
           System.out.println("File does not exist, please provide a valid file name");
       }
       BufferedWriter csvReader = new BufferedWriter(new FileWriter(fileName)); //add coma and true to not overwrite data
       //Writing the new weights of the ANN
           SimpleMatrix[] input = n.getWeights();
           int ok=0;
           for (SimpleMatrix matrix : input) {
               ok++;
               for (int i = 0; i < matrix.numCols(); i++) {
                   for (int j = 0; j < matrix.numRows(); j++) {
                       csvReader.write(String.valueOf(matrix.get(j, i)));
                       if (j < matrix.numRows()-1) csvReader.write(", "); // to separate values on the rows
                   }
                   if (i < matrix.numCols()-1)
                       csvReader.write("\n"); //to separate values on columns
               }
               //if it's the output layer don't add the two empty lines
               if(ok < input.length)
               csvReader.write("\n\n"); // to separate layers
           }

       // csvReader.write(String.valueOf(input[i])); //
       // csvReader.write(","); //
       csvReader.close();
   }

   /*
   Reads the weights of the ANN from the file and sets the new weights.
   @param fileName - the filepath
    */
  public static SimpleMatrix[] ReadToFile(String fileName) throws IOException {
      BufferedReader be = new BufferedReader(new FileReader(fileName));
      String line = "";
      int column = 0;
      System.out.println(column);
      ArrayList<String> input = new ArrayList<>();

      ArrayList<double[][]> matrices = new ArrayList<>();
      matrices.add(new double[RunAgent.neuralNetwork.getHiddenNodes()][RunAgent.neuralNetwork.getInputNodes()]); //241 input nodes, 4 hidden nodes, 2 output nodes
      matrices.add(new double[RunAgent.neuralNetwork.getHiddenNodes()][RunAgent.neuralNetwork.getHiddenNodes()]);
      matrices.add(new double[RunAgent.neuralNetwork.getOutputNodes()][RunAgent.neuralNetwork.getHiddenNodes()]);
      int layerIndex = 0;
      while ((line = be.readLine()) != null) {
          if (line.trim().isEmpty()) {
              layerIndex++;
              column = 0;
          }
          else {
              String[] values = line.split(", ");
              for (int j = 0; j < values.length; j++) {
                  matrices.get(layerIndex)[j][column] = Double.parseDouble(values[j]);
              }
              column++;
          }
      }
      SimpleMatrix[] weights = new SimpleMatrix[matrices.size()];
      for (int i = 0; i < weights.length; i++) {
          weights[i] = new SimpleMatrix(matrices.get(i));
      }

      return weights;
  }
}
