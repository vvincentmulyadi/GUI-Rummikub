package com.example.rummikubfrontscreen.setup.MCTS;

import java.io.IOException;

import com.example.rummikubfrontscreen.setup.GameTracker;
import com.example.rummikubfrontscreen.setup.RandomAgent;

/**
 * The `MctsVSRandom` class simulates a game between an MCTS agent and a random agent, collecting data
 * on the game outcomes.
 */
public class MctsVSRandom {

    MCTS mcts = new MCTS();
    RandomAgent randomAgent = new RandomAgent(null);

    public static void main(String[] huhu) throws IOException {
        MCTSmain mctsMain = new MCTSmain(2);
        MctsVSRandom mctsVSRandom = new MctsVSRandom();
        Node root = mctsMain.getRoot();

        mctsVSRandom.dataCollecter(root, 10);

    }

    private Node simulateRound(Node root, int iterations) {

        Node playFromMcts = null;// mcts.MctsAlgorithm(root, iterations);
        if (playFromMcts.isWinner()) {
            return playFromMcts;
        }

        MCTSGameState mctsGameState = randomAgent.applyAction(playFromMcts.getGameState());

        return new Node(mctsGameState, null);
    }

    private void dataCollecter(Node root, int playOutIterations) throws IOException {

        int dmcts = 3;

        System.out.println("playOutIterations: " + playOutIterations + " dmcts: " + dmcts);

        for (int i = 0; i < 50; i++) {
            MCTSmain mctsMain = new MCTSmain(2);
            Node newRoot = mctsMain.getRoot();
            root = mcts.MctsVotingAlgorithm(newRoot, playOutIterations, dmcts);
            int roundCounter = 0;

            while (root.getGameState().hasWinner() == 0) {
                roundCounter++;
                MCTSGameState mctsGameState = randomAgent.applyAction(root.getGameState());
                root = new Node(mctsGameState, null);
                System.out.println("After Random Agent: !!!!");
                System.out.println(root);
                root = mcts.MctsVotingAlgorithm(root, playOutIterations, dmcts);
                System.out.println("After MCTS Agent: !!!!");
                System.out.println(root);
            }
            System.out.println("GAME FINISHED DATA IS BEING COLLECTED");
            int winerInt = root.getGameState().isAIPlayerWinner();
            GameTracker.setWinner(winerInt);
            GameTracker.setNumOfMoves(roundCounter);
            GameTracker.writeToFile();
        }

    }
}
