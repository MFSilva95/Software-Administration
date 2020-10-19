package wwwordz.puzzle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import wwwordz.puzzle.Trie.Search;
import wwwordz.shared.Configs;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Puzzle.Solution;
import wwwordz.shared.Table;
import wwwordz.shared.Table.Cell;

public class Generator {

	public final Dictionary dictionary = Dictionary.getInstance();
	private final Random random = new Random();
	
	/**
	 * Generate a high quality puzzle with many words in it
	 * @return - table
	 */
	public Puzzle generate() {
		
		Puzzle puzzle = new Puzzle();
		Table table = new Table();
		List<Cell> emptyCells = table.getEmptyCells();
		
		while(table.getEmptyCells().size() > 0) {
			Cell randomEmptyCell = emptyCells.get(random.nextInt(
					emptyCells.size()));
			char[] randomWord = dictionary.getRandomLargeWord().toCharArray();
				
			insert(table, randomEmptyCell, randomWord, 0);
		}
		puzzle.setTable(table);
		puzzle.setSolutions(getSolutions(table));
		return puzzle;
	}
	
	
	/**
	 * Function to insert a word into a cell and its neighbors 
	 * in the given table.
	 * 
	 * @param table - table 
	 * @param cell - cell to hold a letter
	 * @param word - word array that hold the letters to be placed 
	 * @param i - index of the word array
	 */
	private void insert(Table table, Cell cell, char[] word, int i) {
		cell.setLetter(word[i]);
		if(i + 1 < word.length) {
			for(Cell neighbor: table.getNeighbors(cell)) {
				if(neighbor.isEmpty() || neighbor.getLetter() == word[i+1]) {
					insert(table, neighbor, word, i+1);
					break;
				}
			}
		}
	}
	

	/**
	 * Function to generate a random puzzle of the
	 * game by building it with random letters.
	 * 
	 * @return random generated puzzle
	 */
	public Puzzle random() {
		Puzzle puzzle = new Puzzle();
		String[] words = new String[Configs.TABLE_SIZE];
		
		for(int i = 0; i < Configs.TABLE_SIZE; i++) {
			StringBuilder builder = new StringBuilder(Configs.TABLE_SIZE);
			
			for(int n = 0; n < Configs.TABLE_SIZE; n++) {
				builder.append((char) ('A' + random.nextInt('Z' - 'A')));
			}
			words[i] = builder.toString();
		}
		
		Table table = new Table(words);
		puzzle.setTable(table);
		puzzle.setSolutions(getSolutions(table));
		return puzzle;
	}

	/**
	 * Return a list of solutions for this table. 
	 * Solutions have at least 2 letters. 
	 * Different solutions for the same word are discarded.
	 * 
	 * @param table - containing solutions
	 * @return - list of solutions
	 */
	public List<Solution> getSolutions(Table table) {
		
		List<Solution> solutions = new ArrayList<>();
		Dictionary dictionary = Dictionary.getInstance();
		
		for(Cell cell: table) {
			Search search = dictionary.startSearch();
			List<Cell> visited = new ArrayList<>();
			StringBuilder builder = new StringBuilder();
			getSolutions(table, cell, search, builder, visited, solutions);
		}
		
		return solutions;
	}
	
	/**
	 * Function that searches for solutions starting in a given cell and
	 * goes through it's neighbors recursively. 
	 * 
	 * @param table - to look for @cell's neighbors
	 * @param cell - starting cell to look for solutions
	 * @param search - search done so far
	 * @param builder - builder to hold the trie word information 
	 * @param visited - cells visited
	 * @param solutions - solutions acquired so far
	 */
	private void getSolutions(Table table, Cell cell, Search search, StringBuilder builder, List<Cell> visited, List<Solution> solutions) {
		
		if(visited.contains(cell)) {
			return;
		}else {
			visited.add(cell);
		}
	
		if(!search.continueWith(cell.getLetter())) {
			return;
		}
		builder.append(cell.getLetter());
		
		String word = builder.toString();
		if(search.isWord() && word.length() >= 3 && !contains(solutions,word)) {
			Solution newSolution = new Solution(word, visited);
			solutions.add(newSolution);
		}
		
		for(Cell neighbor: table.getNeighbors(cell)) {
			List<Cell> visitedAux = new ArrayList<>(visited);
			StringBuilder builderAux = new StringBuilder(builder);
			Search searchAux = new Search(search);
			getSolutions(table, neighbor, searchAux, builderAux, visitedAux, solutions);
		}
	}
	
	/**
	 * Check if given list of solutions already contains a word
	 * @param solutions
	 * @param word
	 * @return
	 */
	private boolean contains(List<Solution> solutions, String word) {
		for(Solution solution:solutions)
			if(solution.getWord().equals(word))
				return true;
		return false;
	}
}
