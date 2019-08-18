package square;

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
 * https://leetcode.com/discuss/interview-question/125037/The-2-player-game-of-Drawdown-with-N-groups-of-stones
 *
 */
public class Drawdown {

}
