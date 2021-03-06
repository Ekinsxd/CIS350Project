import java.io.Serializable;
import java.util.ArrayList;

/*****************************************************************
* Game Logic for the Sudoku Board. This class will be used inside
* of the panel. A game object will be created inside of the panel
* to be manipulated and displayed.
*
* @author Ethan Tran, Matthew Davis, and Cole Hyink
* @version 2020.11.5
******************************************************************/

public class SudokuGame implements Serializable {
    /**
     * Board represents the Sudoku game board.
     */
    private final int[][] board;

    /**
     * Board represents the initial Sudoku game board.
     */
    private final int[][] initboard;

    /**
     * Represents maximum board size.
     */
    private final int board_size = 9;

    /**
     * Represents maximum data size.
     */
    private final int min_data = 0;

    /**
     * Represents minimum board size.
     */
    private final int max_data = 9;

    /**
     * Status represents the status of the game.
     */
    private GameStatus status;

    /**
     * Undo represents a list of all the previous moves so they
     * can be undone if needed.
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
        board = new int[board_size][board_size];
        initboard = new int[board_size][board_size];
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
        for (int i = 0; i < 10; i++) { //randomize board
            r = (int) (Math.random() * board_size);
            c = (int) (Math.random() * board_size);
            if (board[r][c] != 0) {
                i--;
                continue;
            }
            board[r][c] = (int) (Math.random() * board_size);
            if (!legalMove(r, c, board[r][c])) {
                board[r][c] = 0;
                i--;
                continue;
            }
            if (i == board_size) {
                solve(board);
                if (!validboard(board)) {
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
                x = 15;
            default:
                break;
        }
        while (x > 0) {
            r = (int) (Math.random() * board_size);
            c = (int) (Math.random() * board_size);
            if (board[r][c] != 0) {
                x--;
                board[r][c] = 0;
                initboard[r][c] = 1;
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
        for (int i = 0; i < board_size; i++) {
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
            for (int col = subsectionColumnStart; col < subsectionColumnEnd;
                 col++) {
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
     * Method that checks if a given Sodokuo game board is valid.
     *
     * @param board1 Sudokuo game board to check
     * @return Returns true if the board is valid,
     * returns false if the board is false
     ***********************************************************/

    public boolean isFilledBoard(final int[][] board1) {
        for (int i = 0; i < board_size; i++) {
            for (int j = 0; j < board_size; j++) {
                if (board1[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }


    /************************************************************
     * Method that checks if a given Sodokuo game board is valid.
     *
     * @param board1 Sudokuo game board to check
     * @return Returns true if the board is valid,
     * returns false if the board is false
     ***********************************************************/

    public boolean validboard(final int[][] board1) {
        for (int i = 0; i < board_size; i++) {
            for (int j = 0; j < board_size; j++) {
                if (!legalMove(i, j, board1[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    /************************************************************
     * Method that solves the given Sudoku board.
     *
     * @param board1 Sudokuo game board to solve
     * @return Returns true if the board is solved,
     * returns false if the board is unsolved
     ***********************************************************/

    public boolean solve(final int[][] board1) {
        for (int row = 0; row < board_size; row++) {
            for (int column = 0; column < board_size; column++) {
                if (board1[row][column] == 0) {
                    for (int k = 1; k <= max_data; k++) {
                        board1[row][column] = k;
                        if (validboard(board1) && solve(board1)) {
                            return true;
                        }
                        board1[row][column] = 0;
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
        if (data >= min_data && data <= max_data) {
            int[] x = new int[3];
            x[0] = row;
            x[1] = col;
            x[2] = board[row][col];
            board[row][col] = data;
            undo.add(x);
            isWinner();
        }
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
        for (int i = 0; i < board_size; i++) {
            for (int j = 0; j < board_size; j++) {
                if (board[i][j] == 0 || !legalMove(i, j, board[i][j])) {
                    return false;
                }
            }
        }
        setGameStatus(GameStatus.SOLVED);
        return true;
    }

    /************************************************************
     * Getter method that returns the game board.
     *
     * @return Returns the current game board
     ***********************************************************/

    public int[][] getBoard() {
        return board;
    }

    /************************************************************
     * Getter method that returns the initial board.
     *
     * @return Returns the current game board
     ***********************************************************/

    public int[][] getInitBoard() {
        return initboard;
    }

    /************************************************************
     * Getter method that returns the current game status.
     *
     * @return Returns the current game status
     ***********************************************************/

    public GameStatus getGameStatus() {
        return status;
    }

    /************************************************************
     * Setter method that changes the current game status.
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
        board[x[0]][x[1]] = x[2];
    }

    /************************************************************
     * Method that resets the game board to blank.
     *
     * @param board1 Board to be reset
     ***********************************************************/

    private void reset(final int[][] board1) {
        for (int i = 0; i < board_size; i++) {
            for (int j = 0; j < board_size; j++) {
                board1[i][j] = 0;
            }
        }
    }
}
