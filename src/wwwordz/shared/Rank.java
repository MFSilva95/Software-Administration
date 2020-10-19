package wwwordz.shared;

import java.io.Serializable;

public class Rank implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String nick;
	int points;
	int accumulated;
	
	/**
	 * An instance with all fields initialized
	 * @param nick - of player
	 * @param points - of player
	 * @param points of player
	 */
	public Rank(String nick, int points, int accumulated) {
		this.nick = nick;
		this.points = points;
		this.accumulated = accumulated;
	}

	/**
	 * Change the nick in this rank
	 * @param nick - to change
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * Current nick in this rank
	 * @return
	 */
	public String getNick() {
		return this.nick;
	}

	/**
	 * Change points in this rank
	 * @param points - to change
	 */
	public void setPoints(int points) {
		this.points = points;
	}

	/**
	 * Current points in this rank
	 * @return
	 */
	public Integer getPoints() {
		return this.points;
	}

	/**
	 * Change accumulated points in this rank
	 * @param accumulated - points to set
	 */
	public void setAccumulated(int points) {
		this.accumulated = points;
	}

	/**
	 * Current accumulated points in this rank
	 * @return accumulated points
	 */
	public Integer getAccumulated() {
		return this.accumulated;
	}


}
