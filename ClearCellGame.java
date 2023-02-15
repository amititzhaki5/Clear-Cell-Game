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
	private int score, strategy;

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
	}

	/**
	 * The game is over when the last board row (row with index board.length -1) is
	 * different from empty row.
	 */
	public boolean isGameOver() {
		int differences = 0;
		int lastRow = board.length - 1;
		for (int col = 0; col < board[lastRow].length; col++) {
			if (board[lastRow][col] != BoardCell.EMPTY) {
				differences++;
			}
		}
		if (differences > 0) {
			return true;
		}
		return false;
	}

	public int getScore() {
		return score;
	}

	/**
	 * This method will attempt to insert a row of random BoardCell objects if the
	 * last board row (row with index board.length -1) corresponds to the empty row;
	 * otherwise no operation will take place.
	 */
	public void nextAnimationStep() {
		nextAnimationStepAux();
	}

	/**
	 * This method will turn to BoardCell.EMPTY the cell selected and any adjacent
	 * surrounding cells in the vertical, horizontal, and diagonal directions that
	 * have the same color. The clearing of adjacent cells will continue as long as
	 * cells have a color that corresponds to the selected cell. Notice that the
	 * clearing process does not clear every single cell that surrounds a cell
	 * selected (only those found in the vertical, horizontal or diagonal
	 * directions).
	 * 
	 * IMPORTANT: Clearing a cell adds one point to the game's score.<br />
	 * <br />
	 * 
	 * If after processing cells, any rows in the board are empty,those rows will
	 * collapse, moving non-empty rows upward. For example, if we have the following
	 * board (an * represents an empty cell):<br />
	 * <br />
	 * RRR<br />
	 * GGG<br />
	 * YYY<br />
	 * * * *<br/>
	 * <br />
	 * then processing each cell of the second row will generate the following
	 * board<br />
	 * <br />
	 * RRR<br />
	 * YYY<br />
	 * * * *<br/>
	 * * * *<br/>
	 * <br />
	 * IMPORTANT: If the game has ended no action will take place.
	 * 
	 * 
	 */
	public void processCell(int rowIndex, int colIndex) {
		BoardCell color = board[rowIndex][colIndex];
		horizontalProcessCell(rowIndex, colIndex, color);
		verticalProcessCell(rowIndex, colIndex, color);
		diagonalProcessCell(rowIndex, colIndex, color);
		board[rowIndex][colIndex] = BoardCell.EMPTY;
		score++;
		collapse();
	}

	private void nextAnimationStepAux() {
		if (!isGameOver()) {
			shiftedDownOneRow();
			for (int col = 0; col < board[0].length; col++) {
				board[0][col] = BoardCell.getNonEmptyRandomBoardCell(random);
			}
		}
	}

	private BoardCell[][] shiftedDownOneRow() {
		int k = 1;
		BoardCell[][] newBoard = new BoardCell[board.length][board[0].length];
		for (int row = 0; row < board.length - 1; row++) {
			newBoard[k++] = board[row];
		}
		this.board = newBoard;
		return newBoard;
	}

	private void horizontalProcessCell(int rowIndex, int colIndex, BoardCell color) {
		for (int i = colIndex + 1; i < board[0].length; i++) {
			if (board[rowIndex][i] != color)
				break;
			if (board[rowIndex][i] == color) {
				board[rowIndex][i] = BoardCell.EMPTY;
				score++;
			}
		}
		for (int i = colIndex - 1; i >= 0; i--) {
			if (board[rowIndex][i] != color)
				break;
			if (board[rowIndex][i] == color) {
				board[rowIndex][i] = BoardCell.EMPTY;
				score++;
			}
		}
	}

	private void verticalProcessCell(int rowIndex, int colIndex, BoardCell color) {
		for (int i = rowIndex + 1; i < board.length; i++) {
			if (board[i][colIndex] != color)
				break;
			if (board[i][colIndex] == color) {
				board[i][colIndex] = BoardCell.EMPTY;
				score++;
			}
		}
		for (int i = rowIndex - 1; i >= 0; i--) {
			if (board[i][colIndex] != color)
				break;
			if (board[i][colIndex] == color) {
				board[i][colIndex] = BoardCell.EMPTY;
				score++;
			}
		}
	}

	private void diagonalProcessCell(int rowIndex, int colIndex, BoardCell color) {
		int originalRow = rowIndex;
		int originalCol = colIndex;
		int highestRowIndex = board.length - 1;
		int highestColIndex = board[0].length - 1;

		int downRightMinValue = rowIndex > colIndex ? rowIndex : colIndex;
		int downRightMaxValue = rowIndex > colIndex ? highestRowIndex : highestColIndex;
		int currentRow1 = rowIndex + 1;
		int currentCol1 = colIndex + 1;
		if (rowIndex != board.length - 1 && colIndex != board[0].length - 1) {
			for (int i = downRightMinValue; i < downRightMaxValue; i++) {
				if (board[currentRow1][currentCol1] != color)
					break;
				if (board[currentRow1][currentCol1] == color) {
					board[currentRow1][currentCol1] = BoardCell.EMPTY;
					score++;
					currentRow1++;
					currentCol1++;
				}
			}
		}
		rowIndex = originalRow;
		colIndex = originalCol;
		int downLeftMinValue = highestRowIndex - rowIndex < colIndex ? rowIndex : 0;
		int downLeftMaxValue = highestRowIndex - rowIndex < colIndex ? highestRowIndex : colIndex;
		int currentRow2 = rowIndex + 1;
		int currentCol2 = colIndex - 1;
		if (rowIndex != board.length - 1 && colIndex != 0) {
			for (int i = downLeftMinValue; i < downLeftMaxValue; i++) {
				if (board[currentRow2][currentCol2] != color)
					break;
				if (board[currentRow2][currentCol2] == color) {
					board[currentRow2][currentCol2] = BoardCell.EMPTY;
					score++;
					currentRow2++;
					currentCol2--;
				}
			}
		}
		rowIndex = originalRow;
		colIndex = originalCol;
		int upRightMinValue = rowIndex < highestColIndex - colIndex ? 0 : colIndex;
		int upRightMaxValue = rowIndex < highestColIndex - colIndex ? rowIndex : highestColIndex;
		int currentRow3 = rowIndex - 1;
		int currentCol3 = colIndex + 1;
		if (rowIndex != 0 && colIndex != board[0].length - 1) {
			for (int i = upRightMinValue; i < upRightMaxValue; i++) {
				if (board[currentRow3][currentCol3] != color)
					break;
				if (board[currentRow3][currentCol3] == color) {
					board[currentRow3][currentCol3] = BoardCell.EMPTY;
					score++;
					currentRow3--;
					currentCol3++;
				}
			}
		}
		rowIndex = originalRow;
		colIndex = originalCol;
		int UpLeftMaxValue = rowIndex > colIndex ? colIndex : rowIndex;
		int currentRow4 = rowIndex - 1;
		int currentCol4 = colIndex - 1;
		if (rowIndex != 0 && colIndex != 0) {
			for (int i = 0; i < UpLeftMaxValue; i++) {
				if (board[currentRow4][currentCol4] != color)
					break;
				if (board[currentRow4][currentCol4] == color) {
					board[currentRow4][currentCol4] = BoardCell.EMPTY;
					score++;
					currentRow4--;
					currentCol4--;
				}
			}
		}
	}

	private void collapse() {
		int currentRow = 0;
		while ((currentRow < board.length - 1)) {
			if (indexOfEmptyRow(currentRow) != -1) {
				BoardCell[][] newBoard = new BoardCell[board.length][board[0].length];
				for (int row = 0, k = 0; row < indexOfEmptyRow(currentRow); row++) {
					newBoard[row] = board[k++];
				}
				for (int currentIndex = indexOfEmptyRow(currentRow); currentIndex < board.length - 1; currentIndex++) {
					newBoard[currentIndex] = board[currentIndex + 1];
				}
				for (int col = 0; col < board[0].length; col++) {
					newBoard[board.length - 1][col] = BoardCell.EMPTY;
				}
				this.board = newBoard;
			}
			currentRow++;
		}
	}

	private int indexOfEmptyRow(int currentRow) {
		int count = 0;
		for (int col = 0; col < board[0].length; col++) {
			if (board[currentRow][col] == BoardCell.EMPTY) {
				count++;
			}
		}
		if (count == board[0].length) {
			return currentRow;
		}
		return -1;
	}
}