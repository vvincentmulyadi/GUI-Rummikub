package com.example.rummikubfrontscreen.setup;

import java.util.*;

public class MCTsActions {
    // Big problem:
    // Limiting our moves to only the max length or maximal # of tiles we place may
    // hinder a second move.
    // How do we account for jokers in our hand? Are we gonna place a value on the
    // Move such that we prefer playing without the joker?
    // How are we going to keep track the legal Moves ?
    // Like git ? By only keeping track of the changes ? No to complex
    /*
     * TODO: Sorting the current hand before executing the ownMove method
     * 
     */
    private ArrayList<ArrayList<Tile>> ownMoverRun(ArrayList<ArrayList<Tile>> currentHand,
            ArrayList<ArrayList<Tile>> legalMoves) {
        ArrayList<Tile> blueTiles = new ArrayList<>();
        ArrayList<Tile> blackTiles = new ArrayList<>();
        ArrayList<Tile> greenTiles = new ArrayList<>();
        ArrayList<Tile> yellowTiles = new ArrayList<>();

        ArrayList<ArrayList<Tile>> allTiles = new ArrayList<>();
        allTiles.add(blueTiles);
        allTiles.add(blackTiles);
        allTiles.add(greenTiles);
        allTiles.add(yellowTiles);

        // Exp
        allTiles.add(currentHand.get(0));

        ArrayList<ArrayList<Tile>> groups = new ArrayList<>();
        for (ArrayList<Tile> color : allTiles) {
            // we try to find a possible series of 3 at least tiles
            if (color.size() < 3) {
                continue;
            }
            for (int i = 0; i < color.size(); i++) {
                ArrayList<Tile> currentGroup = new ArrayList<>();
                currentGroup.add(color.get(i));
                for (int j = i + 1; j < color.size(); j++) {
                    if (color.get(j).getInt() == currentGroup.get(currentGroup.size() - 1).getInt()) {
                        continue;
                    }
                    if (color.get(j).getInt() != currentGroup.get(currentGroup.size() - 1).getInt() + 1
                            && color.get(j).getInt() != currentGroup.get(currentGroup.size() - 1).getInt() - 1) {
                        break;
                    }
                    currentGroup.add(color.get(j));
                    if (currentGroup.size() >= 3) {
                        groups.add(currentGroup);
                        currentGroup = (ArrayList<Tile>) currentGroup.clone();

                    }
                }

            }
        }
        return groups;
    }

    private ArrayList<ArrayList<Tile>> ownMoveGroup(ArrayList<ArrayList<Tile>> currentHand,
            ArrayList<ArrayList<Tile>> legalMoves) {

        return null;
    }

    public static void main(String[] args) {
        ArrayList<Tile> groupYe = new ArrayList<>();

        groupYe.add(new Tile(Colour.YELLOW, Value.ONE));
        groupYe.add(new Tile(Colour.YELLOW, Value.TWO));
        groupYe.add(new Tile(Colour.YELLOW, Value.THREE));
        groupYe.add(new Tile(Colour.YELLOW, Value.FIVE));
        groupYe.add(new Tile(Colour.YELLOW, Value.SIX));
        groupYe.add(new Tile(Colour.YELLOW, Value.SEVEN));
        groupYe.add(new Tile(Colour.YELLOW, Value.SEVEN));
        groupYe.add(new Tile(Colour.YELLOW, Value.EIGHT));
        groupYe.add(new Tile(Colour.YELLOW, Value.EIGHT));
        ArrayList<ArrayList<Tile>> groups = new ArrayList<>();
        groups.add(groupYe);
        MCTsActions mcts = new MCTsActions();
        ArrayList<ArrayList<Tile>> legalMoves = mcts.ownMoverRun(groups, new ArrayList<>());
        System.out.println(legalMoves.toString());

    }

}
