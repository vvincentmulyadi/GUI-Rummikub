package com.example.rummikubfrontscreen.setup;

import com.example.rummikubfrontscreen.BoardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class GameApp {

    GameSetup gs = new GameSetup();
    BoardOverhead bohe = new BoardOverhead (gs.getBoard(),gs.getPlayers());
    ArrayList<Player> plrs = bohe.getPlayers();

    private Player curPlr = plrs.get(0);
    private ArrayList<Tile> tiles = gs.getTiles();

    public void letsPlayRummi(){
    }

    public boolean isWinner(){
        return false;
    }

    private void nextPlayer () {
        int i = plrs.indexOf(curPlr);
        curPlr = plrs.get((i+1)%(plrs.size()-1));
    }

    public void draw(Player player){
        Random rand = new Random();
        int i = rand.nextInt(tiles.size())-1;
        Tile tile = tiles.get(i);
        player.hand.add(tile);
        tiles.remove(i);
    }
    public Player getCurPlr() {
        return curPlr;
    }

    public GameSetup getGs() {
        return gs;
    }

    public BoardOverhead getBohe() {
        return bohe;
    }

}
