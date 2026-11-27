class KnightsTour {

    static int[] dx = {2, 1, -1, -2, -2, -1, 1, 2};
    static int[] dy = {1, 2, 2, 1, -1, -2, -2, -1};

    static boolean isValid(int x, int y, int n, int[][] board) {
        if (x >= 0 && x < n && y >= 0 && y < n && board[x][y] == 0) {
            return true;
        }
        return false;
    }

    static boolean solve(int[][] board, int x, int y, int move, int n) {
        if (move == n * n) {
            return true;
        }
        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (isValid(nx, ny, n, board)) {
                board[nx][ny] = move + 1;
                if (solve(board, nx, ny, move + 1, n)) {
                    return true;
                }
                board[nx][ny] = 0;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int n = 5;
        if (args.length > 0) {
            n = Integer.parseInt(args[0]);
        }
        int[][] board = new int[n][n];
        board[0][0] = 1;
        if (solve(board, 0, 0, 1, n)) {
            System.out.println("Solution found for " + n + "x" + n + " board.");
        } else {
            System.out.println("No solution for " + n + "x" + n + " board.");
        }
    }
}
