package com.example.rummikubfrontscreen.setup.MCTS;

import com.example.rummikubfrontscreen.setup.Board;
import com.example.rummikubfrontscreen.setup.GameApp;
import com.example.rummikubfrontscreen.setup.GameSetup;
import com.example.rummikubfrontscreen.setup.LR;
import com.example.rummikubfrontscreen.setup.PossibleMoves;
import com.example.rummikubfrontscreen.setup.Tile;
import com.example.rummikubfrontscreen.setup.Utils;

import java.util.ArrayList;
import java.util.Random;

public class annSim {
    GameApp ga;
    GameSetup gs;
    LR model;

    public annSim(){
        gs = new GameSetup(2);
        ga = new GameApp(gs);
        model = new LR();
        MCTSmain mcmain = new MCTSmain();
        MCTS mcts = mcmain.getMcts();
        mcts.MctsPlayThrough(20);

        ArrayList<Node> inputs = mcts.inputs;
        ArrayList<Node> targets = mcts.targets;
        model.train(inputs, targets);
    }

    public void simulateGame(){
        while (!ga.isWinner()) {
            //System.out.println("new round");
            ga.getGs().getBoard().addDrawPile(ga.getGs().getTiles());
            Board b = ga.getGs().getBoard();
            
            if(ga.getGs().getBoard().getDrawPile().isEmpty()){
                System.out.println("no winner!");
                return;
            }

            ArrayList<Tile> h = ga.getCurPlr().getHand();
            System.out.println("player playing: " + ga.getCurPlr().getId() + ", hand: " + h);
            System.out.println("board before move: "+  b.toString());

            ArrayList<Object[]> moves = PossibleMoves.possibleMoves(b, h, 0);
            //System.out.println(moves.size());
            //System.out.println(ga.toString());
            if(ga.getCurPlr().getId()!=0){
                makeMove(moves);
            }else{
               makeMoveLR(moves, h.size());
            }
            moves.clear();
            //System.out.println("size after clearing: " +moves.size());
            System.out.println("\n");
        }
        System.out.println("The winner is: " + ga.getCurPlr().toString());
    }

    public boolean firstMove(ArrayList<Tile> move){
        int sum = 0;
        for(Tile tile : move){
            sum += tile.getInt();
        }
        if(sum>=30){
            return true;
        }
        return false;
    }

    public void makeMoveLR(ArrayList<Object[]> moves, int h){
        Object[] predictedOutput = null;
        if(!moves.isEmpty()){
            predictedOutput = model.predict(moves, h);
            if(predictedOutput!=null){
                ga.getGs().getBoard().setCurrentGameBoard(((Board) predictedOutput[0]).getCurrentGameBoard(), ((Board) predictedOutput[0]).getDrawPile());
                ga.getCurPlr().setHand((ArrayList<Tile>) predictedOutput[1]);
                System.out.println("move chosen: ");
                Utils.print(predictedOutput);
            }
        }else if(moves.isEmpty() || predictedOutput == null){
            Tile tile = ga.draw();
            System.out.println("drawn: " + tile);
            if(tile == null){
                System.out.println("No winner!");
            }
        }
        ga.nextPlayer();
    }

    public void makeMove(ArrayList<Object[]> moves){
        ArrayList<ArrayList<Tile>> newBoard = new ArrayList<>();
        ArrayList<Tile> newHand = new ArrayList<>();
        ArrayList<Tile> newDrawPile = new ArrayList<>();
        if(!moves.isEmpty()){
            int m = Integer.MAX_VALUE;
            int i = -1;
            for(Object[] state : moves) {
                ArrayList<Tile> n = (ArrayList<Tile>) state[1];
                Board ne = ((Board) state[0]);
                int l = n.size();
                if (l < m) {
                    m = l;
                    newBoard = new ArrayList<>(ne.getCurrentGameBoard());
                    newHand = new ArrayList<>(n);
                    newDrawPile = new ArrayList<>(ne.getDrawPile());
                }
            }
            if(!newBoard.isEmpty()) {
                System.out.println("move chosen: " + newBoard);
                ga.getGs().getBoard().setCurrentGameBoard(newBoard, newDrawPile);
                ga.getCurPlr().setHand(newHand);
            }
        }else if(moves.isEmpty() || newBoard.isEmpty()){
            Tile tile = ga.draw();
            System.out.println("drawn: " + tile);
            if(tile == null){
                System.out.println("No winner!");
            }
        }
        ga.nextPlayer();
    }

    // public void makeMoveRand(ArrayList<Object[]> moves){
    //     Random rand = new Random();
    //     int rn = rand.nextInt(moves.size());
    //     Object[] move = moves.get(rn);
    //     Board b = new Board(((Board) move[0]).getCurrentGameBoard());
    //     ArrayList<Tile> h = new ArrayList<>((ArrayList<Tile>) move[1]);
    //     System.out.println(ga.getCurPlr().getId());
    //     ga.getGs().getBoard().setCurrentGameBoard(b.getCurrentGameBoard());
    //     ga.getCurPlr().setHand(h);
    // }

    public static void main(String[] args) {
        annSim s = new annSim();
        s.simulateGame();
    }
}
