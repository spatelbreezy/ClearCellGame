package tests;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import model.BoardCell;
import model.ClearCellGame;
import model.Game;

/* The following directive executes tests in sorted order */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class StudentTests {
	
	@Test
	public void testingGameClass() {
		Game game = new ClearCellGame(10, 10, new Random(1L), 1);
		String rowsAndCols = game.getMaxCols() + " " + game.getMaxRows();
		assertTrue(rowsAndCols.equals("10 10"));
		//Rows and cols in this should be 10 each
		game.setBoardWithColor(BoardCell.YELLOW);
		game.setColWithColor(1, BoardCell.RED);
		game.setRowWithColor(0, BoardCell.GREEN);
		game.setBoardCell(0, 0, BoardCell.EMPTY);
		String board = getBoardStr(game);
		String expected = "Board(Rows: 10, Columns: 10)\n"
				+ ".GGGGGGGGG\n"
				+ "YRYYYYYYYY\n"
				+ "YRYYYYYYYY\n"
				+ "YRYYYYYYYY\n"
				+ "YRYYYYYYYY\n"
				+ "YRYYYYYYYY\n"
				+ "YRYYYYYYYY\n"
				+ "YRYYYYYYYY\n"
				+ "YRYYYYYYYY\n"
				+ "YRYYYYYYYY\n";
		//Checks if the correct colors are printed for the board
		assertEquals(board, expected);
		
	
	}
	
	@Test
	public void testingClearCellGameClass() {
		int maxRows = 8, maxCols = 8, strategy = 1;
		Game ccGame = new ClearCellGame(maxRows, maxCols, new Random(1L), strategy);
		
		
		//Randomly setting the board
		ccGame.setBoardWithColor(BoardCell.BLUE);
		ccGame.setRowWithColor(maxRows - 1, BoardCell.EMPTY);
		ccGame.setRowWithColor(1, BoardCell.YELLOW);
		ccGame.setBoardCell(1, maxCols - 1, BoardCell.RED);
		ccGame.setColWithColor(1, BoardCell.YELLOW);
		ccGame.setBoardCell(maxRows - 1, 1, BoardCell.EMPTY);
		
		assertTrue(ccGame.isGameOver() == false);
		//Game should not be over 
		
		
		ccGame.processCell(1, 1);
		String after = getBoardStr(ccGame);
		String expected = "Board(Rows: 8, Columns: 8)\n"
				+ "B.BBBBBB\n"
				+ ".......R\n"
				+ "B.BBBBBB\n"
				+ "B.BBBBBB\n"
				+ "B.BBBBBB\n"
				+ "B.BBBBBB\n"
				+ "B.BBBBBB\n"
				+ "........\n";
		assertEquals(ccGame.getScore(), 13);
		//Checks if the clearing rules apply correctly
		assertEquals(after, expected);
		ccGame.setBoardCell(7, 0, BoardCell.GREEN);
		assertTrue(ccGame.isGameOver() == true);
		ccGame.setBoardCell(7, 0, BoardCell.EMPTY);
		//Checking if the bottom row is changed to something not empty than 
		//game over == true
		
		String expected2 = "java.lang.ArrayIndexOutOfBoundsException: Index 10 out of bounds for length 8";
		try {
			ccGame.processCell(10, 10);
		} catch (Exception e) {
			String error = e.toString();
			assertEquals(error, expected2);
		}
		//Should have thrown error^
		
		ccGame.processCell(3, 3);
		String expected3 = "Board(Rows: 8, Columns: 8)\n"
				+ "B.BBBBBB\n"
				+ ".......R\n"
				+ "B....BBB\n"
				+ "B.......\n"
				+ "B....BBB\n"
				+ "B.B.B.BB\n"
				+ "B.B.BB.B\n"
				+ "........\n";
		assertEquals(expected3, getBoardStr(ccGame));
		
		ccGame.setRowWithColor(3, BoardCell.EMPTY);
		ccGame.processCell(3, 7);
		//Collapses row 3 because it was set to empty
		String expected4 = "Board(Rows: 8, Columns: 8)\n"
				+ "B.BBBBBB\n"
				+ ".......R\n"
				+ "B....BBB\n"
				+ "B....BBB\n"
				+ "B.B.B.BB\n"
				+ "B.B.BB.B\n"
				+ "........\n"
				+ "........\n";
		assertEquals(expected4, getBoardStr(ccGame));
	}


	/* Support methods */
	private static String getBoardStr(Game game) {
		int maxRows = game.getMaxRows(), maxCols = game.getMaxCols();

		String answer = "Board(Rows: " + maxRows + ", Columns: " + maxCols + ")\n";
		for (int row = 0; row < maxRows; row++) {
			for (int col = 0; col < maxCols; col++) {
				answer += game.getBoardCell(row, col).getName();
			}
			answer += "\n";
		}

		return answer;
	}
}
