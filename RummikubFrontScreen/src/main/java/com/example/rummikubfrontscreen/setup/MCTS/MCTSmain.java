//package com.example.rummikubfrontscreen.setup.MCTS;
//
//import com.example.rummikubfrontscreen.setup.GameApp;
//import com.example.rummikubfrontscreen.setup.GameSetup;
//
//public class MCTSmain {
//
//    MCTSmain() {
//        // This is a test
//        // Assumption: player 0 is the AI
//        GameApp gameApp = new GameApp();
//        GameSetup gameInfo = gameApp.getGs();
//
//        MCTSGameState gameState = new MCTSGameState(gameApp.getCurPlr(), gameInfo.getBoard(),
//                gameInfo.getPlayers().get(0).getHand(), gameInfo.getPlayers());
//        MCTS mcts = new MCTS(gameState);
//        int numberofTiles = 0;
//        Node node = mcts.findNextMove();
//        // apply move to board
//    }
//}