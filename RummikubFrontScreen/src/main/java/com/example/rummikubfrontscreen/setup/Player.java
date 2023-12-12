package com.example.rummikubfrontscreen.setup;

import java.util.ArrayList;

public class Player {

    ArrayList<Tile> hand;

    public int getId() {
        return id;
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
    public void sortByNum(ArrayList<Tile> tilesList, int start, int end) {
        if (start < end) {
            int i = partitionSort(tilesList, start, end);
            sortByNum(tilesList, start, i - 1);
            sortByNum(tilesList, i + 1, end);
        }
    }

    public int partitionSort(ArrayList<Tile> tilesList, int start, int end) {
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
    public void sortByColour(ArrayList<Tile> tilesList) {
        ArrayList<ArrayList<Tile>> separated = seperateColours(tilesList);

        // sorting each list with separated colours
        for (int i = 0; i < separated.size(); i++) {
            sortByNum(separated.get(i), 0, separated.get(i).size() - 1);
        }

        int index = 0;
        for (int i = 0; i < separated.size(); i++) {
            for (int j = 0; j < separated.get(i).size(); j++) {
                tilesList.set(index, separated.get(i).get(j));
                index++;
            }
        }
    }

    public void sortByColour() {
        sortByColour(hand);
    }

    // dividing the tiles into four lists according to colour
    public ArrayList<ArrayList<Tile>> seperateColours(ArrayList<Tile> tilesList) {
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
        String str = "";
        str += "Index " + 0 + ":  " + hand.get(0).toString() + "  ";
        for (int i = 1; i < hand.size(); i++) {
            str += "Index " + i + ":  " + hand.get(i).toString() + "  ";
            if (hand.get(i).getColour() == hand.get(i - 1).getColour())
                str += "\n";
        }
        return str;
    }

}
