# Ultimate-Tic-Tac-Toe
An artificial intelligence that plays Ultimate Tic Tac Toe on the site http://theaigames.com/

This bot uses negamax, a variation of the minimax algorithm (designed to reduce reduncancy).

I have created an abstract class Board that keeps track of a certain Type of a Object. This was important because, in Ultimate Tic Tac Toe,
  there is a big board (denoted as the Macroboard in the code) that keeps track of 9 smaller boards (denoted as Microboards).
  Each Microboard keeps track of 9 locations (these are integers of 0, 1, or 2),
    where 1 or 2 means that the location belongs to the player with that id
    while 0 means that the location belongs to neither player
  The same id concept is used for assigning ids to the boards themselves within Macroboard.
    e.g. A Microboard with an id of 1 means that player 1 has won that microboard.
  More information on how the game is played can be found on The AI Games website.

Creating the Board API allowed me to interact with possible game scenarios. This is how I was able to use recursive backtracking:
  1.  I chose a possible move that one player could make.
  2.  I looked at the possibilites that would arise from that move.
      a.  The next player would obviously make his/her best move.
  3.  I then unchose that move.
  4.  Of the possible moves that one player could make, he/she would pick the one that would be least beneficial to the opposing player.
