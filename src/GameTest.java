import junit.framework.TestCase;


public class GameTest extends TestCase {
	
	public void testGame() {
		Game game = new Game();
		assertTrue(!game.isOver());
		assertTrue(game.getCurrentPlayerId() == 0);
	}

	public void testClone() {
		Game g = new Game();
		Game c = g.clone();
		c.display();
	}

	public void testSwapPlayers() {
		Game game = new Game();
		int firstplayer = 0;
		int secondplayer = 1;
		assertTrue(game.getCurrentPlayerId() == firstplayer);
		game.swapPlayers();
		assertTrue(game.getCurrentPlayerId() == secondplayer);
		game.swapPlayers();
		assertTrue(game.getCurrentPlayerId() == firstplayer);
		
	}

	public void testAllValidMoves() {
		Game game = new Game();
		//All moves valid at the beginning of game.
		int[] validmovesp1 = {0, 1, 2, 3, 4, 5};  
		int[] validmovesp2 = {6, 7, 8, 9, 10, 11};
		int[] validmovesp1t2 = {-1, 1, 2, 3, 4, 5};
		
		assertTrue(game.allValidMoves() == validmovesp1);
		game.move(0);
		game.swapPlayers();
		assertTrue(game.allValidMoves() == validmovesp2);
		game.swapPlayers();
		assertTrue(game.allValidMoves() == validmovesp1t2);
	}

	public void testIsValidMove() {
		fail("Not yet implemented");
	}

	public void testCanCurrentPlayerMove() {
		fail("Not yet implemented");
	}

	public void testMove() {
		Bowl[] b = {new Bowl(0), new Bowl(6), new Bowl(0), new Bowl(7), new Bowl(6), new Bowl(5), new Bowl(5), new Bowl(5), new Bowl(6), new Bowl(5), new Bowl(5), new Bowl(1)};
		Game g = new Game(new Player(), new Player(), b);
		g.swapPlayers();
		g.display();
		g.move(7);
		g.display();
		g.move(0);
		g.display();
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
