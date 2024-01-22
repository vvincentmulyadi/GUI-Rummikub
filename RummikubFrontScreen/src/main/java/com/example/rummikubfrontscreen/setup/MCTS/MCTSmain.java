package com.example.rummikubfrontscreen.setup.MCTS;

import java.util.ArrayList;

import com.example.rummikubfrontscreen.setup.Colour;
import com.example.rummikubfrontscreen.setup.GameApp;
import com.example.rummikubfrontscreen.setup.GameSetup;
import com.example.rummikubfrontscreen.setup.Tile;
import com.example.rummikubfrontscreen.setup.Utils;
import com.example.rummikubfrontscreen.setup.Value;

/**
 * The MCTSmain class initializes the MCTS algorithm and game state for a given number of players.
 */
public class MCTSmain {
    Node root;
    MCTS mcts;
    GameApp gameApp;
    GameSetup gameInfo;

    MCTSmain(int numOfPlayers) {

        gameApp = new GameApp(numOfPlayers);
        gameInfo = gameApp.getGs();

        MCTSGameState gameState = new MCTSGameState(gameApp.getCurPlr(), gameInfo.getBoard(), gameInfo.getPlayers());

        mcts = new MCTS(gameState);
        root = new Node(gameState, null);
    }

    public Node getRoot() {
        return root;
    }

    public MCTS getMcts() {
        return mcts;
    }
}