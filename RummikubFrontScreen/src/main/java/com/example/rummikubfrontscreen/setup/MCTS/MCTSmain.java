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

    MCTSmain() {
        // This is a test
        // Assumption: player 0 is the AI
        gameApp = new GameApp();
        gameInfo = gameApp.getGs();
        // System.out.println(gameInfo.getBoard().toString());
        System.out.println(gameInfo.getTiles());
        gameInfo.getBoard().addDrawPile(gameInfo.getTiles());


        MCTSGameState gameState = new MCTSGameState(gameApp.getCurPlr(), gameInfo.getBoard(), gameInfo.getPlayers());
        mcts = new MCTS(gameState);
        root = new Node(gameState, null);
        System.out.println(gameState.getBoard().getDrawPile());
        // apply move to board
    }

    // What is the difference between mcts and mncts gamestate ?
    public static void main(String[] args) {
        MCTSmain mcmain = new MCTSmain();

        mcmain.getRoot().expandOwnMovesOnly();

        mcmain.mcts.deepFirstSearch();
        mcmain.getRoot().depthFirstSearch(mcmain.getRoot());

        // String movestaeString = Utils.MoveStatetoString(args)
        System.out.println(mcmain.getRoot().getChildren().toString());
    }

    private static void addGoodTiles(ArrayList<Tile> hand) {
        // TODO Auto-generated method stub

        hand.add(new Tile(Colour.YELLOW, Value.ONE));
        hand.add(new Tile(Colour.RED, Value.ONE));
        hand.add(new Tile(Colour.BLACK, Value.ONE));
    }

    public Node getRoot() {
        return root;
    }

    public MCTS getMcts(){
        return mcts;
    }
}