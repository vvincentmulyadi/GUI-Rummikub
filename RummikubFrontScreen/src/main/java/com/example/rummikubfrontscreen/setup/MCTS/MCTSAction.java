package com.example.rummikubfrontscreen.setup.MCTS;

import com.example.rummikubfrontscreen.setup.Colour;
import com.example.rummikubfrontscreen.setup.Player;
import com.example.rummikubfrontscreen.setup.Tile;
import com.example.rummikubfrontscreen.setup.Value;

import java.util.*;

public class MCTSAction {

    HashMap<Colour, Integer> colorMap;
    private ArrayList<ArrayList<Tile>>initialBoard;
    private ArrayList<Tile> initialHand;
    private ArrayList<Tile>outputHand;
    private ArrayList<ArrayList<Tile>>outputBoard;
    private ArrayList<ArrayList<Tile>>possibleCombos;
    private ArrayList<ArrayList<Tile>>allPossibleCombos;
    private ArrayList<Tile>deck;
    private ArrayList<ArrayList<Tile>> randomMove;
    private boolean endTurn;
    private Random randomizer;

    public MCTSAction() {
        colorMap = new HashMap<>();
        colorMap.put(Colour.RED, 1);
        colorMap.put(Colour.BLUE, 2);
        colorMap.put(Colour.YELLOW, 3);
        colorMap.put(Colour.BLACK, 4);
        ArrayList<Tile> initialHand = new ArrayList<>();
    }

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
    public ArrayList<ArrayList<Tile>> ownMoverRun(ArrayList<Tile> currentHand) {
        // ArrayList<ArrayList<Tile>> legalMoves) {

        // RED, BLUE, YELLOW, BLACK
        ArrayList<ArrayList<Tile>> currentSepHand = partitionByColors(currentHand);
        ArrayList<ArrayList<Tile>> groups = new ArrayList<>();
        for (ArrayList<Tile> color : currentSepHand) {

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
                        // legalMoves.add(currentGroup);
                        currentGroup = (ArrayList<Tile>) currentGroup.clone();
                    }
                }

            }
        }

        return groups;
    }
    
    public ArrayList<ArrayList<Tile>> ownMoveGroup(ArrayList<Tile> currentHand) {
        // ArrayList<ArrayList<Tile>> legalMoves) {
        ArrayList<ArrayList<Tile>> currentSepHand = partitionByNumbers(currentHand);
        ArrayList<ArrayList<Tile>> groups = new ArrayList<>();
        System.out.println(currentSepHand);
        for (ArrayList<Tile> Number : currentSepHand) {
            HashSet<Colour> hashSet = new HashSet<>();

            if (Number.size() < 3) {
                continue;
            }

            for (int i = 0; i < Number.size(); i++) {
                ArrayList<Tile> currentGroup = new ArrayList<>();
                if (hashSet.contains(Number.get(i).getColour())) {
                    continue;
                }
                hashSet = new HashSet<>();

                hashSet.add(Number.get(i).getColour());
                currentGroup.add(Number.get(i));

                for (int j = i + 1; j < Number.size(); j++) {
                    if (hashSet.contains(Number.get(j).getColour())) {
                        continue;
                    }
                    hashSet.add(Number.get(j).getColour());
                    currentGroup.add(Number.get(j));
                    System.out.println(currentGroup);
                    if (hashSet.size() >= 3) {
                        groups.add(currentGroup);
                        // legalMoves.add(currentGroup);
                        currentGroup = (ArrayList<Tile>) currentGroup.clone();
                    }
                }

            }
        }

        return groups;
    }

    private ArrayList<ArrayList<Tile>> partitionByNumbers(ArrayList<Tile> unsortedHand) {

        Player sorter = new Player(unsortedHand);
        sorter.sortByColour();

        ArrayList<ArrayList<Tile>> partitionedByNumbers = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            partitionedByNumbers.add(new ArrayList<>());
        }

        for (int i = 1; i < unsortedHand.size(); i++) {
            Tile tile = unsortedHand.get(i);
            int index = tile.getInt();
            partitionedByNumbers.get(index - 1).add(tile);
        }

        System.out.println(partitionedByNumbers.toString());
        return partitionedByNumbers;
    }

    private ArrayList<ArrayList<Tile>> partitionByColors(ArrayList<Tile> unsortedHand) {

        Player sorter = new Player(unsortedHand);
        sorter.sortByColour();

        ArrayList<ArrayList<Tile>> partitionedByColour = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            partitionedByColour.add(new ArrayList<>());
        }

        for (int i = 0; i < unsortedHand.size(); i++) {
            Tile tile = unsortedHand.get(i);
            int index = colorMap.get(tile.getColour());
            partitionedByColour.get(index - 1).add(tile);
        }

        return partitionedByColour;
    }
    

    public static void main(String[] args) {

        ArrayList<Tile> groupYe = new ArrayList<>();

        groupYe.add(new Tile(Colour.YELLOW, Value.TWO));
        groupYe.add(new Tile(Colour.YELLOW, Value.ONE));
        groupYe.add(new Tile(Colour.YELLOW, Value.THREE));
        groupYe.add(new Tile(Colour.YELLOW, Value.SEVEN));
        groupYe.add(new Tile(Colour.YELLOW, Value.FIVE));
        groupYe.add(new Tile(Colour.YELLOW, Value.SIX));
        groupYe.add(new Tile(Colour.YELLOW, Value.SEVEN));
        groupYe.add(new Tile(Colour.YELLOW, Value.EIGHT));
        groupYe.add(new Tile(Colour.YELLOW, Value.EIGHT));
        groupYe.add(new Tile(Colour.BLACK, Value.EIGHT));
        groupYe.add(new Tile(Colour.RED, Value.EIGHT));
        groupYe.add(new Tile(Colour.RED, Value.NINE));
        groupYe.add(new Tile(Colour.RED, Value.SEVEN));
        groupYe.add(new Tile(Colour.BLUE, Value.EIGHT));
        ArrayList<ArrayList<Tile>> groups = new ArrayList<>();
        groups.add(groupYe);
        MCTSAction mcts = new MCTSAction();
        ArrayList<ArrayList<Tile>> legalMoves = mcts.ownMoveGroup(groupYe);
        System.out.println("Legal moves" + legalMoves.toString());

        HashMap<Colour, Integer> colorMap = new HashMap<>();
        colorMap.put(Colour.RED, 1);
        colorMap.put(Colour.BLUE, 2);
        colorMap.put(Colour.YELLOW, 3);
        colorMap.put(Colour.BLACK, 4);

    }

}
