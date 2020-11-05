/*****************************************************************
 Facade of the Sudoku game. Simple code to initialize the panel
 and let the game play.

 @author Ethan Tran, Matthew Davis, and Cole Hyink
 @version 2020.11.5
 *****************************************************************/
import javax.swing.*;

public class Sudoku {

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

