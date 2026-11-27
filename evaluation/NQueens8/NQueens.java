class NQueens {

    static int solutionCount = 0;

    static boolean isSafe(int[] board, int row, int col) {
        for (int i = 0; i < row; i++) {
            if (board[i] == col) {
                return false;
            }
            if (board[i] - i == col - row) {
                return false;
            }
            if (board[i] + i == col + row) {
                return false;
            }
        }
        return true;
    }

    static void solve(int[] board, int row, int n) {
        if (row == n) {
            solutionCount++;
            return;
        }
        for (int col = 0; col < n; col++) {
            if (isSafe(board, row, col)) {
                board[row] = col;
                solve(board, row + 1, n);
                board[row] = -1;
            }
        }
    }

    public static void main(String[] args) {
        int n = 8;
        if (args.length > 0) {
            n = Integer.parseInt(args[0]);
        }
        int[] board = new int[n];
        for (int i = 0; i < n; i++) {
            board[i] = -1;
        }
        solve(board, 0, n);
        System.out.println("N=" + n + ", solutions=" + solutionCount);
    }
}
