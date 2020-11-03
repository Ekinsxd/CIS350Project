
import java.util.ArrayList;

public class SudokuGame {
    private final int[][] board;
    private GameStatus status;
    private final ArrayList undo = new ArrayList();

    public SudokuGame(final int diff) {
        status = GameStatus.IN_PROGRESS;
        board = new int[9][9];
        reset(board);
        initBoard(diff);
    }

    private void initBoard(final int diff) {

        int x = 0;
        int r;
        int c;
        for (int i =0; i<10; i++){ //randomize board
            r = (int) (Math.random() * 9);
            c = (int) (Math.random() * 9);
            if (board[r][c] != 0) {
                i--;
                continue;
            }
            board[r][c] = (int) (Math.random() * 9);
            if (!legalMove(r, c, board[r][c])){
                board[r][c] = 0;
                i--;
                continue;
            }
            if (i == 9){
                solve(board);
                if (!validboard(board)){
                    reset(board);
                    i = 0;
                    continue;
                }
            }
        }
        switch (diff) {
            case 3:
                x = 60;
                break;
            case 2:
                x = 30;
                break;
            case 1:
                x = 20;
            default:
                break;
        }
        while (x > 0) {
            r = (int) (Math.random() * 9);
            c = (int) (Math.random() * 9);
            if (board[r][c] != 0) {
                x--;
                board[r][c] = 0;
            }
        }
    }

    public boolean legalMove(final int r, final int c, final int val) {
        if (val == 0) {
            return true;
        }
        for (int i = 0; i < 9; i++) {
            if (i != c) {
                if (board[r][i] == val) {
                    return false;
                }
            }
            if (r != i) {
                if (board[i][c] == val) {
                    return false;
                }
            }
        }
        int subsectionRowStart = (r / 3) * 3;
        int subsectionRowEnd = subsectionRowStart + 3;

        int subsectionColumnStart = (c / 3) * 3;
        int subsectionColumnEnd = subsectionColumnStart + 3;

        for (int row = subsectionRowStart; row < subsectionRowEnd; row++) {
            for (int col = subsectionColumnStart; col < subsectionColumnEnd; col++) {
                if (r != row && c != col) {
                    if (board[row][col] == val) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void copyBoard(int[][] dest, int[][] source){
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                dest[r][c] = source[r][c];
            }
        }
    }

    private boolean validboard(final int[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!legalMove(i, j, board[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean solve(final int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (board[row][column] == 0) {
                    for (int k = 1; k <= 9; k++) {
                        board[row][column] = k;
                        if (validboard(board) && solve(board)) {
                            return true;
                        }
                        board[row][column] = 0;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public void select(final int row, final int col) {
        if (board[row][col] > 8) {
            board[row][col] = 0;
        } else {
            board[row][col]++;
        }
        int[] x = new int[2];
        x[0] = row;
        x[1] = col;
        undo.add(x);
        isWinner();
    }

    private void undoSelect(final int row, final int col) {
        if (board[row][col] < 1) {
            board[row][col] = 9;
        } else {
            board[row][col]--;
        }
    }

    private boolean isWinner() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0 || !legalMove(i, j, board[i][j])) {
                    return false;
                }
            }
        }
        setGameStatus(GameStatus.SOLVED);
        return true;
    }

    public int[][] getBoard() {
        return board;
    }

    public GameStatus getGameStatus() {
        return status;
    }

    public void setGameStatus(final GameStatus stat) {
        this.status = stat;
    }


    public void undoTurn() {
        if (undo.size() < 1) {
            return;
        }
        int[] x  = (int[]) undo.remove(undo.size() - 1);
        undoSelect(x[0], x[1]);
    }

    private void reset(int[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = 0;
            }
        }
    }
}



