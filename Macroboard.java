package bot;

import java.util.List;
import java.util.Stack;

/**
 * Macroboard is a type of Board. It is a board of boards.
 *    The objects that it contains in its board are Microboard objects.
 * 
 * Since we refer to locations as the Objects in our board, and this object
 *    keeps track of Microboards, we will refer to the locations
 *    within the Microboards as microlocations.
 * 
 * @author RyanPachauri
 * @version 5/26/16
 */
public class Macroboard extends Board<Microboard> {

   public Macroboard() {
      super(Microboard.class);
   }
   
   /**
    * 
    * @param id
    * @return  the available moves in the board for a player with the given id
    */
   public Stack<Move> getAvailableMoves(int id) {
      Stack<Move> moves = new Stack<Move>();
      for (int row = 0; row < Board.ROWS; row++) {
         for (int col = 0; col < Board.COLS; col++) {
            Microboard microboard = ((Microboard)this.board[row][col]);
            if (microboard.getID() == -1) {//available board
               List<Move> boardMoves =
                     microboard.getAvailableMoves(row, col, id);
               moves.addAll(boardMoves);
            }
         }
      }
      return moves;
   }
   
   /**
    * @Precondition: newField contains ids of exact number locations required;
    *                   otherwise, throws new IllegalArgumentException
    *                
    * Updates the Macroboard with the given inputs
    * 
    * @param newField   A String that details the id of every single loc within
    *                      the field
    *                   Goes row by row of the entire field
    *        e.g. 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    *             0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    *             0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
    */
   public void updateField(String newField) {
      int numRowLocs = Board.ROWS * Board.ROWS;//number of rows of locations
      int numColLocs = Board.COLS * Board.COLS;//number of cols of locations
      String[] locationIDs = newField.split(",");
      if (locationIDs.length != numRowLocs * numColLocs) {
         throw new IllegalArgumentException();
      }
      for (int i = 0; i < locationIDs.length; i++) {
         int microRow = i / numRowLocs;
         int microCol = i % numColLocs;
         this.update(microRow, microCol, Integer.parseInt(locationIDs[i]));
      }
   }
   
   /**
    * @Precondition: newMacroboard contains ids of exact number of boards;
    *                   otherwise, throws new IllegalArgumentException
    * 
    * @param newMacroboard
    *                   A String that details the id of every board in field
    *        e.g. -1,-1,-1,
    *             -1,-1,-1,
    *             -1,-1,-1
    */
   public void updateBoards(String newMacroboard) {
      String[] boardIDs = newMacroboard.split(",");
      if (boardIDs.length != Board.ROWS * Board.COLS) {
         throw new IllegalArgumentException();
      }
      for (int i = 0; i < boardIDs.length; i++) {
         int boardRow = i / Board.ROWS;
         int boardCol = i % Board.COLS;
         int id = Integer.parseInt(boardIDs[i]);
         ((Microboard)this.board[boardRow][boardCol]).setID(id);
      }
   }
   
   /**
    * NOTE: This method does not update the state of the game.
    * 
    * @Precondition: 1. the given location is a valid location in the field;
    *                      otherwise, throws an IllegalStateException
    *                2. id is a 0, 1, or 2;
    *                      otherwise, throws an IllegalArgumentException
    *                
    * Updates the given location of the Macroboard with the given id
    * 
    * @param microRow  microlocation in the entire macroboard
    * @param microCol  microlocation in the entire macroboard
    * @param id   id belong to a player (can be 0, 1, or 2)
    */
   public void update(int microRow, int microCol, int id) {
      if (!this.isValidMicroLocation(microRow, microCol)) {
         throw new IllegalArgumentException();
      }
      super.checkID(id);
      //location of the board within the field
      int macroRow = microRow / Board.ROWS;
      int macroCol = microCol / Board.COLS;
      //location within the board
      int boardRow = microRow % Board.ROWS;
      int boardCol = microCol % Board.COLS;
      ((Microboard)this.board[macroRow][macroCol]).
         update(boardRow, boardCol, id);
   }
   
   /**
    * Changes the ids of all boards with the currentID to newID
    *    Only changes boards that are not full.
    * 
    * @param currentID
    * @param newID
    */
   private void changeIDs(int currentID, int newID) {
      Microboard nextBoard;
      for (int row = 0; row < Board.ROWS; row++) {
         for (int col = 0; col < Board.COLS; col++) {
            nextBoard = (Microboard)this.board[row][col];
            if (nextBoard.getID() == currentID && !nextBoard.isFull()) {
               nextBoard.setID(newID);
            }
         }
      }
   }
   
   /**
    * @Precondition:    the microboard we are trying to make a move on has an
    *                   id of -1; otherwise, throws an IllegalStateExcpetion
    * @Postcondition:   changes the id of the given location to the given id
    *                   changes all ids of affected boards in macroboard
    * @param move a Move that we are attempting to make on this macroboard
    * @return  String the ids of the boards before they were changed
    *          We do this because Ultimate Tic Tac Toe is a game where a move
    *             changes the state of the board (the ids of the microboards)
    *          There is no way to return the macroboard to its prior state
    *             without knowing what the prior state is.
    */
   public String makeMove(Move move) {
      String boards = this.getBoards();
      Microboard microboard =
            (Microboard)this.board[move.boardRow][move.boardCol];
      if (microboard.getID() != -1) {
         throw new IllegalStateException();
      }
      microboard.update(move.row, move.col, move.id);
      //update id of current board
      if (microboard.isWinningLocation(move.row, move.col, move.id)) {
         microboard.setID(move.id);
      } else {
         microboard.setID(0);
      }
      //update id of rest of macroboard
      Microboard nextMicroboard = (Microboard)this.board[move.row][move.col];
      if (nextMicroboard.getID() == 0 && !nextMicroboard.isFull()) {
         this.changeIDs(-1, 0);
         nextMicroboard.setID(-1);
      } else {
         this.changeIDs(0, -1);
      }
      return boards;
   }

   /**
    * Unmakes a move: to be used in conjunction with makeMove(move)
    *    The String that makeMove(move) returns should be the String passed in
    *    here. That is the state of the macroboard before we changed it.
    * @param move
    * @param boards
    */
   public void unmakeMove(Move move, String boards) {
      Microboard microboard =
            ((Microboard)this.board[move.boardRow][move.boardCol]);
      microboard.update(move.row, move.col, 0);
      this.updateBoards(boards);
   }
   
   @Override
   public int getID(int row, int col) {
      super.checkLocation(row, col);
      return ((Microboard)this.board[row][col]).getID();
   }
   
   /**
    * For more information on what this method returns, please refer to the
    *    description of the parameter newField in the method:
    *    update(String newField, String newMacroboard)
    * 
    * @return a representation of the locations in String form
    */
   public String getLocations() {
      String result = "";
      for (int row = 0; row < Board.ROWS; row++) {
         for (int microRow = 0; microRow < Board.ROWS; microRow++) {
            for (int col = 0; col < Board.COLS; col++) {
               Microboard microboard =
                     (Microboard)this.board[row][col];
               result += microboard.getRow(microRow) + "\t";
            }
            result += "\n";
         }
         result += "\n";
      }
      return result.substring(0, result.length() - 1);//takes care of fencepost
   }
   
   /**
    * For more information on what this method returns, please refer to the
    *    description of the parameter newMacroboard in the method:
    *    update(String newField, String newMacroboard)
    * 
    * @return a representation of the boards in String form
    */
   public String getBoards() {
      String result = "";
      for (int row = 0; row < Board.ROWS; row++) {
         for (int col = 0; col < Board.COLS; col++) {
            result += "," + String.valueOf(this.getID(row, col));
         }
      }
      return result.substring(1);//takes care of fencepost
   }
   
   /**
    * 
    * @param microRow  microlocation in field
    * @param microCol  microlocation in field
    * @return  true if the given location exists within the macroboard field;
    *          otherwise, false
    */
   private boolean isValidMicroLocation(int microRow, int microCol) {
      return microRow >= 0 && microRow < Board.ROWS * Board.ROWS &&
             microCol >= 0 && microCol < Board.COLS * Board.COLS;
   }
}