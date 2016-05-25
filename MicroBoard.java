package bot;

import java.util.ArrayList;
import java.util.List;

public class MicroBoard {
   
   /*
    * 2D int array that keeps track of one game of Tic Tac Toe
    *    Tic Tac Toe is always played with a square of side length 3
    *    However, we save the side length of each square as 3 in the
    *    Macroboard as SQUARE_LENGTH
    */
   private int[][] board;
   /*
    * int id that can be -1, 0, 1, or 2
    *    -1 represents that this board can be played
    *    0  "                        " belongs to neither player
    *    1  "                        " belongs to player 1
    *    2  "                        " belongs to player 2
    */
   private int id;

   public MicroBoard() {
      this.board = new int[Macroboard.SQUARE_LENGTH][Macroboard.SQUARE_LENGTH];
   }
   
   public void setID(int myID) {
      this.id = myID;
   }
   
   public int getID() {
      return this.id;
   }
   
   /**
    * A location is defined as an avaialable move if the board
    * @param id
    * @return  List of locations in this board that are free
    */
   public List<Move> getAvailableMoves(int id) {
      List<Move> moves = new ArrayList<Move>();//TODO
      
      return moves;
   }
   
   /**
    * @Precondition: given location is a valid location; otherwise,
    *                   throws new IllegalStateException
    * @param row
    * @param col
    * @param myId
    */
   public void update(int row, int col, int newId) {
      this.board[row][col] = newId;//TODO
   }
   
   /**
    * 
    * @param row
    * @param col
    * @return  true if the given location is a valid location in this board;
    *             otherwise, false
    */
   private boolean isValidLocation(int row, int col) {
      return false;//TODO
   }
   
   /**
    * 
    * @return  true if there are no more available locations;
    *          false if there is at least one available location
    */
   public boolean isFull() {
      return false;//TODO
   }
}
