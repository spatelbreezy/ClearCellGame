package model;

import java.util.Random;

/**
 * This class extends GameModel and implements the logic of the clear cell game.
 * We define an empty cell as BoardCell.EMPTY. An empty row is defined as one
 * where every cell corresponds to BoardCell.EMPTY.
 * 
 * @author Department of Computer Science, UMCP
 */

public class ClearCellGame extends Game {
	private Random random;
	private int strategy, score;

	/**
	 * Defines a board with empty cells. It relies on the super class constructor to
	 * define the board. The random parameter is used for the generation of random
	 * cells. The strategy parameter defines which clearing cell strategy to use
	 * (for this project it will be 1). For fun, you can add your own strategy by
	 * using a value different that one.
	 * 
	 * @param maxRows
	 * @param maxCols
	 * @param random
	 * @param strategy
	 */
	public ClearCellGame(int maxRows, int maxCols, Random random, int strategy) {
		super(maxRows, maxCols);
		this.random = random;
		this.strategy = strategy;
		this.score = 0;
		// Instantiates all instances variables
	}

	/**
	 * The game is over when the last board row (row with index board.length -1) is
	 * different from empty row.
	 */
	public boolean isGameOver() {
		boolean gameOver = false;
		for (int col = 0; col < board[board.length - 1].length; col++) {
			if (board[board.length - 1][col] != BoardCell.EMPTY) {
				gameOver = true;
			}
		}
		// Iterates through the entire board and checks if the bottom row is anything
		// but empty and if it is then gameOver
		return gameOver;
	}

	public int getScore() {
		return score; // Returns instance score variable
	}

	/**
	 * This method will attempt to insert a row of random BoardCell objects if the
	 * last board row (row with index board.length -1) corresponds to the empty row;
	 * otherwise no operation will take place.
	 */
	public void nextAnimationStep() {
		// Checks if game is over then makes a copy of board using same # of rows and
		// cols
		if (!isGameOver()) {
			BoardCell[][] copy = new BoardCell[maxRows][maxCols];
			// Iterates thru the entire board copying the rows, but one row lower on the new
			// (copy) board
			for (int row = 0; row < board.length - 1; row++) {
				for (int col = 0; col < board[row].length; col++) {
					copy[row + 1][col] = board[row][col];
				}
			}
			// The top row will be empty representing the new step in animation
			for (int i = 0; i < board[0].length; i++) {
				copy[0][i] = BoardCell.getNonEmptyRandomBoardCell(random);
			}
			// Changes the address of the board to the new copy version
			board = copy;
		}
	}

	public void processCell(int rowIndex, int colIndex) {
		// Checks strategy and then calls processCell auxiliary method
		if (strategy == 1) {
			processCellsAux(board, rowIndex, colIndex, board[rowIndex][colIndex]);
		}
		// Checks all rows in the board to see if they are empty
		for (int rows = 0; rows < board.length - 1; rows++) {
			if (isRowEmpty(board, rows)) {
				// If empty, then calls collapse cell method
				collapseCell(board, rows);
				setRowWithColor(board.length - 1, BoardCell.EMPTY); // Makes the bottom row empty because some rows went
																	// up leaving null as bottom row
			}
		}
	}

	private boolean isRowEmpty(BoardCell[][] array, int rowIndex) {
		int numEmpty = 0; // Counter variable
		for (int col = 0; col < array[rowIndex].length; col++) {
			// Iterates through the column counting how many empty cells
			if (array[rowIndex][col] == BoardCell.EMPTY) {
				numEmpty++;
			}
		}
		// If the counted # of empty cells is equal to the entire length of the row
		// then the row is deemed empty
		if (numEmpty == maxCols) {
			return true;
		}

		return false;

	}

	private void collapseCell(BoardCell[][] array, int rowIndex) {
		// Makes a copy of the board with same # of rows and cols
		BoardCell[][] copy = new BoardCell[maxRows][maxCols];
		// Iterates through the entire rows and columns
		for (int row = 0; row < board.length - 1; row++) {
			for (int col = 0; col < board[row].length; col++) {
				// If the current row is before the row that is supposed to be collapse then it
				// copies over normally 1:1
				// Otherwise it copies to the new board, but from +1 row from the OG board
				if (row < rowIndex) {
					copy[row][col] = board[row][col];
				} else {
					copy[row][col] = board[row + 1][col];
				}
			}
		}
		board = copy; // Changes address of board to the new copy version
	}

	private boolean isValidPair(BoardCell[][] array, int x, int y) {
		// Checks if the x,y coordinates are possible and won't throw any errors (no
		// negatives and inside the board)
		if (x > array.length - 1 || x < 0 || y > array[x].length - 1 || y < 0) {
			return false;
		}
		return true;
	}

	private void processCellsAux(BoardCell[][] array, int x, int y, BoardCell target) {
		// Checks if the game is over and allows the process to occur if it is not over
		if (!isGameOver()) {
			// Checks if the x,y coordinates are possible
			if (isValidPair(array, x, y)) {
				// Checks if the x,y coordinates are target
				if (array[x][y] == target) {
					// Changes it to empty
					array[x][y] = BoardCell.EMPTY;
					score++; // Updates score
					// Calls recursive process going in all directions
					// Diagonally, horizontally, vertically, etc
					processUp(array, x - 1, y, target);
					processDown(array, x + 1, y, target);
					processLeft(array, x, y - 1, target);
					processRight(array, x, y + 1, target);

					processTopLeft(array, x - 1, y - 1, target);
					processTopRight(array, x - 1, y + 1, target);
					processBottomLeft(array, x + 1, y - 1, target);
					processBottomRight(array, x + 1, y + 1, target);
				}
			}
		}
	}

	// All these private recursive methods run the same, but continue to recursively
	// go in the same direction
	// checking if the next coordinate is supposed to be changed to empty or not
	// based on if it is the same as the OG target coordinate
	// Also updates the score variable
	private void processUp(BoardCell[][] array, int x, int y, BoardCell target) {
		if (isValidPair(array, x, y)) {
			if (array[x][y] == target) {
				array[x][y] = BoardCell.EMPTY;
				score++;
				processUp(array, x - 1, y, target);
			}
		}
	}

	private void processDown(BoardCell[][] array, int x, int y, BoardCell target) {
		if (isValidPair(array, x, y)) {
			if (array[x][y] == target) {
				array[x][y] = BoardCell.EMPTY;
				score++;
				processDown(array, x + 1, y, target);
			}
		}
	}

	private void processLeft(BoardCell[][] array, int x, int y, BoardCell target) {
		if (isValidPair(array, x, y)) {
			if (array[x][y] == target) {
				array[x][y] = BoardCell.EMPTY;
				score++;
				processLeft(array, x, y - 1, target);
			}
		}
	}

	private void processRight(BoardCell[][] array, int x, int y, BoardCell target) {
		if (isValidPair(array, x, y)) {
			if (array[x][y] == target) {
				array[x][y] = BoardCell.EMPTY;
				score++;
				processRight(array, x, y + 1, target);
			}
		}
	}

	private void processTopLeft(BoardCell[][] array, int x, int y, BoardCell target) {
		if (isValidPair(array, x, y)) {
			if (array[x][y] == target) {
				array[x][y] = BoardCell.EMPTY;
				score++;
				processTopLeft(array, x - 1, y - 1, target);
			}
		}
	}

	private void processTopRight(BoardCell[][] array, int x, int y, BoardCell target) {
		if (isValidPair(array, x, y)) {
			if (array[x][y] == target) {
				array[x][y] = BoardCell.EMPTY;
				score++;
				processTopRight(array, x - 1, y + 1, target);
			}
		}
	}

	private void processBottomLeft(BoardCell[][] array, int x, int y, BoardCell target) {
		if (isValidPair(array, x, y)) {
			if (array[x][y] == target) {
				array[x][y] = BoardCell.EMPTY;
				score++;
				processBottomLeft(array, x + 1, y - 1, target);
			}
		}
	}

	private void processBottomRight(BoardCell[][] array, int x, int y, BoardCell target) {
		if (isValidPair(array, x, y)) {
			if (array[x][y] == target) {
				array[x][y] = BoardCell.EMPTY;
				score++;
				processBottomRight(array, x + 1, y + 1, target);
			}
		}
	}

}