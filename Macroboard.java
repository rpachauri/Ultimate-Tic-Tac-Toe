package bot;

public class Macroboard {
   
   /* In our game of Ultimate Tic Tac Toe,
    *    we need to keep track of the 3 x 3 field of boards
    */
   private MicroBoard[][] field;
   public static final int SQUARE_LENGTH = 3;

   public Macroboard() {
      this.field =
            new MicroBoard[Macroboard.SQUARE_LENGTH][Macroboard.SQUARE_LENGTH];
      for (int row = 0; row < Macroboard.SQUARE_LENGTH; row++) {
         for (int col = 0; col < Macroboard.SQUARE_LENGTH; col++) {
            this.field[row][col] = new MicroBoard();
         }
      }
   }
   
   /**
    * Updates the Macroboard with the given inputs
    * @param newField   A String that details the id of every single loc within
    *                      the field
    *                   Goes row by row of the entire field
    *        e.g. 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    *             0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    *             0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
    * 
    * @param newMacroboard
    *                   A String that details the id of every board in field
    *        e.g. -1,-1,-1,
    *             -1,-1,-1,
    *             -1,-1,-1
    */
   public void update(String newField, String newMacroboard) {
      //TODO
   }
   
   /**
    * @Precondition: 1. the given location is a valid location in the field;
    *                      otherwise, throws an IllegalStateException
    *                2. id is a 0, 1, or 2;
    *                      otherwise, throws an IllegalArgumentException
    *                
    * Updates the given location of the Macroboard with the given id
    * 
    * @param row  location in field
    * @param col  location in field
    * @param id   id belong to a player (can be 0, 1, or 2)
    */
   public void update(int row, int col, int id) {
      if (!this.isValidLocation(row, col)) {
         throw new IllegalStateException();
      }
      if (id != 0 && id != 1 && id != 2) {
         throw new IllegalArgumentException();
      }
      //location of the board within the field
      int macroRow = row / Macroboard.SQUARE_LENGTH;
      int macroCol = col / Macroboard.SQUARE_LENGTH;
      //location within the board
      int boardRow = row % Macroboard.SQUARE_LENGTH;
      int boardCol = col % Macroboard.SQUARE_LENGTH;
      this.field[macroRow][macroCol].update(boardRow, boardCol, id);
   }
   
   /**
    * 
    * @param row  location in field
    * @param col  location in field
    * @return  true if the given location exists within the macroboard field;
    *          otherwise, false
    */
   private boolean isValidLocation(int row, int col) {
      int locLength = Macroboard.SQUARE_LENGTH * Macroboard.SQUARE_LENGTH;
      return row >= 0 && row < locLength &&
             col >= 0 && col < locLength;
   }
   
   /**
    * @Precondition: 1. the given board location is a valid board in the field;
    *                      otherwise, throws an IllegalStateException
    * 
    * @param row  board in field
    * @param col  board in field
    * @return TTTBoard the board at the given location in the macroboard
    */
   public MicroBoard getBoard(int row, int col) {
      if (!this.isValidBoard(row, col)) {
         throw new IllegalStateException();
      }
      return this.field[row][col];
   }
   
   /**
    * 
    * @param row  board in field
    * @param col  board in field
    * @return  true if the given board exists within the macroboard field;
    *          otherwise, false
    */
   private boolean isValidBoard(int row, int col) {
      return row >= 0 && row < Macroboard.SQUARE_LENGTH &&
             col >= 0 && col < Macroboard.SQUARE_LENGTH;
   }
   
   /**
    * @Precondition: 1. We are playing UTTT, where there are only 2 players
    * @param id   the id of a player in the game
    * @return     the id of the opposing player in the game
    */
   public static int calculateOppID(int id) {
      return 3 - id;
   }
}
