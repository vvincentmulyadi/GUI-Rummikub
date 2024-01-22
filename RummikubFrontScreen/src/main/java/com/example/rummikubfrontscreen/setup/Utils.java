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
            array[tileIndex] += 1;
        }
        return array;
    }

    public static void print(Board board, ArrayList<Tile> hand) {
        System.out.println("\nPRINTING STATE \nBoard: \n" + board.toString() + " Hand: " + hand.toString());
    }

    public static void print(Object[] moveState) {
        Board board = (Board) moveState[0];
        ArrayList<Tile> hand = (ArrayList<Tile>) moveState[1];
        System.out.println("Board: \n" + board.toString() + " Hand: " + hand.toString());
    }

    public static ArrayList<Tile> ArrayToArrayList(int[] array) {
        ArrayList<Tile> arrayList = new ArrayList<Tile>();
        for (int i = 0; i < array.length; i++) {
            for (int j = array[i]; j > 0; j--) {
                Tile tile = TileProbs.indexToTileConverter(i);
                arrayList.add(tile);
            }
        }
        return arrayList;
    }

    public static int[] arrayMinusList(int[] arrayc, ArrayList<Tile> list) {
        int[] array = arrayc.clone();
        for (Tile tile : list) {
            int i = TileProbs.tileToIndexConverter(tile);
            array[i] -= 1;
            if (array[i] < 0) {
                System.out.print("Error: array has negative values");
                System.out.println(tile);
            }
        }
        return array;
    }

}