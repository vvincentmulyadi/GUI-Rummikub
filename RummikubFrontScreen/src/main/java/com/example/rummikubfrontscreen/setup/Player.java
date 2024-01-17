package com.example.rummikubfrontscreen.setup;

import java.util.ArrayList;
import java.util.Collections;



/**
 * The Player class represents a player in a game and provides methods for managing their hand of
 * tiles.
 */
public class Player {

    ArrayList<Tile> hand;

    public int getId() {
        return id;
    }

    /**
     * A way of deep cloning the player
     */
    @Override
    public Player clone() {
        ArrayList<Tile> newHand = new ArrayList<>();
        for (Tile tile : hand) {
            newHand.add(tile.clone());
        }
        Player newPlayer = new Player(newHand);
        newPlayer.setId(this.id);
        return newPlayer;
    }

    /**
     * A way of deep cloning the player with a different hand
     * 
     * @param hand
     * @return
     */
    public Player clone(ArrayList<Tile> hand) {
        Player newPlayer = new Player(hand);
        newPlayer.setId(this.id);
        return newPlayer;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public void setHand(ArrayList<Tile> hand) {
        this.hand = hand;
    }

    public Player(ArrayList<Tile> hand) {
        this.hand = hand;
    }

    public void addTile(Tile tile) {
        hand.add(tile);
    }

    public void removeTile(Tile tile) {
        if (hand.contains(tile)) {
            hand.remove(tile);
        }
        ;
    }

    public ArrayList<Tile> getHand() {
        return hand;
    }

    // sorts the tiles by number using quicksort
    public static ArrayList<Tile> sortByNum(ArrayList<Tile> tilesList, int start, int end) {
        if (start < end) {
            int i = partitionSort(tilesList, start, end);
            sortByNum(tilesList, start, i - 1);
            sortByNum(tilesList, i + 1, end);
        }
        return tilesList;
    }

    public void sortByColour() {
        sortByColor(hand);
    }

    public static int partitionSort(ArrayList<Tile> tilesList, int start, int end) {
        int pivot = tilesList.get(end).getValue().getValue();
        int i = start - 1;

        for (int j = start; j < end; j++) {
            if (tilesList.get(j).getValue().getValue() <= pivot) {
                i++;
                Tile temp = tilesList.get(i);
                tilesList.set(i, tilesList.get(j));
                tilesList.set(j, temp);
            }
        }

        Tile temp = tilesList.get(i + 1);
        tilesList.set(i + 1, tilesList.get(end));
        tilesList.set(end, temp);

        return i + 1;
    }

    // sorting the list of tiles first by colour and then by number
    public static void sortByColor(ArrayList<Tile> tilesList) {
        ArrayList<ArrayList<Tile>> separated = seperateColours(tilesList);
        Collections.sort(tilesList,new TileComparator());
    }

    // dividing the tiles into four lists according to colour
    public static ArrayList<ArrayList<Tile>> seperateColours(ArrayList<Tile> tilesList) {
        ArrayList<ArrayList<Tile>> separated = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            separated.add(new ArrayList<Tile>());
        }

        for (int i = 0; i < tilesList.size(); i++) {
            Tile currentTile = tilesList.get(i);
            Colour c = currentTile.getColour();
            switch (c) {
                case RED:
                    separated.get(0).add(currentTile);
                    break;
                case BLUE:
                    separated.get(1).add(currentTile);
                    break;
                case YELLOW:
                    separated.get(2).add(currentTile);
                    break;
                case BLACK:
                    separated.get(3).add(currentTile);
                    break;
            }
        }
        return separated;
    }

    @Override
    public String toString() {
        String str = "Player " + id + ": \n";
        for (Tile tile : hand) {
            str += tile.toString() + " ";
        }
        return str + "\n";
    }

}
