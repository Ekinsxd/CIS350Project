import javax.swing.*;

/**
 * A class that initizlies and constructs the Soduku GUI
 * @author Matthew DAvis, Ethan Tran, and Cole Hyink
 */
public class Sudoku {

    /**
     *  Initilizes and constructs Soduku GUI
     * @param args
     */
    public static void main (String[] args)
    {
        JFrame frame = new JFrame ("Sudoku");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

        SudokuPanel panel = new SudokuPanel();

        frame.getContentPane().add(panel);

        frame.setSize(600,650);
        frame.setVisible(true);
    }
}

