package com.example.rummikubfrontscreen.setup;

import java.util.ArrayList;

public class Testi {
    public static void main(String[] args) {
        Tile ti = new Tile(Colour.BLACK, Value.EIGHT);
        System.out.println(ti.getColour());
        System.out.println(ti.getValue());

        Tile t1 = new Tile(Colour.BLUE, Value.SEVEN);
        Tile t2 = new Tile(Colour.RED, Value.SEVEN);
        Tile t3 = new Tile(Colour.BLUE, Value.SEVEN);
        Tile t4 = new Tile(Colour.BLUE, Value.FOUR);
        Tile t5 = new Tile(Colour.BLUE, Value.FIVE);
        Tile t6 = new Tile(Colour.BLUE, Value.SIX);
        ArrayList<ArrayList<Tile>> toConvert = new ArrayList<>();

        for(int i = 0; i<2;i++){
            toConvert.add(new ArrayList<Tile>());
            toConvert.get(i).add(t1);
            toConvert.get(i).add(t2);
            toConvert.get(i).add(t3);
            toConvert.get(i).add(null);
            toConvert.get(i).add(t4);
            toConvert.get(i).add(t5);
            toConvert.get(i).add(t6);
            toConvert.get(i).add(t3);
        }

        Board game = new Board(toConvert);
        game.convert(toConvert);
        System.out.println(game.getList());

    }
    
    
}
