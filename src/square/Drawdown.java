package square;

import java.util.Arrays;

/**
 * The 2-player game of Drawdown is played with a board made up of N groups of stones.
 * At the start of each game a board and a list of moves are provided.
 * Each move is a list of N integers, which represent the number of stones the move adds/removes
 * from the board at each index. All moves result in a net reduction of stones on the board.
 * Moves can be re-used, but a move can no longer be performed if doing so would reduce the number
 * of stones in any group below 0. After no more moves can be completed, player 1 wins if there are
 * more stones at the first index than at the last index. Otherwise, player 2 wins.
 *
 * Example: Let's say the game begins with a board of [6, 4, 2, 4] and these are the available moves provided: 
 * 1. [-2, -2, 1, 0] 
 * 2. [-4, -4, 0 ,0] 
 * 3. [0, 0, -2, -2] 
 * Player One: 3, Player Two: 2 
 * move 1, move 1, move 3, move 3 
 * Initial board: [6, 4, 2, 4] 
 * Player 1 decides to perform move 1. New board: [4, 2, 3, 4] 
 * Player 2 can perform move 1 or move 3. They decide to perform move 1.  New‍‌‌‍‍‍‌‌‌‌‌‌‍‍ board: [2, 0, 4, 4] 
 * Player 1 has to perform move 3.  New board: [2, 0, 2, 2] 
 * Player 2 has to perform move 3. New board: [2, 0, 0, 0] 
 * The game is now over and player 1 is the winner.
 * 求两个玩家各有多少种走法可以赢
 *
 * 1. check winning condition
 * 2. Make the move
 * 3. How make get the winning results for each player
 * 4. Perf improvement.
 *
 * https://leetcode.com/discuss/interview-question/125037/The-2-player-game-of-Drawdown-with-N-groups-of-stones
 * https://www.1point3acres.com/bbs/thread-515407-1-1.html
 */
public class Drawdown {

  private int[] result = new int[]{0, 0};

  public int[] playGame(int[] board, int[][] moves) {
    // invalid input
    if (board.length != moves[0].length) {
      return result;
    }
    dfs(board, moves);
    return result;
  }

  private void dfs(int[] board, int[][] moves) {
    // count the fail to execute time, if it equals the length of moves,
    // means all moves are failed, which means we reach the terminated state
    int falseCount = 0;
    for (int[] move : moves) {
      if (!checkContinue(board, move)) {
        // we can not continue with current move
        falseCount++;
      } else {
        int[] newBoard = execution(board, move);
        dfs(newBoard, moves);
      }
    }
    if (falseCount == moves.length) {
      // we reach the terminal point
      if (board[0] > board[board.length - 1]) {
        result[0]++;
        return;
      } else {
        result[1]++;
        return;
      }
    }
  }

  private boolean checkContinue(int[] board, int[] move) {
    for (int i = 0; i < board.length; i++) {
      if (board[i] + move[i] < 0) {
        return false;
      }
    }
    return true;
  }

  private int[] execution(int[] board, int[] move) {
    int[] newBoard = board.clone();
    for (int i = 0; i < newBoard.length; i++) {
      newBoard[i] += move[i];
    }
    return newBoard;
  }

  public static void main(String[] args) {
    int[] board = new int[]{6, 4, 2, 4};
    int[][] moves = new int[][]{{-2, -2, 1, 0}, {-4, -4, 0 ,0}, {0, 0, -2, -2}};

    Drawdown drawdown = new Drawdown();
    System.out.println(Arrays.toString(drawdown.playGame(board, moves)));
  }
}
