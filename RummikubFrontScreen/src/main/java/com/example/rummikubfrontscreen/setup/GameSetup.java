package com.example.rummikubfrontscreen.setup;

import com.example.rummikubfrontscreen.GameBoardController;
import com.example.rummikubfrontscreen.Main;

import java.util.ArrayList;
import java.util.Random;

// The `GameSetup` class is responsible for setting up the game of Rummikub. It initializes the tiles,
// board, players, and other necessary components for the game.
public class GameSetup {

    // Tiles not in play (bag)
    private ArrayList<Tile> tiles;

    // All the existing tiles
    private ArrayList<Tile> allTiles = new ArrayList<>();

    private ArrayList<Tile> tilesInPlay = new ArrayList<>();

    private ArrayList<Player> players = new ArrayList<>();

    private RandomAgent agent;
    private int numOfPlayers = 4;

    Board board;

    public void setTilesInPlay(ArrayList<Tile> tilesInPlay) {
        this.tilesInPlay = tilesInPlay;
    }

    public ArrayList<Tile> getTilesInPlay() {
        return tilesInPlay;
    }

    public RandomAgent getAgent() {
        return agent;
    }

    public GameSetup(double d) {
        generateTiles();
    }

    public GameSetup() {
        generateTiles();
        for (Tile tile : tiles) {
            System.out.println(tile);
        }
        generateBoard();
        for (int i = 0; i < numOfPlayers; i++) {
            Player player = new Player(generateHand());
            player.setId(i);
            if (i == numOfPlayers - 1 && GameBoardController.getPlayAgainstAI()) {
                System.out.println("Agent init");
                agent = new RandomAgent(player);
            }
            players.add(player);

        }
        for (Player player : players) {
            player.sortByColor(player.hand);
        }
    }

    public GameSetup(int numOfPlayers) {
        generateTiles();
        generateBoard2();
        for (int i = 0; i < numOfPlayers; i++) {
            Player player = new Player(generateHand());
            player.setId(i);
            if (i == numOfPlayers - 1 && GameBoardController.getPlayAgainstAI()) {
                System.out.println("Agent init");
                agent = new RandomAgent(player);
            }
            players.add(player);

        }
        for (Player player : players) {
            player.sortByColor(player.hand);
        }
        System.out.println(board.toString());
    }

    private void generateBoard() {
        ArrayList<ArrayList<Tile>> newGameBoard = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            // newGameBoard.add(new ArrayList());
            // newGameBoard.get(i).add(null);
        }
        board = new Board(newGameBoard, tiles);
    }

    private void generateBoard2() {
        board = new Board(new ArrayList<>(), tiles);
    }

    public ArrayList<Tile> generateHand() {
        Random rand = new Random();
        int size = tiles.size();
        ArrayList<Tile> hand = new ArrayList<>();

        for (int i = 0; i < 14; i++) {
            int index = rand.nextInt(size - i);
            hand.add(tiles.get(index));
            tiles.remove(index);
        }
        return hand;
    }

    private void generateTiles() {
        tiles = new ArrayList<>();

        for (Value v : Value.values()) {
            for (Colour c : Colour.values()) {
                tiles.add(new Tile(c, v));
            }
        }

        for (int i = tiles.size() - 4; i < tiles.size(); i++) {
            tiles.remove(i);
        }

        for (Tile tile : tiles) {
            allTiles.add(tile);
        }
        for (Value v : Value.values()) {
        for (Colour c : Colour.values()) {
        tiles.add(new Tile(c, v));
        }
        }

        // Delete 6 Jokers
        for (int i = 0; i < 4; i++) {
        tiles.remove(tiles.size() - 1);
        }
        System.out.println("Tiles size: " + tiles.size());
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public ArrayList<Tile> getAllTiles() {
        return allTiles;
    }

    /**
     * 🥰
     * * @return
     */
    public ArrayList<Player> getPlayers() {

        return players;
    }

    public static void main(String[] locoMotiv) {
        GameSetup gs = new GameSetup();

        System.out.println(gs.getAllTiles());
    }
}
