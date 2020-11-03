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

