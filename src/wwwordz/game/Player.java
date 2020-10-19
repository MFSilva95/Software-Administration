package wwwordz.game;

public class Player extends java.lang.Object implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	java.lang.String nick;
	java.lang.String password;
	int points;
	int accumulated;
	/**
	 * @param nick
	 * @param password
	 */
	public Player(String nick, String password) {
		super();
		this.nick = nick;
		this.password = password;
		this.points = 0;
		this.accumulated =0;
	}
	/**
	 * @return the nick
	 */
	public java.lang.String getNick() {
		return nick;
	}
	/**
	 * @param nick the nick to set
	 */
	public void setNick(java.lang.String nick) {
		this.nick = nick;
	}
	/**
	 * @return the password
	 */
	public java.lang.String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(java.lang.String password) {
		this.password = password;
	}
	/**
	 * @return the points
	 */
	public int getPoints() {
		return points;
	}
	/**
	 * @param points the points to set
	 */
	public void setPoints(int points) {
		this.points = points;
		this.accumulated+=points;
	}
	/**
	 * @return the accumulated
	 */
	public int getAccumulated() {
		return accumulated;
	}
	/**
	 * @param accumulated the accumulated to set
	 */
	public void setAccumulated(int accumulated) {
		this.accumulated = accumulated;
	}
	
	

}
