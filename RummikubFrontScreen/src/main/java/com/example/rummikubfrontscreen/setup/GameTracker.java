package com.example.rummikubfrontscreen.setup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GameTracker {

    private static boolean draw;
    private static boolean MCSTWon;
    private static int numOfMoves = 0;

    public static void writeToFile() throws IOException {
        BufferedWriter csvReader = new BufferedWriter(new FileWriter("RummikubFrontScreen/src/main/java/com/example/rummikubfrontscreen/setup/expiResults.txt"));
        if (draw) {
            csvReader.write("No winner!");
            csvReader.write("\n\n");
        } else if (MCSTWon) {
            csvReader.write("1");
            csvReader.write("\n");
            csvReader.write("number of moves: " + numOfMoves);
            csvReader.write("\n\n");
        } else {
            csvReader.write("0");
            csvReader.write("\n");
            csvReader.write("number of moves: " + numOfMoves);
            csvReader.write("\n\n");
        }
        csvReader.close();
    }

    public static String readToFile() throws IOException{
        BufferedReader be = new BufferedReader(new FileReader("RummikubFrontScreen/src/main/java/com/example/rummikubfrontscreen/setup/expiResults.txt"));
        String line = "";
        int numOfDraws = 0;
        int timesMctsWon = 0;
        int timesRandomWon = 0; 

        while ((line = be.readLine()) != null) {
            if(line.equals("No winner!")){
                numOfDraws += 1;
            }else if(line.equals("1")){
                timesMctsWon += 1;
            }else{
                timesRandomWon += 1;
            }
        }

        String result = "number of draws: " + numOfDraws + ", times MCTS won: " + timesMctsWon + ", times Random Agent won: " + timesRandomWon;
        return result;
    }

    public static void setDraw(boolean isDraw){
        draw = isDraw;
    }

    public static void setWinner(boolean didMCTSWon){
        MCSTWon = didMCTSWon;
    }

    public static void setNumOfMoves(int theNumOfMoves){
        numOfMoves = theNumOfMoves;
    }

    public void addOneMove(){
        this.numOfMoves += 1;
    }

    public static void main(String args[]) throws IOException{
        // setDraw(false);
        // setWinner(true);
        // setNumOfMoves(24);
        // writeToFile();
        

        String result = new String();
        result = readToFile();
        System.out.println(result);

    }

}
