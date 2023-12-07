package com.example.rummikubfrontscreen;

import com.example.rummikubfrontscreen.setup.Tile;

import java.util.ArrayList;
import java.util.HashMap;

public class TilePositionScanner {


    public static ArrayList<ArrayList<Tile>> scanner (ArrayList<Tile> unstructuredTiles) {
        // first scanning the y positions
        HashMap<Integer, ArrayList<Tile>> map = yScanner(unstructuredTiles);
        System.out.println(map);
        // then seperating tiles into runs and rows
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

        // Checks for empty or null lists
        if (tiles == null || tiles.isEmpty()) return map;

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

            for(int j = 0; j < map.get(i).size()-1; j++){
                Tile currTile = map.get(i).get(j);
                Tile nextTile = map.get(i).get(j+1);
                double currX = currTile.getX();
                double nextX = nextTile.getX();

                // range of acceptable space between
                if(Math.abs(currX-nextX) <= 43) {
                    arr.get(arr.size() - 1).add(nextTile);
                }
                else{
                    arr.add(new ArrayList<>());
                    arr.get(arr.size()-1).add(nextTile);
                }
            }
        }
        return arr;
    }
}
