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
import java.io.Serializable;
import java.util.*;

public class SudokuPanel extends JFrame
		implements ActionListener, Serializable {

	/* All the buttons the boards */
	private JTextField[][] board2;
	
	/* Temporay board to work with in memory for the panel */
	private int[][] iBoard;

	/* Button to Quit Game */
	private final JButton quitButton;
	
	/* Textfield for inside prompts */
	private JTextField helper;

	/* Undo Button */
	private final JButton undoButton;
	
	/* Hint Button */
	private final JButton hintButton;
	
	/* Give Up Button */
	private final JButton giveupButton;
	
	/* New Game Button */
	private final JButton newGameButton;

	/* Panel for the clock */
	private JPanel clock;

	/* Holds menu bar */
	private JMenuBar menus;
	
	/* Pull down menu */
	private JMenu fileMenu;
	
	/* Load Game */
	private JMenuItem openSerItem;

	/* Sudoku Game */
	private SudokuGame game;
	
	/* Save Game */
	private SavedGame save;

	/* Size of the Sudoku Board */
	private final int BOARD_SIZE = 9;

	/* Timer for Sudoku Game */
	Clock.SimpleClock timer = new Clock.SimpleClock();
	
	
	//choose filename ensures randomness
	//probably do it with a clock instead
	/* Filename for saving or loading Sudoku game */
	String fileName = "SudokuGame" + (Calendar.getInstance().get(Calendar.YEAR)) + "-" + (Calendar.getInstance().get(Calendar.MONTH)) + "-"
			+ (Calendar.getInstance().get(Calendar.DATE)) + "-" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) + "-"
			+ (Calendar.getInstance().get(Calendar.MINUTE)) + ".ser";
	
	/* Leaderboard Text */
	String leaderString = "LeaderBoardSave.ser";
	
	/* Leaderboard List */
	private ArrayList<String> LeaderBoard;

	/*****************************************************************
	 Constructor creates a game of sudoku with the selected difficulty
	 *****************************************************************/

	public SudokuPanel() {

		helper = new JTextField("");
		int diff = 0;
		try {
			diff = Integer.parseInt(JOptionPane.showInputDialog(helper,
					"Type in a Difficulty (1 = Easy, 2 = Medium, 3 = Hard):"));
		}
		catch (Exception e){
		}

		while (diff != 2 && diff != 1 && diff != 3 && diff != 666){
			try {
				diff = Integer.parseInt(JOptionPane.showInputDialog(helper,
						"Type in a Difficulty (1 = Easy, 2 = Medium, 3 = Hard):"));
			}
			catch (Exception e){

			}
		}

		game = new SudokuGame(diff);
		save = new SavedGame();

		if ((LeaderBoard = (ArrayList<String>) save.load(leaderString)) == null){
			LeaderBoard = new ArrayList<String>();
		}


		quitButton = new JButton("Quit Game");
		undoButton = new JButton("Undo");
		hintButton = new JButton("Hint");
		giveupButton = new JButton("Give Up");
		newGameButton = new JButton("New Game");

		menus = new JMenuBar();
		fileMenu = new JMenu("Load");
		openSerItem = new JMenuItem("Load Game");

		fileMenu.add(openSerItem);
		menus.add(fileMenu);
		openSerItem.addActionListener(this);
		setJMenuBar(menus);

		clock = new JPanel();

		this.setTitle(fileName);
		initBoardPanel();
		displayBoard();

	}

	/*****************************************************************
	 Initialize GUI with the board given by the randomly generated
	 board from the constructor.
	 *****************************************************************/

	private void initBoardPanel () {

		timer = new Clock.SimpleClock();
		clock.add(timer);

		JPanel center = new JPanel();
		JPanel bottom = new JPanel();
		// create game, listeners
		//ButtonListener listener = new ButtonListener();

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
				board2[row][col].addActionListener(this);
				center.add(board2[row][col]);
			}

			// add all to contentPane
			add (center, BorderLayout.CENTER);
			add (bottom, BorderLayout.SOUTH);
			add (clock, BorderLayout.NORTH);

		undoButton.addActionListener(this);
		quitButton.addActionListener(this);
		hintButton.addActionListener(this);
		giveupButton.addActionListener(this);
		newGameButton.addActionListener(this);

		bottom.add (newGameButton);
		bottom.add (undoButton);
		bottom.add (hintButton);
		bottom.add (giveupButton);
		bottom.add (quitButton);
	}

	/*****************************************************************
	 Sets the correct buttons to editable Jtextfields
	 *****************************************************************/

	private  void setEditable(){
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				board2[row][col].setEditable(false);
				if (game.getInitBoard()[row][col] == 1) {
					board2[row][col].setEditable(true);
				}
			}
		}
	}

	/*****************************************************************
	 Sets all board text fields to empty.
	 *****************************************************************/

	private void resetBoardPanel(){
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				board2[row][col].setEditable(true);
				board2[row][col].setText("");
				if (game.getBoard()[row][col] != 0) {
					board2[row][col].setEditable(false);
				}
			}
		}
		timer.restartTimer();
		timer.resetClock();
	}

	/*****************************************************************
	 Update and Display the text fields when an action is executed
	 *****************************************************************/

	private void displayBoard() {
		iBoard = game.getBoard();

		for (int r = 0; r < game.getBoard().length; r++)
			for (int c = 0; c < game.getBoard().length; c++) {
				if (game.getGameStatus() == GameStatus.HINT) {
					if (iBoard[r][c] == 0) {
						board2[r][c].setBackground(Color.white);
					}
					if (game.legalMove(r, c, iBoard[r][c]) && iBoard[r][c] != 0) {
						board2[r][c].setBackground(Color.green);
					} else if (iBoard[r][c] != 0) {
						board2[r][c].setBackground(Color.red);
					}
				} else if (game.getGameStatus() != GameStatus.GAME_DONE || game.getGameStatus() != GameStatus.GIVE_UP) {
					board2[r][c].setBackground(Color.white);
				}
				switch (iBoard[r][c]) {
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
		if (game.getGameStatus() != GameStatus.GAME_DONE && game.getGameStatus() != GameStatus.GIVE_UP){
			save.save(fileName, game);
		}
	}

	/*****************************************************************
	 Class to watch if any actions are performed, if so, update the
	 state of the game.
	 *****************************************************************/

	@Override
	public void actionPerformed(ActionEvent e) {

		if (openSerItem == e.getSource()) {
			JFileChooser chooser = new JFileChooser();
			int status = chooser.showOpenDialog(null);
			if (status == JFileChooser.APPROVE_OPTION) {
				String filename = chooser.getSelectedFile().getAbsolutePath();
				if (openSerItem == e.getSource()){
					game = (SudokuGame) save.load(filename);
					fileName = filename;
					setEditable();
				}
			}
		}

		for (int r = 0; r < game.getBoard().length; r++)
			for (int c = 0; c < game.getBoard().length; c++)
				if (board2[r][c] == e.getSource() && game.getGameStatus() != GameStatus.SOLVED
						&& game.getGameStatus() != GameStatus.GIVE_UP && game.getGameStatus() != GameStatus.GAME_DONE) {
					try{
						if (board2[r][c].getText().equals("")){
							game.select(r, c, 0);
						}
						else if(Integer.parseInt(board2[r][c].getText()) <= 9 && Integer.parseInt(board2[r][c].getText()) > 0){
							game.select(r, c, Integer.parseInt(board2[r][c].getText()));
						}
						else {
							board2[r][c].setText("");
							game.select(r, c, 0);
						}
					}
					catch(Exception err){
						board2[r][c].setText("");
					}
				}

		if (newGameButton == e.getSource()){
			helper = new JTextField("");
			int diff = 0;
			try {
				diff = Integer.parseInt(JOptionPane.showInputDialog(helper,
						"Type in a Difficulty (1 = Easy, 2 = Medium, 3 = Hard):"));
			}
			catch (Exception er){
			}

			while (diff != 2 && diff != 1 && diff != 3 && diff != 666){
				try {
					diff = Integer.parseInt(JOptionPane.showInputDialog(helper,
							"Type in a Difficulty (1 = Easy, 2 = Medium, 3 = Hard):"));
				}
				catch (Exception er){

				}
			}
			game = new SudokuGame(diff);
			resetBoardPanel();
		}

		else if (quitButton == e.getSource()){
			System.exit(0);
		}

		else if (game.getGameStatus() == GameStatus.GAME_DONE);

		else if (hintButton == e.getSource()) {
			if (game.getGameStatus() == GameStatus.IN_PROGRESS)
				game.setGameStatus(GameStatus.HINT);
			else game.setGameStatus(GameStatus.IN_PROGRESS);
		}

		else if (undoButton == e.getSource()){
			game.undoTurn();
		}

		else if (giveupButton == e.getSource()) {
			if (game.getGameStatus() == GameStatus.IN_PROGRESS || game.getGameStatus() == GameStatus.HINT)
				if (game.solve(game.getBoard()) && game.validboard(game.getBoard())){
					game.setGameStatus(GameStatus.GIVE_UP);
					timer.stopTimer();
					displayBoard();
					game.setGameStatus(GameStatus.GAME_DONE);
					JOptionPane.showMessageDialog(null, "Here is the solved board!\n Start a New Game to Play Again!");
				}
				else {
					game.setGameStatus(GameStatus.IN_PROGRESS);
					JOptionPane.showMessageDialog(helper, "Board Cannot be Solved from current state.");
				}
		}


		else if (game.getGameStatus() == GameStatus.SOLVED){
			LeaderBoard.add(timer.getStringTime());
			timer.stopTimer();
			timer.resetClock();
			save.save(leaderString, LeaderBoard);
			Collections.sort(LeaderBoard);
			String x = "";
			for (int i = 0; i< LeaderBoard.size() && i < 10; i++){
				x += i + 1 + ": " + LeaderBoard.get(i) + "\n";
			}
			JOptionPane.showMessageDialog(null, "Congrats You Win!\n Start a New Game to Play Again!\n LeaderBoards: \n" + x);
			game.setGameStatus(GameStatus.GAME_DONE);
		}
		if (game.isFilledBoard(game.getBoard()) && (game.getGameStatus() != GameStatus.GAME_DONE)){
			JOptionPane.showMessageDialog(null,"You Filled out the board, but the board is not correect!");
		}
		displayBoard();
	}

}
