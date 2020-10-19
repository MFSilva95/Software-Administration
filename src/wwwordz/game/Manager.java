package wwwordz.game;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import wwwordz.shared.WWWordzException;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;


public class Manager extends java.lang.Object {
	static final long INITIAL_TIME = 0L;
	static Manager manager;
	Round round;
	static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();

	/**
	 * 
	 * @return manager
	 */
	public static Manager getInstance() {
		return manager;
	}
	/**
	 * 
	 * @return
	 */
	public long timeToNextPlay() {
		return round.getTimetoNextPlay();
	}
	/**
	 * 
	 * @param nick
	 * @param password
	 * @return time in seconds for next found
	 * @throws WWWordzException
	 */
	public long register(java.lang.String nick,java.lang.String password)throws WWWordzException{
		return round.register(nick, password);
	}
	/**
	 * Get table of current round
	 * @return table
	 * @throws WWWordzException
	 */
	public Puzzle getPuzzle() throws WWWordzException{
		return round.getPuzzle();
	}
	/**
	 * Set number of points obtained by user in current round
	 * @param nick
	 * @param points
	 * @throws WWWordzException
	 */
	public void setPoints(java.lang.String nick,int points)throws WWWordzException{
		round.setPoints(nick, points);
		
	}
	
	/**
	 * List of players in current round sorted by points
	 * @return
	 * @throws WWWordzException
	 */
	public java.util.List<Rank> getRanking()throws WWWordzException{
		return round.getRanking();
	}



}
