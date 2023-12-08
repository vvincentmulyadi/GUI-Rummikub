package com.example.rummikubfrontscreen.setup;

import com.example.rummikubfrontscreen.BoardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

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

    public boolean isWinner(){
        return curPlr.getHand().isEmpty();
    }

    public void nextPlayer () {
        int i = plrs.indexOf(curPlr);
        curPlr = plrs.get((i+1)%(plrs.size()-1));
    }

    public Tile draw(){
        Random rand = new Random();
        int i = rand.nextInt(tiles.size()-1);
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


}
