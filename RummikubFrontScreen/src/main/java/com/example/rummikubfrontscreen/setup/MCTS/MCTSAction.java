package com.example.rummikubfrontscreen.setup.MCTS;

import java.util.*;

import com.example.rummikubfrontscreen.setup.Board;
import com.example.rummikubfrontscreen.setup.Colour;
import com.example.rummikubfrontscreen.setup.Player;
import com.example.rummikubfrontscreen.setup.Tile;
import com.example.rummikubfrontscreen.setup.Utils;
import com.example.rummikubfrontscreen.setup.Value;

/**
 * The MCTSAction class contains methods for generating legal moves in a game and partitioning a hand
 * of tiles by color or number.
 */
public class MCTSAction {

    HashMap<Colour, Integer> colorMap;

    public MCTSAction() {
        colorMap = new HashMap<>();
        colorMap.put(Colour.RED, 1);
        colorMap.put(Colour.BLUE, 2);
        colorMap.put(Colour.YELLOW, 3);
        colorMap.put(Colour.BLACK, 4);
    }

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

    public static ArrayList<ArrayList<Tile>> ownMoveGroup(ArrayList<Tile> currentHand) {
        // ArrayList<ArrayList<Tile>> legalMoves) {
        ArrayList<ArrayList<Tile>> currentSepHand = partitionByNumbers(currentHand);
        ArrayList<ArrayList<Tile>> groups = new ArrayList<>();
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

    public static Object[] drawTileFromBoard(Board board, ArrayList<Tile> currentHand) {
        Board drawBoard = board.clone();

        Tile drawTile = drawBoard.drawTile();
        if (drawTile == null) {
            Object[] moveState = new Object[2];
            moveState[0] = board;
            moveState[1] = currentHand;
            System.out.println("Error: drawTile is null");
            return null;
        }
        currentHand.add(drawTile);

        Object[] moveState = new Object[2];
        moveState[0] = drawBoard;
        moveState[1] = currentHand;
        return moveState;
    }

    public static ArrayList<Object[]> ownMoveGroup(Board board, ArrayList<Tile> currentHand) {
        ArrayList<Object[]> legalMoveStates = new ArrayList<>();
        int[] handArray = Utils.aListToArray(currentHand);
        ArrayList<ArrayList<Tile>> legalMoves = ownMoveGroup(currentHand);

        // First move is drawing a tile
        if (board.getDrawPile() == null || !board.getDrawPile().isEmpty()) {

            Board drawBoard = board.clone();
            // System.out.println("Hand before drawing tile: " + currentHand.toString());
            Tile drawTile = drawBoard.drawTile();

            currentHand.add(drawTile);
            // System.out.println("Board before finding moves");
            // System.out.println(currentHand.toString());
            // System.out.println(board.toString());

            Object[] moveState = new Object[2];
            moveState[0] = board;
            moveState[1] = currentHand;
            legalMoveStates.add(moveState);
        }

        for (int i = 0; i < legalMoves.size(); i++) {

            ArrayList<Tile> move = legalMoves.get(i);
            Object[] newMoveState = new Object[2];

            int[] handArrayAfterMove = Utils.arrayMinusList(handArray, move);

            ArrayList<Tile> newHand = Utils.ArrayToArrayList(handArrayAfterMove);

            Board newBoard = board.clone();
            System.out.println("\nThe new added Move Added: \n" + newBoard.addSeries(move));
            System.out.println("Board: \n" + newBoard.toString());
            newMoveState[0] = newBoard;
            newMoveState[1] = newHand;
            legalMoveStates.add(newMoveState);
        }
        return legalMoveStates;
    }

    private static ArrayList<ArrayList<Tile>> partitionByNumbers(ArrayList<Tile> unsortedHand) {

        Player sorter = new Player(unsortedHand);
        sorter.sortByColour();

        ArrayList<ArrayList<Tile>> partitionedByNumbers = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            partitionedByNumbers.add(new ArrayList<>());
        }

        for (int i = 1; i < unsortedHand.size(); i++) {
            Tile tile = unsortedHand.get(i);
            int index = tile.getInt();
            partitionedByNumbers.get(index - 1).add(tile);
        }

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
            if (tile == null)
                continue;
            int index = colorMap.get(tile.getColour());
            partitionedByColour.get(index - 1).add(tile);
        }

        return partitionedByColour;
    }

}
