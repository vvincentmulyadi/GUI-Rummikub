package com.example.rummikubfrontscreen.setup;

//import javax.naming.PartialResultException;
//import javax.swing.*;

import com.example.rummikubfrontscreen.setup.Exceptions.InvalidTilePlacement;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Board {

    //2D arraylist grid representing the runs and rows
    private ArrayList<ArrayList<Tile>> currentGameBoard;

    //Constructor for the board
    public Board(ArrayList<ArrayList<Tile>> listOfTiles){
        currentGameBoard = listOfTiles;
    }

    public Board(){};

    public boolean addPeaceToBoard (int x, int y, Tile tl) throws InvalidTilePlacement{
        // Every row will always have at least two null objects in every line so that you can either
        // continue the current series or start a new one
        if (x >= currentGameBoard.size()) {
            throw new InvalidTilePlacement("The Board doesnt have that many rows");
        } else if (y >= currentGameBoard.get(x).size()){
            throw new InvalidTilePlacement("The Board doesnt have that many columns in this row");
        } else if (currentGameBoard.get(x).get(y) != null) {
            throw new InvalidTilePlacement ("The place you chose to put your tile already has a tl.\nWe will enable theswitching of tiles soon");
        }


        currentGameBoard.get(x).add(y,tl);
        return true;
    }



    public static boolean boardVerifier(ArrayList<ArrayList<Tile>> seriesInGame){
        Tile tile1 = null, tile2 = null;
        //convert(currentGameBoard);
        for(int i = 0; i < seriesInGame.size();i++){
            if (seriesInGame.get(i).size() < 3) {return false;}
            //index of a first tl which is not a joker
            int indexTile1 = 0;
            //loop assigning values of first two tiles not being a joker
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

    private static boolean checkRun (ArrayList<Tile> run, Tile tile1, int indexOfTile1){
        Colour c = tile1.getColour();
        for(int i = 0; i<run.size(); i++){
            if (run.get(i).getValue() == Value.JOKER) continue;
            if (run.get(i).getColour() != c) return false;
            if (i - indexOfTile1 + tile1.getValue().getValue()!= run.get(i).getValue().getValue()) return false;
        }
        return true;
    }



    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < currentGameBoard.size(); i ++) {
            str+=  i + "  ";
            str += currentGameBoard.get(i).toString() + "  ";
            
            str += "\n";
        }
        return str;
    }

    public static void main(String[] args) {
        ArrayList<Tile> group = new ArrayList<>();
        Tile t1 = new Tile(Colour.BLUE, Value.SEVEN);
        group.add(t1);
        group.add(new Tile(Colour.RED, Value.SEVEN));
        group.add(new Tile(Colour.BLACK, Value.SEVEN));
        group.add(new Tile(Colour.YELLOW, Value.SEVEN));
        group.add(new Tile(Colour.YELLOW, Value.JOKER));

        ArrayList<Tile> run = new ArrayList<>();
        run.add(new Tile(Colour.YELLOW, Value.JOKER));
        run.add(new Tile(Colour.BLACK, Value.JOKER));
        Tile t2 = new Tile(Colour.BLACK, Value.NINE);
        run.add(t2);
        run.add(new Tile(Colour.YELLOW, Value.TEN));
        run.add(new Tile(Colour.BLACK, Value.ELEVEN));
        run.add(new Tile(Colour.BLACK, Value.JOKER));
        //run.add(new Tile(Colour.BLACK, Value.TWELVE));
        run.add(new Tile(Colour.BLACK, Value.THIRTEEN));

        Board board = new Board();

        System.out.println(board.checkGroup(group, t1));
        System.out.println(board.checkRun(run, t2, 2));
    }
    //copies board for mcts
    public Board copy() {
        ArrayList<ArrayList<Tile>> copyTiles = new ArrayList<>();
        for (ArrayList<Tile> row : currentGameBoard) {
            ArrayList<Tile> copyRow = new ArrayList<>(row);
            copyTiles.add(copyRow);
        }
        return new Board(copyTiles);
    }
}
