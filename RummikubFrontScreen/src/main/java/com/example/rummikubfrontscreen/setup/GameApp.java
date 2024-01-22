package com.example.rummikubfrontscreen.setup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.example.rummikubfrontscreen.setup.MCTS.MCTSAction;

/**
 * The GameApp class represents a game application that manages players, tiles,
 * and game logic.
 */
public class GameApp {

    GameSetup gs;
    ArrayList<Player> plrs;
    private Player curPlr;
    public ArrayList<Tile> tiles;

    public GameApp() {
        gs = new GameSetup();
        plrs = gs.getPlayers();
        curPlr = plrs.get(0);
        tiles = gs.getTiles();
    }

    public GameApp(int amountOfPlayers) {
        gs = new GameSetup(amountOfPlayers);
        plrs = gs.getPlayers();
        curPlr = plrs.get(0);
        tiles = gs.getTiles();
    }

    public GameApp(GameSetup gs) {
        this.gs = gs;
        plrs = gs.getPlayers();
        curPlr = plrs.get(0);
        tiles = gs.getTiles();
    }

    public boolean isWinner() {
        return curPlr.getHand().isEmpty();
    }

    public void nextPlayer() {
        int i = plrs.indexOf(curPlr);
        if (i == plrs.size() - 1) {
            curPlr = plrs.get(0);
        } else {
            curPlr = plrs.get(i + 1);
        }
    }

    public void previousPlayer() {
        int i = plrs.indexOf(curPlr);
        if (i == 0) {
            i = 4;
        } else {
            i = i - 1;
        }
        curPlr = plrs.get(i);
    }

    public Tile draw() {
        Random rand = new Random();
        if ((tiles.size() - 1) < 1) {
            System.out.println("No more tiles to draw!");
            return null;
        }
        int i = rand.nextInt(tiles.size() - 1);
        Tile tile = tiles.get(i);
        curPlr.addTile(tile);
        tiles.remove(i);
        return tile;
    }

    public Player getCurPlr() {
        return curPlr;
    }

    public GameSetup getGs() {
        return gs;
    }

    public ArrayList<Player> getPlayers(){
        return plrs;
    }

}
