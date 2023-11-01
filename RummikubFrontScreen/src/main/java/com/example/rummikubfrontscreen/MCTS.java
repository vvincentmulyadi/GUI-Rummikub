package com.example.rummikubfrontscreen.setup;

import java.util.ArrayList;
import java.util.Random;

import org.w3c.dom.Node;
public class MCTS {
private static final double C=math.sqrt(2);
private int maxIterations; // Maximum number of MCTS iterations
private Board initialBoard;
private Player currentPlayer;
 public MCTS() {
    this.initialBoard = initialBoard;
    this.maxIterations = maxIterations;
    this.currentPlayer = currentPlayer;
 }
 public  void findBestMove() {
        Node rootNode = new Node(initialBoard, null);
        for (int i = 0; i < maxIterations; i++) {
            Node selectedNode = select(rootNode);
            double result = simulate(selectedNode);
            backpropagate(selectedNode, result);
        }
        Node bestChild = bestChild(rootNode, 0.0); // Choose child with highest UCB value
        return bestChild.getMove();
    }
}