package wwwordz.puzzle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class Trie implements Iterable<String>{
	Node root = new Node();
	
	private class Node extends HashMap<Character, Node>{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private boolean isWord = false;
		
		Node(){
			isWord = false;
		}
		
		private void put(String name, int index) {
			
			if(index == name.length()) {
				isWord = true;
			}else {
				char head = name.charAt(index);
				Node curr;
				
				if(this.containsKey(head)) {
					curr = this.get(head);
				}else {
					curr = new Node();
					this.put(head, curr);
				}
				curr.put(name, index+1);
			}
		}
		
		/**
		 * Generates a random word by going through a trie
		 * 
		 * @param builder - string builder to hold the word
		 */
		void collectRandomLargeWord(StringBuilder builder) {
			
			Random random = new Random();
			ArrayList<Character> chars = new ArrayList<Character>(keySet());
			
			if(chars.size() == 0)
				return;
			else {
				char letter = chars.get(random.nextInt(chars.size()));
				builder.append(letter);
				this.get(letter).collectRandomLargeWord(builder);
			}
		
		}	
		
	}
	
	/**
	 * Iterator over strings stored in the internal node structure 
	 * It traverses the node tree depth first, 
	 * using coroutine with threads, 
	 * and collects all possible words in no particular order. 
	 * An instance of this class is returned by iterator()
	 * 
	 */
	class NodeIterator extends Node implements Runnable, Iterator<String>{
		
		private static final long serialVersionUID = 1L;
		private String	nextWord = null;
		private boolean	terminated = false;
		private Thread	thread;
		
		public NodeIterator() {
			terminated = false;
			nextWord = null;
			thread = new Thread(this, "Node Iterator");
			thread.start();
		}
		
		@Override
		public String next() {
			String newWord = this.nextWord;
			synchronized (this) {
				nextWord = null;
			}
			return newWord;
		}
		
		@Override
		public boolean hasNext() {
			synchronized (this) {
				if(!terminated) {
					handshake();					
				}
			}
			return nextWord != null;
		}
		
		@Override
		public void run() {
			this.terminated = false;
			StringBuilder stringBuilder = new StringBuilder();
			visitValues(root, stringBuilder);
			synchronized (this) {
				this.terminated = true;
				handshake();
			}
		}

		/**
		 * Recursive function to lookup the trie structure
		 * 
		 * @param root - start of trie
		 * @param stringBuilder - string builder to hold vales stored in the trie
		 */
		private void visitValues(Node root, StringBuilder stringBuilder) {
			
			if(root == null) {
				return;
			}
			if(root.isWord == true) {
				nextWord = stringBuilder.toString();
				synchronized (this) {
					handshake();
				}
			}
			
			ArrayList<Character> chars = new ArrayList<Character>(root.keySet());
			
			for(int i = 0; i < chars.size(); i++) {
				if(root != null) {
					char letter = chars.get(i);
					StringBuilder newStringBuilder = new StringBuilder(stringBuilder);
					newStringBuilder.append(letter);
					visitValues(root.get(letter), newStringBuilder);
				}	
			}
			
		}

		private void handshake() {
			notify();
			try {
				wait();
			} catch (InterruptedException cause) {
				throw new RuntimeException("Unexpected interruption while waiting", cause);
			}
		}
		
	}
	
	public static class Search{
		
		Node node;
		
		/**
		 * Create a search starting in given node
		 * @param node - prefix already searched
		 */
		public Search(Node node) {
			this.node = node;
		}
		
		/**
		 * Create a clone of the given search, with the same fields.
		 * @param search - to be cloned
		 */
		public Search(Search search) {
			this.node = search.node;
		}
		
		/**
		 * Check if the search can continue with the given letter. 
		 * Internal node is updated if the search is valid.
		 * @param letter - to continue search
		 * @return true if letter found; false otherwise
		 */
		boolean continueWith(char letter) {
			if(node == null)
				return false;
			if(node.containsKey(letter))
				node = node.get(letter);
			else
				node = null;
			return node != null;
		}
	
		/**
		 * Check if characters searched so far correspond to a word
		 * 
		 * @return - true if node is a complete word; false otherwise
		 */
		boolean isWord() {
			return node != null && node.isWord;
		}
		
	}
	
	/**
	 * Insert a word in the structure, starting from the root.
	 * @param word - to be inserted
	 */
	public void put(String word) {
		root.put(word, 0);
	}
	
	/**
	 * Start a word search from the root.
	 * @return
	 */
	public Search startSearch() {
		return new Search(root);
	}

	/**
	 * Performs a random walk in the data structure, 
	 * randomly selecting a path in each node, 
	 * until reaching a leaf (a node with no descendants).
	 * @return word as a String
	 */
	public String getRandomLargeWord() {
		StringBuilder stringBuilder = new StringBuilder();
		root.collectRandomLargeWord(stringBuilder);
		return stringBuilder.toString();
	}

	/**
	 *Returns an iterator over the strings stored in the trie
	 */
	@Override
	public Iterator<String> iterator() {
		return new NodeIterator();
	}
}
