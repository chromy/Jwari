import junit.framework.TestCase;


public class GameTest extends TestCase {

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
		int[] validmoves1 = {0, 1, 2, 3, 4, 5, 6}; 
	}

	public void testIsValidMove() {
		fail("Not yet implemented");
	}

	public void testCanCurrentPlayerMove() {
		fail("Not yet implemented");
	}

	public void testMove() {
		fail("Not yet implemented");
	}

	public void testMoveSequence() {
		fail("Not yet implemented");
	}

	public void testAfterMove() {
		fail("Not yet implemented");
	}

	public void testGetLeadingPlayer() {
		fail("Not yet implemented");
	}

	public void testIsOver() {
		fail("Not yet implemented");
	}

}
