package com.example.rummikubfrontscreen.setup.MCTS;

import com.example.rummikubfrontscreen.Main;
import com.example.rummikubfrontscreen.setup.*;

import java.util.*;

public class Move {
    // everything simplified to the bare parameters to avoid tight decoupling here
    Player currentPlayer;
    private ArrayList<ArrayList<Tile>> initialBoard;
    private Board currentBoard;
    private ArrayList<Tile> initialHand;
    private ArrayList<Tile> currentHand;
    private ArrayList<Tile> outputHand;

    private ArrayList<ArrayList<ArrayList<Tile>>> resultingBoards;
    private ArrayList<ArrayList<Tile>> possibleCombos;
    private ArrayList<ArrayList<Tile>> allSets;
    private ArrayList<Tile> deck;
    private ArrayList<ArrayList<Tile>> randomMove;
    HashMap<Colour, Integer> colorMap;

    public Move(ArrayList<ArrayList<Tile>> board, ArrayList<Tile> hand) {
        this.initialBoard = board;
        this.initialHand = hand;
        this.resultingBoards = new ArrayList<>();
        this.outputHand = new ArrayList<>();
        ArrayList<ArrayList<Tile>> simulatedBoard = copy(board);
        colorMap = new HashMap<>();
        colorMap.put(Colour.RED, 1);
        colorMap.put(Colour.BLUE, 2);
        colorMap.put(Colour.YELLOW, 3);
        colorMap.put(Colour.BLACK, 4);
        allSets = generateSets(hand);
    }

    private ArrayList<ArrayList<Tile>> generateSets(ArrayList<Tile> hand) {
        this.currentHand = hand;
        ArrayList<ArrayList<Tile>> runs = ownMoverRun(this.currentHand);
        ArrayList<ArrayList<Tile>> groups = ownMoveGroup(this.currentHand);
        ArrayList<ArrayList<Tile>> lines = simpleMove(this.currentHand, this.currentPlayer, this.currentBoard);
        ArrayList<ArrayList<Tile>> allSets = new ArrayList<ArrayList<Tile>>();
        allSets.addAll(runs);
        allSets.addAll(groups);
        allSets.addAll(lines);
        return allSets;
    }

    private static boolean isSetPresent(ArrayList<Tile> hand, ArrayList<Tile> set) {
        if (hand.isEmpty()) {
            return false;
        }
        Set<Tile> checkHand = new HashSet<>(hand);
        Set<Tile> checkSet = new HashSet<>(set);
        return checkHand.containsAll(checkSet);

    }

    public ArrayList<ArrayList<Tile>> getAllSets() {
        return allSets;
    }

    private static void remove(List<Tile> tileList, ArrayList<Tile> elementsToRemove) {
        for (Tile tile : elementsToRemove) {
            tileList.remove(tile);
        }
    }

    public static ArrayList<ArrayList<Tile>> simpleMove(ArrayList<Tile> currentHand, Player currentPlayer,
            Board board) {
        ArrayList<ArrayList<Tile>> lines = board.getCurrentGameBoard();
        for (ArrayList<Tile> line : lines) {
            for (Tile tile : currentHand) {
                ArrayList<Tile> newLine = new ArrayList<>(line);
                newLine.add(tile);
                if (board.checkGroup(newLine, tile)) {
                    lines.set(lines.indexOf(line), newLine);
                    return lines;
                }
                if (board.checkRun(newLine, tile, getFirstNonJokerTileIndex(line))) {
                    ArrayList<Tile> newHand = new ArrayList<>(currentHand);
                    newHand.remove(tile);
                    lines.set(lines.indexOf(line), newLine);
                    while (true) {
                        Tile newTile = getFirstTileFromHand(newHand);
                        if (newTile == null) {
                            break;
                        }
                        newLine.add(newTile);
                        if (!board.checkRun(newLine, getFirstNonJokerTile(newLine),
                                getFirstNonJokerTileIndex(newLine))) {
                            break;
                        }
                        lines.set(lines.indexOf(line), newLine);
                    }
                    return lines;
                }
            }
        }

        return lines;
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

    private static int getFirstNonJokerTileIndex(ArrayList<Tile> line) {
        for (int i = 0; i < line.size(); i++) {
            if (!line.get(i).getValue().equals(Value.JOKER)) {
                return i;
            }
        }
        return -1;
    }

    private static Tile getFirstNonJokerTile(ArrayList<Tile> line) {
        for (Tile tile : line) {
            if (!tile.getValue().equals(Value.JOKER)) {
                return tile;
            }
        }
        return null;
    }

    private static Tile getFirstTileFromHand(ArrayList<Tile> hand) {
        if (!hand.isEmpty()) {
            return hand.remove(0);
        }
        return null;
    }

    public ArrayList<ArrayList<Tile>> copy(ArrayList<ArrayList<Tile>> original) {
        ArrayList<ArrayList<Tile>> copyArrayList = new ArrayList<>();
        for (ArrayList<Tile> inArrayList : original) {
            ArrayList<Tile> tempCopy = new ArrayList<>(inArrayList);
            copyArrayList.add(tempCopy);
        }
        return copyArrayList;
    }

    public ArrayList<Tile> convert(ArrayList<ArrayList<Tile>> currentBoard) {
        ArrayList<Tile> result = new ArrayList<>();
        for (ArrayList<Tile> line : currentBoard) {
            for (Tile tile : line) {
                result.add(tile);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        ArrayList<Tile> groupYe = new ArrayList<>();

        groupYe.add(new Tile(Colour.YELLOW, Value.TWO));
        groupYe.add(new Tile(Colour.YELLOW, Value.ONE));
        groupYe.add(new Tile(Colour.YELLOW, Value.THREE));
        Tile node = groupYe.get(0);

        ArrayList<ArrayList<Tile>> groups = new ArrayList<>();
        ArrayList<ArrayList<Tile>> board = new ArrayList<>();
        groups.add(groupYe);
        Move move = new Move(board, groupYe);
        ArrayList<ArrayList<Tile>> legalMoves = move.generateSets(groupYe);
        System.out.println("Legal moves" + legalMoves.toString());

        HashMap<Colour, Integer> colorMap = new HashMap<>();
        colorMap.put(Colour.RED, 1);
        colorMap.put(Colour.BLUE, 2);
        colorMap.put(Colour.YELLOW, 3);
        colorMap.put(Colour.BLACK, 4);
    }

}