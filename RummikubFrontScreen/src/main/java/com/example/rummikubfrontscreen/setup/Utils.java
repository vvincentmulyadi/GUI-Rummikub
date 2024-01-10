package com.example.rummikubfrontscreen.setup;

import java.util.*; // Import the ArrayList class
import com.example.rummikubfrontscreen.setup.MCTS.TileProbs;

public class Utils {
    /**
     * ArrayList<Tile> = {Y1, Y1, R1,Y2, B2}
     * -> int[] = [0, 2, 1, 0, 0, 1, 0, 0, 1...] (53 elements)
     * 
     * @param arrayList
     * @return array
     */
    public static int[] aListToArray(ArrayList<Tile> arrayList) {
        int[] array = new int[53];
        for (Tile tile : arrayList) {
            int tileIndex = TileProbs.tileToIndexConverter(tile);
            System.out.println(tileIndex);
            array[tileIndex] += 1;
        }
        return array;
    }

    public static ArrayList<Tile> ArrayToaListArray(int[] array) {
        ArrayList<Tile> arrayList = new ArrayList<Tile>();
        for (int i : array) {
            Tile tile = TileProbs.indexToTileConverter(i);
            arrayList.add(tile);
        }
        return arrayList;
    }

    public static int[] arrayMinusList(int[] array, ArrayList<Tile> list) {
        for (Tile tile : list) {
            int i = TileProbs.tileToIndexConverter(tile);
            array[i] -= 1;
            if (array[i] < 0) {
                System.out.println("Error: array has negative values");
            }
        }
        return array;
    }

    public static void main(String[] args) {
        ArrayList<Tile> groupYe = new ArrayList<>();

        groupYe.add(new Tile(Colour.YELLOW, Value.ONE));
        groupYe.add(new Tile(Colour.YELLOW, Value.ONE));
        groupYe.add(new Tile(Colour.YELLOW, Value.TWO));
        groupYe.add(new Tile(Colour.RED, Value.ONE));
        groupYe.add(new Tile(Colour.BLACK, Value.TWO));
        groupYe.add(new Tile(Colour.YELLOW, Value.SIX));
        groupYe.add(new Tile(Colour.YELLOW, Value.SEVEN));
        groupYe.add(new Tile(Colour.YELLOW, Value.EIGHT));
        groupYe.add(new Tile(Colour.YELLOW, Value.EIGHT));
        groupYe.add(new Tile(Colour.BLACK, Value.EIGHT));
        groupYe.add(new Tile(Colour.RED, Value.EIGHT));
        groupYe.add(new Tile(Colour.RED, Value.NINE));
        groupYe.add(new Tile(Colour.RED, Value.SEVEN));
        groupYe.add(new Tile(Colour.BLUE, Value.EIGHT));
        ArrayList<Tile> groups = new ArrayList<>();

        System.out.println(Arrays.toString(aListToArray(groupYe)));

    }

}