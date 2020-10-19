package wwwordz.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class Table implements Iterable<Table.Cell>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A cell in the enclosing table
	 * @author Grandsons
	 *
	 */
	public static class Cell implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		int column;
		int row;
		char letter;
		 
		public Cell() {}
		
		/**
		 * Create a cell with letter at given row and column
		 * 
		 * @param row
		 * @param column
		 * @param letter
		 */
		public Cell(int row, int column, char letter) {
			this.row = row;
			this.column = column;
			this.letter = letter;
		}
		
		/**
		 * Create an empty cell at the given row an column
		 * 
		 * @param row
		 * @param column
		 */
		public Cell(int row, int column) {
			this.row = row;
			this.column = column;
			this.letter = ' ';
		}

		/**
		 * Check if cell is empty
		 * @return
		 */
		public boolean isEmpty() {
			if(this.letter == ' ') {
				return true;
			}
			return false;
		}
		
		/**
		 * Letter in this cell
		 * @return
		 */
		public char getLetter() {
			return letter;
		}

		/**
		 * Change letter in this cell
		 * @param letter
		 */
		public void setLetter(char letter) {
			this.letter = letter;
		}

		@Override
		public String toString() {
			return "Cell [column=" + column + ", row=" + row + ", letter=" + letter + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + column;
			result = prime * result + letter;
			result = prime * result + row;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Cell other = (Cell) obj;
			if (column != other.column)
				return false;
			if (letter != other.letter)
				return false;
			if (row != other.row)
				return false;
			return true;
		}

	}
	
	@Override
	public Iterator<Table.Cell> iterator() {
		return new CellIterator();
	}
	
	private class CellIterator implements Iterator<Table.Cell>{

		private int col = 0;
		private int row = 0;
		
		@Override
		public boolean hasNext() {
			if(col > Configs.TABLE_SIZE-1 || row > Configs.TABLE_SIZE-1) {
				return false;				
			}else {
				return true;
			}
		}

		@Override
		public Cell next() {
			Cell curr = table[row][col];
			col++;
			if(col > Configs.TABLE_SIZE-1) {
				row++;
				col = 0;
			}
			return curr;
		}
		
	}
	
	private Cell table[][];
	
	/**
	 * Create a table with empty cells
	 */
	public Table() {
		
		table = new Cell[Configs.TABLE_SIZE][Configs.TABLE_SIZE];
		
		for(int row = 0; row < Configs.TABLE_SIZE; row++) {
			for(int col = 0; col < Configs.TABLE_SIZE; col++) {
				table[row][col] = new Cell(row, col, ' ');
			}
		}
		
	}

	/**
	 * Create a table with given data
	 * 
	 * @param data
	 */
	public Table(String[] data) {
		
		table = new Cell[Configs.TABLE_SIZE][Configs.TABLE_SIZE];
		for(int row = 0; row < Configs.TABLE_SIZE; row++) {
			for(int col = 0; col < Configs.TABLE_SIZE; col++) {
				table[row][col] = new Cell(row, col, data[row].charAt(col));
			}
		}
		
	}

	/**
	 * Get cell of given row and column
	 * 
	 * @param line
	 * @param column
	 * @return
	 */
	public Cell getCell(int line, int column) {
		return table[line-1][column-1];
	}

	/**
	 * The 8 neighboring cells of the given cell.
	 * 
	 * @param cell
	 * @return
	 */
	public List<Cell> getNeighbors(Cell cell) {
		List<Cell> neighbors = new ArrayList<>();
		
		int row = cell.row;
		int col = cell.column;
		
		if(row - 1 < 0) {} else neighbors.add(table[row-1][col]);
		if(row + 1 > Configs.TABLE_SIZE-1) {} else neighbors.add(table[row+1][col]);
		
		if(col - 1 < 0) {} else neighbors.add(table[row][col-1]);
		if(col + 1 > Configs.TABLE_SIZE-1) {} else neighbors.add(table[row][col+1]);
		
		
		if(row - 1 < 0 || col - 1 < 0) {} else neighbors.add(table[row-1][col-1]);
		if(row - 1 < 0 || col + 1 > Configs.TABLE_SIZE-1) {} else neighbors.add(table[row-1][col+1]);
		if(row + 1 > Configs.TABLE_SIZE-1  || col - 1 < 0) {} else neighbors.add(table[row+1][col-1]);
		if(row + 1 > Configs.TABLE_SIZE-1 || col + 1 > Configs.TABLE_SIZE-1) {} else neighbors.add(table[row+1][col+1]);
			
		return neighbors;
	}

	@Override
	public String toString() {
		return "Table [table=" + Arrays.toString(table) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(table);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Table other = (Table) obj;
		if (!Arrays.deepEquals(table, other.table))
			return false;
		return true;
	}
	
	/**
	 * Return a list of empty cells in this table
	 * @return - list of cells
	 */
	public List<Cell> getEmptyCells() {
		List<Cell> emptyCells = new ArrayList<>();
		for(int line=0; line < Configs.TABLE_SIZE; line++)
			for(int column=0; column < Configs.TABLE_SIZE; column++)
				if(table[line][column].letter == ' ')
					emptyCells.add(table[line][column]);
		return emptyCells;
	} 

}
