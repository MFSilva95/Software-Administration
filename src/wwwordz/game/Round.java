package wwwordz.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import wwwordz.puzzle.Generator;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

public class Round extends java.lang.Object {

	private static long JOIN_STAGE_DURATION = 5000L;
	private static long PLAY_STAGE_DURATION = 30000L;
	private static long REPORT_STAGE_DURATION = 5000L;
	private static long RANKING_STAGE_DURATION = 5000L;
	private static long ROUND_DURATION = 0L;
	
	private static final Generator generator = new Generator();
	Puzzle puzzle = generator.generate();
	
	HashMap<String,Player> roundPlayers = new HashMap<>();

	public static enum Relative {
		after, before;
	}

	public static enum Stage {
		join, play, ranking, report;
	}

	/**
	 * 
	 */
	public Round() {
		 
	}

	/**
	 * @return the JOIN_STAGE_DURATION
	 */
	public static long getJoinStageDuration() {
		return JOIN_STAGE_DURATION;
	}

	/**
	 * @param JOIN_STAGE_DURATION the JOIN_STAGE_DURATION to set
	 */
	public static void setJoinStageDuration(long JOIN_STAGE_DURATION) {
		Round.JOIN_STAGE_DURATION = JOIN_STAGE_DURATION;
	}

	/**
	 * @return the PLAY_STAGE_DURATION
	 */
	public static long getPlayStageDuration() {
		return PLAY_STAGE_DURATION;
	}

	/**
	 * @param PLAY_STAGE_DURATION the PLAY_STAGE_DURATION to set
	 */
	public static void setPlayStageDuration(long PLAY_STAGE_DURATION) {
		Round.PLAY_STAGE_DURATION = PLAY_STAGE_DURATION;
	}

	/**
	 * @return the REPORT_STAGE_DURATION
	 */
	public static long getReportStageDuration(){
		return REPORT_STAGE_DURATION;
	}

	/**
	 * @param REPORT_STAGE_DURATION the REPORT_STAGE_DURATION to set
	 */
	public static void setReportStageDuration(long REPORT_STAGE_DURATION) {
		Round.REPORT_STAGE_DURATION = REPORT_STAGE_DURATION;
	}

	/**
	 * @return the RANKING_STAGE_DURATION
	 */
	public static long getRankingStageSuration() {
		return RANKING_STAGE_DURATION;
	}

	/**
	 * @param RANKING_STAGE_DURATION the RANKING_STAGE_DURATION to set
	 */
	public static void setRankingStageSuration(long RANKING_STAGE_DURATION) {
		Round.RANKING_STAGE_DURATION = RANKING_STAGE_DURATION;
	}

	/**
	 * Complete duration of a round (all stages)
	 * 
	 * @return the ROUND_DURATION
	 */
	public static long getRoundDuration() {
		setRoundDuration();
		return ROUND_DURATION;
	}

	/**
	 * @param ROUND_DURATION the ROUND_DURATION to set
	 */
	public static void setRoundDuration() {
		Round.ROUND_DURATION = getRankingStageSuration() + getReportStageDuration() + getJoinStageDuration()
				+ getPlayStageDuration();
	}

	Date join = new Date();
	Date play = new Date(join.getTime() + JOIN_STAGE_DURATION);
	Date report = new Date(play.getTime() + PLAY_STAGE_DURATION);
	Date ranking = new Date(report.getTime() + REPORT_STAGE_DURATION);
	Date end = new Date(ranking.getTime() + RANKING_STAGE_DURATION);

	/**
	 * Time to the next play stage.
	 * 
	 * @return
	 */
	public long getTimetoNextPlay() {
		Date now = new Date();
		if (now.before(play)) {
			return play.getTime() - now.getTime();
		} else {
			return end.getTime() - now.getTime() + JOIN_STAGE_DURATION;
		}
	}

	Players players = Players.getInstance();

	/**
	 * Register user with nick and password for this round
	 * 
	 * @param nick
	 * @param password
	 * @return
	 * @throws WWWordzException
	 */
	public long register(String nick, String password) throws WWWordzException {
		Date now = new Date();
		
		if (now.after(play)) {
			throw new WWWordzException("Join stage is invalid");
		} else if (players.verify(nick, password) == false) {
			throw new WWWordzException("user is invalid");
		}  
		
		Player player = players.getPlayer(nick);
		roundPlayers.put(nick, player);
		return play.getTime() - now.getTime();
	}

	/**
	 * Get table of this round
	 * 
	 * @return table
	 * @throws WWWordzException
	 */
	public Puzzle getPuzzle() throws WWWordzException {
		Date now = new Date();
		if (now.before(play)) {
			throw new WWWordzException("Not in play stage");
		} else if (now.after(report)) {
			throw new WWWordzException("Not in play stage");
		}
		return puzzle;

	}

	/**
	 * Set number of points obtained by user in this round
	 * 
	 * @param nick
	 * @param points
	 * @throws WWWordzException
	 */
	public void setPoints(String nick, int points) throws WWWordzException {
		Date now = new Date();
		if (now.before(report) || now.after(ranking)) {
			throw new WWWordzException("Not in report stage");
		} else if (roundPlayers.containsKey(nick) == false) {
			throw new WWWordzException("Unknown nick");
		} else {
			roundPlayers.get(nick).setPoints(points);
		}
	}

	/**
	 * List of players in this round sorted by points
	 * TODO: return object fails the equality test, needs to be fixed
	 * @return players
	 * @throws WWWordzException
	 */
	public List<Rank> getRanking() throws WWWordzException {
		Date now = new Date();
		if (now.before(ranking)) {
			throw new WWWordzException("Not in ranking stage");
		}
		
		List<Rank> ranking = new ArrayList<Rank>();
		List<Player> players = new ArrayList<>(roundPlayers.values());
		
		Collections.sort(players,new Comparator<Player>() {
				@Override
				public int compare(Player p1, Player p2) {
					return p2.points - p1.points;
				}
			});
		
		for(Player player: players) {
			ranking.add(new Rank(player.getNick(), 
					player.getPoints(), 
					player.getAccumulated()));
		}
		
		return ranking;
	}
}
