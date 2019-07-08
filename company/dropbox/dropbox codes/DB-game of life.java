//time O(m * n), space O(m * n)

public class Solution {
    public void gameOfLife(int[][] board) {
        if (board == null || board[0].length == 0) return;
        int m = borad.length; int n = board[0].length;
        int[][] newBoard = new int[m][n];

        for (int i = 0; i < m; i ++) {
            for (int j = 0; j < n; j ++) {
                int lives = countLives(board, m, n, i, j);

                if (board[i][j] == 1 && (lives < 2 || lives > 3))
                    newboard[i][j] = 0;
                if (board[i][j] == 1 && (lives >= 2 || lives <= 3))
                    newboard[i][j] = 1;
                if (board[i][j] == 0 && lives == 3)
                    newboard[i][j] = 1;
            }
        }

        for (int i = 0; i < m; i ++) {
            for (int j = 0; j < n; j ++) {
                board[i][j] = newboard[i][j];
            }
        }
    }

    private int countLives(int[][] board, int m, int n, int i, int j) {
        int lives = 0;

        for (int x = Math.max(i - 1, 0); x <= Math.min(i + 1, m - 1); x ++) {
            for (int y = Math.max(j - 1, 0); y <= Math.min(j + 1, n - 1); y ++) {
                lives += board[x][y];
            }
        }

        lives -= board[i][j];
        return lives;
    }
}


// use two bits to store (next state, current state) 
//time O(m * n) space O(m * n)

public class Solution {
    public void gameOfLife(int[][] board) {
        if (board == null || board[0].length == 0) return;

        int m = borad.length; int n = board[0].length;

        for (int i = 0; i < m; i ++) {
            for (int j = 0; j < n; j ++) {
                int lives = countLives(board, m, n, i, j);
                if(board[i][j] == 1 && (lives == 2 || lives == 3)) //(0,1)->(1,1)
                    board[i][j] = 3;
                if (board[i][j] == 0 && lives == 3) // (0,0)->(1,0)
                    board[i][j] = 2;
            }
        }

        for (int i = 0; i < m; i ++) {
            for (int j = 0; j < n; j ++) {
                board[i][j] >>= 1;
            }
        }
    }

    private int countLives(int[][] board, int m, int n, int i, int j) {
        int lives = 0;

        for (int x = Math.max(0, i - 1); x <= Math.min(m - 1, i + 1); x ++) {
            for (int y = Math.max(0, j - 1); y <= Math.min(n - 1, j + 1); y ++) {
                lives += borad[i][j] & 1;
            }
        }
        lives -= board[i][j] & 1;
        return lives;
    }
}

// if the board is too large, read the data line by line, and compute the new cell state using 3 lines each time
// then throw away the top line and read in a new line
// I/O API: int[] readLine(), void writeLine(int[] array)

public class Solution {
    public void gameOfLife() {
        int [] prev = null, cur = null, next = null;
        int[] pointer = null;

        while ((pointer = readLine()) != null) {
            if (cur == null) {
                cur = pointer;
                continue;
            }

            if (next == null) 
                next = pointer;

            //First row
            if (prev == null) {
                int[][] tmpBoard = new int[2][];
                tmpBoard[0] = cur.clone();
                tmpBoard[1] = next.clone();
                int[][] nextStateBoard = updateBoard(tmpBoard);
                writeLine(nextStateBoard[0]);
            }

            else {
                int[][] tmpBoard = new int[3][];
                tmpBoard[0] = prev.clone();
                tmpBoard[1] = cur.clone();
                tmpBoard[2] = next.clone();
                int[][] nextStateBoard = updataBoard(tmpBoard);
                writeLine(nextStateBoard[1]);
            }

            prev = cur;
            cur = next;
            next = null;
        }

        //last row
        int[][] tmpBoard = new int[2][];
        tmpBoard[0] =  prev.clone();
        tmpBoard[1] = cur.clone();
        int[][] nextStateBoard = updateBoard(tmpBoard);
        writeLine(nextStateBoard[1]);
    }

    public class Coord {
        int x, y;
        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean equals(Object o){
            return o instanceOf Coord && ((Coord) o).x == x && (Coord) o).y == y; 
        }

        public int hashCode() {
            int hashCode = 1;
            hashCode = hashCode * 31 + x;
            hashCode = hashCode * 31 + y;

            return hashCode;
        }
    }

    private int[][] updateBoard(int[][] board) {
        if (board == null || board.length == 0) return board;

        int m = board.length; int n = board[0].length;

        Set<Coord> lives = new HashSet<>();

        //find out all live cell in current state
        for (int i = 0; i < m; i ++) {
            for (int j = 0; j < n; j ++) {
                if (board[i][j] == 1)
                    lives.add(new Coord(i,j));
            }
        }

        live = updateLives(live, m, n);

        //update board
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = lives.contains(new Coord(i,j)) ? 1 : 0;    
            }
        }
        return board;
    }

    private Set<Coord> updateLives(Set<Coord> lives, int m, int n) {
        Map<Coord, Integer> neighbours = new HashMap<>();

        //update the number of live neighbours of each cell
        for(Coord cell : lives) {
            for(int i = Math.max(0, cell.x-1); i <= Math.min(m-1, cell.x+1)) {
 				for(int j = Math.max(0, cell.y-1); j <= Math.min(n-1, cell.y+1)) {
                    if (i == cell.x && j = cell.y) continue;
                    Coord c = new Coord(i, j);

                    if (neighbours.containsKey(c))
                        neighbours.put(c, neighbours.get(c) + 1);
                    else
                        neighbours.put(c, 1);

                }
            }
        }

        //update live cells
        Set<Coord> newLives = new HashSet<>();
        for (Map.Entry<Coord, Integer> entry : neighbours.entrySet()) {
            Coord key = entry.getKey();
            int value = entry.getValue();

            if (lives.contains(key) && (value == 2 || value == 3)) {
                newLives.add(key);
            }
            if (!lives.contains(key) && value == 3) {
                newLives.add(key);
            }
        }
        return newLives;
    }
}

