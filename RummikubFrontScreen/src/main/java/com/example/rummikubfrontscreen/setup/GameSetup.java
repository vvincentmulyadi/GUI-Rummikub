package com.example.rummikubfrontscreen.setup;

import java.util.ArrayList;
import java.util.Random;

public class GameSetup {

    private ArrayList<Tile> tilesInBag;
    private ArrayList<Tile> allTiles = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();


    // generating the tiles and players
    public GameSetup () {
        generateTiles();
        for (int i = 0; i < 5; i++) {
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
        System.out.println(game.getTilesInBag());
    }

    // generating hand for each player
    private ArrayList<Tile> generateHand(){
        Random rand = new Random();
        int size = tilesInBag.size();
        ArrayList<Tile> hand = new ArrayList<>();

        for (int i = 0; i <  14; i++) {
            int index = rand.nextInt(size - i);
            hand.add(tilesInBag.get(index));
            tilesInBag.remove(index);
        }
        return hand;
    }

    // generating the tiles for the game
    private void generateTiles () {
        tilesInBag = new ArrayList<>();

        for (Value v : Value.values()) {
            for (Colour c: Colour.values()){
                tilesInBag.add(new Tile(c,v));
            }
        }
        for (int i = tilesInBag.size()-4; i < tilesInBag.size(); i++){
            tilesInBag.remove(i);
        }
        for (Value v : Value.values()) {
            for (Colour c: Colour.values()){
                tilesInBag.add(new Tile(c,v));
            }
        }
        // Delete 6 Jokers
        for (int i = 0; i < 4; i ++) {
            tilesInBag.remove(tilesInBag.size()-1);
        }
        for (Tile tile : tilesInBag) {
            allTiles.add(tile);
        }
    }


    public ArrayList<Tile> getTilesInBag() {
        return tilesInBag;
    }

    public ArrayList<Tile> getAllTiles() {
        return allTiles;
    }


    public ArrayList<Player> getPlayers() {
        return players;
    }
}
