import java.util.ArrayList;

/*****************************************************************
* Game Logic for the Sudoku Board. This class will be used inside 
* of the panel. A game object will be created inside of the panel
* to be manipulated and displayed.
*
* @author Ethan Tran, Matthew Davis, and Cole Hyink
* @version 2020.11.5
******************************************************************/
public class SudokuGame {
    /**
     * Board represents the Sudoku game board
     */
    private final int[][] board;
    
    /**
     * Status represents the status of the game
     */
    private GameStatus status;
    
    /**
     * Undo represents a list of all the previous moves so they
     * can be undone if needed
     */
    private final ArrayList undo = new ArrayList();

    /************************************************************
     * Constructor that sets the GameStatus to in progress, and 
     * creates a 9x9 Sudoku board. The board is created with a 
     * dificculty setting.
     *
     * @param diff The specified difficulty of the board created
     ***********************************************************/
    public SudokuGame(final int diff) {
        status = GameStatus.IN_PROGRESS;
        board = new int[9][9];
        reset(board);
        initBoard(diff);
    }

    /************************************************************
     * A method that initilizes a Sudoku board and randomly
     * fills in a number of slots based on the difficulty. 
     *
     * @param diff The specified difficulty of the board created
     ***********************************************************/
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

    /************************************************************
     * Method that checks if a move on a specified board space
     * is valid.
     *
     * @param r Row number of board space
     * @param c Column number of board space
     * @param val Value trying change the board space value to
     * @return Returns true if it is a valid move, false if
     * it is an invalid move
     ***********************************************************/
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

    /************************************************************
     * Method that copies the values in one Sudoku board to
     * another Sudoku board.
     *
     * @param dest Board that is copied to
     * @param source Board that is copied from
     ***********************************************************/
    public void copyBoard(int[][] dest, int[][] source){
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                dest[r][c] = source[r][c];
            }
        }
    }

    /************************************************************
     * Method that checks if a given Sodokuo game board is valid
     *
     * @param board Sudokuo game board to check
     * @return Returns true if the board is valid,
     * returns false if the board is false
     ***********************************************************/
    public boolean validboard(final int[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!legalMove(i, j, board[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    /************************************************************
     * Method that solves the given Sudoku board
     *
     * @param board Sudokuo game board to solve
     * @return Returns true if the board is solved,
     * returns false if the board is unsolved
     ***********************************************************/
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

    /************************************************************
     * Method that selects a place on the board and updates it 
     * with a new value. Then tracks the information to the undo
     * list. Finally, it checks if the board is solved.
     *
     * @param row Row number of board space
     * @param col Column number of board space
     * @param data Data to add to specifed board space
     ***********************************************************/
    public void select(final int row, final int col, final int data) {
        if (data >= 0 || data <= 9) {
            board[row][col] = data;
            int[] x = new int[3];
            x[0] = row;
            x[1] = col;
            x[2] = board[row][col];
            undo.add(x);
            isWinner();
        }
    }

    /************************************************************
     * Method that undos the previous selection/move made by the
     * player
     *
     * @param row Row number of board space
     * @param col Column number of board space
     * @param data Data to add to specifed board space
     ***********************************************************/
    private void undoSelect(final int row, final int col, final int data) {
        board[row][col] = data;
    }

     /************************************************************
     * Method that determies if the board is solved and the
     * player is a winner. GameStatus is updated to solved if
     * the board is solved.
     * 
     * @return Returns true if the board is solved, false if the 
     * board is unsolved
     ***********************************************************/
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

    /************************************************************
     * Getter method that returns the game board
     *
     * @return Returns the current game board
     ***********************************************************/
    public int[][] getBoard() {
        return board;
    }

    /************************************************************
     * Getter method that returns the current game status
     *
     * @return Returns the current game status
     ***********************************************************/
    public GameStatus getGameStatus() {
        return status;
    }

    /************************************************************
     * Setter method that changes the current game status
     *
     * @param stat Status of the game you want to set to
     ***********************************************************/
    public void setGameStatus(final GameStatus stat) {
        this.status = stat;
    }

    /************************************************************
     * Method that undos a turn made by the player. Undo
     * turns are removed from the undo list.
     ***********************************************************/
    public void undoTurn() {
        if (undo.size() < 1) {
            return;
        }
        int[] x  = (int[]) undo.remove(undo.size() - 1);
        undoSelect(x[0], x[1], x[2]);
    }

    /************************************************************
     * Method that resets the game board to blank
     *
     * @param board Board to be reset
     ***********************************************************/
    private void reset(int[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = 0;
            }
        }
    }
}



