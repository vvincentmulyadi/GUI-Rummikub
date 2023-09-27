package com.example.rummikubfrontscreen.setup;

import java.util.ArrayList;
import java.util.Random;

public class GameSetup {

    private ArrayList<Tile> tiles;
    
    private ArrayList<Player> players = new ArrayList<>();

    Board board;


    
    public GameSetup () {
        generateTiles();
        generateBoard();
        for (int i = 0; i < 4; i++) {
            players.add(new Player(generateHand()));
        }
        for (Player player : players) {
            player.sortByColour(player.hand);
        }
    }
    

    public static void main(String[] args) {
        GameSetup game = new GameSetup();
        game.generateTiles();
        for (int i = 0; i < 4; i++) {
            game.players.add(new Player(game.generateHand()));
        }
        for (Player player : game.players) {
            player.sortByColour(player.hand);
            System.out.println(player.getHand());
        }


        Board board = new Board();
        BoardOverhead overHead = new BoardOverhead(board,game.players);

        
    }

    private void generateBoard () {
        ArrayList<ArrayList<Tile>> newGameBoard = new ArrayList<>();

        for (int i = 0; i < 13; i++){
            newGameBoard.add(new ArrayList());
            newGameBoard.get(i).add(null);
            newGameBoard.get(i).add(null);
        }
        board = new Board(newGameBoard);
    }

    private ArrayList<Tile> generateHand(){
        Random rand = new Random();
        int size = tiles.size();
        ArrayList<Tile> hand = new ArrayList<>();

        for (int i = 0; i <  14; i++) {
            int index = rand.nextInt(size - i);
            hand.add(tiles.get(index));
             tiles.remove(index);
        }
        return hand;
    }


    private void generateTiles () {
        tiles = new ArrayList<>();
        for (Colour c: Colour.values()){
            for (Value v : Value.values()) {
                tiles.add(new Tile(c,v));
            }
            
            for (Value v : Value.values()) {
                tiles.add(new Tile(c,v));
            }
        }

        // Delete 6 Jokers
        for (int i = 0; i < 6; i ++) {
            tiles.remove(13 * (i + 1));
        }
        
    }


    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public Board getBoard() {
        return board;
    }
    
    
    public ArrayList<Player> getPlayers() {
        return players;
    }
}
