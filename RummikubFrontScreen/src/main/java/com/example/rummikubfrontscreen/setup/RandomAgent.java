package com.example.rummikubfrontscreen.setup;

import com.example.rummikubfrontscreen.setup.MCTS.MCTS;
import com.example.rummikubfrontscreen.setup.MCTS.MCTSAction;
import com.example.rummikubfrontscreen.setup.MCTS.MCTSGameState;

import java.util.ArrayList;
import java.util.Random;

/**
 * The RandomAgent class is a Java class that represents an agent that takes random actions in a game.
 */
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

    public MCTSGameState applyAction(MCTSGameState gs) {
        ArrayList<Object[]> possibleMoves = gs.getOwnMoveStates();
        Random rand = new Random();
        if (possibleMoves.size() == 0) {
            return gs.copyAndNextPlayer(gs.getBoard(), gs.getCurPlayer().getHand());
        }
        int rn = rand.nextInt(possibleMoves.size());
        Object[] move = possibleMoves.get(rn);
        Board b = (Board) move[0];
        ArrayList<Tile> hand = (ArrayList<Tile>) move[1];

        MCTSGameState appliedActionGameState = gs.copyAndNextPlayer(b, hand);
        return appliedActionGameState;
    }

    public void takeRandomAction() {

        // get possible moves in the hand from elias method
        MCTSAction moveProvider = new MCTSAction();
        possibleMoves = moveProvider.ownMoverRun(player.getHand());
        ArrayList<ArrayList<Tile>> otherPossibleMoves = moveProvider.ownMoveGroup(player.getHand());

        // Combine all elements of possibleMoves and otherPossibleMoves
        possibleMoves.addAll(otherPossibleMoves);

        // if not any available draw
        if (possibleMoves.isEmpty()) {
            draw = true;
            return;
        }

        // choose a random number of moves
        int numOfMoves = generateRandomNumber(possibleMoves.size());

        // choose random moves
        ArrayList<Integer> ids = new ArrayList();
        for (int i = 0; i < numOfMoves; i++) {
            int id = generateRandomNumber(possibleMoves.size());
            if (ids.contains(id)) {
                i -= 1;
            } else {
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