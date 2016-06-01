package bot;

import java.util.ArrayList;
import java.util.List;

public class Microboard extends Board<Integer> {
   
   /*
    * int id that can be -1, 0, 1, or 2
    *    -1 represents that this board can be played
    *    0  "                        " belongs to neither player
    *    1  "                        " belongs to player 1
    *    2  "                        " belongs to player 2
    */
   private int id;

   public Microboard() {
      super(Integer.class);
   }
   
   public void setID(int myID) {
      this.id = myID;
   }
   
   public int getID() {
      return this.id;
   }
   
   /**
    * @Precondition: this Microboard has an id of -1; otherwise,
    *                   throws an IllegalStateException
    *                
    * Gets the available moves for a player with the given id on this board
    * A player with the given id should be able to play immediately
    * 
    * @param boardRow   the location of this board in the macroboard
    * @param boardCol
    * @param id   the id we want each move to have in our Queue
    * @return  List of locations in this board that are free
    *             Each Move in the list should have the same id as given
    */
   public List<Move> getAvailableMoves(int boardRow, int boardCol, int newId) {
      //this method does not care about any of the given parameters
      //they are only important for creating Move
      if (this.id != -1) {
         throw new IllegalStateException();
      }
      List<Move> moves = new ArrayList<Move>();
      for (int row = 0; row < Board.ROWS; row++) {
         for (int col = 0; col < Board.COLS; col++) {
            if (super.isEmptyLocation(row, col)) {
               moves.add(new Move(row, col, boardRow, boardCol, newId));
            }
         }
      }
      return moves;
   }
   
   /**
    * @Precondition: given location is a valid location; otherwise,
    *                   throws new IllegalStateException
    *                id of this Microboard is -1; otherwise,
    *                   throws new IllegalStateException
    *                given id is 0, 1, or 2; otherwise,
    *                   throws new IllegalArgumentException
    * @param row
    * @param col
    * @param newId
    */
   public void update(int row, int col, int newId) {
      super.checkLocation(row, col);
      if (this.id != -1) {
         throw new IllegalStateException();
      }
      super.checkID(newId);
      this.board[row][col] = newId;
   }

   @Override
   public int getID(int row, int col) {
      super.checkLocation(row, col);
      return (int)this.board[row][col];
   }
   
   /**
    * 
    * @return  true if there are no more available locations;
    *          false if there is at least one available location
    */
   public boolean isFull() {
      return this.getAvailableMoves(0, 0, 0).isEmpty();
   }
   
   /**
    * This method does not solve the fencepost problem, so keep that in mind
    *    when calling it.
    *    e.g. a possible row would look like:
    *       "0,0,0,"
    * 
    * @return a String representation of the ids in the row
    */
   String getRow(int row) {
      String result = "";
      for (int col = 0; col < Board.COLS; col++) {
         result += String.valueOf(this.board[row][col]);
      }
      return result;
   }
}