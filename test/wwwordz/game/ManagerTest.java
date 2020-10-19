package wwwordz.game;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wwwordz.TestData;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

/**
* Template for a test class on Manager - YOU NEED TO IMPLEMENTS THESE TESTS!
* 
*/
@DisplayName("Manager")
public class ManagerTest extends TestData {
	wwwordz.game.Round round;
	static final long STAGE_DURATION = 100;
	static final long SLACK = 20;
	Players players;
	int points = 100;
	/**
	 * Set stage durations in round before any tests
	 */
	@BeforeAll
	public static void prepare() {
		Round.setJoinStageDuration(STAGE_DURATION);
		Round.setPlayStageDuration(STAGE_DURATION);
		Round.setReportStageDuration(STAGE_DURATION);
		Round.setRankingStageSuration(STAGE_DURATION);
	}
	
	
	/**
	 * Get an instance for testing and wait for beginning of round
	 */
	@BeforeEach
	public void before()  throws  InterruptedException {
		Manager.getInstance();
	}
	
	
	/**
	 * Test values to start a next play stage
	 * @throws InterruptedException 
	 */
	@Test
	@DisplayName("Time to next play")
	public void testGetTimeToNextPlay() throws InterruptedException {
		long time = round.getTimetoNextPlay();
	
		assertTrue(time<=STAGE_DURATION,"Less them stage duration");
  		Thread.sleep(time-SLACK);
		
		time = round.getTimetoNextPlay();
		assertTrue( time <= SLACK , "Just slack time before play");
		
		Thread.sleep(SLACK);
		
		assertTrue(round.getTimetoNextPlay() > Round.getRoundDuration() - SLACK,
				"Must wait till next round");
		
	}
	

	/**
	 * Test a sequence of rounds, and for each one
	 * 1) register user and check time to start playing 
	 * 3) get puzzle and check its a new one
	 * 4) add random points
	 * 5) get ranking and check expected accumulated points 
	 * @throws WWWordzException 
	 * @throws InterruptedException
	 */
	@Test
	@DisplayName("Rounds")
	public void TestRounds() throws InterruptedException, WWWordzException {
		assertAll("register",
				() -> {
					long time = round.register(NICK, PASSWORD);
			
					assertTrue(time>0,"Positive value expected");
			
					// register again: OK
					time = round.register(NICK, PASSWORD);
					assertTrue(time>0,"Positive value expected");
				},
				() -> {
		
					assertThrows(WWWordzException.class, 
							() -> round.register(NICK, OTHER_PASSWORD),	
							"register with a different password");
				},
				() -> {
		
					Thread.sleep(STAGE_DURATION);
		
					assertThrows(WWWordzException.class, 
							() -> round.register(NICK, PASSWORD),
							"You can't register after game started");
		});	
		
		assertAll("getPuzzle",
				() -> assertThrows(WWWordzException.class, 
						() -> round.getPuzzle(),
						"Exception expected before play stage"),	
				() -> {
				
					Thread.sleep(STAGE_DURATION);
		
					Puzzle puzzle = round.getPuzzle();
			
					assertNotNull(puzzle.getTable(),"Table expected in puzzle");
					assertNotNull(puzzle.getSolutions(),"Solutions expected in puzzle");
				},
				() -> {
		
					Thread.sleep(STAGE_DURATION);
		
					assertThrows(WWWordzException.class,
							() -> round.getPuzzle(),
							"Exception expected after play stage");
		});
		assertAll("addPoints",
			() -> assertThrows(WWWordzException.class,
					()-> players.verify(NICK,PASSWORD),
					"Player should be created"),
			() -> {
				players.addPoints(NICK, points);

				Player player = players.getPlayer(NICK);
				assertEquals(points,player.getPoints(),"Wrong points");
			},
			() -> {
				
				assertThrows(WWWordzException.class, 
				() -> players.addPoints(OTHER_NICK, points));
			});
				
		assertAll("rankings",
				() -> {
					Collection<Rank>  ranking = round.getRanking();
					
					assertAll("-",
					() -> assertNotNull(ranking,"List of players expected"),
					
					() -> {
						Collection<Rank>  copy = round.getRanking();
						assertTrue(copy == ranking,"Exactly the same object expected");
				
				});
		
		});
		
	}
	
}
