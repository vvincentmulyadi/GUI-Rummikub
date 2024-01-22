package com.example.rummikubfrontscreen.setup.MCTS;

import java.util.ArrayList;

import com.example.rummikubfrontscreen.setup.Colour;
import com.example.rummikubfrontscreen.setup.GameApp;
import com.example.rummikubfrontscreen.setup.GameSetup;
import com.example.rummikubfrontscreen.setup.Tile;
import com.example.rummikubfrontscreen.setup.Utils;
import com.example.rummikubfrontscreen.setup.Value;

public class MCTSmain {
    Node root;
    MCTS mcts;
    GameApp gameApp;
    GameSetup gameInfo;

    public MCTSmain() {

        gameApp = new GameApp(2);
        gameInfo = gameApp.getGs();
        gameInfo.getBoard().addDrawPile(gameInfo.getTiles());

        MCTSGameState gameState = new MCTSGameState(gameApp.getCurPlr(), gameInfo.getBoard(), gameInfo.getPlayers());
        // hacking the hand to be a known hand
        // ArrayList<Tile> hand = new ArrayList<Tile>();
        // hand.add(new Tile(Colour.YELLOW, Value.ONE));
        // hand.add(new Tile(Colour.BLACK, Value.ONE));
        // hand.add(new Tile(Colour.BLUE, Value.ONE));
        // gameState.getCurPlayer().setHand(hand);
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