/*****************************************************************
 Graphical representation of a 9x9 sudoku board with various
 controls over the state of the game. The board is constrained
 between 0-9.

 @author Ethan Tran, Matthew Davis, and Cole Hyink
 @version 2020.11.5
 *****************************************************************/
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuPanel extends JPanel {

	private JTextField[][] board2;
	private int[][] iBoard;

	private final JButton quitButton;
	private JTextField helper;

	private final JButton undoButton;
	private final JButton hintButton;
	private final JButton giveupButton;
	private final JButton newGameButton;

	private SudokuGame game;

	int BOARD_SIZE = 9;

	/*****************************************************************
	 Constructor creates a game of sudoku with the selected difficulty
	 *****************************************************************/

	public SudokuPanel() {

		helper = new JTextField("");
		int diff = Integer.parseInt(JOptionPane.showInputDialog(helper,
				"Type in a Difficulty (1 = Easy, 2 = Medium, 3 = Hard):"));
		while (diff != 2 && diff != 1 && diff != 3 && diff != 666){
			diff = Integer.parseInt(JOptionPane.showInputDialog(helper,
					"Type in a Difficulty (1 = Easy, 2 = Medium, 3 = Hard):"));
		}

		game = new SudokuGame(diff);

		quitButton = new JButton("Quit Game");
		undoButton = new JButton("Undo");
		hintButton = new JButton("Hint");
		giveupButton = new JButton("Give Up");
		newGameButton = new JButton("New Game");

		initBoardPanel();
		displayBoard();

	}

	/*****************************************************************
	 Initialize GUI with the board given by the randomly generated
	 board from the constructor.
	 *****************************************************************/

	private void initBoardPanel () {

		JPanel center = new JPanel();
		JPanel bottom = new JPanel();
		// create game, listeners
		ButtonListener listener = new ButtonListener();

		center.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE, 3, 2));
		Dimension temp = new Dimension(60, 60);
		board2 = new JTextField[BOARD_SIZE][BOARD_SIZE];

		for (int row = 0; row < BOARD_SIZE; row++)
			for (int col = 0; col < BOARD_SIZE; col++) {

				Border thickBorder = new LineBorder(Color.black, 1);
				board2[row][col] = new JTextField();
				board2[row][col].setEditable(true);
				board2[row][col].setPreferredSize(temp);
				board2[row][col].setBorder(thickBorder);
				board2[row][col].setBackground(Color.white);
				board2[row][col].setHorizontalAlignment(JTextField.CENTER);
				board2[row][col].setFont(new Font("Monospaced",Font.BOLD, 20));

				if (game.getBoard()[row][col] != 0){
					board2[row][col].setEditable(false);
				}
				board2[row][col].addActionListener(listener);
				center.add(board2[row][col]);
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

	/*****************************************************************
	 Sets all board text fields to empty.
	 *****************************************************************/

	private void resetBoardPanel(){
		for (int row = 0; row < BOARD_SIZE; row++)
			for (int col = 0; col < BOARD_SIZE; col++) {
				board2[row][col].setEditable(true);
				board2[row][col].setText("");
				if (game.getBoard()[row][col] != 0){
					board2[row][col].setEditable(false);
				}
			}
	}

	/*****************************************************************
	 Update and Display the text fields when an action is executed
	 *****************************************************************/

	private void displayBoard() {
		iBoard = game.getBoard();

		if (game.getGameStatus() == GameStatus.GIVE_UP){
			if (game.solve(game.getBoard()));
			else
			{
				game.copyBoard(game.getBoard(), iBoard);
				game.setGameStatus(GameStatus.IN_PROGRESS);
			}
		}

		for (int r = 0; r < game.getBoard().length; r++)
			for (int c = 0; c < game.getBoard().length; c++) {
				if (game.getGameStatus() == GameStatus.HINT) {
					if (iBoard[r][c] == 0){
						board2[r][c].setBackground(Color.white);
					}
					if (game.legalMove(r, c, iBoard[r][c]) && iBoard[r][c] != 0){
						board2[r][c].setBackground(Color.green);
					}
					else if (iBoard[r][c] != 0){
						board2[r][c].setBackground(Color.red);
					}
				}
				else board2[r][c].setBackground(Color.white);
				switch(iBoard[r][c]){
					case 0:
						board2[r][c].setText("");
						board2[r][c].setBackground(Color.yellow);
						break;
					case 1:
						board2[r][c].setText("1");
						break;
					case 2:
						board2[r][c].setText("2");
						break;
					case 3:
						board2[r][c].setText("3");
						break;
					case 4:
						board2[r][c].setText("4");
						break;
					case 5:
						board2[r][c].setText("5");
						break;
					case 6:
						board2[r][c].setText("6");
						break;
					case 7:
						board2[r][c].setText("7");
						break;
					case 8:
						board2[r][c].setText("8");
						break;
					case 9:
						board2[r][c].setText("9");
						break;


				}
			}

	}

	/*****************************************************************
	 Class to watch if any actions are performed, if so, update the
	 state of the game.
	 *****************************************************************/

	private class ButtonListener implements ActionListener {

		/*****************************************************************
		 Watch if any actions are performed, if so, update the state of
		 the game.
		 *****************************************************************/

		public void actionPerformed(ActionEvent e) {
			for (int r = 0; r < game.getBoard().length; r++)
				for (int c = 0; c < game.getBoard().length; c++)
					if (board2[r][c] == e.getSource() && game.getGameStatus() != GameStatus.SOLVED
							&& game.getGameStatus() != GameStatus.GIVE_UP && game.getGameStatus() != GameStatus.GAME_DONE) {
						if (board2[r][c].getText().equals("")){
							game.select(r, c, 0);
						}
						else if(Integer.parseInt(board2[r][c].getText()) <= 9 || Integer.parseInt(board2[r][c].getText()) >= 0){
							game.select(r, c, Integer.parseInt(board2[r][c].getText()));
						}
						else {
							board2[r][c].setText("");
							game.select(r, c, 0);
						}
                    }


			if (newGameButton == e.getSource()){
				helper = new JTextField("");
				int diff = Integer.parseInt(JOptionPane.showInputDialog(helper, "Type in a Difficulty (1 = Easy, 2 = Medium, 3 = Hard):"));
				while (diff != 2 && diff != 1 && diff != 3 && diff != 666){
					diff = Integer.parseInt(JOptionPane.showInputDialog(helper, "Type in a Difficulty (1 = Easy, 2 = Medium, 3 = Hard:"));
				}
				game = new SudokuGame(diff);
				resetBoardPanel();
			}

			else if (hintButton == e.getSource()) {
				if (game.getGameStatus() == GameStatus.IN_PROGRESS)
					game.setGameStatus(GameStatus.HINT);
				else game.setGameStatus(GameStatus.IN_PROGRESS);
			}

			else if (quitButton == e.getSource()){
				System.exit(0);
			}

			else if (game.getGameStatus() == GameStatus.GAME_DONE) {
			}

			else if (undoButton == e.getSource()){
				game.undoTurn();
			}

			else if (giveupButton == e.getSource()) {
				if (game.getGameStatus() == GameStatus.IN_PROGRESS || game.getGameStatus() == GameStatus.HINT)
					if (game.solve(game.getBoard()) && game.validboard(game.getBoard())){
						game.setGameStatus(GameStatus.GIVE_UP);
						JOptionPane.showMessageDialog(null, "Here is the solved board!\n Start a New Game to Play Again!");
					}
					else {
						game.setGameStatus(GameStatus.IN_PROGRESS);
						JOptionPane.showMessageDialog(helper, "Board Cannot be Solved from current state.");
					}
			}


			else if (game.getGameStatus() == GameStatus.SOLVED){
				JOptionPane.showMessageDialog(null, "Congrats You Win!\n Start a New Game to Play Again!");
				game.setGameStatus(GameStatus.GAME_DONE);
			}
			displayBoard();
		}
	}
}
