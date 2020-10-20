import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuPanel extends JPanel {

	private JButton[][] board;
	private int[][] iBoard;

	private JButton quitButton;
	private JTextField helper;

	private JButton undoButton;
	private JButton giveupButton;

	private SudokuGame game;

	public SudokuPanel() {

		helper = new JTextField("");
		int diff = Integer.parseInt(JOptionPane.showInputDialog(helper, "Type in a Difficulty (1 = Easy, 2 = Medium, 3 = Hard):"));
		while (diff != 2 && diff != 1 && diff != 3 && diff != 666){
			diff = Integer.parseInt(JOptionPane.showInputDialog(helper, "Type in a Difficulty (1 = Easy, 2 = Medium, 3 = Hard):"));
		}

		game = new SudokuGame(diff);

		quitButton = new JButton("Quit Game");
		undoButton = new JButton("Undo");
		giveupButton = new JButton("Give up");

		resetBoardPanel();
		displayBoard();

	}

	private void resetBoardPanel () {

		JPanel center = new JPanel();
		JPanel bottom = new JPanel();
		// create game, listeners
		ButtonListener listener = new ButtonListener();

		center.setLayout(new GridLayout(9, 9, 3, 2));
		Dimension temp = new Dimension(60, 60);
		board = new JButton[9][9];

		for (int row = 0; row < 9; row++)
			for (int col = 0; col < 9; col++) {

				Border thickBorder = new LineBorder(Color.black, 1);
				board[row][col] = new JButton("");
				board[row][col].setPreferredSize(temp);
				board[row][col].setBorder(thickBorder);
				board[row][col].setBackground(Color.white);

				board[row][col].addActionListener(listener);
				center.add(board[row][col]);
			}

			// add all to contentPane
			add (center, BorderLayout.CENTER);
			add (bottom, BorderLayout.SOUTH);

		undoButton.addActionListener(listener);
		quitButton.addActionListener(listener);
		giveupButton.addActionListener(listener);
		bottom.add (undoButton);
		bottom.add (giveupButton);
		bottom.add (quitButton);

	}

	private void displayBoard() {
		iBoard = game.getBoard();

		for (int r = 0; r < game.getBoard().length; r++)
			for (int c = 0; c < game.getBoard().length; c++) {
				if (game.getGameStatus() == GameStatus.GIVE_UP) {
					if (game.legalMove(r, c, iBoard[r][c]))
						board[r][c].setBackground(Color.white);
					else board[r][c].setBackground(Color.red);
				}
				else board[r][c].setBackground(Color.white);
				switch(iBoard[r][c]){
					case 0:
						board[r][c].setText("");
						break;
					case 1:
						board[r][c].setText("1");
						break;
					case 2:
						board[r][c].setText("2");
						break;
					case 3:
						board[r][c].setText("3");
						break;
					case 4:
						board[r][c].setText("4");
						break;
					case 5:
						board[r][c].setText("5");
						break;
					case 6:
						board[r][c].setText("6");
						break;
					case 7:
						board[r][c].setText("7");
						break;
					case 8:
						board[r][c].setText("8");
						break;
					case 9:
						board[r][c].setText("9");
						break;


				}
			}

	}

	private class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			for (int r = 0; r < game.getBoard().length; r++)
				for (int c = 0; c < game.getBoard().length; c++)
					if (board[r][c] == e.getSource()) {
                        game.select(r, c);
                    }

			displayBoard();

			if (undoButton == e.getSource()){
				game.undoTurn();
				displayBoard();
			}

			if (giveupButton == e.getSource()) {
				if (game.getGameStatus() == GameStatus.IN_PROGRESS)
				game.setGameStatus(GameStatus.GIVE_UP);
				else game.setGameStatus(GameStatus.IN_PROGRESS);
				displayBoard();
			}

			if (game.getGameStatus() == GameStatus.SOLVED){
				JOptionPane.showMessageDialog(null, "Congrats You Win!\n The game will reset");
				helper = new JTextField("");
				int diff = Integer.parseInt(JOptionPane.showInputDialog(helper, "Type in a Difficulty (1 = Easy, 2 = Medium, 3 = Hard):"));
				while (diff != 2 && diff != 1 && diff != 3 && diff != 666){
					diff = Integer.parseInt(JOptionPane.showInputDialog(helper, "Type in a Difficulty (1 = Easy, 2 = Medium, 3 = Hard:"));
				}
				game = new SudokuGame(diff);
				displayBoard();
			}

		}
	}
}
