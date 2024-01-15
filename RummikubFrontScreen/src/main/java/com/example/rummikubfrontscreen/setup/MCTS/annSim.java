// package com.example.rummikubfrontscreen.setup.MCTS;

// import com.example.rummikubfrontscreen.setup.Board;
// import com.example.rummikubfrontscreen.setup.GameApp;
// import com.example.rummikubfrontscreen.setup.GameSetup;
// import com.example.rummikubfrontscreen.setup.Tile;

// import java.util.ArrayList;
// import java.util.Random;

// public class annSim {
//     GameApp ga;
//     GameSetup gs;

//     public annSim(){
//         gs = new GameSetup(4);
//         ga = new GameApp(gs);
//     }

//     public void simulateGame(){
//         while (!ga.isWinner()) {
//             //System.out.println("new round");
//             ga.getGs().getBoard().addDrawPile(ga.getGs().getTiles());
//             ga.b = ga.getGs().getBoard();

//             ArrayList<Tile> h = ga.getCurPlr().getHand();
//             System.out.println("player playing: " + ga.getCurPlr().getId() + ", hand: " + h);
//             System.out.println("board before move: "+  b.toString());

//             ga.possibleMoves(b, h);
//             ArrayList<Object[]> moves = ga.getStates();
//             //System.out.println(moves.size());
//             //System.out.println(ga.toString());
//             makeMove(moves, b);
//             moves.clear();
//             //System.out.println("size after clearing: " +moves.size());
//             System.out.println("\n");
//         }
//         System.out.println("The winner is: " + ga.getCurPlr().toString());
//     }

//     public boolean firstMove(ArrayList<Tile> move){
//         int sum = 0;
//         for(Tile tile : move){
//             sum += tile.getInt();
//         }
//         if(sum>=30){
//             return true;
//         }
//         return false;
//     }

//     public void makeMove(ArrayList<Object[]> moves, Board b){
//         ArrayList<ArrayList<Tile>> newBoard = new ArrayList<>();
//         ArrayList<Tile> newHand = new ArrayList<>();
//         if(!moves.isEmpty()){
//             int m = Integer.MAX_VALUE;
//             int i = -1;
//             for(Object[] state : moves) {
//                 ArrayList<Tile> n = (ArrayList<Tile>) state[1];
//                 Board ne = ((Board) state[0]);
//                 int l = n.size();
//                 if (l < m) {
//                     m = l;
//                     newBoard = new ArrayList<>(ne.getCurrentGameBoard());
//                     newHand = new ArrayList<>(n);
//                 }
//             }if(!newBoard.isEmpty()) {
//                 System.out.println("move chosen: " + newBoard);
//                 ga.getGs().getBoard().setCurrentGameBoard(newBoard);
//                 ga.getCurPlr().setHand(newHand);
//             }
//         }else if(moves.isEmpty() || newBoard.isEmpty()){
//             Tile tile = ga.draw();
//             System.out.println("drawn: " + tile);
//             if(tile == null){
//                 System.out.println("No winner!");
//             }
//         }
//         ga.nextPlayer();
//     }

//     public void makeMoveRand(ArrayList<Object[]> moves){
//         Random rand = new Random();
//         int rn = rand.nextInt(moves.size());
//         Object[] move = moves.get(rn);
//         Board b = new Board(((Board) move[0]).getCurrentGameBoard());
//         ArrayList<Tile> h = new ArrayList<>((ArrayList<Tile>) move[1]);
//         System.out.println(ga.getCurPlr().getId());
//         ga.getGs().getBoard().setCurrentGameBoard(b.getCurrentGameBoard());
//         ga.getCurPlr().setHand(h);
//     }

//     public static void main(String[] args) {
//         annSim s = new annSim();
//         s.simulateGame();
//     }
// }
