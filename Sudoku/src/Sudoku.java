import javax.swing.*;

/*****************************************************************
* Facade of the Sudoku game. Simple code to initialize the panel
* and let the game play.
* @author Ethan Tran, Matthew Davis, and Cole Hyink
* @version 2020.11.5
*****************************************************************/
public class Sudoku {

    /**
     *  Initilizes and constructs Soduku GUI
     *
     * @param args
     */
    public static void main (String[] args)
    {
        JFrame frame = new SudokuPanel();
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,675);
        frame.setVisible(true);
    }
}

