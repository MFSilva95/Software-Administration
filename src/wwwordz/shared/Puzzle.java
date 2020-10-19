package wwwordz.shared;

import java.io.Serializable;
import java.util.List;
import wwwordz.shared.Table.Cell;

public class Puzzle implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Table table;
	List<Solution> solutions;
	
	public static class Solution{
		
		String word;
		List<Cell> cells;
		
		public Solution() {
			
		}

		public Solution(String word, List<Cell> cells) {
			super();
			this.word = word;
			this.cells = cells;
		}

		public String getWord() {
			return word;
		}

		public void setWord(String word) {
			this.word = word;
		}

		public List<Cell> getCells() {
			return cells;
		}

		public void setCells(List<Cell> cells) {
			this.cells = cells;
		}
		
		/**
		 * get points of this word
		 * @return points
		 */
		public int getPoints() {
			int points = 0;
			if(this.word.length() < 3) {
				return points;
			}else if(this.word.length() == 3) {
				points = 1;
				return points;
			}else if(this.word.length() > 3) {
				points = 1;
				for(int i = 1; i < this.word.length() ; i++) {
					points = 2*points + 1;	
				}
			}
			
			return points;
		}
		
		
	}
	

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public List<Solution> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<Solution> solutions) {
		this.solutions = solutions;
	}
	
	
	
}
