package bot;

/**
 * A Move is something that can only be played in a Microboard.
 *    The Microboard keeps track of moves that each player makes.
 * @author RyanPachauri
 * @version 5/26/16
 */
public class Move implements Comparable<Move> {
   
   public final int row, col, boardRow, boardCol, id;
   int value;

   public Move(int myRow, int myCol, int myBoardRow, int myBoardCol, int myId){
      this.row = myRow;
      this.col = myCol;
      this.boardRow = myBoardRow;
      this.boardCol = myBoardCol;
      this.id = myId;
   }
   
   /**
    * 
    * @param other
    * @return
    *
   public boolean equals(Object obj) {
      if (obj instanceof Move) {
         Move other = (Move)obj;
         if (this.row == other.row &&
             this.col == other.col &&
             this.id == other.id) {
            return true;
         }
         //TODO if we do end up comparing the list of moves in this move, how do we do it?
      }
      return false;//TODO
   }*/
   
   /**
    * Returns a String representation of this object:
    *    e.g.  "Move 02 is played by P1 in board 11"
    */
   public String toString() {
      String result = "Move";
      result += this.col;
      result += this.row + " is played by P" + this.id + " in board "
            + this.boardCol;
      result += this.boardRow;
      return result;
   }

   /**
    * All we care about when comparing two moves are how they are valued.
    */
   @Override
   public int compareTo(Move o) {
      return ((Integer)this.value).compareTo(o.value);
   }
}
