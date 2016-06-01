package bot;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

/**
 * This board can ONLY be used in games that implement connection.
 * 
 * For example,
 *    Tic Tac Toe is a game that requires this type of board.
 *    In TTT, players can pick one location that belongs to them.
 *    The goal of TTT is to connect 3 in a row.
 *    
 *    Connect Four is another game that would require this type of board.
 *    Instead of connecting 3, we would have to connect four.
 * 
 * This type of board does not support a Chess board because the point of
 *    Chess is not to connect our pieces.
 *    
 * Each element in this board belongs to either 0 or 1 players.
 *    There will only be 2 players in the game.
 *    One will have an id of 1. The other will have an id of 2.
 *    
 * @author Ryan Pachauri
 * @version 5/26/16
 */
public abstract class Board<T> {
   
   //2D array that keeps track of one game that requires a board
   protected T[][] board;
   /*
    * Size of the board is dictated by the number of rows and columns in the
    *    board.
    *    We may not always be dealing with a board that is square
    */
   public static final int ROWS = 3;
   public static final int COLS = 3;
   public static final int CONNECT = 3;//how many we need to connect to win
   
   @SuppressWarnings("unchecked")
   public Board(Class<?> classObject) {
      this.board = (T[][]) Array.newInstance(classObject, Board.ROWS, Board.COLS);
      for (int row = 0; row < Board.ROWS; row++) {
         for (int col = 0; col < Board.COLS; col++) {
            try {
               if (classObject.equals(Integer.class)) {
                  this.board[row][col] = (T) new Integer(0);
               } else {
                  this.board[row][col] = (T) classObject.newInstance();
               }
            } catch (InstantiationException | IllegalAccessException e) {
               e.printStackTrace();
            }
         }
      }
   }
   
   /**
    * @Precondition: given location is a valid location; otherwise,
    *                   throws new IllegalStateException
    * @param row  location in this board
    * @param col  location in this board
    * @return  the Object at the given location
    */
   public T get(int row, int col) {
      this.checkLocation(row, col);
      return this.board[row][col];
   }
   
   /**
    * @Precondition: given location is a valid location; otherwise,
    *                   throws new IllegalStateException
    * @param row
    * @param col
    * @param myId the id of a player
    */
   public abstract void update(int row, int col, int id);
   
   /**
    * 
    * @Precondition: given location is a valid location; otherwise,
    *                   throws new IllegalStateException
    * @param row
    * @param col
    * @return the id of the Object at the given location
    */
   public abstract int getID(int row, int col);

   /**
    * This method is used whenever a method that uses a location as a parameter
    *    is called.
    * Throws an IllegalArgumentException if the given location is not in the
    *    board.
    *
    * @param row
    * @param col
    */
   protected void checkLocation(int row, int col) {
      if (!this.isValidLocation(row, col)) {
         throw new IllegalStateException();
      }
   }
   
   /**
    * This method is used whenever a method that uses an id as a parameter
    *    is called.
    * Throws an IllegalStateException if the given id is not 0, 1, or 2
    * 
    * @param id
    */
   protected void checkID(int id) {
      if (id != 0 && id != 1 && id != 2) {
         throw new IllegalArgumentException();
      }
   }
   
   /**
    * @param row  location in board
    * @param col  location in board
    * @return  true if the given location exists within the instance variable
    *             board; otherwise, false
    */
   public boolean isValidLocation(int row, int col) {
      return row >= 0 && row < board.length &&
             col >= 0 && col < board[0].length;
   }
   
   /**
    * @Precondition: given location is a valid location; otherwise,
    *                   throws new IllegalStateException
    * @param row
    * @param col
    * @return  true if the given location belongs to neither player; otherwise,
    *          false
    */
   public boolean isEmptyLocation(int row, int col) {
      this.checkLocation(row, col);
      return this.getID(row, col) == 0;
   }
   
   /**
    * @Precondition: given location is a valid location; otherwise,
    *                   throws new IllegalStateException
    *                given id is 1 or 2; otherwise,
    *                   throws new IllegalArgumentException
    * To be in a winning line, there must be at least one line that has
    *    Board.CONNECT locations belonging to the player with the given
    *    id
    *           
    * @param row
    * @param col
    * @return  true if the given location is part of a winning line for the
    *             player with the given id
    *               
    */
   public boolean isWinningLocation(int row, int col, int id) {
      this.checkLocation(row, col);
      if (id != 1 && id != 2) {
         throw new IllegalArgumentException();
      }
      Set<Integer[][]> lines = this.getPossibleWins(row, col, id);
      for (Integer[][] line : lines) {
         if (this.getNumInLine(line, id) == Board.CONNECT) {
            return true;
         }
      }
      return false;
   }
   
   /**
    * @Precondition: line.length is equal to Board.CONNECT
    * @param line for more information, please refer to the description
    *          of what is returned in
    *          getPossibleWins(int row, int col, int id)
    * 
    * @param id
    * @return  the number of locations in this line that belong to the
    *             player with the given id
    */
   public int getNumInLine(Integer[][] line, int id) {
      int sum = 0;
      for (int i = 0; i < Board.CONNECT; i++) {
         int col = line[i][0];
         int row = line[i][1];
         if (this.getID(row, col) == id) {
            sum++;
         }
      }
      return sum;
   }
   
   /**
    * @Precondition: There are only 2 players and the ids can be either 1 or 2;
    *                   otherwise, throws an IllegalArgumentException
    * A win is defined as a line of Board.CONNECT locations from the same
    *    player
    *    This line can be:
    *       1. horizontal
    *       2. vertical
    *       3. left diagonal
    *       4. right diagonal
    * 
    * @param row  the row of the disc we are looking at
    * @param col  the column of the disc we are looking at
    * @param id   the player we are looking for wins for
    * @return A Set of 2D arrays that contain the locations.
    * 
    * The 2D Integer array is explained here:
    *    The rows separate the locations.
    *    The first column is the column of the locations
    *    The second column is the row of the locations
    *       e.g.  01
    *             02
    *             03
    *             04
    */
   public Set<Integer[][]> getPossibleWins(int row, int col, int id) {
      if (id != 1 && id != 2) {
         throw new IllegalArgumentException();
      }
      int[][] lines = this.findWinningLines(row, col, id);
      Set<Integer[][]> wins = new HashSet<Integer[][]>();
      int lineLength = Board.CONNECT - 2;
      //compares the columns of the horizontal line
      int horizontalLineLength = lines[1][1] - lines[0][1];
      if (horizontalLineLength > lineLength) {
         this.addPossibleWins(wins, lines[0][0], lines[0][1], 0, 1,
               horizontalLineLength - lineLength);
      }
      //compares the rows of the vertical line
      int verticalLineLengeth = lines[2][0] - lines[3][0];
      if (verticalLineLengeth > lineLength) {
         this.addPossibleWins(wins, lines[3][0], lines[3][1], 1, 0,
               verticalLineLengeth - lineLength);
      }
      //compares the columns of the left diagonal line
      int leftDiagonalLine = lines[5][1] - lines[4][1];
      if (leftDiagonalLine > lineLength) {
         this.addPossibleWins(wins, lines[4][0], lines[4][1], 1, 1,
               leftDiagonalLine - lineLength);
      }
      //compares the columns of the right diagonal line
      int rightDiagonalLine = lines[7][1] - lines[6][1];
      if (rightDiagonalLine > lineLength) {
         this.addPossibleWins(wins, lines[6][0], lines[6][1], -1, 1,
               rightDiagonalLine - lineLength);
      }
      return wins;
   }
   
   /**
    * Adds all possible wins in this line to wins
    * @Precondition: wins is already declared
    * 
    * @param wins A Set of 2D Integer arrays containing possible wins
    * @param row  the row of the location we start the line at
    * @param col  the column of the location we start the line at
    * @param rowDiff the direction the row moves in
    * @param colDiff the direction the column moves in
    * @param increment  the number of possible wins there are in this line
    */
   private void addPossibleWins(Set<Integer[][]> wins, int row, int col,
         int rowDiff, int colDiff, int increment) {
      for (int i = 0; i < increment; i++) {
         Integer[][] four = new Integer[4][2];//four in a row
         for (int j = 0; j < 4; j++) {
            four[j][0] = col + colDiff * (i + j);
            four[j][1] = row + rowDiff * (i + j);
         }
         wins.add(four);
      }
   }
   
   /**
    * @Precondition: The location given is a valid location in this field
    * 
    * This denotes the 8 points surrounding the given location so that
    *    we can figure out the lines that make it
    * The set of 8 points are divided into 4 sets of lines
    * The order of the points are as follows:
    *    0. left side of horizontal
    *    1. right side of horizontal
    *    2. top of vertical
    *    3. bottom of vertical
    *    4. top left of left diagonal
    *    5. bottom right of left diagonl
    *    6. bottom left of right diagonal
    *    7. top right of right diagonal
    *    
    * @param row  the row of the location we are considering
    * @param col  the column of the location we are considering
    * @return  int[][] of 
    */
   private int[][] findWinningLines(int row, int col, int id) {
      int[][] lines = new int[8][2];
      lines[0] = this.findMaxDistance(0, -1, row, col, id);
      lines[1] = this.findMaxDistance(0, 1, row, col, id);
      lines[2] = this.findMaxDistance(1, 0, row, col, id);
      lines[3] = this.findMaxDistance(-1, 0, row, col, id);
      lines[4] = this.findMaxDistance(-1, -1, row, col, id);
      lines[5] = this.findMaxDistance(1, 1, row, col, id);
      lines[6] = this.findMaxDistance(1, -1, row, col, id);
      lines[7] = this.findMaxDistance(-1, 1, row, col, id);
      return lines;
   }
   
   /**
    * Finds the maximum distance away from a given location that
    *    could make a line of connected discs.
    * We keep incrementing while either:
    *    1. the location contains a disc of the same id as the initial location
    *    2. the location contains no disc
    * We stop incrementing and do not include the location of:
    *    1. a location that is not in the bounds of the field
    *    2. a location containing a disc of a different id as the starting
    *          location
    * 
    * @param rowDiff    how much to increment the row by
    * @param colDiff    "                       " column by
    * @param row        the starting row
    * @param col        the starting column
    * @return  an int[] of size two where:
    *             the first element represents a row
    *             the second element represents a column
    */
   private int[] findMaxDistance(int rowDiff, int colDiff, int row, int col,
         int id) {
      int counter = 0;
      int[] location = {row,col};
      while (counter < Board.CONNECT && this.matchingLocation(
            row + rowDiff * counter, col + colDiff * counter, id)) {
         location[0] = row + rowDiff * counter;
         location[1] = col + colDiff * counter;
         counter++;
      }
      return location;
   }
   
   /**
    * Given an id, return true if this location either contains a disc of the
    *    same id or no disc at all
    *    
    * @param row     row of the location
    * @param col     column of the location
    * @param id   1 or 2, which is a disc that belongs to a player
    * @return  true if the disc at this location matches id or
    *               the location belongs to no one; otherwise,
    *          false
    *               this would indicate that the location is out of bounds or
    *               belongs to a different player
    *          
    */
   private boolean matchingLocation(int row, int col, int id) {
      if (!this.isValidLocation(row, col)) {
         return false;
      }
      return this.getID(row, col) != Board.calculateOppID(id);
   }
   
   /**
    * @Precondition: There are only 2 players and the ids can be either 1 or 2;
    *                   otherwise, throws an IllegalArgumentException
    * @param id   the id of a player in the game
    * @return     the id of the opposing player in the game
    */
   public static int calculateOppID(int id) {
      //different from checkID(int) because we can't have an id of 0
      if (id != 1 && id != 2) {
         throw new IllegalArgumentException();
      }
      return 3 - id;
   }
}
