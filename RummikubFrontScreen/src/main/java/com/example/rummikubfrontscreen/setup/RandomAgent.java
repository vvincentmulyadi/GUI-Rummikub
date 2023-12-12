package com.example.rummikubfrontscreen.setup;
import com.example.rummikubfrontscreen.setup.MCTS.MCTS;
import com.example.rummikubfrontscreen.setup.MCTS.MCTSAction;

import java.util.ArrayList;
import java.util.Random;

public class RandomAgent {

    private Player player;

    private ArrayList<ArrayList<Tile>> possibleMoves = new ArrayList<>();

    public ArrayList<ArrayList<Tile>> getChosenMoves() {
        return chosenMoves;
    }

    private ArrayList<ArrayList<Tile>> chosenMoves;
    private boolean draw;

    public RandomAgent(Player player) {
        this.player = player;
        this.draw = false;
        chosenMoves = new ArrayList<>();
    }

    public void takeRandomAction() {

        // get possible moves in the hand from elias method
        MCTSAction moveProvider = new MCTSAction();
        possibleMoves = moveProvider.ownMoverRun(player.getHand(),null);

        // if not any available draw
        if(possibleMoves.isEmpty()){
            draw = true;
            return;
        }

        // choose a random number of moves
        int numOfMoves = generateRandomNumber(possibleMoves.size());

        // choose random moves
        ArrayList<Integer> ids = new ArrayList();
        for(int i = 0; i<numOfMoves; i++){
            int id = generateRandomNumber(possibleMoves.size());
            if(ids.contains(id)){
                i-=1;
            }else{
                chosenMoves.add(possibleMoves.get(id));
            }
        }
      
    }

    private static int generateRandomNumber(int maxValue) {
        // Create a Random object
        Random random = new Random();

        // Generate a random integer between 0 (inclusive) and maxValue (exclusive)
        // To make it inclusive, add 1 to the result
        int randomNumber = random.nextInt(maxValue);

        return randomNumber;
    }

}