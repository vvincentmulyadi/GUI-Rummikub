package com.example.rummikubfrontscreen.setup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.example.rummikubfrontscreen.setup.MCTS.MCTS;

/**
 * The GameTracker class is used to track the results of a game, including the number of moves and the
 * winner, and provides methods to write and read the results to/from a file.
 */
public class GameTracker {

    private static int MCSTWon;
    private static int numOfMoves = 0;

    public static void writeToFile() throws IOException {
        BufferedWriter csvReader = new BufferedWriter(new FileWriter("RummikubFrontScreen/src/main/java/com/example/rummikubfrontscreen/setup/expiResults.txt", true));
        if (MCSTWon == -1) {
            csvReader.write("No winner!");
            csvReader.write("\n\n");
        } else if (MCSTWon == 1) {
            csvReader.write("1");
            csvReader.write("\n");
            csvReader.write("number of moves: " + numOfMoves);
            csvReader.write("\n\n");
        } else if(MCSTWon == 0) {
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
        int avMoves = 0;

        while ((line = be.readLine()) != null) {
            if(line.equals("No winner!")){
                numOfDraws += 1;
            }else if(line.equals("1")){
                timesMctsWon += 1;
                String nLine = be.readLine();
                String[] nl = nLine.split(" ");
                avMoves += Integer.parseInt(nl[nl.length-1]);
            }else if(line.equals("0")){
                timesRandomWon += 1;
            }
        }
        avMoves /= timesMctsWon;
        int totalSim = numOfDraws + timesMctsWon + timesRandomWon;
        String result = "number of draws: " + numOfDraws + ", times Linear Regression won: " + timesMctsWon + ", times Agent won: " + timesRandomWon +", total simualtions: "+ totalSim + ", average number of moves for LR: " + avMoves;
        return result;
    }

    public static void setWinner(int didMCTSWon){
        MCSTWon = didMCTSWon;
    }

    public static void setNumOfMoves(int theNumOfMoves){
        numOfMoves = theNumOfMoves;
    }

    public void addOneMove(){
        this.numOfMoves += 1;
    }

    public static void main(String args[]) throws IOException{
        
        String result = new String();
        result = readToFile();
        System.out.println(result);

    }

}
