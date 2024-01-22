package com.example.rummikubfrontscreen.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.example.rummikubfrontscreen.setup.MCTS.MCTSAction;

public class PossibleMoves {
    
    /**
     * The function `addToLineNextTile` recursively builds valid lines of tiles based on the given line
     * type (group or run) and the remaining tiles, and adds them to the list of valid lines.
     * 
     * @param lineType The lineType parameter is an integer that indicates the type of line being
     * built. A value of 0 represents a group line, while a value of 1 represents a run line.
     * @param cLine The `cLine` parameter is an `ArrayList` that represents the currently built line.
     * It contains `Tile` objects that are part of the line.
     * @param remainingTiles An ArrayList of Tile objects representing the tiles that have not yet been
     * added to the line.
     * @param gLines gLines is an ArrayList of ArrayLists of Tiles. It represents valid single lines
     * that have been built so far. Each inner ArrayList represents a single line.
     */
    private static void addToLineNextTile(int lineType, ArrayList<Tile> cLine, ArrayList<Tile> remainingTiles,
            ArrayList<ArrayList<Tile>> gLines) {

        if (cLine.isEmpty()) {
            Player.sortByColor(remainingTiles);
            cLine.add(remainingTiles.get(0));
            ArrayList<Tile> nextremainingTiles = new ArrayList<>(remainingTiles);
            nextremainingTiles.remove(0);
            addToLineNextTile(lineType, cLine, nextremainingTiles, gLines);
            cLine.remove(cLine.size() - 1);
        } else if (lineType == 1) {
            // it can be joker o a number with same color +1
            ArrayList<Tile> nextremainingTiles = new ArrayList<>(remainingTiles);
            for (Tile remainingTile : remainingTiles) {         
                nextremainingTiles.remove(0);

                if ((remainingTile.getColour() == cLine.get(cLine.size() - 1).getColour() &&
                        remainingTile.getInt() == cLine.get(cLine.size() - 1).getInt() + 1) ||
                        remainingTile.getValue() == Value.JOKER ||
                        (remainingTile.getColour() == cLine.get(cLine.size() - 1).getColour()
                                && cLine.size() > 1 &&
                                cLine.get(cLine.size() - 1).getValue() == Value.JOKER &&
                                remainingTile.getInt() == cLine.get(cLine.size() - 2).getInt() + 2)) {
                    cLine.add(remainingTile);
                    if (cLine.size() >= 3) {
                        gLines.add(new ArrayList<>(cLine));
                    }

                    addToLineNextTile(1, cLine, nextremainingTiles, gLines);
                    cLine.remove(cLine.size() - 1);
                }
            }
        } else {
            // it can be a joker or the same number with a different colour
            ArrayList<Tile> nextremainingTiles = new ArrayList<>(remainingTiles);
            for (Tile remainingTile : remainingTiles) {    

                nextremainingTiles.remove(0);
                
                boolean c = true;
                for (Tile tile : cLine) {
                    if (tile.getColour() == remainingTile.getColour() &&
                            tile.getValue() == remainingTile.getValue()) {
                        c = false;
                        break;
                    }
                }
                if ((remainingTile.getColour() != cLine.get(cLine.size() - 1).getColour() &&
                        remainingTile.getValue() == cLine.get(cLine.size() - 1).getValue()) ||
                        remainingTile.getValue() == Value.JOKER || (cLine.size() > 2 &&
                                cLine.get(cLine.size() - 1).getValue() == Value.JOKER &&
                                remainingTile.getValue() == cLine.get(cLine.size() - 2).getValue())) {
                    if (c) {
                        cLine.add(remainingTile);
                        if (cLine.size() >= 3 && cLine.size() < 5) {
                            gLines.add(new ArrayList<>(cLine));
                        }
                        addToLineNextTile(0, cLine, nextremainingTiles, gLines);
                        cLine.remove(cLine.size() - 1);
                    }
                }
            }
        }
    }

    
    /**
     * The function "makeBoardState" generates all possible valid board states by recursively adding
     * tiles to the current board state and checking if it is valid.
     * 
     * @param cBoard The current board state, represented as an ArrayList of ArrayLists of Tile
     * objects.
     * @param gBoard The remainingSeq parameter is an ArrayList of ArrayLists of Tile objects. It
     * represents the remaining sequences of tiles that can be added to the current board state. Each
     * inner ArrayList represents a sequence of tiles.
     * @param b gBoard is an ArrayList of ArrayLists of ArrayLists of Tile objects. It represents
     * the game board state, where each ArrayList<Tile> represents a row on the board, and each
     * ArrayList<ArrayList<Tile>> represents the entire board.
     * @param hand The parameter "b" is of type Board.
     */
    public static void makeBoardState(ArrayList<ArrayList<Tile>> cBoard, ArrayList<ArrayList<ArrayList<Tile>>> gBoard, ArrayList<ArrayList<Tile>> gHand,ArrayList<Tile> boardandhand, ArrayList<Tile> hand,ArrayList<Tile> unusedTiles) {
        ArrayList<ArrayList<Tile>> gLines;
        if (boardandhand.isEmpty()) {
            gLines = new ArrayList<>();
        }else 
            gLines = getLines(boardandhand);
        //We cannot create any new lines. We check if we used all the board and at least one from hand - if so than it is a valid board
        //And if we have enough tiles left, we skip current first as unused and try to make rest of the board without it
        if (gLines.isEmpty()) {
            if (boardandhand.size()+unusedTiles.size()<hand.size()) {
                if (hand.containsAll(boardandhand)){
                    ArrayList<Tile> lhand = getHandFromBoard(cBoard, hand);
                    boolean newhand = true;
                    for (ArrayList<Tile> ihand : gHand){
                        if (ihand.equals(lhand)) {
                            newhand = false;
                            break;
                        }
                    }
                    if (newhand){ 
                        gBoard.add(new ArrayList<ArrayList<Tile>>(cBoard));
                        gHand.add(lhand);
                    }
                }
            
            }
            if (boardandhand.size()>=4&&hand.contains(boardandhand.get(0))){
                ArrayList<Tile> nextboardandhand = new ArrayList<>(boardandhand);
                nextboardandhand.remove(0);
                unusedTiles.add(boardandhand.get(0));
                makeBoardState(cBoard, gBoard, gHand,nextboardandhand, hand, unusedTiles);
                unusedTiles.remove(unusedTiles.size()-1);


            }
        
        }
        //We created new lines from available tiles containint first tile on the list.
        //Now we try to create 
        for (ArrayList<Tile> seq : gLines) {
            ArrayList<Tile> nextboardandhand = new ArrayList<>(boardandhand);
            nextboardandhand.removeAll(seq);
            
                cBoard.add(seq);
                makeBoardState(cBoard,gBoard,gHand,nextboardandhand,hand,unusedTiles);
                cBoard.remove(cBoard.size() - 1);
           
        }
        
    }

    /**
     * The function checks if a given game board state is valid by comparing the tiles on the board
     * with the tiles in the current game board.
     * 
     * @param gBoard An ArrayList of ArrayLists of Tile objects representing the current game board
     * state.
     * @param board The "board" parameter is an object of type "Board".
     * @return The method is returning a boolean value.
     */
    public static boolean makeValidBoardState(ArrayList<ArrayList<Tile>> gBoard, Board board) {
        ArrayList<ArrayList<Tile>> bList = new ArrayList<>(board.getCurrentGameBoard());
        ArrayList<Tile> boardList = new ArrayList<>();
        for (ArrayList<Tile> sequence : bList) {
            boardList.addAll(sequence);
        }
        ArrayList<Tile> tilesOnBoard = new ArrayList<>();
        for (ArrayList<Tile> seq : gBoard) {
            tilesOnBoard.addAll(seq);
        }
        if (tilesOnBoard.containsAll(boardList) && tilesOnBoard.size() >= boardList.size()) {
            return true;
        }
        return false;
    }

    
    /**
     * The function `possibleMoves` generates all possible moves in a game given the current board
     * state and the player's hand.
     * 
     * @param b The parameter `b` is of type `Board` and represents the current game board.
     * @param hand An ArrayList of Tile objects representing the tiles in the player's hand.
     * @param makeBoardType The parameter `makeBoardType` is an integer that determines the number of
     * board states to be created.
     * @return The method `possibleMoves` returns an `ArrayList` of `Object[]`.
     */
    public static ArrayList<Object[]> possibleMoves(Board b, ArrayList<Tile> hand, int makeBoardType) {
        ArrayList<Object[]> states = new ArrayList<>();

        Board drawBoard = b.clone();
        ArrayList<Tile> drawPile = drawBoard.getDrawPile();
        if (drawPile != null && !drawPile.isEmpty()) {
            ArrayList<Tile> drawHand = new ArrayList<>(hand);
            Object[] drawMove = MCTSAction.drawTileFromBoard(drawBoard, drawHand);
            if (drawMove != null) {
                states.add(drawMove);
            }
        }
        
        ArrayList<ArrayList<Tile>> bList = b.getCurrentGameBoard();
        //boa - all tiles on the board
        ArrayList<Tile> boa = new ArrayList<>();
        for (ArrayList<Tile> sequence : bList) {
            boa.addAll(sequence);
        }
    
        ArrayList<Tile> boardandhand = new ArrayList<>(boa);
        boardandhand.addAll(hand);
        System.out.println(hand.size() + boa.size() + " are all the tiles we try to combine to moves");


        //gBoard - all possible board states from the current board state and hand
        ArrayList<ArrayList<ArrayList<Tile>>> gBoard = new ArrayList<>();
        ArrayList<ArrayList<Tile>> gHand = new ArrayList<>();
        Player.sortByColor(boardandhand);
        makeBoardState(new ArrayList<>(), gBoard,gHand, boardandhand, hand, new ArrayList<Tile>());

        for (int i = 0; i < gBoard.size(); i++) {
            ArrayList<Tile> bo = new ArrayList<>();
            ArrayList<Tile> hl = gHand.get(i);
            for (ArrayList<Tile> seq : gBoard.get(i)) {
                bo.addAll(seq);
            }
            if (!(bo.size() == boa.size())) {
                Object[] arr = new Object[2];
                arr[0] = new Board((ArrayList<ArrayList<Tile>>) gBoard.get(i).stream().map(ArrayList::new)
                        .collect(Collectors.toList()), b.getDrawPile());
                arr[1] = new ArrayList<>(hl);
                states.add(arr);
            }
        }
        return states;
    }

    /**
     * The function "getLines" takes a board and a hand of tiles as input, combines them into a single
     * list, and then creates and returns a list of all possible lines that can be formed using the
     * tiles from the board and the hand.
     * 
     * @param b The parameter `b` is of type `Board`. It represents the game board on which the lines
     * are being calculated.
     * @param hand An ArrayList of Tile objects representing the tiles in the player's hand.
     * @return The method is returning an ArrayList of ArrayLists of Tile objects.
     */
    public static ArrayList<ArrayList<Tile>> getLines(ArrayList<Tile> combined){
        

        ArrayList<ArrayList<Tile>> gLinesG = new ArrayList<>();
        ArrayList<ArrayList<Tile>> gLinesR = new ArrayList<>();
        ArrayList<ArrayList<Tile>> gLines = new ArrayList<>();

        addToLineNextTile(1, new ArrayList<>(), combined, gLinesR);
        addToLineNextTile(0, new ArrayList<>(), combined, gLinesG);
        gLines.addAll(gLinesG);
        gLines.addAll(gLinesR);

        return gLines;
    }

    /**
     * The function takes a board and a hand of tiles as input, and returns a new hand that excludes
     * any tiles that are already on the board.
     * 
     * @param board An ArrayList of ArrayLists of Tile objects representing the current state of the
     * game board. Each inner ArrayList represents a sequence of tiles on the board.
     * @param hand An ArrayList of Tile objects representing the tiles in the player's hand.
     * @return The method is returning an ArrayList of Tile objects.
     */
    public static ArrayList<Tile> getHandFromBoard(ArrayList<ArrayList<Tile>> board, ArrayList<Tile> hand) {
        ArrayList<Tile> h = new ArrayList<>(hand);
        ArrayList<Tile> b = new ArrayList<>();
        for (ArrayList<Tile> seq : board) {
            b.addAll(seq);
        }
        for (Tile tile : b) {
            h.remove(tile);
        }
        return h;
    }


    public static String toString(ArrayList<Object[]> states) {
        String s = "";
        for (int i = 0; i < states.size(); i++) {
            s += "board:\n";
            s += states.get(i)[0].toString();
            s += "hand:\n";
            s += states.get(i)[1].toString();
            s += "\n";
            s += "\n";
        }
        return s;
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        ArrayList<ArrayList<Tile>> board = new ArrayList<>();
        ArrayList<Tile> seq = new ArrayList<>();

        // seq.add(new Tile(Colour.YELLOW, Value.TWO));
        // seq.add(new Tile(Colour.YELLOW, Value.ONE));
        // seq.add(new Tile(Colour.YELLOW, Value.THREE));
        // board.add(seq);
        ArrayList<Tile> seq2 = new ArrayList<>();
        // seq2.add(new Tile(Colour.BLUE, Value.TWO));
        // seq2.add(new Tile(Colour.BLACK, Value.TWO));
        // seq2.add(new Tile(Colour.RED, Value.TWO));
        // board.add(seq2);
        // seq2 = new ArrayList<>();
        // seq2.add(new Tile(Colour.YELLOW, Value.ONE));
        // seq2.add(new Tile(Colour.YELLOW, Value.TWO));
        // seq2.add(new Tile(Colour.YELLOW, Value.THREE));
        // seq2.add(new Tile(Colour.YELLOW, Value.FOUR));
        // seq2.add(new Tile(Colour.YELLOW, Value.FIVE));
        // seq2.add(new Tile(Colour.YELLOW, Value.SIX));
        // board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.RED, Value.ONE));
        seq2.add(new Tile(Colour.BLUE, Value.ONE));
        seq2.add(new Tile(Colour.BLACK, Value.ONE));
        board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.RED, Value.ONE));
        seq2.add(new Tile(Colour.BLUE, Value.ONE));
        seq2.add(new Tile(Colour.YELLOW, Value.ONE));
        board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.RED, Value.NINE));
        seq2.add(new Tile(Colour.BLUE, Value.NINE));
        seq2.add(new Tile(Colour.BLACK, Value.NINE));
        board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.RED, Value.NINE));
        seq2.add(new Tile(Colour.BLACK, Value.NINE));
        seq2.add(new Tile(Colour.YELLOW, Value.NINE));
        board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.RED, Value.TEN));
        seq2.add(new Tile(Colour.BLACK, Value.TEN));
        seq2.add(new Tile(Colour.YELLOW, Value.TEN));
        board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.RED, Value.TWELVE));
        seq2.add(new Tile(Colour.BLUE, Value.TWELVE));
        seq2.add(new Tile(Colour.YELLOW, Value.TWELVE));
        board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.BLUE, Value.SIX));
        seq2.add(new Tile(Colour.BLACK, Value.SIX));
        seq2.add(new Tile(Colour.YELLOW, Value.SIX));
        board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.RED, Value.TWO));
        seq2.add(new Tile(Colour.RED, Value.THREE));
        seq2.add(new Tile(Colour.RED, Value.FOUR));
        board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.BLACK, Value.ELEVEN));
        seq2.add(new Tile(Colour.BLACK, Value.TWELVE));
        seq2.add(new Tile(Colour.BLACK, Value.THIRTEEN));
        board.add(seq2);
        seq2 = new ArrayList<>();
        seq2.add(new Tile(Colour.YELLOW, Value.SIX));
        seq2.add(new Tile(Colour.YELLOW, Value.SEVEN));
        seq2.add(new Tile(Colour.YELLOW, Value.EIGHT));
        board.add(seq2);

        Board b = new Board(board, new ArrayList<>());

        ArrayList<Tile> hand = new ArrayList<>();

        // hand.add(new Tile(Colour.BLACK, Value.FOUR));
        // hand.add(new Tile(Colour.RED, Value.ONE));
        // hand.add(new Tile(Colour.YELLOW, Value.SEVEN));
        // hand.add(new Tile(Colour.BLUE, Value.TWELVE));
        // hand.add(new Tile(Colour.BLUE, Value.JOKER));
        // hand.add(new Tile(Colour.RED, Value.THREE));
        // hand.add(new Tile(Colour.RED, Value.FOUR));
        // hand.add(new Tile(Colour.BLUE, Value.THREE));

        hand.add(new Tile(Colour.RED, Value.FIVE));
        hand.add(new Tile(Colour.BLUE, Value.THREE));
        hand.add(new Tile(Colour.YELLOW, Value.JOKER));
        hand.add(new Tile(Colour.BLUE, Value.FOUR));
        hand.add(new Tile(Colour.BLUE, Value.EIGHT));
        hand.add(new Tile(Colour.BLUE, Value.TEN));
        hand.add(new Tile(Colour.BLUE, Value.THIRTEEN));
        hand.add(new Tile(Colour.BLACK, Value.FIVE));
        hand.add(new Tile(Colour.YELLOW, Value.TWO));
        hand.add(new Tile(Colour.YELLOW, Value.TWO));
        hand.add(new Tile(Colour.YELLOW, Value.SEVEN));
        hand.add(new Tile(Colour.YELLOW, Value.ELEVEN));
        hand.add(new Tile(Colour.YELLOW, Value.FOUR));
        hand.add(new Tile(Colour.BLUE, Value.SIX));
        hand.add(new Tile(Colour.BLUE, Value.NINE));

        System.out.println(toString(possibleMoves(b, hand, 0)));

        long estimatedTime = System.nanoTime() - startTime;
        System.out.println(estimatedTime);
        
    }
}

