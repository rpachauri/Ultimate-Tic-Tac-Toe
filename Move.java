package bot;

import java.util.ArrayList;
import java.util.List;

public class Move {
   
   private final int row, col;
   private int id;
   private List<Move> moves;

   public Move(int myRow, int myCol) {
      this.row = myRow;
      this.col = myCol;
      this.moves = new ArrayList<Move>();
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
    *    e.g.  "Move 00 by P1 lets P2 play at 10, 20, 01, 22"
    */
   public String toString() {
      String result = "Move";
      result += this.col;
      result += this.row + " by P" + this.id + " lets P";
      result += Macroboard.calculateOppID(id) + " play at";
      for (Move move : this.moves) {
         result += " " + move.col;
         result += move.row;
      }
      return result;
   }
}
