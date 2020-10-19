package wwwordz.puzzle;
import wwwordz.puzzle.Trie;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.regex.Pattern;

public class Dictionary extends java.lang.Object{
	
	private static final Dictionary dictionary = new Dictionary();
	private Trie trie = new Trie(); 
	final String  DIC_FILE = "wwwordz/puzzle/pt-PT-AO.dic";
	private Pattern spaceorslash = Pattern.compile("/|\\s");
	private Pattern allLetters = Pattern.compile("[A-Z]+");
	Dictionary() {
		try {
			InputStream in = ClassLoader.getSystemResourceAsStream(DIC_FILE);
			BufferedReader reader;
			reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			Words(reader);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private void Words(BufferedReader reader) throws IOException {
		String line;
		while((line = reader.readLine()) != null) {
			if(Character.isAlphabetic(line.charAt(0))) {
				String word = spaceorslash.split(line)[0];
				if(word.length()<3) {
					continue;
				}
				
				word = Normalizer.normalize(word.toUpperCase(Locale.ENGLISH),Form.NFD).
						replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
				
				if(!allLetters.matcher(word).matches()) {
					continue;
				}
				
				trie.put(word);
			  }	
			
		  }	
		 reader.close();
	}
	/**
	 * Obtain the sole instance of this class. 
	 * @return dictionary
	 */
	public static Dictionary getInstance() {
		return dictionary;
	}
	
	
	/**
	 * Start a dictionary search.
	 * @return search
	 */
	public Trie.Search startSearch(){
		return trie.startSearch();
	}
	/**
	 * 
	 * @return large Word
	 */
	public java.lang.String getRandomLargeWord(){
		return trie.getRandomLargeWord();
	}
}
