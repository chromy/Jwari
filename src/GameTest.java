import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

import junit.framework.TestCase;


public class GameTest extends TestCase {
	private Random random;

	public void testGame() {
		Game g = new Game();
		assertTrue(g.canCurrentPlayerMove());
		assertTrue(g.getCurrentPlayerId()==0);
	}

	public void testClone() {
		Game g = new Game();
		Game c = g.clone();
		assertTrue(g != c);
		assertTrue(g.bowls != c.bowls);
		assertTrue(g.bowls.length == c.bowls.length);
		for (int i=0; i<g.bowls.length; i++) {
			assertTrue(g.bowls[i] != c.bowls[i]);
			assertTrue(g.bowls[i].getStones() == c.bowls[i].getStones());
		}
		assertTrue(g.getCurrentPlayerId() == c.getCurrentPlayerId());
		assertTrue(g.getPlayer(0) != c.getPlayer(0));
		assertTrue(g.getPlayer(1) != c.getPlayer(1));
		assertTrue(g.getPlayer(0).getScore() == c.getPlayer(0).getScore());
	}

	public void testSwapPlayers() {
		for (int i=0; i<100; i++) {
			Game g = new Game();
			for (int j=0; j<i; j++) {
				g.swapPlayers();
			}
			assertTrue(g.getCurrentPlayerId() == i%2);
		}

	}

	public void testAllValidMoves() {
		Game g = new Game();
		int[] validmoves1 = {0, 1, 2, 3, 4, 5};
		int[] validmoves2 = {6, 7, 8, 9, 10, 11};
		int[] validmoves3 = {0, 1, 2, 4, 5};
		int[] validmoves4 = {6, 7, 8, 9, 10, 11};
		int[] moves;
		
		moves = g.allValidMoves();
		assertTrue(matchMoves(validmoves1, moves));
		g.move(3);
		moves = g.allValidMoves();
		assertTrue(matchMoves(validmoves2, moves));
		g.move(6);
		moves = g.allValidMoves();
		assertTrue(matchMoves(validmoves3, moves));
		g.move(5);
		moves = g.allValidMoves();
		assertTrue(matchMoves(validmoves4, moves));
	}

	/**
	 * @param a
	 * @param b
	 * @returns true iff every element in a is present in b.
	 */
	private boolean matchMoves(int[] a, int[] b) {
		Arrays.sort(b);
 		for (int x : a) {
			if (Arrays.binarySearch(b, x) < 0) {
				return false;
			}
		}
 		return true;
	}
	
	public void testIsValidMove() {
		Game g = new Game();
		assertTrue(!g.isValidMove(-1));
		assertTrue(!g.isValidMove(100));
		for (int i=0; i<12; i++) {
			assertTrue(g.isValidMove(i));
		}
		
	}
}
