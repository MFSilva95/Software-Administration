package wwwordz.game;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import wwwordz.shared.WWWordzException;
public class Players extends java.lang.Object implements java.io.Serializable {

	
	private static final long serialVersionUID = 1L;
	HashMap<String,Player> players = new HashMap<>();
	private static Players instance = new Players();
	private static File home = new File(System.getProperty("user.dir"));
	

	
	
	/**
	 * Current home directory, where the data file is stored
	 * @return the home
	 */
	public static java.io.File getHome() {
		return Players.home;
	}

	/**
	 * Update home directory, where the data file is stored
	 * @param home the home to set
	 */
	public static void setHome(java.io.File home) {
		Players.home = home;
		
	}
	/**
	 * Get single instance of this class
	 * @return
	 */
	
	public static Players getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}
	/**
	 * Verify player's password. If player doesn't exist yet then it is created with given password.
	 * @param nick
	 * @param password
	 * @return
	 */
	public boolean verify(java.lang.String nick, java.lang.String password) {
		if(players.containsKey(nick)) {
				return players.get(nick).getPassword().equals(password);
		}
		else {
			players.put(nick, new Player(nick, password));
			return true;
		}
		
	}
	/**
	 * Reset points of current round while keeping accumulated points
	 * @param nick
	 * @throws WWWordzException
	 */
	public void resetPoints(java.lang.String nick) throws WWWordzException{
		if(players.containsKey(nick)) {
			addPoints(nick,0);
		}
		else {
			throw new WWWordzException("player is unknown");
		}
																																			
	}
	/**
	 * Add points to player
	 * @param nick
	 * @param points
	 * @throws WWWordzException
	 */
	public void addPoints(java.lang.String nick, int points) throws WWWordzException{
		if(players.containsKey(nick)) {
			Player player = players.get(nick);
			player.points = points;
			player.accumulated += points;
			
		}
		else {
			throw  new WWWordzException("player is unknown");
		}
	}
	
	/**
	 * 
	 * @param nick
	 * @return
	 */
	public Player getPlayer(java.lang.String nick) {
		return players.get(nick);
	}
	/**
	 * Clears all players stored in this instances. This method is used for testing purposes only.
	 */
	public void cleanup() { 
		Collection<String> nicks = new ArrayList<>(players.keySet());
		for(String nick: nicks) {
			players.remove(nick);
		}
	}


		
}	
	

