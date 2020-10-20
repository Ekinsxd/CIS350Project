
import java.util.ArrayList;

public class SudokuGame {
    private final int[][] board;
    private GameStatus status;
    private final ArrayList undo = new ArrayList();

    public SudokuGame(int diff) {
        status = GameStatus.IN_PROGRESS;
        board = new int[9][9];
        reset();
        initBoard(diff);
    }

    public void initBoard(int diff){
        board[0][0] = (int)(Math.random()*9+1);
        solve(board);
        int x =0;
        int r;
        int c;
        switch (diff){
            case 666:
                break;
            case 3:
                x = 60;
                break;
            case 2:
                x = 30;
                break;
            case 1:
                x = 20;
                break;
        }
        while (x > 0) {
            r = (int) (Math.random() * 9);
            c = (int) (Math.random() * 9);
            if (board[r][c] != 0){
                x--;
                board[r][c] = 0;
            }
        }
    }

    public boolean legalMove(int r, int c, int val){
        if (val == 0)
            return true;
        for (int i=0; i<9;i++){
            if (i != c)
            if (board[r][i] == val)
                return false;
            if (r!=i)
            if (board[i][c] == val)
                return false;
        }
        int subsectionRowStart = (r / 3) * 3;
        int subsectionRowEnd = subsectionRowStart + 3;

        int subsectionColumnStart = (c / 3) * 3;
        int subsectionColumnEnd = subsectionColumnStart + 3;

        for (int row = subsectionRowStart; row < subsectionRowEnd; row++) {
            for (int col = subsectionColumnStart; col < subsectionColumnEnd; col++) {
                if (r!=row && c!=col)
                if (board[row][col] == val) return false;
            }
        }
        return true;
    }

    private boolean validboard(int[][] board){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!legalMove(i,j,board[i][j]))
                    return false;
            }
        }
        return true;
    }

    private boolean solve(int[][] board) {
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

    public void select(int row, int col) {
        if (board[row][col] > 8)
            board[row][col] = 0;
        else board[row][col]++;
        int[] x = new int[2];
        x[0] = row;
        x[1] = col;
        undo.add(x);
        isWinner();
    }

    public void undoSelect(int row, int col){
        if (board[row][col] < 1)
            board[row][col] = 9;
        else board[row][col]--;
    }

    private boolean isWinner() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0 || !legalMove(i,j,board[i][j]))
                    return false;
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

    public void setGameStatus(GameStatus stat){
        this.status = stat;
    }


    public void undoTurn (){
        if (undo.size()<1)
            return;
        int[] x  = (int[]) undo.remove(undo.size()-1);
        undoSelect(x[0],x[1]);
    }

    public void reset() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = 0;
            }
        }
    }
}



