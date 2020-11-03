import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuPanel extends JPanel {

	private JButton[][] board;
	private int[][] iBoard;

	private final JButton quitButton;
	private JTextField helper;

	private final JButton undoButton;
	private final JButton hintButton;
	private final JButton giveupButton;
	private final JButton newGameButton;

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
		hintButton = new JButton("Hint");
		giveupButton = new JButton("Give Up");
		newGameButton = new JButton("New Game");

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
		hintButton.addActionListener(listener);
		giveupButton.addActionListener(listener);
		newGameButton.addActionListener(listener);

		bottom.add (newGameButton);
		bottom.add (undoButton);
		bottom.add (hintButton);
		bottom.add (giveupButton);
		bottom.add (quitButton);

	}

	private void displayBoard() {
		iBoard = game.getBoard();

		if (game.getGameStatus() == GameStatus.GIVE_UP){
			if (game.solve(game.getBoard()));
			else
			{
				game.copyBoard(game.getBoard(), iBoard);
				game.setGameStatus(GameStatus.IN_PROGRESS);
				JOptionPane.showMessageDialog(helper, "Board Cannot be Solved from current state.");
			}
		}

		for (int r = 0; r < game.getBoard().length; r++)
			for (int c = 0; c < game.getBoard().length; c++) {
				if (game.getGameStatus() == GameStatus.HINT) {
					if (iBoard[r][c] == 0){
						board[r][c].setBackground(Color.white);
					}
					if (game.legalMove(r, c, iBoard[r][c]) && iBoard[r][c] != 0)
						board[r][c].setBackground(Color.green);
					else if (iBoard[r][c] != 0){
						board[r][c].setBackground(Color.red);
					}
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
					if (board[r][c] == e.getSource() && game.getGameStatus() != GameStatus.SOLVED && game.getGameStatus() != GameStatus.GIVE_UP && game.getGameStatus() != GameStatus.GAME_DONE) {
                        game.select(r, c);
                    }

			displayBoard();

			if (newGameButton == e.getSource()){
				helper = new JTextField("");
				int diff = Integer.parseInt(JOptionPane.showInputDialog(helper, "Type in a Difficulty (1 = Easy, 2 = Medium, 3 = Hard):"));
				while (diff != 2 && diff != 1 && diff != 3 && diff != 666){
					diff = Integer.parseInt(JOptionPane.showInputDialog(helper, "Type in a Difficulty (1 = Easy, 2 = Medium, 3 = Hard:"));
				}
				game = new SudokuGame(diff);
				displayBoard();
			}

			else if (hintButton == e.getSource()) {
				if (game.getGameStatus() == GameStatus.IN_PROGRESS)
					game.setGameStatus(GameStatus.HINT);
				else game.setGameStatus(GameStatus.IN_PROGRESS);
				displayBoard();
			}

			else if (quitButton == e.getSource()){
				System.exit(0);
			}

			else if (game.getGameStatus() == GameStatus.GAME_DONE) {
				displayBoard();
			}

			else if (undoButton == e.getSource()){
				game.undoTurn();
				displayBoard();
			}

			else if (giveupButton == e.getSource()) {
				if (game.getGameStatus() == GameStatus.IN_PROGRESS || game.getGameStatus() == GameStatus.HINT)
					game.setGameStatus(GameStatus.GIVE_UP);
				else game.setGameStatus(GameStatus.IN_PROGRESS);
				displayBoard();
				JOptionPane.showMessageDialog(null, "Here is the solved board!\n Start a New Game to Play Again!");
				game.setGameStatus(GameStatus.GAME_DONE);
			}


			else if (game.getGameStatus() == GameStatus.SOLVED){
				JOptionPane.showMessageDialog(null, "Congrats You Win!\n Start a New Game to Play Again!");
				game.setGameStatus(GameStatus.GAME_DONE);
				displayBoard();
			}
		}
	}
}
