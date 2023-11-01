package com.example.rummikubfrontscreen.setup;

import java.util.ArrayList;
import java.util.Random;

public class GameApp {

    GameSetup gs;
    ArrayList<Player> players;
    private Player curPlr;

    // list of tiles in the bag
    public ArrayList<Tile> tiles;

    public GameApp() {
        gs = new GameSetup();
        players = gs.getPlayers();
        curPlr = players.get(0);
        tiles = gs.getTilesInBag();
    }

    // method to check if a player is a winner
    public boolean isWinner(){
        return curPlr.getHand().isEmpty();
    }

    // moving to next player
    public void nextPlayer () {
        int i = players.indexOf(curPlr);
        curPlr = players.get((i+1)%(players.size()-1));
    }

    // method for drawing a tile
    public Tile draw(){
        Random rand = new Random();
        int i = rand.nextInt(tiles.size()-1);
        Tile tile = tiles.get(i);
        curPlr.addTile(tile);
        tiles.remove(i);
        return tile;
    }

    // method to verify the state of the board and if it's valid
    public static boolean boardVerifier(ArrayList<ArrayList<Tile>> seriesInGame){
        Tile tile1 = null, tile2 = null;
        for(int i = 0; i < seriesInGame.size();i++){
            if (seriesInGame.get(i).size() < 3) {return false;}

            // index of a first tile which is not a joker
            int indexTile1 = 0;

            // loop assigning values of first two tiles not being a joker
            for(int j = 0; j < seriesInGame.get(i).size();j++){
                if(seriesInGame.get(i).get(j).getValue().equals(Value.JOKER)) continue;
                if (tile1 == null) {
                    tile1 = seriesInGame.get(i).get(j);
                    indexTile1 = i;
                }
                else{
                    tile2 = seriesInGame.get(i).get(j);
                    break;
                }
            }
            //case where there were two joker and one tl
            if (tile2 == null) continue;

            //checking whether we are checking a group or a run
            if (tile1.getValue() == tile2.getValue()) {
                // Check group
                if(!checkGroup(seriesInGame.get(i), tile1)) return false;
            } else {
                // Check Run
                if(!checkRun(seriesInGame.get(i), tile1, indexTile1)) return false;
            }
        }
        return true;
    }

    // method to check if a group is valid
    private static boolean checkGroup (ArrayList<Tile> group, Tile tile1) {
        Value v = tile1.getValue();
        ArrayList<Colour> coloursUsed = new ArrayList<>();
        if (group.size() > 4) return false;
        for (int i = 0; i < group.size(); i++) {
            if (group.get(i).getValue() == Value.JOKER) continue;
            if (group.get(i).getValue() != v) return false;
            if(coloursUsed.contains(group.get(i).getColour())) return false;
            else coloursUsed.add(group.get(i).getColour());
        }
        return true;
    }

    // method to check if a run is valid
    private static boolean checkRun (ArrayList<Tile> run, Tile tile1, int indexOfTile1){
        Colour c = tile1.getColour();
        for(int i = 0; i<run.size(); i++){
            if (run.get(i).getValue() == Value.JOKER) continue;
            if (run.get(i).getColour() != c) return false;
            if (i - indexOfTile1 + tile1.getValue().getValue()!= run.get(i).getValue().getValue()) return false;
        }
        return true;
    }

    public Player getCurPlr() {
        return curPlr;
    }

    public GameSetup getGs() {
        return gs;
    }

}
