// highest minimum sharpness


res = Math.max(Math.min(grid[i][j], Math.max(grid[i-1][j-1],grid[i][j-1],grid[i+1][j-1])))
import java.util.*;

public class Solution {
    public int highestMinVal(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) 
            return 0;

        int m = grid.length, n = grid[0].length;
        int[][] dp = new int[m][n];

        for (int r = 0; r < m; r ++) {
            //filling the col 0 of dp
            dp[r][0] = grid[r][0];
        }

        //interate from col 1
        for (int c = 1; c < n; c ++) {
            for (int r = 0; r < m; r ++) {
                int max = 0;
                //compare left previous column, 3 units
                for (int i = Math.max(0, r - 1); i <= Math.min(m - 1, r + 1); i ++) {
                    max = Math.max(max, dp[i][c - 1]);
                }

                // compare with current position
                dp[r][c] = Math.min(max, grid[r][c]);
            }
        }

        //find the maxmum value of every path
        int res = 0;
        for (int r = 0; r < m; r ++) {
            res = Math.max(res, dp[r][n -1]);
        }
        return res;
    }

    public static void main(String[] args) {
        Solution so = new Solution();

        int[][] grid = new int[][] {
            {5,7,2},
            {7,5,8},
            {9,1,5}
        };
        assert so.highestMinVal(grid) == 7;
        System.out.println("Success!");
    }
}

