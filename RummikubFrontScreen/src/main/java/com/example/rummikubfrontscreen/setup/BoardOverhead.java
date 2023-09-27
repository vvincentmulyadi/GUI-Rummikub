package com.example.rummikubfrontscreen.setup;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import com.example.rummikubfrontscreen.setup.Exceptions.InvalidTilePlacement;

public class BoardOverhead {
    Board board;
    ArrayList<Player> players;

    
    public BoardOverhead(Board board, ArrayList<Player> players){
        this.board = board;
        this.players = players;
     }

     public void addPLayer(Player player){
         players.add(player);
        }

        public void removePlayer(Player player){
            if(players.contains(player)) players.remove(player);
     }

     public void placeTile (int x, int y,Player plyr,Tile tl){
         
         try {
            board.addPeaceToBoard(x,y,tl);
            plyr.removeTile(tl);

            System.out.println("Test");

        } catch (InvalidTilePlacement e) {
            e.printStackTrace();
        }

         
    }



    public static void main(String[] args)  {  
        Scanner sc= new Scanner(System.in); //System.in is a standard input stream 
        String playing; 
        GameSetup gs = new GameSetup();
        BoardOverhead bohe = new BoardOverhead (gs.getBoard(),gs.getPlayers());
        

        System.out.println("Press Y to lay a tile");
        playing = sc.next();

        while (playing.equals("Y") || playing.equals("y")) {
            Player curPlayer = bohe.getPlayers().get(0);
            System.out.println(curPlayer);
            System.out.print("Enter the index of your tile ");  
            
            int indexOfTile= sc.nextInt();
            Tile curTile = curPlayer.getHand().get(indexOfTile);

            System.out.println(gs.getBoard());
            
            System.out.print("Enter in which row you want to place your tile: ");  
            int x = sc.nextInt();
            
            System.out.print("and in which column: ");  
            int y = sc.nextInt();


            bohe.placeTile(x, y, curPlayer, curTile);
            System.out.println();
            System.out.println(gs.getBoard());

            System.out.println("Press Y to lay another tile");
            playing = sc.next();
        }

        if (gs.getBoard().boardVerifier()){
            System.out.println("Your move was valid! \nMaybe you didnt get to 30 but we dont care about that right now :)");
        } else {
            System.out.println("Ajajay that move was not on point. I think you made a mistake");
        }

              
    }  
    
    public ArrayList<Player> getPlayers() {
       return players;
    }
    
}
