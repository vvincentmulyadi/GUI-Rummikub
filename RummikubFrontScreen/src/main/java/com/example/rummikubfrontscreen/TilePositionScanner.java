package com.example.rummikubfrontscreen;

import com.example.rummikubfrontscreen.setup.Colour;
import com.example.rummikubfrontscreen.setup.Tile;
import com.example.rummikubfrontscreen.setup.Value;

import java.util.ArrayList;
import java.util.HashMap;

public class TilePositionScanner {

    public static void main(String[] args) {
        Tile t1 = new Tile(Colour.BLUE, Value.SEVEN, 15, 100);
        Tile t2 = new Tile(Colour.RED, Value.SEVEN, 30, 111);
        Tile t3 = new Tile(Colour.BLACK, Value.SEVEN, 41, 105);
        Tile t4 = new Tile(Colour.BLUE, Value.FOUR, 46, 700);
        Tile t5 = new Tile(Colour.BLUE, Value.FIVE, 31, 705);
        Tile t6 = new Tile(Colour.BLUE, Value.SIX, 33, 690);
        Tile t7 = new Tile(Colour.BLUE, Value.SEVEN, 70, 100);
        Tile t8 = new Tile(Colour.RED, Value.SEVEN, 75, 111);
        Tile t9 = new Tile(Colour.BLACK, Value.SEVEN, 90, 105);
        Tile t10 = new Tile(Colour.BLACK, Value.SEVEN, 300, 1);
        ArrayList<Tile> arr = new ArrayList<>();
        arr.add(t1);
        arr.add(t2);
        arr.add(t3);
        arr.add(t4);
        arr.add(t5);
        arr.add(t6);
        arr.add(t7);
        arr.add(t8);
        arr.add(t9);
        arr.add(t10);
        HashMap<Integer, ArrayList<Tile>> map = yScanner(arr);
        System.out.println(map);
        ArrayList<ArrayList<Tile>> arr2 = xScanner(map);
        System.out.print(arr2);
    }

    public static ArrayList<ArrayList<Tile>> scanner (ArrayList<Tile> unstructuredTiles) {
        HashMap<Integer, ArrayList<Tile>> map = yScanner(unstructuredTiles);
        System.out.println(map);
        ArrayList<ArrayList<Tile>> structeredTiles = xScanner(map);
        for (ArrayList<Tile> structeredTile : structeredTiles) {
            for (Tile tile : structeredTile) {

                System.out.println("Coordinates: " + tile.getY()+ " "+tile.getX());
            }
        }
        return structeredTiles;
    }

    private static HashMap<Integer, ArrayList<Tile>> yScanner(ArrayList<Tile> tiles) {
        // width 33, height 41 range 15
        HashMap<Integer, ArrayList<Tile>> map = new HashMap<>();
        ArrayList<Tile> arr = new ArrayList<>();
        arr.add(tiles.get(0));
        map.put(0, arr);
        for(int i = 1; i < tiles.size(); i++ ){
            boolean added = false;
            Tile currTile = tiles.get(i);
            double y = currTile.getY();
            for(int j = 0; j<map.size(); j++){
                Tile compare = map.get(j).get(0);
                if(Math.abs(y-compare.getY()) <= 15){
                    map.get(j).add(currTile);
                    added = true;
                    continue;
                }
            }
            if(!added){
                ArrayList<Tile> arr1 = new ArrayList<>();
                arr1.add(tiles.get(i));
                map.put(map.size(), arr1);
            }

        }
        return map;
    }

    private static ArrayList<ArrayList<Tile>> xScanner(HashMap<Integer, ArrayList<Tile>> map){
        ArrayList<ArrayList<Tile>> arr = new ArrayList<>();
        for(int i = 0; i < map.size(); i++){
            arr.add(new ArrayList<>());
            arr.get(arr.size()-1).add(map.get(i).get(0));
            for(int j = 0; j<map.get(i).size()-1; j++){
                Tile currTile = map.get(i).get(j);
                Tile nextTile = map.get(i).get(j+1);
                double currX = currTile.getX();
                double nextX = nextTile.getX();
                if(Math.abs(currX-nextX) <= 15){
                    arr.get(arr.size()-1).add(nextTile);
                }else{
                    arr.add(new ArrayList<>());
                    arr.get(arr.size()-1).add(nextTile);
                }
            }
        }
        return arr;
    }
}
