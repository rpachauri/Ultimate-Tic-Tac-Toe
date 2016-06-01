package bot;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * This is an artificial intelligence that plays Ultimate Tic Tac Toe.
 *    It implements the negamax algorithm to determine the best move a player
 *    would make.
 * 
 * @author RyanPachauri
 * @version 5/30/16
 */
public class Bot {
   
   private Macroboard board;
   public final int id;

   public Bot(int myId) {
      this.board = new Macroboard();
      this.id = myId;
   }
   
   public void setField(String newField) {
      this.board.updateField(newField);
   }
   
   public void setMacroboard(String newMacroboard) {
      this.board.updateBoards(newMacroboard);
   }
   
   public String pickBestMove(int max) {
      String result = "";
      Move bestMove = this.pickBestMove(this.id, max);
      if (bestMove != null) {
         int col = bestMove.boardCol * 3 + bestMove.col;
         result += col + " ";
         int row = bestMove.boardRow * 3 + bestMove.row;
         result += row;
      } else {
         System.out.println("No available moves");
      }
      return result;
   }
   
   /**
    * @Precondition: max is greater than 0
    * @param max
    * @return
    */
   public Move pickBestMove(int id, int max) {
      Stack<Move> moves = this.board.getAvailableMoves(id);
      if (moves.size() > 0) {
         if (max == 1) {//base case
            Queue<Move> bestMoves = new PriorityQueue<Move>();
            while (!moves.isEmpty()) {
               Move move = moves.pop();
               String boards = this.board.makeMove(move);
               this.setValue(move);
               this.board.unmakeMove(move, boards);
               bestMoves.offer(move);
            }
            return bestMoves.remove();//returns best value move
         } else {
            int bestValue = Integer.MIN_VALUE;
            Move bestMove = null;
            //recursive backtracking
            while (!moves.isEmpty()) {
               Move move = moves.pop();
               if (this.isWinningMove(move)) {
                  return move;
               }
               //choose
               String boards = this.board.makeMove(move);
               //recurse
               Move nextMove =
                     this.pickBestMove(Board.calculateOppID(id), max - 1);
               if (-1 * nextMove.value > bestValue) {
                  bestValue = -1 * nextMove.value;
                  bestMove = move;
               }
               //unchoose
               this.board.unmakeMove(move, boards);
            }
            return bestMove;
         }
      }
      return null;//TODO
   }
   
   private boolean isWinningMove(Move move) {
      Microboard microboard =
            (Microboard)this.board.get(move.boardRow, move.boardCol);
      return microboard.isWinningLocation(move.row, move.col, move.id) &&
           this.board.isWinningLocation(move.boardRow, move.boardCol, move.id);
   }
   
   /**
    * @Precondition: Move has already been made
    * @param move
    */
   private void setValue(Move move) {
      int boardValue = this.getBoardValue(move);
      int macroboardValue = this.getMacroboardValue(move);
      move.value = 10 * macroboardValue + boardValue;
   }
   
   private int getBoardValue(Move move) {
      Microboard microboard =
            (Microboard)this.board.get(move.boardRow, move.boardCol);
      Set<Integer[][]> wins =
            microboard.getPossibleWins(move.row, move.col, move.id);
      return this.getValue(microboard, wins, move.id);
   }
   
   private int getMacroboardValue(Move move) {
      Set<Integer[][]> wins =
            this.board.getPossibleWins(move.boardRow, move.boardCol, move.id);
      return this.getValue(this.board, wins, move.id);
   }
   
   private int getValue(Board<?> b, Set<Integer[][]> wins, int id) {
      int sum = 0;
      for (Integer[][] win : wins) {
         int numInLine = b.getNumInLine(win, id);
         sum += Math.pow(10, numInLine);
      }
      return sum;
   }
}
