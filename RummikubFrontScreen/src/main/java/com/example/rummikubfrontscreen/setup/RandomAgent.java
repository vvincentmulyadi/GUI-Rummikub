package com.example.rummikubfrontscreen.setup;
import java.util.ArrayList;
import java.util.Random;

public class RandomAgent {

    private Player player;

    private ArrayList<Tile> hand;


    public RandomAgent(Player player) {
        this.player = player;
        this.hand = player.getHand();
    }

    public void takeRandomAction() {
        // Define a set of actions
        ArrayList<Tile> tiles = player.getHand();
        
        //scan the hand of the agent

        //get all the possible moves

        //if not any available draw

        //choose a random move

        //place it on the board


        // Perform the chosen action (replace this with your actual action logic)
      
    }

    private Tile getRandomElement(ArrayList<Tile> tiles) {
        Tile randomTile = null;
        int randomIndex = 0;
        while (randomIndex == 0) {
            randomIndex = (int) (10 * Math.random());
        }
        for(int i = 0; i < randomIndex; i++){
            randomTile = tiles.get(i);
        }
        return randomTile;
    }

    private void performAction(String action) {
        System.out.println("Agent performs action: " + action);
        // Add your logic here to perform the action
    }

    public static void main(String[] args) {
        RandomAgent randomAgent = new RandomAgent();

        // Make multiple random decisions (for demonstration purposes)
        for (int i = 0; i < 5; i++) {
            randomAgent.takeRandomAction();
        }
    }
}